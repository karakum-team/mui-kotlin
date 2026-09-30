'use strict';
const { test } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { createRequire } = require('node:module');
const { ExportIndex } = require('../../main/resources/karakum/mui/runtime-exports.cjs');
const parse = createRequire(path.resolve(process.env.NODE_MODULES_DIR, '../package.json'))('@babel/parser').parse;

function fixture(t, exports, files, extra = {}) {
  const directory = fs.mkdtempSync(path.join(os.tmpdir(), 'mui-exports-test-'));
  t.after(() => fs.rmSync(directory, { recursive: true, force: true }));
  const nodeModulesDir = path.join(directory, 'node_modules');
  const root = path.join(nodeModulesDir, '@test/ui');
  for (const [file, content] of Object.entries({
    'package.json': JSON.stringify({ name: '@test/ui', version: '1.0.0', exports, ...extra }), ...files,
  })) {
    fs.mkdirSync(path.dirname(path.join(root, file)), { recursive: true });
    fs.writeFileSync(path.join(root, file), content);
  }
  const index = new ExportIndex(nodeModulesDir, parse);
  const request = (fields = {}) => ({ id: 0, key: 'test.useThing', name: 'useThing', importName: 'default',
    module: '@test/ui/private/useThing', source: path.join(root, 'private/useThing.d.ts'), members: [], ...fields });
  return { index, root, request, resolve: fields => index.resolve(request(fields), {}) };
}

const hook = {
  'private/useThing.d.ts': 'export default function useThing(): void;',
  'private/useThing.mjs': 'export default function useThing() {}',
};

test('moves a default function to a named barrel export without executing it', t => {
  const f = fixture(t, { './styles': './styles.mjs' }, { ...hook,
    'styles.mjs': 'throw new Error("must not execute"); export {default as useThing} from "./private/useThing.mjs";',
  });
  assert.deepEqual(f.resolve(), { id: 0, module: '@test/ui/styles', name: 'useThing' });
});

test('keeps an existing valid default binding', t => {
  const f = fixture(t, { './private/useThing': './private/useThing.mjs', '.': './index.mjs' }, {
    ...hook, 'index.mjs': 'export {default as useThing} from "./private/useThing.mjs";',
  });
  assert.equal(f.resolve().name, 'default');
});

test('corrects named/default and unstable aliases in the same entrypoint', t => {
  const f = fixture(t, { './hook': './index.mjs' }, { ...hook,
    'index.mjs': 'export {default as unstable_useThing} from "./private/useThing.mjs";',
  });
  assert.deepEqual(f.resolve({ module: '@test/ui/hook', importName: 'useThing' }),
    { id: 0, module: '@test/ui/hook', name: 'unstable_useThing' });
});

test('accepts a private JS alias even when the public typings omit it', t => {
  const f = fixture(t, { './styles': { import: { types: './styles.d.ts', default: './styles.mjs' } } }, {
    ...hook, 'styles.d.ts': 'export interface Options {}',
    'styles.mjs': 'export {default as private_useThing} from "./private/useThing.mjs";',
  });
  assert.equal(f.resolve().name, 'private_useThing');
});

test('does not bind an identically named export from a different source', t => {
  const f = fixture(t, { './wrong': './wrong.mjs', './right': './right.mjs' }, { ...hook,
    'wrong.mjs': 'export function useThing() {}',
    'right.mjs': 'export {default as correct} from "./private/useThing.mjs";',
  });
  assert.equal(f.resolve({ module: '@test/ui/wrong', importName: 'useThing' }).module, '@test/ui/right');
});

test('prefers the nearest ancestor and then a matching Kotlin name', t => {
  const f = fixture(t, { '.': './index.mjs', './private': './private/index.mjs' }, { ...hook,
    'index.mjs': 'export {default as useThing} from "./private/useThing.mjs";',
    'private/index.mjs': 'export {default as useThing, default as unstable_useThing} from "./useThing.mjs";',
  });
  assert.deepEqual(f.resolve(), { id: 0, module: '@test/ui/private', name: 'useThing' });
});

test('rejects equally ranked aliases instead of guessing', t => {
  const f = fixture(t, { './styles': './styles.mjs' }, { ...hook,
    'styles.mjs': 'export {default as first, default as second} from "./private/useThing.mjs";',
  });
  assert.throws(() => f.resolve(), /ambiguous binding/);
});

test('prefers another public entrypoint over the package root', t => {
  const f = fixture(t, { '.': './index.mjs', './hooks': './hooks.mjs' }, { ...hook,
    'index.mjs': 'export {default as useThing} from "./private/useThing.mjs";',
    'hooks.mjs': 'export {default as useThing} from "./private/useThing.mjs";',
  });
  assert.equal(f.resolve().module, '@test/ui/hooks');
});

test('follows imports and a chain of star re-exports, including cycles', t => {
  const f = fixture(t, { './styles': './a.mjs' }, { ...hook,
    'a.mjs': 'export * from "./b.mjs";',
    'b.mjs': 'export * from "./a.mjs"; import hook from "./private/useThing.mjs"; export {hook as useThing};',
  });
  assert.equal(f.resolve().name, 'useThing');
});

test('rejects ambiguous star exports even after a previous cyclic traversal', t => {
  const f = fixture(t, { '.': './a.mjs' }, {
    'a.mjs': 'export * from "./b.mjs"; export * from "./c.mjs";',
    'b.mjs': 'export * from "./a.mjs"; export const value = 1;',
    'c.mjs': 'export const value = 2;',
  });
  assert.throws(() => f.index.symbol(path.join(f.root, 'a.mjs'), 'value'), /Ambiguous star export/);
});

test('does not treat a type-only re-export as a runtime value', t => {
  const f = fixture(t, { '.': './index.mjs' }, { ...hook, 'index.mjs': 'export {};',
    'index.d.ts': 'export type {Options} from "./options.js";', 'options.d.ts': 'export interface Options {}',
  });
  assert.equal(f.index.symbol(path.join(f.root, 'index.d.ts'), 'Options'), null);
  assert.throws(() => f.resolve(), /cannot bind/);
});

test('uses import conditions in manifest order and never selects types for JS', t => {
  const f = fixture(t, { './styles': { import: { types: './missing.d.ts', default: './styles.mjs' }, default: './missing.js' } }, {
    ...hook, 'styles.mjs': 'export {default as useThing} from "./private/useThing.mjs";',
  });
  assert.equal(f.resolve().name, 'useThing');
});

test('inverts wildcard exports targets and respects exact null exclusions', t => {
  const f = fixture(t, { './hooks/*': './private/*.mjs', './hooks/blocked': null }, hook);
  assert.equal(f.resolve().module, '@test/ui/hooks/useThing');
  assert.equal(f.index.entry(f.index.packageFor('@test/ui'), '@test/ui/hooks/blocked'), null);
});

test('supports legacy subdirectory metadata with no exports map', t => {
  const f = fixture(t, undefined, { ...hook,
    'private/package.json': JSON.stringify({ module: './index.js', main: './missing.cjs' }),
    'private/index.js': 'export {default as useThing} from "./useThing.mjs";',
    'private/index.d.ts': 'export {default as useThing} from "./useThing.js";',
  });
  assert.equal(f.resolve({ module: '@test/ui/missing' }).module, '@test/ui/private');
});

test('falls back to legacy main when module is absent', t => {
  const f = fixture(t, undefined, { ...hook,
    'index.mjs': 'export {default as useThing} from "./private/useThing.mjs";',
  }, { main: './index.mjs' });
  assert.equal(f.resolve({ module: '@test/ui/missing' }).module, '@test/ui');
});

test('uses d.mts for mjs declaration re-exports before a neighboring d.ts', t => {
  const f = fixture(t, {}, {
    'index.d.mts': 'export {useThing} from "./hook.mjs";',
    'hook.d.mts': 'export declare function useThing(): void;',
    'hook.d.ts': 'export interface useThing {}',
  });
  assert.equal(f.index.symbol(path.join(f.root, 'index.d.mts'), 'useThing').id,
    `${path.join(f.root, 'hook.d.mts')}:useThing`);
});

test('distinguishes declared runtime values from erased declarations and type-only aliases', t => {
  const f = fixture(t, {}, {
    'index.d.ts': 'export declare const value: number; export declare class Widget {} ' +
      'export interface Options {} export type Alias = string; export type {value as TypeOnly};',
  });
  const file = path.join(f.root, 'index.d.ts');
  assert.ok(f.index.symbol(file, 'value'));
  assert.ok(f.index.symbol(file, 'Widget'));
  for (const name of ['Options', 'Alias', 'TypeOnly']) assert.equal(f.index.symbol(file, name), null);
});

test('validates namespace members, including functions', t => {
  const f = fixture(t, { './menu': './index.mjs' }, {
    'index.mjs': 'export * as Menu from "./parts.mjs";',
    'parts.mjs': 'export const {Root} = {Root: 1}; export function createHandle() {}',
  });
  const request = { module: '@test/ui/menu', name: 'Menu', importName: 'Menu', source: null, members: ['Root', 'createHandle'] };
  assert.equal(f.resolve(request).name, 'Menu');
  assert.throws(() => f.resolve({ ...request, members: ['Missing'] }), /Missing namespace member/);
});

test('rejects a missing package target with package/version/source diagnostics', t => {
  const f = fixture(t, { './styles': './missing.mjs' }, hook);
  assert.throws(() => f.resolve(), /@test\/ui@1\.0\.0: cannot bind test.useThing[\s\S]*source:/);
});

test('rejects a re-export whose target file is missing', t => {
  const f = fixture(t, { './styles': './styles.mjs' }, { ...hook,
    'styles.mjs': 'export {default as useThing} from "./missing.mjs";',
  });
  assert.throws(() => f.resolve(), /Missing re-export target/);
});

test('only permits explicit exclusions and detects stale exclusions', t => {
  const f = fixture(t, {}, hook);
  assert.throws(() => f.resolve(), /cannot bind/);
  assert.deepEqual(f.index.resolve(f.request(), { 'test.useThing': 'No public export' }),
    { id: 0, module: null, name: null, reason: 'No public export' });
  const publicFixture = fixture(t, { './private/useThing': './private/useThing.mjs' }, hook);
  assert.throws(() => publicFixture.index.resolve(publicFixture.request(), { 'test.useThing': 'Old reason' }), /Stale exclusion/);
});
