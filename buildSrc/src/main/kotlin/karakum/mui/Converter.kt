package karakum.mui

import java.io.File

internal data class ConversionResult(
    val main: String,
    val extensions: String,
)

internal fun convertClasses(
    classesName: String,
    definitionFile: File,
): String {
    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val source = content.substringAfter("export interface $classesName {\n", "")

    if (source.isEmpty()) {
        check(classesName == "ContainerClasses" || classesName == "StackClasses")
        return "typealias $classesName = mui.system.$classesName"
    }

    return "sealed external interface $classesName {\n${getClassesContent(source)}\n}\n" +
            "\n" +
            "external val ${classesName.replaceFirstChar(Char::lowercase)}: $classesName\n"
}

internal fun convertInlineClasses(
    classesName: String,
    source: String,
): String {
    return "interface $classesName \n${getClassesContent(source)}"
}

private fun getClassesContent(
    source: String,
): String = source
    .substringBefore("\n}\n")
    .trimIndent()
    .splitToSequence("\n")
    .map {
        val name = it.removeSuffix(": string;").removeSuffix("?")
        if (name == it) return@map it
        val line = "val $name: ClassName"
        if (name.startsWith("'")) "    // $line" else line
    }
    .joinToString("\n")

internal fun convertDefinitions(
    definitionFile: File,
): ConversionResult {
    // MUI v6 sometimes uses `Component/index.d.ts` instead of `Component/Component.d.ts`.
    // Derive the component name from the parent directory in that case.
    val name = (if (definitionFile.name == "index.d.ts")
        definitionFile.parentFile.name
    else
        definitionFile.name.substringBefore("."))
        .removeSuffix("Props")

    val (content, defaultUnions) = definitionFile.readText()
        .replace("\r\n", "\n")
        .adaptRawContent()
        .removeInlineClasses()
        .removeDeprecated()
        .removeExtendsEmptyObject()
        .replace("(inProps: ", "(props: ")
        .replace(
            "interface PopperProps extends Omit<BasePopperProps, 'direction'>",
            "interface PopperProps extends BasePopperProps",
        )
        .replace(
            "StandardProps<Omit<PopoverProps, 'slots' | 'slotProps'>, 'children'>",
            "StandardProps<PopoverProps>",
        )
        .replace(
            "StandardProps<Omit<ModalProps, 'slots' | 'slotProps'>, 'children'>",
            "StandardProps<ModalProps>",
        )
        // MUI v6 multi-line `StandardProps<Foo, 'a' | 'b' | ...>` → collapse to `StandardProps<Foo>`
        // so existing single-arg ParentType.parseStandardProps logic can handle it.
        .replace(Regex("""StandardProps<\s*(\w+),[^<>]*>"""), "StandardProps<$1>")
        // MUI v6 wraps StandardProps in Omit for some components (Dialog, etc.) — unwrap.
        // `(?<!\w)` so we don't accidentally chew inside `DistributiveOmit<…>`.
        .replace(Regex("""(?<!\w)Omit<\s*(StandardProps<\w+>)\s*,[^<>]*>"""), "$1")
        // Generic Omit<SimpleType, 'a' | 'b'> → SimpleType (used in StepIcon's extends list).
        .replace(Regex("""(?<!\w)Omit<\s*(\w+)\s*,\s*'[^']+(?:'\s*\|\s*'[^']+)*'\s*>"""), "$1")
        // MUI v6 uses DistributiveOmit (from @mui/types) for some `extends` clauses.
        // Same semantic as Omit for our purposes — just unwrap to the first type arg.
        .replace(Regex("""DistributiveOmit<\s*(\w+)\s*,\s*'[^']+(?:'\s*\|\s*'[^']+)*'\s*>"""), "$1")
        // Pick<X, 'a' | 'b'> → X (we don't enforce key-narrowing in Kotlin).
        .replace(Regex("""Pick<\s*(\w+)\s*,\s*'[^']+(?:'\s*\|\s*'[^']+)*'\s*>"""), "$1")
        // Partial<X> in extends → X (TS optional-fields semantic is irrelevant for inheritance).
        // SCOPE to `extends ` and `,\s+` contexts only — `Partial<X>` inside member type signatures
        // is consumed by kotlinType()'s STANDARD_TYPE_MAP special-cases (e.g. `Partial<StandardInputProps>`).
        .replace(Regex("""(?<=\bextends )Partial<\s*(\w+(?:<[^<>]+>)?)\s*>"""), "$1")
        .replace(Regex("""(?<=,\s{4})Partial<\s*(\w+(?:<[^<>]+>)?)\s*>"""), "$1")
        // MUI v6 Menu / Popover do the inverse — `StandardProps<Omit<X, '...'>[, '...']>`.
        // Collapse to single-arg StandardProps<X>; parseStandardProps handles that form.
        .replace(Regex("""StandardProps<\s*Omit<\s*(\w+),[^<>]*>\s*(?:,[^<>]*)?>"""), "StandardProps<$1>")
        // MUI v6 declares per-component slot wiring as a TS type alias:
        //   `export type XxxSlotsAndSlotProps = CreateSlotsAndSlotProps<XxxSlots, { ...inline... }>;`
        // and then `extends ..., XxxSlotsAndSlotProps`. Convert the type alias to a real
        // interface so the generator emits `slots?: XxxSlots` and `slotProps?: ...` fields.
        // Inner SlotProps typing is collapsed to `any` for now (see MUI_V6_TODO.md).
        .convertSlotsAndSlotPropsAliases()
        // Components that extend Modal/Popover/InputBase already inherit `slots`/`slotProps`
        // of the parent's types. Their own `XxxSlotsAndSlotProps` would create a diamond
        // (Kotlin's invariant `var` rejects `ModalSlots? vs DialogSlots?`). Drop the
        // component-specific extends — slot fields are still typed (as parent's slots) via the
        // parent inheritance. The XxxSlotsAndSlotProps interface itself is still emitted, so
        // users can cast/access it directly if needed.
        // Also strip `TextFieldSlotsAndSlotProps<...>` (TS generic — our regex doesn't currently
        // produce that interface form; see MUI_V6_TODO.md for the parser limitation).
        .replace(
            Regex(""",\s+(?:Dialog|Drawer|Menu|Popover|SwipeableDrawer|OutlinedInput|FilledInput|Checkbox|Radio|Switch)SlotsAndSlotProps"""),
            ""
        )
        .replace(Regex(""",\s+TextFieldSlotsAndSlotProps<[^<>]*>"""), "")
        // Some `Slots`/`SlotsAndSlotProps`-related interfaces are declared without `export`
        // (e.g. internal SwitchBaseSlots, OutlinedInputSlots). Normalize so findAdditionalProps
        // / convertSlotsAndSlotPropsAliases pick them up.
        .replace(Regex("""(\A|\n)interface (\w+Slots\b)"""), "$1export interface $2")
        // TS indexed access types used in function args (UsePaginationItem['page'] etc.)
        // — go through FunctionType.toFunctionType which doesn't look at TypeAlias.ALIAS_MAP,
        // so collapse here to the primitive type the alias would produce.
        .replace("UsePaginationItem['page']", "Number?")
        .replace("UsePaginationItem['selected']", "Boolean")
        .replace(
            "\ninterface ${name}OwnProps {\n",
            "\nexport interface ${name}OwnProps {\n",
        )
        .replace(
            "extends Omit<TypographyTypeMap['props'], 'classes'>",
            "extends TypographyProps",
        )
        .replace(
            "extends Omit<React.HTMLAttributes<HTMLDivElement>, 'contentEditable'>",
            "extends React.HTMLAttributes<HTMLDivElement>",
        )
        .let { findDefaultUnions(name, it) }

    val declarations = mutableListOf<String>()

    val propsName = "${name}Props"

    findProps(name, propsName, content)
        ?.also(declarations::add)

    findMapProps(name, propsName, content)
        ?.also(declarations::add)

    val additionalInterfaces = findAdditionalProps(propsName, content)
    val functionInterfaces = additionalInterfaces
        .filter { "interface Spacing {" in it }

    declarations += (additionalInterfaces - functionInterfaces)

    val fun0Declaration = "export default function $name<"

    val fun1Declaration = "export default function $name(props: $propsName): JSX.Element;"
    val fun2Declaration = "export default function $name(props: $propsName): React.JSX.Element;"

    val fun3Declaration = "declare function $name(props: $propsName): JSX.Element;"
    val fun4Declaration = "declare function $name(props: $propsName): React.JSX.Element;"

    val typeDeclaration = "declare const $name: React.ComponentType<$propsName>;"
    val const1Declaration = "declare const $name: "
    val const2Declaration = "export default _default"

    declarations += listOfNotNull(
        findComponent(name, propsName, fun0Declaration, content),
        findComponent(name, propsName, fun1Declaration, content),
        findComponent(name, propsName, fun2Declaration, content),
        findComponent(name, propsName, fun3Declaration, content),
        findComponent(name, propsName, fun4Declaration, content),
        findComponent(name, propsName, typeDeclaration, content, "ComponentType"),
        findComponent(name, propsName, const1Declaration, content),
        findComponent(name, propsName, const2Declaration, content),
    ).take(1)

    declarations += listOfNotNull(
        findDefaultFunction(name, content)
    )

    // MUI v6 re-declares Grid* union types in each Grid variant file.
    // Keep them only in the canonical Grid.d.ts; skip in Grid2/PigmentGrid.
    val skipDuplicateUnions = name in setOf("Grid2", "PigmentGrid")

    val enums = content.splitToSequence("export type ", "export declare type ")
        .drop(1)
        .map { it.substringBefore(";") }
        .filter { typeDecl ->
            if (!skipDuplicateUnions) return@filter true
            val typeName = typeDecl.substringBefore(" ").substringBefore("<")
            typeName !in setOf("GridDirection", "GridWrap", "GridSpacing", "GridSize")
        }
        .mapNotNull { convertUnion(it) }
        .plus(defaultUnions)
        .toList()

    val mainContent = fixOverrides(
        name = name,
        content = declarations.joinToString("\n\n")
    )

    return ConversionResult(
        main = mainContent,
        extensions = (enums + functionInterfaces).joinToString("\n\n"),
    )
}

private fun String.removeInlineClasses(): String =
    removeInlineClasses("  classes?: ")

/**
 * MUI v6 declares per-component slot wiring as a TS type alias:
 * ```
 * export type XxxSlotsAndSlotProps = CreateSlotsAndSlotProps<XxxSlots, { ...inline-slotProps... }>;
 * ```
 * Convert each such alias into a real interface
 * ```
 * export interface XxxSlotsAndSlotProps { slots?: XxxSlots; slotProps?: any; }
 * ```
 * so the generator's findAdditionalProps emits `slots`/`slotProps` fields on every component that
 * extends this interface. Inner SlotProps typing is collapsed to `any` for now.
 */
private fun String.convertSlotsAndSlotPropsAliases(): String {
    // Some `XxxSlotsAndSlotProps` aliases are declared without `export ` (e.g. SwitchBase) —
    // normalize them so the export-prefix logic below catches both forms.
    val normalized = replace(
        Regex("""(\A|\n)type (\w+SlotsAndSlotProps\s*=\s*CreateSlotsAndSlotProps)"""),
        "$1export type $2"
    )
    return normalized.convertExportSlotsAndSlotPropsAliases()
}

private fun String.convertExportSlotsAndSlotPropsAliases(): String {
    val prefix = "export type "
    val marker = "SlotsAndSlotProps = CreateSlotsAndSlotProps<"

    val result = StringBuilder()
    var cursor = 0
    while (cursor < length) {
        val typeStart = indexOf(prefix, cursor).takeIf { it >= 0 } ?: break
        val nameStart = typeStart + prefix.length
        val nameEnd = indexOf(marker, nameStart)
        if (nameEnd < 0) {
            result.append(this, cursor, length)
            return result.toString()
        }
        val interfaceName = substring(nameStart, nameEnd)
        if (!interfaceName.matches(Regex("\\w+"))) {
            // not a simple identifier — skip this match
            result.append(this, cursor, nameEnd + marker.length)
            cursor = nameEnd + marker.length
            continue
        }
        // Position right after `CreateSlotsAndSlotProps<` — find balanced `>` close.
        val ltOpen = nameEnd + marker.length
        var depth = 1
        var i = ltOpen
        while (i < length && depth > 0) {
            when (this[i]) {
                '<' -> depth++
                '>' -> depth--
            }
            if (depth == 0) break
            i++
        }
        if (depth != 0) {
            result.append(this, cursor, length)
            return result.toString()
        }
        val gtClose = i
        // Inside `<...>` we expect `XxxSlots,\n   {...inline...}`. Extract slot interface name
        // (before first top-level comma); inline `{ ... }` second arg gives us per-slot members.
        val inside = substring(ltOpen, gtClose)
        val topComma = run {
            var bracketDepth = 0
            var commaIdx = -1
            for (j in inside.indices) {
                when (inside[j]) {
                    '<', '{', '(' -> bracketDepth++
                    '>', '}', ')' -> bracketDepth--
                    ',' -> if (bracketDepth == 0) {
                        commaIdx = j
                        break
                    }
                }
            }
            commaIdx
        }
        val slotInterface = (if (topComma >= 0) inside.substring(0, topComma) else inside).trim()
        val secondArgRaw = if (topComma >= 0) inside.substring(topComma + 1).trim() else ""
        // Parse the inline `{ ... }` for per-slot members. Falls back to empty list if shape isn't
        // a simple object literal — we then emit `slotProps?: any` as before.
        val slotMembers: List<Pair<String, String>> = if (secondArgRaw.startsWith("{")) {
            var bd = 1
            var closeIdx = -1
            for (j in 1 until secondArgRaw.length) {
                when (secondArgRaw[j]) {
                    '{' -> bd++
                    '}' -> {
                        bd--; if (bd == 0) {
                            closeIdx = j; break
                        }
                    }
                }
            }
            if (closeIdx > 0) parseInlineSlotProps(secondArgRaw.substring(1, closeIdx))
            else emptyList()
        } else emptyList()
        // Consume the rest of the declaration up to `;\n` — handles plain
        // `;\n` AND intersection tails like ` & { slots?: …; slotProps?: … };`.
        var endOfDecl = gtClose + 1
        var bDepth = 0
        var lDepth = 0
        while (endOfDecl < length) {
            when (this[endOfDecl]) {
                '<' -> lDepth++
                '>' -> lDepth--
                '{' -> bDepth++
                '}' -> bDepth--
                ';' -> if (bDepth == 0 && lDepth == 0) {
                    endOfDecl++
                    break
                }
            }
            endOfDecl++
        }
        if (endOfDecl < length && this[endOfDecl] == '\n') endOfDecl++

        result.append(this, cursor, typeStart)

        // Emit per-slot XxxSlotProps interface (when we managed to parse the inline shape).
        // TS-type goes into a JSDoc comment above the field (preserves info without nesting).
        // Try to map the inner SlotProps<RootComponent, …> to a concrete type when RootComponent
        // matches one of our recognized forms (string-literal tag, typeof X, React.ElementType<X>).
        val slotPropsTypeName: String = if (slotMembers.isNotEmpty()) {
            val slotPropsInterfaceName = "${interfaceName}SlotProps"
            result.append("export interface ").append(slotPropsInterfaceName).append(" {\n")
            for ((memberName, tsType) in slotMembers) {
                if (tsType.isNotEmpty()) {
                    val safe = tsType.replace("*/", "*\\/").lineSequence().joinToString(" ") { it.trim() }
                    result.append("  /** TS: ").append(safe).append(" */\n")
                }
                val mapped = mapSlotPropsToKotlin(tsType)
                result.append("  ").append(memberName).append("?: ").append(mapped ?: "any").append(";\n")
            }
            result.append("}\n")
            slotPropsInterfaceName
        } else "any"

        result.append("export interface ")
            .append(interfaceName)
            .append("SlotsAndSlotProps {\n  slots?: ")
            .append(slotInterface)
            .append(";\n  slotProps?: ")
            .append(slotPropsTypeName)
            .append(";\n}\n")
        cursor = endOfDecl
    }
    if (cursor < length) result.append(this, cursor, length)
    return result.toString()
}

/**
 * Map a single TS slot type like `SlotProps<'input', Overrides, OwnerState>` to a TS-form
 * RootComponent type that the rest of the generator (`STANDARD_TYPE_MAP` + identifier resolution)
 * can then turn into Kotlin. Returns null when the form isn't recognized — caller falls back to `any`.
 *
 * Handled patterns (both `SlotProps<…>` and `SlotComponentProps<…>`):
 *   - String-literal tag: `SlotProps<'input', …>` → `React.InputHTMLAttributes<HTMLInputElement>`
 *   - `typeof X`:         `SlotProps<typeof Modal, …>` → `ModalProps`
 *   - `React.ElementType<X>`: `SlotProps<React.ElementType<SwitchBaseProps>, …>` → `SwitchBaseProps`
 */
private fun mapSlotPropsToKotlin(tsType: String): String? {
    if (tsType.isEmpty()) return null
    val normalized = tsType.lineSequence().joinToString(" ") { it.trim() }

    Regex("""^Slot(?:Component)?Props<\s*'(\w+)'\s*,""").find(normalized)?.let { m ->
        return TAG_TO_HTML_ATTRS_TS[m.groupValues[1]]
    }
    Regex("""^Slot(?:Component)?Props<\s*typeof\s+(\w+)\s*,""").find(normalized)?.let { m ->
        return "${m.groupValues[1]}Props"
    }
    Regex("""^Slot(?:Component)?Props<\s*React\.ElementType<(\w+)>\s*,""").find(normalized)?.let { m ->
        return m.groupValues[1]
    }
    // Pattern 4: nested-generic ElementType, common in PaginationItem:
    //   SlotProps<React.ElementType<React.HTMLProps<X>>, …> → React.HTMLAttributes<X>
    //   (HTMLProps and HTMLAttributes are semantically equivalent in MUI usage.)
    Regex("""^Slot(?:Component)?Props<\s*React\.ElementType<React\.HTMLProps<(\w+)>>""").find(normalized)?.let { m ->
        return "React.HTMLAttributes<${m.groupValues[1]}>"
    }
    return null
}

/** TS-form tag → React HTMLAttributes variant. The TS strings are matched in STANDARD_TYPE_MAP. */
private val TAG_TO_HTML_ATTRS_TS = mapOf(
    "input" to "React.InputHTMLAttributes<HTMLInputElement>",
    "div" to "React.HTMLAttributes<HTMLDivElement>",
    "span" to "React.HTMLAttributes<HTMLSpanElement>",
    "button" to "React.ButtonHTMLAttributes<HTMLButtonElement>",
    "a" to "React.AnchorHTMLAttributes<HTMLAnchorElement>",
    "label" to "React.LabelHTMLAttributes<HTMLLabelElement>",
    "li" to "React.LiHTMLAttributes<HTMLLIElement>",
    "fieldset" to "React.FieldsetHTMLAttributes<HTMLFieldSetElement>",
    "form" to "React.FormHTMLAttributes<HTMLFormElement>",
    "img" to "React.ImgHTMLAttributes<HTMLImageElement>",
)

/** Parse the inline `{ root: SlotProps<…>; input: …; }` second arg of `CreateSlotsAndSlotProps`. */
private fun parseInlineSlotProps(inline: String): List<Pair<String, String>> {
    val out = mutableListOf<Pair<String, String>>()
    var i = 0
    while (i < inline.length) {
        // skip whitespace
        while (i < inline.length && inline[i].isWhitespace()) i++
        if (i >= inline.length) break
        // skip JSDoc / block comment
        if (i < inline.length - 1 && inline[i] == '/' && inline[i + 1] == '*') {
            val end = inline.indexOf("*/", i + 2)
            if (end < 0) break
            i = end + 2
            continue
        }
        // read identifier name
        val nameStart = i
        while (i < inline.length && (inline[i].isLetterOrDigit() || inline[i] == '_')) i++
        if (i == nameStart) {
            i++
            continue
        }
        val name = inline.substring(nameStart, i)
        // optional `?`
        if (i < inline.length && inline[i] == '?') i++
        // expect `:`
        if (i >= inline.length || inline[i] != ':') continue
        i++
        // read type up to top-level `;`
        val typeStart = i
        var depth = 0
        while (i < inline.length) {
            when (inline[i]) {
                '<', '{', '(' -> depth++
                '>', '}', ')' -> depth--
                ';' -> if (depth == 0) break
            }
            i++
        }
        val tsType = inline.substring(typeStart, i).trim()
        out += name to tsType
        if (i < inline.length && inline[i] == ';') i++
    }
    return out
}

private fun String.removeDeprecated(): String {
    if ("interface MuiMediaQuery" !in this)
        return this

    return substringAfter(substringBefore("export interface UseMediaQueryOptions {"))
}

private fun String.removeExtendsEmptyObject(): String {
    // TODO: Probably need to replace not only for `TValue` but for all others
    return replace(
        "<TValue extends {}>",
        "<TValue>",
    )
}

private fun String.removeInlineClasses(
    trigger: String,
): String {
    if (trigger !in this)
        return this

    val parts = split(trigger)
    if (parts.size != 2)
        return this

    val (s, e) = parts

    val type = when {
        e.startsWith("Partial<BadgeClasses> & {")
            -> "BadgeClasses"

        e.startsWith("Partial<ButtonClasses> & {")
            -> "mui.material.ButtonClasses"

        e.startsWith("{")
            -> "unknown"

        else -> return this
    }

    return s + "  classes?: $type;" + e.substringAfter("};")
}

private fun findProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    when (name) {
        "TextField",
            -> return "typealias $propsName = BaseTextFieldProps"

        "DateField",
        "StaticDatePicker",
        "StaticDateTimePicker",
        "StaticTimePicker",
            -> return props(propsName)
    }

    val propsContent = sequenceOf(" ", "<", "\n")
        .flatMap {
            sequenceOf(
                content.substringAfter("export default interface $propsName$it", ""),
                content.substringAfter("export interface $propsName$it", ""),
            )
        }
        .singleOrNull { it.isNotEmpty() }
        ?: return null

    val propsDeclaration = when {
        propsContent.contains("TDate") && propsContent.contains("TView") && propsContent.contains("TEnableAccessibleFieldDOMStructure")
            -> "$propsName<TDate, TView, TEnableAccessibleFieldDOMStructure>"

        propsContent.contains("TDate") && propsContent.contains("TView")
            -> "$propsName<TDate, TView>"

        propsContent.contains("TDate") && propsContent.contains("TLocale")
            -> "$propsName<TDate, TLocale>"

        propsContent.contains("TDate") && propsContent.contains("TEnableAccessibleFieldDOMStructure")
            -> "$propsName<TDate, TEnableAccessibleFieldDOMStructure>"

        propsContent.startsWith("TDate>") || propsContent.startsWith("TDate extends ")
            -> "$propsName<TDate>"

        propsContent.startsWith("TValue>") || propsContent.startsWith("TValue extends ")
            -> "$propsName<TValue>"

        propsContent.startsWith("OptionValue>") || propsContent.startsWith("OptionValue extends ")
            -> "$propsName<OptionValue>"

        propsContent.startsWith("TOption>") || propsContent.startsWith("TOption extends ")
            -> "$propsName<TOption>"

        propsContent.startsWith("T = unknown>")
            -> "$propsName<T>"

        propsContent.startsWith("Value = unknown>")
            -> "$propsName<Value>"

        propsName == "AutocompleteProps"
            -> "$propsName<Value>"

        else -> propsName
    }

    val parentType = findParentType(
        content.substringBefore(propsContent).substringAfterLast("\n") + propsContent,
    )

    val source = propsContent
        .substringAfter("{\n")

    val membersContent = source
        .takeIf { !it.startsWith("}\n") }
        ?.substringBefore(";\n}")
        ?: ""

    val body = if (source.startsWith("}"))
        ""
    else
        convertMembers(membersContent)

    return props(
        propsName = propsDeclaration,
        parentType = parentType,
        hasChildren = CHILDREN in body,
        hasClassName = CLASS_NAME in body,
        hasSx = SX in body,
    ) + " {\n$body\n}"
}

private fun findMapProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    val propsContent = sequenceOf(
        content.substringAfter("export interface ${name}TypeMap<", "")
            .substringBefore("\n}\n"),
        content.substringAfter("export type ${name}TypeMap<", "")
            .substringBefore("\n}>;\n"),
    ).firstOrNull { it.isNotEmpty() }
        ?: return null

    var intrinsicType = propsContent
        .substringBefore(" {\n")
        .let { str ->
            sequenceOf(
                // todo remove when mui migrates on DefaultComponent generic in all places
                str.substringAfter(" D extends React.ElementType = '", ""),
                str.substringAfter(" DefaultComponent extends React.ElementType = '", ""),
                str.substringAfter(" RootComponent extends React.ElementType = '", ""),
            ).maxByOrNull { it.length }!!
        }

        .substringBefore("'", "")

    if (intrinsicType.isEmpty()) {
        intrinsicType = propsContent
            .substringBefore(" {\n")
            .substringAfter(" RootComponentType extends React.ElementType = '", "")
            .substringBefore("'", "")
    }

    if (name == "AppBar")
        intrinsicType = "div"

    if (name == "Tab")
        intrinsicType = "button"

    val parentType: String? = when {
        "${name}BaseProps & {" in propsContent || "props: AdditionalProps & ${name}BaseProps;" in propsContent
            -> {
            val baseType = "${name}BaseProps"

            if (baseType == "LinkBaseProps") {
                // TODO: Also we need to provide `baseType` here too
                //  Details: But now we skip `baseType`(`LinkBaseProps`) of `LinkProps` because in truth `LinkBaseProps` = `TypographyOwnProps` but there are some inheritance problem  with this type
                INTRINSIC_TYPE_MAP[intrinsicType]
            } else {
                if (name.startsWith("List")) {
                    sequenceOf(baseType, "react.dom.html.HTMLAttributes<web.html.HTMLElement>")
                        .joinToString(",\n", "\n")
                } else baseType
            }
        }

        "props: ${name}OwnProps & AdditionalProps;" in propsContent
            -> {
            val intrinsicProps = when (propsName) {
                "InputProps" -> "react.dom.html.HTMLAttributes<web.html.HTMLInputElement>"
                else -> INTRINSIC_TYPE_MAP[intrinsicType]
            }

            sequenceOf("${name}OwnProps", intrinsicProps)
                .joinToString(",\n", "\n")
        }

        "props: AdditionalProps & ${name}OwnProps" in propsContent || "props: AdditionalProps &\n    ${name}OwnProps" in propsContent
            -> {
            val intrinsicProps = INTRINSIC_TYPE_MAP[intrinsicType]

            sequenceOf(
                "mui.material.ButtonProps".takeIf { name == "LoadingButton" },
                "${name}OwnProps",
                intrinsicProps,
            ).filterNotNull().joinToString(",\n", "\n")
        }

        "DistributiveOmit<PaperOwnProps" in propsContent
            -> "PaperOwnProps"

        "${name}TypeMap<{" in propsContent
            -> "mui.base.${name}Props"

        else -> INTRINSIC_TYPE_MAP[intrinsicType]
    }

    val membersContent = propsContent
        .let { str ->
            sequenceOf(
                // todo remove when mui migrates on AdditionalProps generic in all places
                str.substringAfter("props: P", ""),
                str.substringAfter("props: AdditionalProps", ""),
            ).maxByOrNull { it.length }!!
        }
        .substringAfter(" & {\n", "")
        .let { str ->
            sequenceOf(
                str.substringBefore(";\n    } &", ""),
                str.substringBefore(";\n    };", ""),
                str.substringBefore(";\n  } &", ""),
                str.substringBefore(";\n  };", ""),
            ).maxByOrNull { it.length }!!
        }

    val hasComponent = ": OverridableComponent<" in content
            || "&\n  OverridableComponent<" in content
            || "& {\n  component?: React.ElementType;\n};" in content

    return if (membersContent.isNotEmpty()) {
        val body = convertMembers(membersContent)
        props(
            propsName = propsName,
            parentType = parentType,
            hasChildren = CHILDREN in body,
            hasClassName = CLASS_NAME in body,
            hasSx = SX in body,
            hasComponent = hasComponent,
        ) + " {\n$body\n}"
    } else {
        props(
            propsName = propsName,
            parentType = if (propsName == "TabListProps") "mui.material.TabsProps" else parentType,
            hasComponent = hasComponent,
        )
    }
}

private val EXCLUDED_PREFIXES = setOf(
    "Map",
    "Overrides",

    // TEMP
    "Header",
)

private fun findAdditionalProps(
    propsName: String,
    content: String,
): List<String> {
    var delimiters = arrayOf("export interface ", "export default interface ")
    if ("interface BaseTheme" in content)
        delimiters += "interface "

    val bodies = content.splitToSequence(*delimiters)
        .drop(1)
        .toList()

    if (bodies.isEmpty())
        return emptyList()

    return bodies.mapNotNull { body ->
        val interfaceName = body
            .substringBefore(" ")
            .substringBefore("\n")
            .substringBefore("<")

        val propsLike = interfaceName.endsWith("Props")
        // TODO: check
        if (propsLike && interfaceName == propsName && interfaceName != "UseButtonProps")
            return@mapNotNull null

        // Empty interfaces like `export interface Typography {}` or
        // `export interface XxxOwnerState\n  extends PartiallyRequired<...> {}` (MUI v6)
        // — drop extends clause we can't parse, emit plain empty external interface.
        // Find the body-opening `{` outside any generic `<...>` clause to ignore default-value
        // braces like `<AdditionalProps = {}>`.
        run {
            var ltDepth = 0
            var bodyBrace = -1
            for (i in body.indices) {
                when (body[i]) {
                    '<' -> ltDepth++
                    '>' -> ltDepth--
                    '{' -> if (ltDepth == 0) {
                        bodyBrace = i; break
                    }
                }
            }
            if (bodyBrace > 0) {
                val afterBrace = body.substring(bodyBrace + 1).trimStart()
                if (afterBrace.startsWith("}")) {
                    // Preserve TS type parameters so generic usages (e.g. `DatePickerSlots<TDate>`)
                    // still resolve. Strip `extends X` bounds — we just need the param names.
                    val afterName = body.substring(interfaceName.length)
                    val typeParams = if (afterName.startsWith("<")) {
                        var d = 0
                        var end = -1
                        for (i in afterName.indices) {
                            when (afterName[i]) {
                                '<' -> d++
                                '>' -> {
                                    d--; if (d == 0) {
                                        end = i; break
                                    }
                                }
                            }
                        }
                        if (end > 0) {
                            afterName.substring(1, end)
                                .split(",")
                                .map { it.trim().substringBefore(" ").substringBefore("=").trim() }
                                .filter { it.isNotEmpty() }
                                .joinToString(", ", "<", ">")
                        } else ""
                    } else ""
                    return@mapNotNull "external interface $interfaceName$typeParams"
                }
            }
        }

        if (interfaceName == "ValueLabelProps" || interfaceName == "UseButtonProps")
            return@mapNotNull null

        if (interfaceName == "ExtendableDateType")
            return@mapNotNull null

        if (!propsLike && EXCLUDED_PREFIXES.any { interfaceName.endsWith(it) })
            return@mapNotNull null

        val parentType = findParentType(body)

        val membersContent = when (interfaceName) {
            "ListItemButtonOwnProps",

            "InputBaseComponentProps",
            "CustomSystemProps",

            "CalendarPickerSlotsComponent",
            "CalendarPickerSlotsComponentsProps",

            "ClockPickerSlotsComponent",
            "ClockPickerSlotsComponentsProps",

            "DatePickerSlotsComponent",
            "DatePickerSlotsComponentsProps",

            "DateTimePickerSlotsComponent",
            "DateTimePickerSlotsComponentsProps",

            "DesktopDatePickerSlotsComponent",
            "DesktopDatePickerSlotsComponentsProps",

            "DesktopDateTimePickerSlotsComponent",
            "DesktopDateTimePickerSlotsComponentsProps",

            "DesktopTimePickerSlotsComponent",
            "DesktopTimePickerSlotsComponentsProps",

            "MobileDatePickerSlotsComponent",
            "MobileDatePickerSlotsComponentsProps",

            "MobileDateTimePickerSlotsComponent",
            "MobileDateTimePickerSlotsComponentsProps",

            "MobileTimePickerSlotsComponent",
            "MobileTimePickerSlotsComponentsProps",

            "StaticDatePickerSlotsComponent",
            "StaticDatePickerSlotsComponentsProps",

            "StaticDateTimePickerSlotsComponent",
            "StaticDateTimePickerSlotsComponentsProps",

            "StaticTimePickerSlotsComponents",
            "StaticTimePickerSlotsComponentsProps",

            "TimePickerSlotsComponent",
            "TimePickerSlotsComponentsProps",
                -> ""

            "Spacing",
                -> return@mapNotNull convertSpacing(
                name = interfaceName,
                body = body.substringAfter("{\n")
                    .substringBefore("\n}\n")
                    .trimIndent()
            )

            "MixinsOptions",
                -> body.substringAfter("{\n")
                .substringBefore("\n}\n")

            else
                -> body.substringAfter("{\n")
                .substringBefore(";\n}\n")
        }

        var propsBody = when {
            interfaceName.endsWith("Actions")
                -> convertMethods(membersContent)

            interfaceName == "IUtils"
                -> convertDateUtils(membersContent)

            else -> convertMembers(membersContent)
        }

        when (interfaceName) {
            "RichTreeViewPropsBase",
                -> propsBody = propsBody.replace("override var sx:", "var sx:")

            "CommonColors",
            "PaletteColor",
            "TypeText",
            "TypeAction",
            "SimplePaletteColorOptions",
            "PaletteAugmentColorOptions",
                -> propsBody = propsBody.replace(": String", ": web.cssom.Color")
        }

        when (parentType) {
            "mui.system.ThemeOptions",
            "mui.system.Theme",
            "BaseTheme",
                -> propsBody = sequenceOf(
                "mixins",
                "components",
                "slots",
                "palette",
                "shadows",
                "transitions",
                "typography",
                "zIndex",
            ).fold(propsBody) { acc, propName ->
                acc.replace("var $propName:", "override var $propName:")
            }
        }

        val hasChildren = CHILDREN in propsBody
        var declaration = when {
            propsLike || hasChildren
                -> props(
                propsName = interfaceName,
                parentType = parentType,
                hasChildren = hasChildren,
                hasClassName = CLASS_NAME in propsBody,
                hasSx = SX in propsBody,
            )

            interfaceName.endsWith("Params") || interfaceName == "UsePaginationItem"
                -> props(interfaceName, parentType, hasChildren = false)

            else -> "external interface $interfaceName" +
                    if (parentType != null) ": $parentType" else ""
        }

        when (interfaceName) {
            "UseAutocompleteProps",
                -> declaration = declaration.replaceFirst(":", "<Value>:")

            "SelectOption",
            "SelectOptionDefinition",
            "UseOptionParameters",
            "AutocompleteChangeDetails",
            "UseAutocompleteRenderedOption",
            "UseAutocompleteReturnValue",
            "CreateFilterOptionsConfig",
            "FilterOptionsState",
                -> declaration += "<Value>"

            "BrowserAutofillAction",
                -> declaration += "<OptionValue>"

            "UseSelectSingleParameters",
            "UseSelectMultiParameters",
            "UseSelectSingleResult",
            "UseSelectMultiResult",
            "MultiSectionDigitalClockOption",
                -> declaration += "<TValue>"

            "ListState",
                -> declaration += "<ItemValue>"

            "SelectInternalState",
                -> declaration = declaration.replaceFirst(
                "SelectInternalState",
                "SelectInternalState<OptionValue>"
            )

            "UseListParameters",
                -> declaration += "<ItemValue, State, CustomAction, CustomActionContext>"

            "UseSelectParameters",
                -> declaration += "<OptionValue, Multiple>"

            "UseSelectReturnValue",
                -> declaration += "<Value, Multiple>"

            "SelectOwnerState",
                -> declaration = declaration.replaceFirst(
                "SelectOwnerState",
                "SelectOwnerState<TValue> : SelectOwnProps<TValue>"
            )

            "OptionOwnProps",
            "SelectOwnProps",
                -> declaration = declaration.replaceFirst(":", "<OptionValue>:")

            "OptionProps",
                -> declaration = declaration.replaceFirst(":", "<TValue>: OptionProps<TValue>")

            "SelectProps",
                -> declaration = declaration.replaceFirst(":", "<TValue>: SelectProps<TValue>")

            "DateCalendarSlots",
            "DatePickerSlots",
            "DateTimePickerSlots",
            "DesktopDatePickerSlots",
            "DesktopDateTimePickerSlots",
            "DesktopTimePickerSlots",
            "MobileDatePickerSlots",
            "TimePickerSlots",
                -> declaration += "<TDate>"

            "MobileDateTimePickerSlots",
            "MobileTimePickerSlots",
                -> declaration += "<TDate, TView>"

            "ExportedClockPickerProps",
            "ExportedMonthPickerProps",
            "ExportedYearPickerProps",
            "BaseDateRangePickerProps",
            "DateCalendarSlotProps",
            "ExportedDateCalendarProps",
            "PickersCalendarHeaderSlotProps",
                -> declaration = declaration.replaceFirst(":", "<TDate>:")

            "DatePickerSlotProps",
            "DateTimePickerSlotProps",
            "DesktopDatePickerSlotProps",
            "DesktopDateTimePickerSlotProps",
            "DesktopTimePickerSlotProps",
            "MobileDatePickerSlotProps",
            "TimePickerSlotProps",
                -> declaration = declaration.replaceFirst(":", "<TDate, TEnableAccessibleFieldDOMStructure>:")

            "MobileDateTimePickerSlotProps",
            "MobileTimePickerSlotProps",
                -> declaration = declaration.replaceFirst(":", "<TDate, TView, TEnableAccessibleFieldDOMStructure>:")

            "DateIOFormats",
                -> declaration += "<TLibFormatToken: Any>"

            "IUtils",
                -> declaration += "<TDate: Any>"

            "UseSelectResult",
                -> declaration = declaration.replaceFirst("UseSelectResult", "UseSelectResult<TValue>")

            "UseListboxParameters",
                -> declaration = declaration.replaceFirst("UseListboxParameters", "UseListboxParameters<TOption>")

            "ListboxState",
                -> declaration = declaration.replaceFirst("ListboxState", "ListboxState<TOption>")
        }

        val annotations = when (interfaceName) {
            "SliderValueLabelProps" -> "@Suppress(\"VIRTUAL_MEMBER_HIDDEN\")\n"
            else -> ""
        }

        annotations + declaration + " {\n" +
                propsBody +
                "\n}"
    }
}

private fun props(
    propsName: String,
    parentType: String? = null,
    hasChildren: Boolean = false,
    hasClassName: Boolean = false,
    hasSx: Boolean = false,
    hasComponent: Boolean = false,
): String {
    val baseInterfaces = mutableListOf<String>()
    if (hasChildren)
        baseInterfaces += "react.PropsWithChildren"
    if (hasClassName)
        baseInterfaces += "react.PropsWithClassName"
    if (hasSx)
        baseInterfaces += "mui.system.PropsWithSx"
    if (hasComponent)
        baseInterfaces += "mui.types.PropsWithComponent"

    if (propsName == "InputOwnProps")
        baseInterfaces += "InputBaseProps"

    if (propsName in setOf("FormLabelProps", "InputLabelProps"))
        baseInterfaces += "FormLabelOwnProps"

    if (propsName == "ModalProps" && hasClassName)
        baseInterfaces += "mui.base.ModalProps"

    if (propsName == "AppBarProps" || propsName == "AccordionProps")
        baseInterfaces += "PaperProps"

    if (propsName == "TreeViewProps")
        baseInterfaces += "SimpleTreeViewProps"

    if (propsName == "RichTreeViewProps")
        baseInterfaces += "RichTreeViewPropsBase"

    if (
    // TODO: Commented props has conflicts by intrinsic types
        propsName in setOf(
//            "AccordionSummaryProps",
            "BottomNavigationActionProps",
            "CardActionAreaProps",
            "IconButtonProps",
            "FabProps",
//            "ListItemProps",
//            "ListItemButtonProps",
//            "MenuItemProps",
            "StepButtonProps",
//            "TabProps",
//            "TableSortLabelProps",
            "ToggleButtonProps",
        )
    ) {
        baseInterfaces += "mui.material.ButtonBaseProps"
    }

    if (propsName == "PopperProps" && hasSx)
        baseInterfaces += "mui.base.PopperProps"

    if (propsName == "StepProps")
        baseInterfaces += "mui.system.StandardProps"

    if (propsName == "StepperProps")
        baseInterfaces += listOf("mui.system.StandardProps", "PaperProps")

    if (propsName == "InputLabelOwnProps")
        baseInterfaces += "react.PropsWithChildren"

    if (propsName == "AlertProps")
        baseInterfaces += "AlertSlots"

    baseInterfaces
        .tryToAddInheritanceInterfaces(propsName)

    val parentTypes = when {
        parentType == null
            -> if (baseInterfaces.size > 1) {
            baseInterfaces.distinct().joinToString(",\n", "\n")
        } else baseInterfaces.firstOrNull() ?: "react.Props"

        baseInterfaces.isNotEmpty()
            -> {
            // Dedupe: parentType may already contain entries that tryToAddInheritanceInterfaces
            // re-adds (e.g. Card's CardOwnProps already extends PaperOwnProps in v6).
            val existingTokens = parentType.removePrefix("\n")
                .split(",")
                .map { it.trim() }
                .toSet()
            sequenceOf(parentType.removePrefix("\n"))
                .plus(baseInterfaces.filter { it !in existingTokens })
                .joinToString(",\n", "\n")
        }

        "\n" in parentType
            -> parentType

        else -> "\n" + parentType
    }

    return "external interface $propsName: $parentTypes"
}

// TODO: This is a WA for inheritance error. Try to avoid somehow...
//  `Types of inherited var-properties do not match:
//  public abstract var classes: AppBarClasses? defined in mui.material.AppBarProps,
//  public abstract var classes: PaperClasses? defined in mui.material.PaperProps`
private fun MutableList<String>.tryToAddInheritanceInterfaces(
    propsName: String,
) {
    if (propsName == "AppBarOwnProps")
        this += "PaperProps"

    if (propsName == "BottomNavigationActionOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "CardActionAreaOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "CardOwnProps")
        this += "PaperOwnProps"

    if (propsName == "CardHeaderOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("div")

    if (propsName == "ChipOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("div")

    if (propsName == "FabOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "IconButtonOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "InputLabelOwnProps")
        this += "FormLabelOwnProps"

    if (propsName == "MenuItemOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("li")

    if (propsName == "StepButtonOwnProps")
        this += "ButtonBaseProps"

    if (propsName == "StepperOwnProps")
        this += "PaperProps"

    if (propsName == "SvgIconOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("svg")

    if (propsName == "TabOwnProps")
        this += INTRINSIC_TYPE_MAP.getValue("button")

    if (propsName == "ToggleButtonOwnProps")
        this += "ButtonBaseProps"
}

private fun findComponent(
    name: String,
    propsName: String,
    declaration: String,
    content: String,
    type: String = "FC",
): String? {
    if (declaration !in content)
        return null

    if (name.startsWith("use") || name.startsWith("create") || name.startsWith("z"))
        return null

    var comment = content.substringBefore("\n$declaration")
    comment = when {
        "\n\n" in comment
            -> comment.substringAfterLast("\n\n")

        ";\n/**" in comment
            -> comment.substringAfterLast(";\n")

        "}\n/**" in comment
            -> comment.substringAfterLast("}\n")

        else -> comment.substringAfterLast("};\n")
    }

    if (comment.startsWith("export "))
        comment = comment
            .substringAfterLast(";\n")
            .substringAfterLast("}\n")

    if (name == "PickersActionBar")
        comment = ""

    val typeParameter = when (propsName) {
        "CalendarPickerProps",
        "ClockPickerProps",
        "DateRangePickerProps",
        "DateRangePickerDayProps",
        "MonthPickerProps",
        "PickersDayProps",
        "YearPickerProps",

        "AutocompleteProps",
        "SelectProps",

        "MultiSelectProps",
        "OptionProps",
        "DateCalendarProps",
        "DateTimeFieldProps",
        "DigitalClockProps",
        "MonthCalendarProps",
        "MultiSectionDigitalClockProps",
        "PickersCalendarHeaderProps",
        "YearCalendarProps",
            -> "$propsName<*>"

        "DatePickerProps",
        "DateTimePickerProps",
        "DesktopDatePickerProps",
        "DesktopDateTimePickerProps",
        "DesktopTimePickerProps",
        "LocalizationProviderProps",
        "MobileDatePickerProps",
        "TimeClockProps",
        "TimePickerProps",
            -> "$propsName<*, *>"

        "MobileDateTimePickerProps",
        "MobileTimePickerProps",
            -> "$propsName<*, *, *>"

        // TODO: Remove when `TreeItem` will be removed from `@mui/lab`
        "TreeItemProps",
            -> "muix.tree.view.TreeItemProps"

        "TreeViewProps",
            -> "muix.tree.view.TreeViewProps"

        else -> propsName
    }

    val jsNameDefault = optionalJsNameDefaultAnnotation(content)

    return "$comment\n" +
            jsNameDefault +
            "external val $name: react.$type<$typeParameter>"
}

private fun optionalJsNameDefaultAnnotation(
    content: String,
): String =
    if ("export default" in content || "export { default " in content) {
        "@JsName(\"default\")\n"
    } else {
        ""
    }
