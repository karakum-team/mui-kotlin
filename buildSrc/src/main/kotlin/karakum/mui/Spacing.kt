package karakum.mui

private val SPACING_VALUE_TYPES = listOf("Double", "web.cssom.Length")

internal fun convertSpacingOptions(
    name: String,
    body: String,
): String {
    val types = splitSpacingUnion(body).flatMap { branch ->
        when (branch) {
            "number" -> listOf("Double")
            "string" -> listOf("web.cssom.Length")
            "Spacing" -> listOf("Spacing")
            "((abs: number) => number | string)",
            "((abs: number | string) => number | string)" ->
                listOf("(abs: Double) -> web.cssom.Length")

            "ReadonlyArray<string | number>" ->
                SPACING_VALUE_TYPES.map { "ReadonlyArray<$it>" }

            else -> error("Unsupported SpacingOptions branch: $branch")
        }
    }

    // Expose numeric factors and typed CSS lengths rather than every primitive union combination.
    val factories = types.distinct().joinToString("\n\n") { type ->
        val modifier = if (type.startsWith("(")) "noinline " else ""

        """
            inline fun $name(
                ${modifier}value: $type,
            ): $name =
                unsafeCast(value)
        """.trimIndent()
    }

    return "sealed /* union */\nexternal interface $name\n\n$factories"
}

// Callback inputs/results and array elements contain their own unions. Split only the outer union;
// unlike a closing generic bracket, the '>' in a function arrow must not change bracket depth.
private fun splitSpacingUnion(body: String): List<String> {
    val parts = mutableListOf<String>()
    var depth = 0
    var start = 0
    for (index in body.indices) {
        when (body[index]) {
            '(', '<' -> depth++
            ')' -> depth--
            '>' -> if (body.getOrNull(index - 1) != '=') depth--
            '|' -> if (depth == 0) {
                parts += body.substring(start, index).trim()
                start = index + 1
            }
        }
        check(depth >= 0) { "Unbalanced SpacingOptions: $body" }
    }
    check(depth == 0) { "Unbalanced SpacingOptions: $body" }
    parts += body.substring(start).trim()
    return parts
}

internal fun convertSpacing(
    name: String,
    body: String,
): String {
    val members = body
        .lineSequence()
        .filter { it.isNotBlank() }
        .joinToString("\n\n") { source ->
            val declaration = source.trim().removeSuffix(";")
            check(declaration.endsWith("): string")) { "Unsupported Spacing signature: $source" }
            val signature = declaration.removeSuffix(": string")

            "operator fun invoke$signature: web.cssom.Length"
        }

    return "sealed external interface $name {\n" +
            members +
            "\n}"
}
