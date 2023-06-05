package karakum.mui

private const val QUERY_INPUT_TYPE = "string | ((theme: Theme) => string)"

internal fun findDefaultFunction(
    name: String,
    content: String,
): String? {
    if (!name.startsWith("use") && !name.startsWith("create"))
        return null

    when (name) {
        "createTransitions",

            // TODO: Hooks has some problems while default function generation so we skip it
        "useMenu",
        "useAutocomplete",
        "useListbox",
        "useOption",
        "useSlider",
        -> return null
    }

    if (QUERY_INPUT_TYPE in content)
        return QUERY_INPUT_TYPE.splitToSequence(" | ")
            .map {
                val newContent = content.replace(QUERY_INPUT_TYPE, it)
                if ("Theme" !in it) {
                    newContent.replace("<Theme = unknown>", "")
                } else newContent
            }
            .mapNotNull { findDefaultFunction(name, it) }
            .joinToString("\n\n")

    val splitContent = content.split("export default function ")

    val before = splitContent.getOrNull(0) ?: return null
    val source = splitContent.getOrNull(1) ?: return null

    val comment = if (before.endsWith("**/\n")) {
        before.substringAfterLast("\n\n")
    } else ""

    var declaration = source
        .substringBefore(";")
        .replace(": Breakpoints,", ": mui.system.Breakpoints,")
        .replace(": Spacing,", ": mui.system.Spacing,")
        .replace("?: ThemeOptions", ": ThemeOptions? = definedExternally")
        .replace("?: SpacingOptions", ": SpacingOptions? = definedExternally")
        .replace("...args: object[]", "vararg args: dynamic")
        .replace("(styles: any): never", "(styles: Any)")
        .replace("useTheme<T = Theme>", "<T : Theme> useTheme")
        .replace("?: T)", ": T? = definedExternally)")
        .replace("useMediaQuery<Theme = unknown>", "<Theme : Any> useMediaQuery")
        .replace("?: UseMediaQueryOptions", ": UseMediaQueryOptions? = definedExternally")
        .replace(": boolean", ": Boolean")
        .replace(": string", ": String")
        .replace(": ((theme: Theme) => string)", ": (theme: Theme) -> String")

    if ("()" !in declaration && "(\n" !in declaration)
        declaration = declaration.replaceFirst("(", "(\n")
            .replace(", ", ",\n")

    return comment +
            "@JsName(\"default\")\n" +
            "external fun $declaration"
}
