'use strict';

// Build-time only. Parse modules; never import/execute the packages being inspected (adapters have
// optional peers, hooks require React, and importing a package can itself have side effects).
const fs = require('node:fs');
const path = require('node:path');
const { createRequire } = require('node:module');

const EXCLUSIONS = {
  'mui.material.styles.createPalette': 'Material exports the palette types, but not the factory.',
  'mui.material.styles.createMotion': 'Material exports the motion types, but not the factory.',
  'mui.material.SwitchBase': 'The internal component has no package export.',
  'mui.material.switchBaseClasses': 'The internal class object has no package export.',
};

const nameOf = node => node.name ?? node.value;
const isType = file => /\.d\.[cm]?ts$/.test(file);
const readJson = file => JSON.parse(fs.readFileSync(file, 'utf8'));
const exists = file => fs.existsSync(file) && fs.statSync(file).isFile();
const bindingNames = node => {
  if (!node) return [];
  if (node.type === 'Identifier') return [node.name];
  if (node.type === 'ObjectPattern') return node.properties.flatMap(item => bindingNames(item.type === 'RestElement' ? item.argument : item.value));
  if (node.type === 'ArrayPattern') return node.elements.flatMap(bindingNames);
  if (node.type === 'AssignmentPattern') return bindingNames(node.left);
  if (node.type === 'RestElement') return bindingNames(node.argument);
  throw new Error(`Unsupported binding pattern ${node.type}`);
};

class ExportIndex {
  constructor(nodeModulesDir, parse) {
    this.nodeModulesDir = path.resolve(nodeModulesDir);
    this.parse = parse || createRequire(path.join(this.nodeModulesDir, '../package.json'))('@babel/parser').parse;
    this.packages = new Map();
    this.models = new Map();
    this.resolved = new Map();
    this.nameSets = new Map();
  }

  packageFor(specifier, from = this.nodeModulesDir) {
    const parts = specifier.split('/');
    const name = parts.slice(0, specifier.startsWith('@') ? 2 : 1).join('/');
    let directory = from;
    let root;
    while (true) {
      const candidate = path.join(directory, directory.endsWith('node_modules') ? '' : 'node_modules', name);
      if (exists(path.join(candidate, 'package.json'))) { root = candidate; break; }
      const parent = path.dirname(directory);
      if (parent === directory) throw new Error(`Cannot locate package ${name} from ${from}`);
      directory = parent;
    }
    if (!this.packages.has(root)) {
      const json = readJson(path.join(root, 'package.json'));
      this.packages.set(root, { root, name, json, label: `${name}@${json.version}` });
    }
    return this.packages.get(root);
  }

  // Conditions are tested in manifest order, as specified by Node's conditional exports algorithm.
  select(value, types = false) {
    if (typeof value === 'string' || value === null) return value;
    if (Array.isArray(value)) {
      for (const item of value) { const result = this.select(item, types); if (result !== undefined) return result; }
      return undefined;
    }
    if (value && typeof value === 'object') {
      const conditions = new Set(types ? ['types', 'import', 'browser', 'default'] : ['import', 'browser', 'default']);
      for (const [condition, target] of Object.entries(value)) {
        if (conditions.has(condition)) {
          const result = this.select(target, types);
          if (result !== undefined) return result;
        }
      }
    }
    return undefined;
  }

  entry(pkg, specifier, types = false) {
    const subpath = specifier === pkg.name ? '.' : `.${specifier.slice(pkg.name.length)}`;
    const exports = pkg.json.exports;
    if (exports !== undefined) {
      const map = typeof exports === 'object' && exports !== null && !Array.isArray(exports) &&
        Object.keys(exports).some(key => key.startsWith('.')) ? exports : { '.': exports };
      let target;
      if (Object.hasOwn(map, subpath)) target = this.select(map[subpath], types);
      else {
        const patterns = Object.keys(map).filter(key => key.includes('*')).sort((a, b) =>
          b.indexOf('*') - a.indexOf('*') || b.length - a.length);
        for (const pattern of patterns) {
          const [prefix, suffix] = pattern.split('*');
          if (!subpath.startsWith(prefix) || !subpath.endsWith(suffix) || subpath.length < prefix.length + suffix.length) continue;
          const selected = this.select(map[pattern], types);
          target = typeof selected === 'string' ? selected.replaceAll('*', subpath.slice(prefix.length, subpath.length - suffix.length)) : selected;
          break;
        }
      }
      if (typeof target !== 'string') return null;
      if (!target.startsWith('./')) throw new Error(`${pkg.label}: invalid exports target ${target}`);
      const file = path.resolve(pkg.root, target);
      if (!file.startsWith(pkg.root + path.sep)) throw new Error(`${pkg.label}: exports target escapes package: ${target}`);
      return exists(file) ? file : null;
    }
    const directory = path.resolve(pkg.root, subpath);
    if (exists(directory + '.js') && !types) return directory + '.js';
    const local = exists(path.join(directory, 'package.json')) ? readJson(path.join(directory, 'package.json')) : {};
    const target = types ? local.types || local.typings || 'index.d.ts' : local.module || local.main || 'index.js';
    const file = path.resolve(directory, target);
    return exists(file) ? file : null;
  }

  importedFile(file, specifier) {
    if (!specifier.startsWith('.')) {
      const pkg = this.packageFor(specifier, path.dirname(file));
      const target = this.entry(pkg, specifier, isType(file));
      if (!target) throw new Error(`${pkg.label}: no target for ${specifier} (re-exported by ${file})`);
      return target;
    }
    const base = path.resolve(path.dirname(file), specifier);
    const candidates = isType(file) ? [
      base.replace(/\.mjs$/, '.d.mts').replace(/\.cjs$/, '.d.cts').replace(/\.js$/, '.d.ts'),
      base.replace(/\.[cm]?js$/, '.d.ts'), base + '.d.ts', path.join(base, 'index.d.ts'),
    ] : [base, base + '.js', base + '.mjs', path.join(base, 'index.js')];
    const target = candidates.find(exists);
    if (!target) throw new Error(`Missing re-export target ${specifier} from ${file}`);
    return target;
  }

  model(file) {
    if (this.models.has(file)) return this.models.get(file);
    const ast = this.parse(fs.readFileSync(file, 'utf8'), {
      sourceType: 'module', plugins: isType(file) ? [['typescript', { dts: true }]] : [],
    });
    const model = { exports: new Map(), imports: new Map(), aliases: new Map(), types: new Set(), stars: [] };
    this.models.set(file, model);
    const local = name => ({ file, local: name });
    const declaration = node => {
      if (/^(TSInterfaceDeclaration|TSTypeAliasDeclaration|TSModuleDeclaration)$/.test(node.type)) {
        if (node.id) model.types.add(nameOf(node.id));
        return [];
      }
      if (node.type === 'VariableDeclaration') {
        return node.declarations.flatMap(item => {
          if (item.id.type === 'Identifier' && item.init?.type === 'Identifier') model.aliases.set(item.id.name, item.init.name);
          return bindingNames(item.id);
        });
      }
      return node.id ? [nameOf(node.id)] : [];
    };
    for (const node of ast.program.body) {
      if (node.type === 'ImportDeclaration') {
        for (const spec of node.specifiers) {
          if (node.importKind === 'type' || spec.importKind === 'type') { model.types.add(spec.local.name); continue; }
          model.imports.set(spec.local.name, { from: node.source.value, name: spec.type === 'ImportDefaultSpecifier' ? 'default' : spec.type === 'ImportNamespaceSpecifier' ? '*' : nameOf(spec.imported) });
        }
      } else if (node.type === 'ExportNamedDeclaration') {
        // Babel also marks `export declare function/const/class` as "type". These describe
        // runtime values; only explicit type-only export lists and erased declarations are skipped.
        if (node.exportKind === 'type' && !node.declaration) continue;
        if (node.declaration) for (const name of declaration(node.declaration)) model.exports.set(name, local(name));
        for (const spec of node.specifiers) {
          if (spec.exportKind === 'type') continue;
          const exported = nameOf(spec.exported);
          model.exports.set(exported, node.source ? { file, from: node.source.value, name: spec.type === 'ExportNamespaceSpecifier' ? '*' : nameOf(spec.local) } : local(nameOf(spec.local)));
        }
      } else if (node.type === 'ExportDefaultDeclaration') {
        const value = node.declaration;
        const name = value.type === 'Identifier' ? value.name : value.id?.name || '#default';
        if (value.type !== 'Identifier') declaration(value);
        model.exports.set('default', local(name));
      } else if (node.type === 'ExportAllDeclaration') {
        if (node.exportKind !== 'type') model.stars.push(node.source.value);
      } else declaration(node);
    }
    return model;
  }

  names(file, seen = new Set()) {
    if (this.nameSets.has(file)) return this.nameSets.get(file);
    if (seen.has(file)) return new Set();
    const visited = new Set(seen).add(file);
    const model = this.model(file);
    const names = new Set(model.exports.keys());
    for (const star of model.stars) {
      for (const name of this.names(this.importedFile(file, star), visited)) if (name !== 'default') names.add(name);
    }
    // Only cache complete traversals; caching a partial result inside a cycle loses valid exports.
    if (seen.size === 0) this.nameSets.set(file, names);
    return names;
  }

  local(file, name, seen) {
    const model = this.model(file);
    if (model.types.has(name)) return null;
    const alias = model.aliases.get(name);
    if (alias) {
      const key = `${file}:local:${name}`;
      if (seen.has(key)) return null;
      return this.local(file, alias, new Set(seen).add(key));
    }
    const imported = model.imports.get(name);
    if (imported) {
      const target = this.importedFile(file, imported.from);
      return imported.name === '*' ? { id: `${target}:namespace`, namespace: target } : this.symbol(target, imported.name, seen);
    }
    return { id: `${file}:${name}` };
  }

  symbol(file, name, seen = new Set()) {
    const key = `${file}:export:${name}`;
    if (this.resolved.has(key)) return this.resolved.get(key);
    if (seen.has(key)) return null;
    const visited = new Set(seen).add(key);
    const model = this.model(file);
    const ref = model.exports.get(name);
    let result;
    if (ref) {
      if (ref.from) {
        const target = this.importedFile(file, ref.from);
        result = ref.name === '*' ? { id: `${target}:namespace`, namespace: target } : this.symbol(target, ref.name, visited);
      } else result = this.local(file, ref.local, visited);
    } else {
      const candidates = name === 'default' ? [] : model.stars.map(star => this.symbol(this.importedFile(file, star), name, visited)).filter(Boolean);
      if (new Set(candidates.map(value => value.id)).size > 1) throw new Error(`Ambiguous star export ${name} in ${file}`);
      result = candidates[0] || null;
    }
    if (result && seen.size === 0) this.resolved.set(key, result);
    return result;
  }

  origin(request) {
    if (!request.source) return null; // Explicit templates (icons, adapters, namespace objects).
    const base = request.source.replace(/\.d\.[cm]?ts$/, '');
    const file = [base + '.mjs', base + '.js'].find(exists);
    if (!file) throw new Error(`No JavaScript sibling of runtime declaration ${request.source}`);
    const named = this.symbol(file, request.name);
    if (named) return named;
    // Default is a fact about the original typings, not the normalized text used by converters.
    if (this.model(request.source).exports.has('default')) return this.symbol(file, 'default');
    throw new Error(`Cannot identify original runtime symbol ${request.name} in ${request.source}`);
  }

  entries(pkg, source) {
    const exports = pkg.json.exports;
    const specifiers = new Set([pkg.name]);
    if (exports !== undefined) {
      for (const key of Object.keys(exports || {})) {
        if (key.startsWith('./') && !key.includes('*') && key !== './package.json') specifiers.add(pkg.name + key.slice(1));
      }
      // Invert wildcard targets: ./widgets/* can map to ./dist/*.mjs. Guessing the public path
      // from the source directory would reintroduce the very bug this resolver eliminates.
      if (source) {
        const base = './' + path.relative(pkg.root, source).replace(/\.d\.[cm]?ts$/, '').split(path.sep).join('/');
        for (const [key, value] of Object.entries(exports || {})) {
          const target = this.select(value);
          if (!key.includes('*') || typeof target !== 'string' || !target.includes('*')) continue;
          const [prefix, suffix] = target.split('*');
          for (const relative of [base + '.mjs', base + '.js']) {
            if (relative.startsWith(prefix) && relative.endsWith(suffix)) {
              const wildcard = relative.slice(prefix.length, relative.length - suffix.length);
              specifiers.add(pkg.name + key.replace('*', wildcard).slice(1));
            }
          }
        }
      }
    } else {
      for (const dir of fs.readdirSync(pkg.root, { withFileTypes: true })) {
        if (dir.isDirectory() && dir.name !== 'node_modules' && exists(path.join(pkg.root, dir.name, 'index.d.ts'))) specifiers.add(`${pkg.name}/${dir.name}`);
      }
    }
    return [...specifiers].sort().flatMap(module => {
      const file = this.entry(pkg, module);
      return file && /\.[cm]?js$/.test(file) ? [{ module, file }] : [];
    });
  }

  checkMembers(request, symbol) {
    if (!request.members?.length) return;
    if (!symbol.namespace) throw new Error(`${request.key} is not a module namespace`);
    for (const member of request.members) {
      if (!this.symbol(symbol.namespace, member)) throw new Error(`Missing namespace member ${request.key}.${member}`);
    }
  }

  resolve(request, exclusions = EXCLUSIONS) {
    const pkg = this.packageFor(request.module);
    const origin = this.origin(request);
    const current = this.entry(pkg, request.module);
    const candidates = [];
    const collect = (module, file, onlyName) => {
      for (const name of onlyName ? [onlyName] : this.names(file)) {
        const symbol = this.symbol(file, name);
        if (symbol && (!origin || symbol.id === origin.id)) candidates.push({ module, name, symbol });
      }
    };
    if (current) collect(request.module, current, request.importName);
    if (!candidates.length && current && origin) collect(request.module, current);
    if (!candidates.length && origin) for (const entry of this.entries(pkg, request.source)) collect(entry.module, entry.file);

    if (Object.hasOwn(exclusions, request.key)) {
      if (candidates.length) throw new Error(`Stale exclusion for ${request.key}: now exported by ${candidates.map(c => `${c.module}#${c.name}`).join(', ')}`);
      return { id: request.id, module: null, name: null, reason: exclusions[request.key] };
    }
    if (!candidates.length) throw new Error(`${pkg.label}: cannot bind ${request.key}\n  source: ${request.source || '(explicit template)'}\n  requested: ${request.module}#${request.importName}\n  checked: ${this.entries(pkg, request.source).map(e => e.module).join(', ')}`);

    const ancestors = [];
    if (request.source) {
      let directory = path.dirname(request.source);
      while (directory.startsWith(pkg.root)) {
        const relative = path.relative(pkg.root, directory).split(path.sep).join('/');
        ancestors.push(relative ? `${pkg.name}/${relative}` : pkg.name);
        if (directory === pkg.root) break;
        directory = path.dirname(directory);
      }
    }
    const priority = candidate => {
      if (candidate.module === request.module) return 0;
      if (candidate.module === pkg.name) return 1000;
      const index = ancestors.indexOf(candidate.module);
      if (index >= 0) return index + 1;
      return 100;
    };
    const rank = Math.min(...candidates.map(priority));
    let best = candidates.filter(candidate => priority(candidate) === rank);
    const sameName = best.filter(candidate => candidate.name === request.name);
    if (sameName.length) best = sameName;
    if (best.length !== 1) throw new Error(`${pkg.label}: ambiguous binding for ${request.key}: ${best.map(c => `${c.module}#${c.name}`).join(', ')}`);
    this.checkMembers(request, best[0].symbol);
    return { id: request.id, module: best[0].module, name: best[0].name };
  }
}

function resolveRequests(input, exclusions = EXCLUSIONS) {
  const index = new ExportIndex(input.nodeModulesDir);
  return input.requests.map(request => index.resolve(request, exclusions));
}

module.exports = { ExportIndex, resolveRequests };

if (require.main === module) {
  try {
    const input = readJson(process.argv[2]);
    const output = resolveRequests(input);
    fs.writeFileSync(process.argv[3], JSON.stringify(output, null, 2) + '\n');
    const changes = new Set();
    output.forEach((result, i) => {
      const request = input.requests[i];
      if (result.module !== request.module || result.name !== request.importName) changes.add(`${request.key}: ${result.module ? `${result.module}#${result.name}` : `types only (${result.reason})`}`);
    });
    for (const change of changes) console.log(`Runtime binding: ${change}`);
  } catch (error) {
    console.error(error.stack);
    process.exitCode = 1;
  }
}
