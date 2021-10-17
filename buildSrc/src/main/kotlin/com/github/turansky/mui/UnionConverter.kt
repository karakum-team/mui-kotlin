package com.github.turansky.mui

private const val UNION_MARKER = """/*union*/"""

internal fun convertUnion(
    source: String,
): String? {
    val name = source.substringBefore(" =")
    val body = source.substringAfter(" =")
        .removePrefix(" ")
        .removePrefix("\n  | ")

    if ((!body.startsWith("'") || !body.endsWith("'")) && body.substringAfterLast("| ").toIntOrNull() == null)
        return null

    val values = body.splitToSequence(" | ", "\n  | ")
        .map { it.removeSurrounding("'") }
        .toList()

    val jsName = values.asSequence()
        .map { "${enumConstant(it)}: ${it.toIntOrNull() ?: "'$it'"}" }
        .joinToString(", ", "@JsName(\"\"\"($UNION_MARKER{", "}$UNION_MARKER)\"\"\")")

    val constantNames = values.asSequence()
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

private fun enumConstant(
    value: String,
): String =
    when {
        value.toIntOrNull() != null
        -> "s$value"

        else -> value.removePrefix("@")
            .kebabToCamel()
    }
