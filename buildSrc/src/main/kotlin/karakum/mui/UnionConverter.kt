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

    // TS type identifiers (lowercase: `number`, `string`, `boolean`) inside a union are TS *types*,
    // not literal values — they cannot be emitted as concrete sealed entries. Track them in
    // `lostTypes` for the JSDoc comment, exclude from the sealed enumeration.
    val rawValues = body.splitToSequence(" | ", "\n  | ").toList()
    val lostTypes = rawValues.filter { it in TS_PRIMITIVE_TYPE_NAMES }
    val values = rawValues
        .filter { it !in TS_PRIMITIVE_TYPE_NAMES }
        .map { it.removeSurrounding("'") }

    if (values.isEmpty()) {
        return "typealias $name = Any /* $body */"
    }

    val properties = values.asSequence()
        .map {
            // Call unionValue on the RAW (unescaped) value so booleans like `false` produce
            // `@JsValue("false")` not `@JsValue("`false`")` (Kotlin identifier backticks must
            // not leak into the JS-side string).
            """
                @JsValue("${unionValue(it)}")
                val ${unionKey(escape(it))}: $name
            """.trimIndent()
        }
        .joinToString("\n")

    val lostDoc = if (lostTypes.isNotEmpty()) {
        "/**\n * Lost from union: ${lostTypes.joinToString(" | ")}\n */\n"
    } else ""

    return lostDoc + """
        sealed external interface $name {
            companion object {
                $properties
            }
        }
    """.trimIndent()
}

private val TS_PRIMITIVE_TYPE_NAMES = setOf("number", "string", "boolean", "object", "any", "unknown", "never")

internal fun convertSealed(
    name: String,
    keys: List<String>,
    getValue: (String) -> String,
    type: String,
): String {
    val companionContent = keys.asSequence()
        .map { "@JsValue(\"${getValue(it)}\")\nval $it: $type" }
        .joinToString("\n")

    return convertSealed(name, companionContent)
}

private fun convertSealed(
    name: String,
    companionContent: String,
): String = """
    sealed external interface $name {
        companion object {
            $companionContent
        }
    }
""".trimIndent()

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
