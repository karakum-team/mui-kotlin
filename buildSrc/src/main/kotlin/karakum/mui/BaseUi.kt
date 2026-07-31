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

            BaseUiModule(id = id, parts = parts.filter { it.file.exists() })
        }
        .filter { it.parts.isNotEmpty() }

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
): String =
    content
        // Order matters: `dropCallSignatureInterfaces` scans `{\n` … `\n}` blocks, and once
        // `expandEmptyInterfaceBodies` has turned `{}` into `{\n}` that scan cannot terminate on the
        // empty body and runs on into the next declaration — deleting it if the span happens to contain
        // a call signature. `drawer/title/DrawerTitle.d.ts` and `.../DrawerDescription.d.ts` put
        // `export interface DrawerTitleState {}` directly before a callable interface and would lose
        // the State type silently.
        .dropCallSignatureInterfaces()
        .expandEmptyInterfaceBodies()
        .flattenBaseUiNamespaces(aliases)
        .interfaceEventDetails()
        .resolveComponentProps()
        .arrowifyMethodSignatures()

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
 * Turns the `…ChangeEventDetails` / `…HighlightEventDetails` type aliases into interfaces, which is
 * what the converter can emit.
 *
 * Runs after [flattenBaseUiNamespaces], so the right-hand side is already a flat name. Two shapes
 * occur, and both become an interface extending the aliased type:
 *
 *     export type MenuRootChangeEventDetails = BaseUIChangeEventDetails<MenuRootChangeEventReason> & {
 *       preventUnmountOnClose(): void;
 *     };
 *     export type MenuRadioGroupChangeEventDetails = MenuRootChangeEventDetails;
 *
 * As TS type aliases neither form is emitted: the first is an intersection, and `BaseUIChangeEventDetails`
 * itself is a conditional type over a mapped reason→event table that cannot be translated at all (it is
 * provided as a hand-written stub instead). Modelling them as inheritance keeps `reason` / `event` /
 * `cancel()` reachable, which is the whole point of the type at a call site.
 */
private fun String.interfaceEventDetails(): String =
    EVENT_DETAILS_ALIAS.replace(this) { match ->
        val (name, parent, members) = match.destructured

        // The closing brace must start its own line: the shared member extractor cuts bodies at the
        // `\n}` boundary, and a brace trailing the last member makes it run on into whatever follows.
        "export interface $name extends $parent {${members.trimEnd()}\n}\n"
    }

// `export type X(Change|Highlight)EventDetails = Parent<…>[ & { members }];`
// The parent's own type arguments are dropped: the stub is not generic (see BASE_UI_STUBS).
private val EVENT_DETAILS_ALIAS = Regex(
    """^export type (\w+(?:Change|Highlight)EventDetails) = (\w+)(?:<[^>]*>)?(?: & \{(.*?)\n})?;\n""",
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
// (`type Props<Payload = unknown> = …`), so it is matched up to its closing angle bracket.
private val NAMESPACE_ALIAS = Regex("""\n\s*type (\w+)(?:<[^>]*>)?\s*=\s*([A-Za-z0-9_.]+)""")
