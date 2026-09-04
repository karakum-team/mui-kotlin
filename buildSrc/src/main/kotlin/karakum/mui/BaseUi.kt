package karakum.mui

import java.io.File

// Base UI (`@base-ui/react`) — successor of the frozen `@mui/base`. Its `.d.ts` layout differs from
// every `@mui/*` package, so the traversal lives here instead of reusing the MUI directory scans.
//
// Layout, per module (e.g. `menu/`):
//   index.d.ts         `export * as Menu from "./index.parts.js"` + `export type *` of every part
//   index.parts.d.ts   the authoritative part list: `export { MenuPopup as Popup } from "./popup/MenuPopup.js"`
//   <kebab-part>/<PascalName>.d.ts        the part itself (props / state / event types)
//   <kebab-part>/<PascalName>Context.d.ts internal React context — not public API
//   <kebab-part>/<PascalName>DataAttributes.d.ts, …CssVars.d.ts   `declare enum` of data-* / --css-var names
//   store/, utils/, use<Part>.d.ts        internal plumbing
//
// Driving generation off `index.parts.d.ts` rather than walking for `*.d.ts` is deliberate: a walk
// would also pick up the Context / DataAttributes / CssVars / store / utils files (in `menu/` that is
// 51 `.d.ts` files for 20 actual parts) plus a `.d.mts` twin of each.

/**
 * One entry of a module's `index.parts.d.ts`.
 *
 * `export { MenuPopup as Popup } from "./popup/MenuPopup.js"` →
 * alias `Popup`, declaredName `MenuPopup`, and the resolved `MenuPopup.d.ts`.
 */
internal data class BaseUiPart(
    /** Name inside the JS namespace object: `Menu.Popup` → `Popup`. */
    val alias: String,
    /** Name of the flat declaration the module actually exports: `MenuPopup`, `MenuPopupProps`, … */
    val declaredName: String,
    val file: File,
)

internal data class BaseUiModule(
    /** npm subpath and output package segment: `menu`, `number-field` → `numberField`. */
    val id: String,
    /**
     * Name of the namespace object the module exports (`Menu`), or `null` for a module that exports its
     * values directly. See [parseBaseUiNamespace].
     */
    val namespace: String?,
    val parts: List<BaseUiPart>,
)

/**
 * Parses a module's `index.parts.d.ts` into its part list.
 *
 * Handles the three binding shapes present in 1.6.0:
 *  - renamed:  `export { MenuPopup as Popup } from "./popup/MenuPopup.js"`
 *  - verbatim: `export { Separator } from "../separator/Separator.js"` (shared part, note the `../`)
 *  - multiple: `export { MenuHandle as Handle, createMenuHandle as createHandle } from "./store/MenuHandle.js"`
 */
internal fun parseBaseUiParts(
    indexPartsFile: File,
): List<BaseUiPart> {
    val moduleDir = indexPartsFile.parentFile

    return EXPORT_CLAUSE.findAll(indexPartsFile.readText())
        .flatMap { match ->
            val (bindings, path) = match.destructured

            // "./popup/MenuPopup.js" → MenuPopup.d.ts next to it; "../separator/Separator.js" escapes
            // the module, which is how shared parts (Menu.Separator) are wired.
            val file = moduleDir.resolve(path.removeSuffix(".js") + ".d.ts").normalize()

            bindings.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                // `export { type Orientation } from "../internals/types.js"` (toolbar) re-exports a type,
                // not a part: it names no value, and its target is `internals/`, which is not generated.
                // Left in, its "alias" would be the two words `type Orientation` — not a Kotlin
                // identifier, and so not usable as a member name of the namespace object.
                .filter { !it.startsWith("type ") }
                .map { binding ->
                    val declaredName = binding.substringBefore(" as ").trim()
                    val alias = binding.substringAfter(" as ", declaredName).trim()
                    BaseUiPart(alias = alias, declaredName = declaredName, file = file)
                }
        }
        .toList()
}

private val EXPORT_CLAUSE = Regex("""export\s*\{([^}]*)}\s*from\s*"([^"]*)";""")

/**
 * Reads the name of the module's namespace object from its `index.d.ts`:
 *
 *     export * as Menu from "./index.parts.js";
 *
 * This is the only *value* the module exports (everything else in `index.d.ts` is `export type *`), so
 * it is also the only way to reach a part at runtime — see [baseUiNamespaceObject].
 *
 * Read rather than derived from [BaseUiModule.id]: kebab-to-Pascal would give `OtpField` for
 * `otp-field`, whose namespace is actually `OTPField`. `null` for the modules that export their values
 * directly and have no namespace object — the 13 flat ones plus `csp-provider` / `direction-provider`,
 * which do have an `index.parts.d.ts`.
 */
internal fun parseBaseUiNamespace(
    indexFile: File,
): String? {
    if (!indexFile.exists())
        return null

    return NAMESPACE_EXPORT.find(indexFile.readText())
        ?.groupValues?.get(1)
}

private val NAMESPACE_EXPORT = Regex("""export \* as (\w+) from "\./index\.parts\.js";""")

/**
 * Discovers the public modules of `@base-ui/react` from its `exports` map keys, which is also what
 * bounds what can be imported at runtime: the map has no wildcard entries, so only these subpaths
 * exist. The `internals` subpaths and type-only helpers are excluded — they are not public API.
 *
 * NB: no `/`-star glob in this KDoc — Kotlin nests block comments, so it would open one.
 */
internal fun baseUiModules(
    reactDir: File,
    include: Set<String>,
): List<BaseUiModule> =
    include.sorted()
        .mapNotNull { id ->
            val dir = reactDir.resolve(id)
            val indexParts = dir.resolve("index.parts.d.ts")

            val parts = when {
                indexParts.exists() -> parseBaseUiParts(indexParts)

                // FLAT module: no namespace object, the value is exported directly
                // (`export { Button } from "./Button.js"` in `button/index.d.ts`).
                dir.resolve("index.d.ts").exists() -> parseBaseUiParts(dir.resolve("index.d.ts"))

                else -> {
                    println("Skipping Base UI module '$id': no index.parts.d.ts / index.d.ts")
                    return@mapNotNull null
                }
            }

            BaseUiModule(
                id = id,
                namespace = parseBaseUiNamespace(dir.resolve("index.d.ts")),
                parts = parts.filter { it.file.exists() },
            )
        }
        .filter { it.parts.isNotEmpty() }

/**
 * Synthesizes the module's namespace object — the values that make the generated types renderable.
 *
 * `menu/index.d.ts` exports its parts as one object (`export * as Menu from "./index.parts.js"`), and
 * the package's `exports` map has no wildcard entry, so a part's own subpath
 * (`@base-ui/react/menu/popup/MenuPopup`) is not importable at all: `Menu.Popup` is the only way to
 * reach a part at runtime. The Kotlin counterpart is an `external object` in a file annotated
 * `@file:JsModule("@base-ui/react/menu")`.
 *
 * A part is exposed as `FC<…Props>` only when that props interface is in [declaredTypes] — i.e. it
 * actually reached the generated Kotlin. The rest are listed in the object's documentation rather than
 * dropped in silence: in `menu` they are `Handle` (the `declare class MenuHandle`, which converts to an
 * empty body) and its `createHandle` factory, neither of which is a component.
 *
 * Returns `null` for a module with no namespace object (see [parseBaseUiNamespace]).
 */
internal fun baseUiNamespaceObject(
    module: BaseUiModule,
    declaredTypes: Set<String>,
): String? {
    val namespace = module.namespace ?: return null

    // An alias becomes a property name of the object and so must appear once — defensively, since no
    // `index.parts.d.ts` in 1.6.0 lists one twice (a file can back two, but under different aliases:
    // `Menu.Handle` and `Menu.createHandle` both come from `MenuHandle.d.ts`). Sorted to keep the member
    // order independent of the order `index.parts.d.ts` happens to list the parts in.
    val (components, unexposed) = module.parts
        .distinctBy { it.alias }
        .sortedBy { it.alias }
        .partition { "${it.declaredName}Props" in declaredTypes }

    // Each omission is logged as well as documented in the object: a part missing from an otherwise
    // plausible-looking namespace object is not visible in the generated output.
    for (part in unexposed)
        println("Base UI $namespace: no generated ${part.declaredName}Props, '${part.alias}' not exposed")

    if (components.isEmpty()) {
        println("Skipping Base UI namespace object '$namespace': no part has a generated props type")
        return null
    }

    val unexposedNote = if (unexposed.isEmpty()) "" else
        "\n *\n * Omitted, having no generated props type: " +
                unexposed.joinToString(", ") { "`${it.alias}`" } + "."

    val members = components.joinToString("\n") { part ->
        "val ${part.alias}: react.FC<${part.declaredName}Props>"
    }

    // Emitted without indentation, as every generated body is — `formatDeclarations` lays out the tree
    // afterwards.
    return """
/**
 * `export * as $namespace` from `@base-ui/react/${module.id}` — the module's only value export.
 *
 * The package's `exports` map has no wildcard entry, so a part's own subpath is not importable: this
 * object is the only way to reach a part at runtime.$unexposedNote
 */
external object $namespace {
$members
}
""".trim()
}

/**
 * Names of the `external interface` declarations in the given file bodies — the props and state types
 * that made it into Kotlin, which is what [baseUiNamespaceObject] checks a part against.
 *
 * Read off the emitted bodies rather than the `.d.ts`: a props type can be declared upstream and still
 * not be emitted (`ComboboxRootProps` is an `Omit<…> &` intersection, which has no Kotlin equivalent),
 * and a namespace member referring to a type that does not exist would not compile. The hand-written
 * bodies count too, so that a part whose props type is supplied as a stub is still exposed.
 */
internal fun baseUiDeclaredTypes(
    bodies: Iterable<String>,
): Set<String> =
    bodies.flatMapTo(mutableSetOf()) { body ->
        DECLARED_INTERFACE.findAll(body).map { it.groupValues[1] }
    }

// Generated bodies are emitted without indentation, so a declaration always starts its own line.
private val DECLARED_INTERFACE =
    Regex("""^(?:sealed )?external interface (\w+)""", RegexOption.MULTILINE)

/**
 * State-typed accessors for the three props Base UI puts on every element it renders, emitted into the
 * part's `.ext.kt`.
 *
 * `className`, `style` and `render` are `Any?` on the shared per-tag parents (`BaseUiDivProps` and its 16
 * siblings): each is a value-or-callback union over the *part's* state type, which a parent shared by 126
 * parts cannot name. The value arm needs nothing — `className = ClassName("popup")` assigns to `Any?`
 * as it is. The callback arm is what has no type-safe spelling otherwise, and is what these add:
 *
 *     Menu.Popup {
 *         className { state -> if (state.open) ClassName("open") else ClassName("closed") }
 *     }
 *
 * Emitted only for a part that renders an element — i.e. whose props extend one of the
 * [ELEMENT_PROPS_MARKER] interfaces — and only if its state type was generated. In `menu` that is 18 of
 * the 20 parts; `MenuRoot` and `MenuSubmenuRoot` render nothing and so have no such props to type.
 */
internal fun baseUiStateHelpers(
    componentName: String,
    body: String,
): String {
    val props = "${componentName}Props"
    val state = "${componentName}State"

    // No props declaration at all: the file holds no part props (`MenuHandle.d.ts`). Not worth a line of
    // log — unlike the two cases below, where a part that looks like it should get helpers does not.
    val parents = declarationParents(props, body)
        ?: return ""

    // The concrete marker, so that the documentation can point at the interface the prop is declared on.
    val marker = ELEMENT_PROPS_MARKER.find(parents)?.value
    if (marker == null) {
        // Parents come one per line at this point; collapsed so the log stays one line per part.
        val extends = parents.replace(WHITESPACE, " ").trim()
        println("Base UI: $props extends no BaseUi<Tag>Props ($extends), no state helpers")
        return ""
    }

    if (!DECLARED_INTERFACE.findAll(body).any { it.groupValues[1] == state }) {
        println("Base UI: no generated $state, no state helpers for $props")
        return ""
    }

    return """
/**
 * The state-dependent arm of `$props.className`, upstream
 * `string | ((state: $state) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is inherited through [$marker] from a parent shared by every part that
 * renders this tag, which cannot name one part's state type. Assign a [web.cssom.ClassName] directly
 * when the class does not depend on state.
 */
fun $props.className(
block: (state: $state) -> web.cssom.ClassName?,
) {
className = block
}

/**
 * The state-dependent arm of `$props.style`, upstream
 * `CSSProperties | ((state: $state) => CSSProperties | undefined)`. See [$props.className].
 */
fun $props.style(
block: (state: $state) -> react.CSSProperties?,
) {
style = block
}

/**
 * The callback arm of `$props.render`, upstream
 * `ReactElement | ((props: HTMLProps, state: $state) => ReactElement)`.
 *
 * `props` are the ones Base UI expects on the element the callback returns; upstream types them as its
 * own `HTMLProps`, which is `HTMLAttributes<any> & { ref }`. Assign a [react.ReactElement] directly to
 * render a fixed element instead.
 *
 * Applying them is the callback's job — `useRenderElement` calls `render(props, state)` and takes the
 * result as it is, merging nothing, so a callback that ignores `props` drops `ref` and the `data-*`
 * state attributes with them. `+props` inside the element builder does it (`Object.assign` underneath):
 *
 *     render { props, _ -> hr.create { +props } }
 *
 * That copies `children` as well, so a builder using it must not also add children of its own: the
 * wrappers' `jsx` reports "Both `children` source options used" and keeps the builder's, dropping the
 * ones that came in through `props`.
 */
fun $props.render(
block: (props: react.dom.html.HTMLAttributes<web.html.HTMLElement>, state: $state) -> react.ReactElement<*>,
) {
render = block
}
""".trim()
}

/**
 * The `extends` list of a generated `external interface`, or `null` when it declares none.
 *
 * Matched against the body as the converter emits it, which is neither indented nor line-wrapped the way
 * the committed tree is — that is `formatDeclarations`' work and it runs later. Two consequences: the colon has no
 * space before it (`MenuPopupProps: `), and each parent sits on its own line, so the list has to be read
 * across newlines up to the opening brace.
 *
 * Deliberately does not match a declaration carrying type parameters (`MenuRootProps<Payload>:`): were
 * generics preserved one day (BASE_UI_TODO.md gap 4), whatever is built on top of this would need the
 * parameter too, and getting nothing is the safe outcome until it does.
 */
private fun declarationParents(
    name: String,
    body: String,
): String? =
    Regex("""^(?:sealed )?external interface ${Regex.escape(name)}\s*:([^{]*)\{""", RegexOption.MULTILINE)
        .find(body)
        ?.groupValues?.get(1)
        // A parentless declaration is emitted with no body at all (`external interface MenuPortalState`),
        // in which case the match above would have run on to the next declaration's brace.
        ?.takeIf { "external " !in it }

// The interfaces that carry `className` / `style` / `render`: the per-tag parents from
// `BASE_UI_ELEMENT_PROPS` (`BaseUiDivProps`, `BaseUiSpanProps`, …) plus the hand-written stubs that
// extend one of them. `Ui`, not `UI`: `BaseUIChangeEventDetails` is a stub, not a marker, and must not
// match. Listed rather than resolved transitively — `declarationParents` reads one file's body, which
// does not contain the parents' own declarations.
private val ELEMENT_PROPS_MARKER = Regex("""\b(?:BaseUi\w+Props|FloatingPortalProps)\b""")

private val WHITESPACE = Regex("""\s+""")

/**
 * Rewrites `Component.Part`-style namespace references to the flat declarations they alias, then drops
 * the namespace blocks.
 *
 * Every part file ends with an alias layer over its own flat declarations:
 *
 *     export interface MenuPopupProps extends … {}
 *     export interface MenuPopupState {}
 *     export declare namespace MenuPopup {
 *       type Props = MenuPopupProps;
 *       type State = MenuPopupState;
 *     }
 *
 * The flat interfaces are the real declarations; the namespace only re-labels them. Kotlin has no
 * equivalent of a TS namespace, and Karakum's emulation of one via `external object` produces awkward
 * output — so the flat names are kept and every `MenuPopup.Props` reference is rewritten to
 * `MenuPopupProps`.
 *
 * The alias targets are read from the block itself rather than assumed to be `<NS><Member>`: that
 * concatenation holds for 568 of 584 aliases in 1.6.0, but hooks capitalize (`useRender.State` →
 * `UseRenderState`) and a few are genuine renames (`DrawerRoot.SnapPoint` → `DrawerSnapPoint`,
 * `OTPFieldRoot.ValidationType` → `OTPValidationType`, `AccordionRoot.Value` → `AccordionValue`).
 *
 * Only the target's leading identifier is substituted, dropping the alias' own type arguments, so that
 * type arguments from the use site survive: `type Props<Payload> = MenuRootProps<Payload>` turns
 * `MenuRoot.Props<Payload>` into `MenuRootProps<Payload>`, not `MenuRootProps<Payload><Payload>`.
 *
 * The alias map must be built across the whole module set ([buildBaseUiAliases]), not per file:
 * references cross file boundaries — `MenuSubmenuRoot.d.ts` extends `MenuRoot.Props`, whose namespace
 * block lives in `MenuRoot.d.ts`.
 */
/**
 * Whole-file adaptation of a Base UI `.d.ts` before the shared conversion runs: flattens namespace
 * aliases, and normalizes two shapes the converter cannot parse (see the individual steps).
 */
internal fun adaptBaseUiContent(
    content: String,
    aliases: Map<String, String>,
    knownTypes: Map<String, String>,
): String =
    content
        // Order matters: `dropCallSignatureInterfaces` scans `{\n` … `\n}` blocks, and once
        // `expandEmptyInterfaceBodies` has turned `{}` into `{\n}` that scan cannot terminate on the
        // empty body and runs on into the next declaration — deleting it if the span happens to contain
        // a call signature. `drawer/title/DrawerTitle.d.ts` and `.../DrawerDescription.d.ts` put
        // `export interface DrawerTitleState {}` directly before a callable interface and would lose
        // the State type silently.
        .dropCallSignatureInterfaces()
        .dropNonExportedInterfaces()
        .expandEmptyInterfaceBodies()
        .flattenBaseUiNamespaces(aliases)
        .substituteTypeParameterBounds(knownTypes)
        .interfaceStateAliases()
        .interfaceEventDetails()
        .resolveNamespaceStubs()
        .resolveComponentProps()
        .inlineForeignAliases()
        .arrowifyMethodSignatures()

/**
 * Removes a top-level `interface` that upstream does not export.
 *
 * The converter already declines to emit one, so this changes no output directly — what it changes is
 * that the *earlier* passes stop seeing it. `findDefaultUnions` builds a sealed type for every member
 * whose name is in `UnionFinder.UNION_PROPERTIES`, and it runs over the whole file: the non-exported
 * `SideFlipMode.align?: 'flip' | 'shift' | 'none'` in `utils/useAnchorPositioning.d.ts` produced a
 * `useAnchorPositioningAlign` union that nothing could ever refer to, because the interface holding its
 * only use was then dropped. A dead declaration in the public API is worse than a missing one — it is
 * also named after the file rather than the interface, since the union is named for the component.
 *
 * Two such interfaces exist in the generated set, both in `utils/useAnchorPositioning.d.ts`. (A third,
 * `slider/utils/getPushedThumbValues.d.ts`, is not reached at all — `index.parts.d.ts` does not list it.)
 */
private fun String.dropNonExportedInterfaces(): String =
    NON_EXPORTED_INTERFACE.replace(this, "")

// Anchored at column 0 on both ends: Base UI indents members by two spaces, so a nested object literal's
// closing brace cannot end the match. Non-greedy, so the first line-initial `}` closes the block.
//
// The `{}` alternative is not redundant. It is the same hazard documented above `dropCallSignatureInterfaces`
// — an empty body written on one line has no line-initial `}`, so a non-greedy scan would run on and take
// the *next* declaration's brace, deleting it silently. This pass runs before `expandEmptyInterfaceBodies`,
// so nothing else guards it. 1.6.0 has no such interface; the alternative is what keeps that from mattering.
private val NON_EXPORTED_INTERFACE = Regex(
    """^interface \w+[^{]*\{(?:}|.*?^})\n?""",
    setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL),
)

/**
 * Replaces a type imported from a package we do not generate with the definition it stands for.
 *
 * Such a name ordinarily widens to `Any?` with itself recorded in a marker, which is fine — that is what
 * `Middleware`, `FloatingContext` and `VirtualElement` get. `Padding` is the exception: it ends with one
 * of `KotlinType.kt`'s `KNOWN_TYPE_SUFFIXES` (seeded with the capitalized `UNION_PROPERTIES`, and `padding`
 * is one of those), so `kotlinType` claims to know it and emits the bare name — which resolves to
 * nothing in `baseui` and fails to compile. Substituting `@floating-ui/utils`' own definition both fixes
 * that and says more than the name would have.
 *
 * The same accident is what `substituteTypeParameterBounds` handles for type *parameters*; this is its
 * counterpart for imported names.
 */
private fun String.inlineForeignAliases(): String =
    FOREIGN_ALIASES.entries.fold(this) { content, (name, definition) ->
        Regex("""(?<![\w.])${Regex.escape(name)}(?![\w])""").replace(content, definition)
    }

private val FOREIGN_ALIASES = mapOf(
    // `@floating-ui/utils`: `number | Prettify<Partial<SideObject>>`, reached through
    // `UseAnchorPositioningSharedParameters.collisionPadding`.
    "Padding" to "number | Partial<SideObject>",
)

/**
 * Replaces a member whose type *is* one of its declaration's own type parameters with what upstream
 * bounds that parameter to.
 *
 * The converter drops type parameters from the declaration it emits (BASE_UI_TODO.md gap 4), which
 * leaves every member that mentioned one referring to a name that is no longer declared. That is
 * ordinarily harmless: an unknown name widens to `Any?` with the TypeScript recorded beside it, which is
 * what `MenuRootProps.payload` gets and is why `Payload` needs nothing here. It stops being harmless when
 * the name happens to be one [isKnownTypeName] answers for — `Value` is `Autocomplete`'s value parameter
 * in the MUI target and `SliderRootProps<Value extends number | readonly number[]>`'s in Base UI — and
 * the member then resolves against a type from the other package. Only those names are substituted, so
 * the rewrite touches exactly the declarations that would otherwise fail to compile.
 *
 * The replacement is the `extends` bound, or the parameter's own default when it declares no bound
 * (`RadioGroupProps<Value = any>`). Either is what the parameter meant at that position anyway, and says
 * more than its name would have: `value: Any?` with `number | readonly number[]` recorded beside it.
 *
 * Deliberately limited to the whole-member-type position. A parameter also appears inside larger
 * expressions — in `slider`, `(value: Value extends number ? number : Value, …) => void` — and those
 * have no Kotlin form whatever the parameter resolves to, so they are widened whole either way.
 * Substituting there would only replace upstream's own text in the marker with
 * `number | readonly number[] extends number ? …`.
 */
private fun String.substituteTypeParameterBounds(
    knownTypes: Map<String, String>,
): String =
    GENERIC_INTERFACE.replace(this) { match ->
        val (header, parameters, body) = match.destructured

        val substitutions = parameters.depthAwareSplitOnComma()
            .mapNotNull { parameter ->
                // `Value extends number | readonly number[] = number | readonly number[]` — the name comes
                // before both clauses, either of which may be absent.
                val name = parameter.substringBefore(" extends ").substringBefore(" = ").trim()
                if (name.isEmpty() || !isKnownTypeName(name, knownTypes))
                    return@mapNotNull null

                val bound = if (" extends " in parameter) parameter.substringAfter(" extends ").substringBefore(" = ").trim() else ""
                val default = if (" = " in parameter) parameter.substringAfter(" = ").trim() else ""

                when (val replacement = bound.ifEmpty { default }) {
                    // Neither clause: nothing to put in its place, and the member will resolve to the
                    // other package's type. No such parameter exists in 1.6.0 — logged rather than
                    // handled, since the failure is a compile error whose cause is not obvious.
                    "" -> {
                        println("Base UI: ${header.substringAfterLast(' ')} type parameter '$name' has no bound to substitute")
                        null
                    }

                    else -> name to replacement
                }
            }

        if (substitutions.isEmpty()) return@replace match.value

        val substituted = substitutions.fold(body) { acc, (name, replacement) ->
            val kotlinReplacement = if (replacement == "any") "Any" else replacement
            acc.replace(Regex("""AccordionValue<${Regex.escape(name)}>"""), "ReadonlyArray<$kotlinReplacement>")
                .replace(wholeMemberType(name), replacement)
        }

        "$header<$parameters>$substituted"
    }

// The parameter as a whole member type: `defaultValue?: Value | undefined;` or `AccordionValue<Value> | undefined;`. Anchored on the `: ` that
// opens the type and on the `;` that closes the member, so an occurrence anywhere else — a function
// parameter, a type argument, an arm of a conditional — is left alone.
private fun wholeMemberType(name: String): Regex =
    Regex("""(?<=: )(?:AccordionValue<)?${Regex.escape(name)}(?:>)?(?=(?: \| undefined)?;)""")

// `export interface X<params> …body…\n}` — the body runs to the first line-initial `}`, which is where
// every declaration in these files ends (`expandEmptyInterfaceBodies` has already given the empty ones
// a body of their own by this point).
private val GENERIC_INTERFACE = Regex(
    """^(export interface \w+)<((?:[^<>]|<[^<>]*>)*)>(.*?\n})""",
    setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL),
)

private fun String.depthAwareSplitOnComma(): List<String> =
    depthAwareSplit(',')
        .map { it.trim() }
        .filter { it.isNotEmpty() }

/**
 * Rewrites references to an interface declared *inside* a namespace to the hand-written stub standing in
 * for it.
 *
 * [flattenBaseUiNamespaces] resolves the `type Props = MenuPopupProps` members of a namespace, which is
 * every namespace in the part files. A handful of namespaces elsewhere in the package instead declare
 * their member as an `interface` with a body — there is no flat declaration to redirect to, because the
 * namespace member *is* the declaration:
 *
 *     export declare namespace FloatingPortal {
 *       interface Props<TState> extends BaseUIComponentProps<'div', TState> {
 *         container?: UseFloatingPortalNodeProps['container'] | undefined;
 *       }
 *     }
 *
 * Left alone, `MenuPortalProps extends FloatingPortal.Props<MenuPortalState>` loses its parent entirely:
 * a dotted name is not an identifier, so `ParentType.isAcceptableParent` drops it and the props end up
 * extending nothing but `react.Props` — no `children`, no `className`, no div attributes. All of those
 * come from `BaseUIComponentProps<'div', …>`, which the stub extends in the one form Kotlin can express.
 *
 * The use-site type argument is dropped, as [EVENT_DETAILS_ALIAS] and [resolveComponentProps] also do:
 * it only parameterizes the state type, the stubs are not generic, and [flattenBaseUiNamespaces] would
 * otherwise preserve it and produce `FloatingPortalProps<MenuPortalState>`.
 */
private fun String.resolveNamespaceStubs(): String =
    NAMESPACE_STUBS.entries.fold(this) { content, (reference, stub) ->
        stubReference(reference).replace(content, stub)
    }

// One level of nesting is tolerated in the argument list, as `COMPONENT_PROPS` does: the next entry due
// here is `AriaCombobox.Props`, whose only use site is
// `Omit<AriaCombobox.Props<Value, ModeFromMultiple<Multiple>>, …>`. The lookbehind keeps a longer dotted
// path from being rewritten through its tail (no such path exists in 1.6.0, but the table is meant to
// grow), and the lookahead keeps `FloatingPortal.PropsSomething` from matching.
private fun stubReference(reference: String): Regex =
    Regex(
        """(?<![A-Za-z0-9_.])""" + Regex.escape(reference) +
                """(?:<(?:[^<>]|<[^<>]*>)*>)?(?![A-Za-z0-9_])"""
    )

// `<Namespace>.<Member>` → the stub in `BASE_UI_STUBS` that replaces it. `AriaCombobox.Props` and
// `.Actions` have the same shape and will need entries here when combobox / autocomplete are added.
private val NAMESPACE_STUBS = mapOf(
    "FloatingPortal.Props" to "FloatingPortalProps",
)

/**
 * The stubs from [NAMESPACE_STUBS] that no generated declaration ended up referring to.
 *
 * A rewrite that stops matching is otherwise invisible: the reference just fails
 * `ParentType.isAcceptableParent` again and the props silently go back to extending nothing but
 * `react.Props`, taking `children` with them — which is the whole regression this machinery exists to
 * prevent. Upstream renaming or reshaping the namespace member is the way that happens.
 */
internal fun unusedNamespaceStubs(
    bodies: Iterable<String>,
): List<String> {
    val referenced = bodies.flatMapTo(mutableSetOf()) { body ->
        NAMESPACE_STUBS.values.filter { stub -> Regex("""\b$stub\b""").containsMatchIn(body) }
    }

    return NAMESPACE_STUBS.values.filter { it !in referenced }
}

/**
 * Parents named by the generated declarations that no generated declaration provides.
 *
 * `findParentType` accepts any bare identifier, so a parent whose declaration was never emitted is kept
 * and the tree stops compiling against a name that does not exist. The cause is never local to the
 * error: `SliderRootState extends FieldRootState` failed because `field` was not in `BASE_UI_MODULES`,
 * which is not something the Kotlin compiler can say. [declaredTypes] is already collected for the
 * namespace objects, so checking against it costs a pass over the bodies.
 *
 * In 1.6.0 the one module set that would trip this is `combobox` / `autocomplete`, whose
 * `AriaComboboxState` lives in a file `index.parts.d.ts` does not list — i.e. exactly what
 * `BASE_UI_EXTRA_FILES` is for.
 *
 * Qualified names are skipped: they are the `react.` / `web.` / `mui.` types the converter emits
 * directly, and nothing here declares them.
 */
internal fun unresolvedParents(
    bodies: Iterable<String>,
    declaredTypes: Set<String>,
): List<String> =
    bodies.flatMap { body ->
        // Declarations are emitted unindented and back to back, so each one runs to the next. A parent
        // list ends at the body brace, or at the end of the declaration when there is no body at all —
        // which is the shape this check most needs to see.
        body.split(Regex("""^(?=(?:sealed )?external interface )""", RegexOption.MULTILINE))
            .mapNotNull { declaration ->
                declaration
                    .substringBefore("{")
                    .substringAfter(':', "")
                    .takeIf { it.isNotBlank() }
            }
            .flatMap { parents ->
                parents.depthAwareSplit(',')
                    .map { it.trim().substringBefore('<') }
                    .filter { it.isNotEmpty() && '.' !in it && it !in declaredTypes }
            }
    }.distinct().sorted()

/**
 * `export interface MenuGroupLabelProps extends BaseUIComponentProps<'div', MenuGroupLabelState> {}`
 * → same declaration with the body on its own lines.
 *
 * The shared member extractor cuts bodies at `{\n` … `\n}` boundaries. A single-line `{}` body leaves
 * it unterminated, so it swallows the following declaration as a bogus member — `MenuGroupLabelProps`
 * came out holding a `var extends BaseUIComponentProps<'div', …> {}` member and the real
 * `MenuGroupLabelState` was consumed.
 */
private fun String.expandEmptyInterfaceBodies(): String =
    EMPTY_INTERFACE_BODY.replace(this) { it.groupValues[1] + "{\n}" }

private val EMPTY_INTERFACE_BODY = Regex("""^(export interface [^\n{]*)\{}$""", RegexOption.MULTILINE)

/**
 * Drops `export interface <Part> { <Payload>(props: …): React.JSX.Element }` — a callable interface
 * used as the component's own type (`export declare const MenuTrigger: MenuTrigger;`).
 *
 * Kotlin has no equivalent of a call-signature member, and the interface carries no props: it is the
 * component value's type, which is emitted as `FC<…Props>` in the module's namespace object instead.
 * Left in place, the converter emits `var <Payload>(componentProps: …)` — not valid Kotlin.
 */
private fun String.dropCallSignatureInterfaces(): String =
    INTERFACE_BLOCK.replace(this) { match ->
        val body = match.groupValues[2]
        if (CALL_SIGNATURE.containsMatchIn(body)) "" else match.value
    }

private val INTERFACE_BLOCK = Regex("""^export interface (\w+) \{\n(.*?)\n}\n""", setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))

// A member that is a call signature: `(props: X): Y;` or `<T>(props: X): Y;`
private val CALL_SIGNATURE = Regex("""^\s*(?:<[^>]*>)?\([^\n]*\):""", RegexOption.MULTILINE)

/**
 * Turns a part's state type into an interface when upstream declares it as an alias of another part's:
 *
 *     export type SliderLabelState = SliderRoot.State;
 *
 * The converter emits nothing for a bare type alias, so `SliderLabelState` did not exist — and with it
 * missing, [baseUiStateHelpers] found no state type and `Slider.Label` silently lost its
 * `className` / `style` / `render` helpers. Every other part declares its state as an interface, which
 * is why this only surfaced with the second module.
 *
 * Modelled as inheritance rather than a Kotlin `typealias`, which is what [interfaceEventDetails] does
 * with the identical shape one alias over. A subtype is not the same type, but nothing here depends on
 * the distinction: the state object only ever arrives as a callback parameter, and its members are what
 * that callback reads.
 *
 * Scoped to the `State`-to-`State` shape, both sides being the names of part declarations. The two
 * occurrences in 1.6.0 are `SliderLabel` and `SelectLabel`; the target is usually in another file, so
 * there is nothing local to check it against beyond the name.
 */
private fun String.interfaceStateAliases(): String =
    STATE_ALIAS.replace(this) { match ->
        val (name, target) = match.destructured

        "export interface $name extends $target {\n}\n"
    }

private val STATE_ALIAS = Regex("""^export type (\w+State) = (\w+State);\n""", RegexOption.MULTILINE)

/**
 * Turns the `…EventDetails` type aliases into interfaces, which is what the converter can emit.
 *
 * Runs after [flattenBaseUiNamespaces], so the right-hand side is already a flat name. Three shapes
 * occur, and all become an interface extending the aliased type:
 *
 *     export type MenuRootChangeEventDetails = BaseUIChangeEventDetails<MenuRootChangeEventReason> & {
 *       preventUnmountOnClose(): void;
 *     };
 *     export type MenuRadioGroupChangeEventDetails = MenuRootChangeEventDetails;
 *     export type SliderRootCommitEventDetails = BaseUIGenericEventDetails<SliderRootCommitEventReason>;
 *
 * As TS type aliases none of them are emitted: the first is an intersection, and the two
 * `BaseUI*EventDetails` bases are conditional types over a mapped reason→event table that cannot be
 * translated at all (they are provided as hand-written stubs instead). Modelling them as inheritance
 * keeps `reason` / `event` / `cancel()` reachable, which is the whole point of the type at a call site.
 *
 * The alias name is matched on the `EventDetails` suffix alone. It used to require `Change` or
 * `Highlight` before it, which silently dropped the whole declaration for the other four kinds the
 * package uses — `Commit` (`slider`, `number-field`), `Submit` (`form`), `Invalid` and `Complete`
 * (`otp-field`) — leaving the handler that takes one pointing at a type that was never emitted.
 *
 * The base's *first* type argument is dropped: it names the reason union, which parameterizes nothing
 * the stub declares. A *second* argument is not a parameterization at all but a bag of extra fields that
 * upstream intersects in (`BaseUIChangeEventDetails<Reason, CustomProperties>`), so it becomes a second
 * parent — otherwise `SliderRootChangeEventDetails` loses `activeThumbIndex` and the
 * `SliderRootChangeEventCustomProperties` that carries it is generated with nothing referring to it.
 */
private fun String.interfaceEventDetails(): String =
    EVENT_DETAILS_ALIAS.replace(this) { match ->
        val (name, base, typeArguments, members) = match.destructured

        val second = typeArguments.depthAwareSplitOnComma().getOrNull(1)
        val customProperties = when {
            second == null -> null

            // Assumed to be available because it is either declared in this file or imported from another.
            // (e.g. `ChangeEventCustomProperties` is imported from `../utils/types.js`).
            // `combobox` passes an inline object literal instead (`BaseUIGenericEventDetails<Reason, { … }>`),
            // which has no Kotlin form and no name to extend.
            !second.contains("{") -> second

            else -> {
                println("Base UI: $name drops the extra properties of $base<…, $second>")
                null
            }
        }

        val parents = listOfNotNull(base, customProperties).joinToString(", ")

        // The closing brace must start its own line: the shared member extractor cuts bodies at the
        // `\n}` boundary, and a brace trailing the last member makes it run on into whatever follows.
        "export interface $name extends $parents {${members.trimEnd()}\n}\n"
    }

// `export type XEventDetails = Base<args>[ & { members }];` — the arguments are captured rather than
// skipped so the second one can be kept (see above); one level of nesting is tolerated in them, as
// `COMPONENT_PROPS` does.
private val EVENT_DETAILS_ALIAS = Regex(
    """^export type (\w+EventDetails) = (\w+)(?:<((?:[^<>]|<[^<>]*>)*)>)?(?: & \{(.*?)\n})?;\n""",
    setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL),
)

/**
 * Rewrites `BaseUIComponentProps<'div', MenuPopupState>` to the per-tag `BaseUiDivProps` marker
 * interface (see `BASE_UI_ELEMENT_PROPS` in `Generator.kt`).
 *
 * Upstream, `BaseUIComponentProps` is a type-level computation that cannot be translated:
 *
 *     Omit<WithBaseUIEvent<React.ComponentPropsWithRef<ElementType>>,
 *          'className' | 'color' | 'defaultValue' | 'defaultChecked' | 'style'>
 *       & { className?, render?, style? }
 *
 * — a mapped type over a tag-indexed attribute table. Only 17 tags are ever passed, so each one gets a
 * hand-written Kotlin interface extending the matching `react.dom.html.*HTMLAttributes` and adding
 * `render`. Left unresolved, the parent is silently dropped and every part loses its DOM attributes.
 *
 * The state type argument is discarded: it only parameterizes the `className` / `style` / `render`
 * callbacks, which are typed per part by the generated `.ext.kt` helpers instead.
 */
private fun String.resolveComponentProps(): String =
    COMPONENT_PROPS.replace(this) { match ->
        val tag = match.groupValues[1]
        "BaseUi" + tag.replaceFirstChar(Char::uppercase) + "Props"
    }

// `BaseUIComponentProps<'div', State>` / `<'div', State<T>>` / `<'div', State, RenderProps>`, and the
// union-of-tags form `<'h1' | 'h2' | … , State>` used by `Popover.Title` and friends — the first tag
// wins there, which is also the element those parts render by default.
// The alternation tolerates one level of nesting in the trailing arguments.
private val COMPONENT_PROPS =
    Regex("""BaseUIComponentProps<'([a-z0-9]+)'(?:\s*\|\s*'[a-z0-9]+')*\s*,\s*(?:[^<>]|<[^<>]*>)*>""")

/**
 * `preventUnmountOnClose(): void;` → `preventUnmountOnClose: () => void;`
 *
 * The shared member converter understands arrow-typed properties (`unmount: () => void`) but not TS
 * method signatures, which it turns into `var preventUnmountOnClose(): Any?`. Base UI uses the method
 * form in its event-details and actions types.
 */
private fun String.arrowifyMethodSignatures(): String =
    METHOD_SIGNATURE.replace(this) { match ->
        val (indent, name, params, returnType) = match.destructured
        "$indent$name: ($params) => $returnType;"
    }

private val METHOD_SIGNATURE =
    Regex("""^(\s+)(\w+)\(([^)]*)\): ([\w.<>\[\]| ]+);$""", RegexOption.MULTILINE)

private fun String.flattenBaseUiNamespaces(
    aliases: Map<String, String>,
): String {
    var result = NAMESPACE_BLOCK.replace(this, "")

    // Longest first: `Menu.Root.Props` must not be partially rewritten by a `Menu.Root` entry.
    for ((reference, target) in aliases.entries.sortedByDescending { it.key.length }) {
        result = result.replace(
            Regex(Regex.escape(reference) + "(?![A-Za-z0-9_])"),
            Regex.escapeReplacement(target),
        )
    }

    return result
}

/**
 * Collects `<Namespace>.<Member>` → flat-declaration aliases from every given `.d.ts`, so that
 * cross-file references resolve. See [flattenBaseUiNamespaces].
 */
internal fun buildBaseUiAliases(
    files: Iterable<File>,
): Map<String, String> {
    val aliases = mutableMapOf<String, String>()

    for (file in files) {
        // Base UI ships its `.d.ts` without a trailing newline, and the namespace block is the last
        // declaration in the file — `NAMESPACE_BLOCK` needs the closing `}` to be newline-terminated,
        // so add it here as `convertDefinitions` does for the conversion pass.
        val content = file.readText()
            .replace("\r\n", "\n")
            .let { if (it.endsWith("\n")) it else "$it\n" }

        for (block in NAMESPACE_BLOCK.findAll(content)) {
            val (namespace, body) = block.destructured

            for (alias in NAMESPACE_ALIAS.findAll(body)) {
                val (member, target) = alias.destructured
                aliases["$namespace.$member"] = target
            }
        }
    }

    return aliases
}

private val NAMESPACE_BLOCK = Regex("""export declare namespace (\w+) \{\n(.*?)\n}\n""", RegexOption.DOT_MATCHES_ALL)

// `type Props<Payload = unknown> = MenuRootProps<Payload>;` → member `Props`, target `MenuRootProps`.
// The target is captured without its own type arguments (see flattenBaseUiNamespaces).
// The optional `<…>` is the alias' own parameter list, which may carry defaults containing `=`
// (`type Props<Payload = unknown> = …`), so it is matched up to its closing angle bracket — tolerating
// one level of nesting, as `GENERIC_INTERFACE` and `EVENT_DETAILS_ALIAS` do. Without that,
// `form`'s `type Props<FormValues extends Record<string, any> = Record<string, any>> = FormProps<…>`
// stops at the inner `>` and maps `Form.Props` to `Record`, which is worse than not mapping it: an
// unresolved dotted name is dropped by `isAcceptableParent`, whereas `Record` passes as an identifier.
//
// `(?:^|\n)`, not `\n`: `NAMESPACE_BLOCK` captures the body starting *at* its first member, so anchoring
// on a preceding newline dropped that member from every block — `MenuPopup.Props` and `MenuRoot.State`
// among them. Nothing referred to a first member until `slider`, whose `SliderLabelProps` is declared
// against `SliderLabel.State`.
private val NAMESPACE_ALIAS =
    Regex("""(?:^|\n)\s*type (\w+)(?:<(?:[^<>]|<[^<>]*>)*>)?\s*=\s*([A-Za-z0-9_.]+)""")
