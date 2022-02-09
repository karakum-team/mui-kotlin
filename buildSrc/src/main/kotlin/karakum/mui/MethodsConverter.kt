package karakum.mui

internal fun convertMethods(
    source: String,
): String {
    return source.splitToSequence(";\n")
        .map { it.trim() }
        .map { it.removeSuffix(": void") }
        .joinToString("\n") { "fun $it" }
}
