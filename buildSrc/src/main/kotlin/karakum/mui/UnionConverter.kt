package karakum.mui

internal fun convertUnion(
    source: String,
): String? {
    val name = source.substringBefore(" =")
    val sourceBody = source.substringAfter(" =")
        .removePrefix(" ")
        .removePrefix("\n  | ")
    // Unquoted literals (`false`, `1`) are JS values; quoted ones (`'false'`) are strings. The quoting below
    // only lets boolean members through the literal-union check, so remember which were unquoted.
    val unquoted = sourceBody.splitToSequence(" | ", "\n  | ").filter(::isRawLiteral).toSet()
    val body = sourceBody
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
            // Annotate the RAW (unescaped) value: Kotlin identifier backticks (`false`) must not leak into
            // the JS side. Boolean and number literals are JS expressions, not strings — `@JsValue("false")`
            // would hand MUI the truthy string "false".
            val annotation = if (it in unquoted) "JsRawValue" else "JsValue"
            """
                @$annotation("$it")
                val ${unionKey(escape(it))}: $name
            """.trimIndent()
        }
        .joinToString("\n")

    // A primitive type next to the literals (`GridSize = 'auto' | 'grow' | number | false`) becomes a factory,
    // as in `convertSpacingOptions`; anything else is only recorded.
    val factories = lostTypes.mapNotNull { type ->
        PRIMITIVE_FACTORY_TYPES[type]?.let { kotlinType ->
            """
                inline fun $name(
                    value: $kotlinType,
                ): $name =
                    unsafeCast(value)
            """.trimIndent()
        }
    }

    val unhandled = lostTypes.filter { it !in PRIMITIVE_FACTORY_TYPES }
    val lostDoc = if (unhandled.isNotEmpty()) {
        "/**\n * Lost from union: ${unhandled.joinToString(" | ")}\n */\n"
    } else ""

    return lostDoc + """
        sealed external interface $name {
            companion object {
                $properties
            }
        }
    """.trimIndent() + factories.joinToString("") { "\n\n$it" }
}

private val PRIMITIVE_FACTORY_TYPES = mapOf(
    "number" to "Number",
    "string" to "String",
    "boolean" to "Boolean",
)

private fun isRawLiteral(
    value: String,
): Boolean =
    value == "false" || value == "true" || value.toDoubleOrNull() != null

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
            -> "s" + value.replace("-", "Minus")

        else -> value.removePrefix("@")
            .kebabToCamel()
    }

