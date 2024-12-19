package karakum.mui

internal fun convertSpacingOptions(
    name: String,
    body: String,
): String {
    val factories = body
        .replace("number | string", "web.cssom.Length")
        .replace("string | number", "Int")
        .replace("number", "Int")
        .replace("=>", "->")
        .splitToSequence(" | ")
        .map { it.removeSurrounding("(", ")") }
        .joinToString("\n\n") { type ->
            val modifier = if (type.startsWith("(")) "noinline" else ""

            """
                inline fun $name(
                    $modifier value: $type,
                ): $name =
                    unsafeCast(value)
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
                .replace(": string", ": web.cssom.Length")

            "@JsNative" + "\n" +
                    "operator fun invoke$declaration"
        }

    return "sealed external interface $name {\n" +
            members +
            "\n}"
}
