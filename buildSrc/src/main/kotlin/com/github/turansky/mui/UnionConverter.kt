package com.github.turansky.mui

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
        "SpacingOptions" -> return "typealias $name = Any"
        "TableCellAlign" -> return "typealias $name = react.dom.html.TdAlign /* $body */"
        "TableCellBaseProps" -> return "typealias TableCellBaseProps = react.dom.html.TdHTMLAttributes<org.w3c.dom.HTMLTableCellElement>"
    }

    if (body == "React.LabelHTMLAttributes<HTMLLabelElement>")
        return "typealias $name = react.dom.html.LabelHTMLAttributes<org.w3c.dom.HTMLLabelElement>"

    if (body == "Omit<PopperUnstyledProps, 'direction'>")
        return "typealias $name = mui.base.PopperUnstyledProps /* $body */"

    if (body.startsWith("Omit<TableCellProps,"))
        return "typealias $name = TableCellBaseProps /* $body */"

    if ((!body.startsWith("'") || !body.endsWith("'")) && body.substringAfterLast("| ").toIntOrNull() == null)
        return null

    val values = body.splitToSequence(" | ", "\n  | ")
        .map { it.removeSurrounding("'") }
        .toList()

    val jsName = values.asSequence()
        .map { "${enumConstant(it)}: ${it.toIntOrNull() ?: "'$it'"}" }
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
