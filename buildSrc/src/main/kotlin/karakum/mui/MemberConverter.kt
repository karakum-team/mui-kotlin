package karakum.mui

internal fun convertMembers(
    source: String,
): String {
    if (source.isEmpty())
        return ""

    return source
        .replaceIndent("  ")
        .replace("  // component tokens\n", "")
        .replace(";\n    ", "??11??")
        .replace(";\n  }", "??12??")
        .replace(";\n  })", "??22??")
        .replace(";\n  }[", "??33??")
        .replace(";\n   * ", "??44??")
        .splitToSequence(";\n")
        .map { it.replace("??11??", ";\n    ") }
        .map { it.replace("??12??", ";\n  }") }
        .map { it.replace("??22??", ";\n  })") }
        .map { it.replace("??33??", ";\n  }[") }
        .map { it.replace("??44??", ";\n   * ") }
        .map { it.trimIndent() }
        .map { convertMember(it) }
        .filter { it.isNotEmpty() }
        .joinToString("\n\n")
}

private fun convertMember(
    source: String,
): String {
    // Peel ONLY the member's leading documentation (a `/** … */` block and/or `//` lines),
    // then convert the whole remainder as a single property. The property body may itself
    // contain `*/\n` — v7 gives inline object-literal values (`componentsProps`/`slotProps`)
    // their own inner JSDoc — and those inner docs must stay with the property so
    // kotlinType()/componentInterface()/stripInlineDocs() can strip them. Splitting on every
    // `*/\n` here used to tear such a member apart into an empty interface plus a leaked member.
    val comments = mutableListOf<String>()
    var rest = source

    while (true) {
        val trimmed = rest.trimStart()
        when {
            trimmed.startsWith("/**") -> {
                val end = rest.indexOf("*/")
                if (end < 0) break
                comments += rest.substring(0, end + 2).trimStart()
                rest = rest.substring(end + 2).removePrefix("\n")
            }

            trimmed.startsWith("//") -> {
                val nl = rest.indexOf("\n")
                if (nl < 0) {
                    comments += rest.trimStart()
                    rest = ""
                } else {
                    comments += rest.substring(0, nl).trimStart()
                    rest = rest.substring(nl + 1)
                }
            }

            else -> break
        }
    }

    if (comments.any { "@deprecated" in it })
        return ""

    val property = rest.trimStart().takeIf { it.isNotEmpty() }?.let { convertProperty(it) } ?: ""

    return (comments + property)
        .filter { it.isNotEmpty() }
        .joinToString("\n")
}

private const val CSS_RECORD = "[k: string]: unknown | CSSProperties"

private fun convertProperty(
    source: String,
): String {
    if (source == CSS_RECORD)
        return "// $CSS_RECORD"

    // TS index signatures (e.g. MUI-X v9's `[x: `data-${string}`]: string;`) have no Kotlin equivalent
    // on an external interface — drop them.
    if (source.trimStart().startsWith("["))
        return ""

    val rawNameToken = source.substringBefore(":").removeSuffix("?")
    val jsNameAnnotation = if (rawNameToken.startsWith("'"))
        "@JsName(\"${rawNameToken.removeSurrounding("'")}\")\n"
    else ""
    val name = kotlinName(rawNameToken)

    if (name == "ref" || name == "}")
        return ""

    val jsName = if (rawNameToken.startsWith("'")) rawNameToken.removeSurrounding("'") else null
    val type = ARIA_ATTR_TYPES[jsName]
        ?: kotlinType(source.substringAfter(":").removePrefix(" "), name)

    if (name == "children") {
        if (type == "react.ReactNode") {
            return CHILDREN
        }

        if (type == "react.ReactElement<*>") {
            return "$CHILDREN /* react.ReactElement<*>? */"
        }

        // Fallback type with ReactNode/ReactElement in the original TS — still treat as React children.
        if (type.startsWith("Any? /*") && ("ReactNode" in type || "ReactElement" in type)) {
            val comment = type.substringAfter("Any? /* ").removeSuffix(" */")
            return "$CHILDREN /* $comment */"
        }
    }

    if (name == "id")
        return ID

    if (name == "className" && type == "String")
        return CLASS_NAME

    if (name == "sx")
        return SX

    val optional = source.substringBefore(":")
        .endsWith("?")

    val fullType = when {
        !optional -> type
        type == DYNAMIC -> type
        type.startsWith("$DYNAMIC ") -> type
        type.startsWith("Components\n") -> type.replaceFirst("Components\n", "Components?\n")
        type.startsWith("ComponentsProps\n") -> type.replaceFirst("ComponentsProps\n", "ComponentsProps?\n")
        type.startsWith("Slots\n") -> type.replaceFirst("Slots\n", "Slots?\n")
        type.startsWith("SlotProps\n") -> type.replaceFirst("SlotProps\n", "SlotProps?\n")
        type.startsWith("(") -> "($type)?"
        "? /*" in type -> type
        type.endsWith("*/") -> type.replace(" /*", "? /*")
        " //" in type -> type.replace(" //", "? //")
        type.endsWith("?") -> type
        else -> "$type?"
    }

    val modifier = if (": Readonly<" in source) "val" else "var"
    val declaration = jsNameAnnotation + "$modifier $name: $fullType"

    return declaration
}

private fun kotlinName(name: String): String =
    when {
        name == "in" ->
            "`in`"

        name.startsWith("'") -> {
            val raw = name.removeSurrounding("'")
            ARIA_ATTR_NAMES[raw] ?: raw.split("-").mapIndexed { i, s ->
                val part = if (i == 0) s else s.replaceFirstChar { it.uppercaseChar() }
                if (i == 0) part.dropWhile { !it.isLetter() && it != '_' } else part
            }.joinToString("")
        }

        name.isNotEmpty() && name[0].isDigit() ->
            "`$name`"

        else -> name
    }

// Canonical Kotlin types for ARIA attributes, matching AriaAttributes.kt in kotlin-react-dom.
private val ARIA_ATTR_TYPES = mapOf(
    // ElementId (typealias String) — ID-reference attributes
    "aria-activedescendant" to "ElementId",
    "aria-controls" to "ElementId",
    "aria-describedby" to "ElementId",
    "aria-details" to "ElementId",
    "aria-errormessage" to "ElementId",
    "aria-flowto" to "ElementId",
    "aria-labelledby" to "ElementId",
    // Boolean attributes
    "aria-atomic" to "Boolean",
    "aria-busy" to "Boolean",
    "aria-disabled" to "Boolean",
    "aria-expanded" to "Boolean",
    "aria-grabbed" to "Boolean",
    "aria-hidden" to "Boolean",
    "aria-modal" to "Boolean",
    "aria-multiline" to "Boolean",
    "aria-multiselectable" to "Boolean",
    "aria-readonly" to "Boolean",
    "aria-required" to "Boolean",
    "aria-selected" to "Boolean",
    // Int attributes
    "aria-colcount" to "Int",
    "aria-colindex" to "Int",
    "aria-colspan" to "Int",
    "aria-level" to "Int",
    "aria-posinset" to "Int",
    "aria-rowcount" to "Int",
    "aria-rowindex" to "Int",
    "aria-rowspan" to "Int",
    "aria-setsize" to "Int",
    // Double attributes
    "aria-valuemax" to "Double",
    "aria-valuemin" to "Double",
    "aria-valuenow" to "Double",
)

// Canonical Kotlin names for ARIA attributes, matching AriaAttributes.kt in kotlin-react-dom.
private val ARIA_ATTR_NAMES = mapOf(
    "aria-activedescendant" to "ariaActiveDescendant",
    "aria-atomic" to "ariaAtomic",
    "aria-autocomplete" to "ariaAutoComplete",
    "aria-braillelabel" to "ariaBrailleLabel",
    "aria-brailleroledescription" to "ariaBrailleRoleDescription",
    "aria-busy" to "ariaBusy",
    "aria-checked" to "ariaChecked",
    "aria-colcount" to "ariaColCount",
    "aria-colindex" to "ariaColIndex",
    "aria-colindextext" to "ariaColIndexText",
    "aria-colspan" to "ariaColSpan",
    "aria-controls" to "ariaControls",
    "aria-current" to "ariaCurrent",
    "aria-describedby" to "ariaDescribedBy",
    "aria-description" to "ariaDescription",
    "aria-details" to "ariaDetails",
    "aria-disabled" to "ariaDisabled",
    "aria-dropeffect" to "ariaDropEffect",
    "aria-errormessage" to "ariaErrorMessage",
    "aria-expanded" to "ariaExpanded",
    "aria-flowto" to "ariaFlowTo",
    "aria-grabbed" to "ariaGrabbed",
    "aria-haspopup" to "ariaHasPopup",
    "aria-hidden" to "ariaHidden",
    "aria-invalid" to "ariaInvalid",
    "aria-keyshortcuts" to "ariaKeyShortcuts",
    "aria-label" to "ariaLabel",
    "aria-labelledby" to "ariaLabelledBy",
    "aria-level" to "ariaLevel",
    "aria-live" to "ariaLive",
    "aria-modal" to "ariaModal",
    "aria-multiline" to "ariaMultiline",
    "aria-multiselectable" to "ariaMultiSelectable",
    "aria-orientation" to "ariaOrientation",
    "aria-owns" to "ariaOwns",
    "aria-placeholder" to "ariaPlaceholder",
    "aria-posinset" to "ariaPosInSet",
    "aria-pressed" to "ariaPressed",
    "aria-readonly" to "ariaReadOnly",
    "aria-relevant" to "ariaRelevant",
    "aria-required" to "ariaRequired",
    "aria-roledescription" to "ariaRoleDescription",
    "aria-rowcount" to "ariaRowCount",
    "aria-rowindex" to "ariaRowIndex",
    "aria-rowindextext" to "ariaRowIndexText",
    "aria-rowspan" to "ariaRowSpan",
    "aria-selected" to "ariaSelected",
    "aria-setsize" to "ariaSetSize",
    "aria-sort" to "ariaSort",
    "aria-valuemax" to "ariaValueMax",
    "aria-valuemin" to "ariaValueMin",
    "aria-valuenow" to "ariaValueNow",
    "aria-valuetext" to "ariaValueText",
)
