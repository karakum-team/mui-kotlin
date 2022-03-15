package karakum.mui

private const val UNION_MARKER = """/*union*/"""

internal fun convertUnion(
    source: String,
): String? {
    val name = source.substringBefore(" =")
    val body = source.substringAfter(" =")
        .removePrefix(" ")
        .removePrefix("\n  | ")
        .replace(" | false", " | 'false'")
        .replace(" | true", " | 'true'")

    when (name) {
        "NativeFormControlElement" -> return "typealias $name = org.w3c.dom.HTMLElement /* $body */"
        "SpacingOptions" -> return convertSpacingOptions(name, body)
        "SpacingArgument" -> {
            check(body == "number | string")
            return "typealias $name = csstype.Length /* csstype.Auto */"
        }
        "TableCellAlign" -> return "typealias $name = react.dom.html.TdAlign /* $body */"
        "TableCellBaseProps" -> return "typealias TableCellBaseProps = react.dom.html.TdHTMLAttributes<org.w3c.dom.HTMLTableCellElement>"
    }

    if (body == "React.LabelHTMLAttributes<HTMLLabelElement>")
        return "typealias $name = react.dom.html.LabelHTMLAttributes<org.w3c.dom.HTMLLabelElement>"

    if (body == "Omit<PopperUnstyledProps, 'direction'>")
        return "typealias $name = mui.base.PopperUnstyledProps /* $body */"

    if (body.startsWith("Omit<TableCellProps,"))
        return "typealias $name = TableCellBaseProps /* $body */"

    if (body.startsWith("StandardProps<React.HTMLAttributes<HTMLElement>> & {"))
        return "external interface $name : ${findParentType(" extends " + body.replace("> & {", "> {"))}"

    if ((!body.startsWith("'") || !body.endsWith("'")) && body.substringAfterLast("| ").toIntOrNull() == null)
        return null

    val values = body.splitToSequence(" | ", "\n  | ")
        .map { it.removeSurrounding("'") }
        .toList()

    val jsName = values.asSequence()
        .map { "${enumConstant(it)}: ${enumValue(it)}" }
        .joinToString(", ", "@JsName(\"\"\"($UNION_MARKER{", "}$UNION_MARKER)\"\"\")")

    val constantNames = values.asSequence()
        .map { escape(it) }
        .map { "${enumConstant(it)},\n" }
        .joinToString("")

    return """
        @Suppress("NAME_CONTAINS_ILLEGAL_CHARS")
        // language=JavaScript
        $jsName
        external enum class $name {
            $constantNames
            ;
        }
    """.trimIndent()
}

internal fun convertSealed(
    name: String,
    keys: List<String>,
    getValue: (String) -> String,
    type: String,
): String {
    val jsName = keys.asSequence()
        .map { "$it: '${getValue(it)}'" }
        .joinToString(", ", "@JsName(\"\"\"($UNION_MARKER{", "}$UNION_MARKER)\"\"\")")

    val companionContent = keys.asSequence()
        .map { "val $it: $type" }
        .joinToString("\n")

    return """
        @Suppress("NAME_CONTAINS_ILLEGAL_CHARS")
        // language=JavaScript
        $jsName
        sealed external interface $name {
            companion object {
                $companionContent
            }
        }
    """.trimIndent()
}

private fun escape(
    value: String,
): String =
    when (value) {
        "false",
        "true",
        -> "`$value`"

        else -> value
    }

private fun enumConstant(
    value: String,
): String =
    when {
        value.toIntOrNull() != null
        -> "s$value"

        else -> value.removePrefix("@")
            .kebabToCamel()
    }

private fun enumValue(
    value: String,
): String =
    when {
        value == "false" -> value
        value == "true" -> value
        value.toIntOrNull() != null -> value

        else -> "'$value'"
    }
