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
