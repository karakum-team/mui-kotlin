package karakum.mui

internal fun convertSpacingOptions(
    name: String,
    body: String,
): String {
    val factories = body
        .replace("number | string", "csstype.Length")
        .replace("string | number", "Int")
        .replace("number", "Int")
        .replace("=>", "->")
        .splitToSequence(" | ")
        .map { it.removeSurrounding("(", ")") }
        .joinToString("\n\n") {
            """
                inline fun $name(
                    value: $it,
                ): $name =
                    value.unsafeCast<$name>()
            """.trimIndent()
        }

    return "sealed external interface $name" +
            "\n\n" +
            factories
}

internal fun convertSpacing(
    name: String,
    body: String,
): String {
    val members = body
        .splitToSequence("\n")
        .joinToString("\n") { source ->
            val declaration = source
                .removeSuffix(";")
                .replace(": number", ": Int")
                .replace(": string", ": csstype.Length")

            val parameters = declaration
                .substringAfter("(")
                .substringBefore(")")
                .splitToSequence(",")
                .joinToString(",") { it.substringBefore(":") }

            "inline operator fun invoke$declaration =\n" +
                    "asDynamic()($parameters)"
        }

    return "sealed interface $name {\n" +
            members +
            "\n}"
}
