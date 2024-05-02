package karakum.mui

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
        "NativeFormControlElement" -> return "typealias $name = web.html.HTMLElement /* $body */"
        "SpacingOptions" -> return convertSpacingOptions(name, body)
        "SpacingArgument" -> {
            check(body == "number | string")
            return "typealias $name = Int /* web.cssom.Auto */"
        }

        "TableCellAlign" -> return "typealias $name = react.dom.html.TdAlign /* $body */"
        "TableCellBaseProps" -> return "typealias TableCellBaseProps = react.dom.html.TdHTMLAttributes<web.html.HTMLTableCellElement>"
    }

    if (body == "React.LabelHTMLAttributes<HTMLLabelElement>")
        return "typealias $name = react.dom.html.LabelHTMLAttributes<web.html.HTMLLabelElement>"

    if (body == "Omit<PopperProps, 'direction'>")
        return "typealias $name = mui.base.PopperProps /* $body */"

    if (body.startsWith("Omit<TableCellProps,"))
        return "typealias $name = TableCellBaseProps /* $body */"

    if (body.startsWith("StandardProps<React.HTMLAttributes<HTMLElement>> & {"))
        return "external interface $name : ${findParentType(" extends " + body.replace("> & {", "> {"))}"

    if ((!body.startsWith("'") || !body.endsWith("'")) && body.substringAfterLast("| ").toIntOrNull() == null)
        return null

    val values = body.splitToSequence(" | ", "\n  | ")
        .map { it.removeSurrounding("'") }
        .toList()

    val properties = values.asSequence()
        .map { escape(it) }
        .map {
            """
                @JsValue("${unionValue(it)}")
                val ${unionKey(it)}: $name
            """.trimIndent()
        }
        .joinToString("\n")

    return """
        @JsVirtual
        sealed external interface $name {
            companion object {
                $properties
            }
        }
    """.trimIndent()
}

internal fun convertSealed(
    name: String,
    keys: List<String>,
    getValue: (String) -> String,
    type: String,
): String {
    val companionContent = keys.asSequence()
        .map { "@JsValue(\"${getValue(it)}\")\nval $it: $type" }
        .joinToString("\n")

    return """
        @JsVirtual
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

private fun unionKey(
    value: String,
): String =
    when {
        value.toIntOrNull() != null
        -> "s$value"

        else -> value.removePrefix("@")
            .kebabToCamel()
    }

private fun unionValue(
    value: String,
): String =
    when {
        value == "false" -> value
        value == "true" -> value
        value.toIntOrNull() != null -> value

        else -> value
    }
