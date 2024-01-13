package karakum.mui

private const val QUERY_INPUT_TYPE = "string | ((theme: Theme) => string)"
private const val DEFAULT_FUNCTION_PREFIX = "export default function "

internal fun findDefaultFunction(
    name: String,
    initialContent: String,
): String? {
    if (!name.startsWith("use") && !name.startsWith("create"))
        return null

    when (name) {
        "createTransitions",

        "useDropdown",
        "useAutocomplete",
        -> return null
    }

    val content = when (name) {
        "useSlider",
        "useBadge",
        "useButton",
        "useInput",
        "useMenu",
        "useMenuButton",
        "useMenuItem",
        "useSnackbar",
        "useSwitch",
        "useOption",
        "useTreeItem",
        "useTransitionTrigger",
        "useTransitionStateManager",
        -> initialContent.replace("export declare function $name", "$DEFAULT_FUNCTION_PREFIX$name")

        "useSelect",
        "useTab",
        "useTabPanel",
        "useTabsList",
        -> initialContent.replace("declare function $name", "$DEFAULT_FUNCTION_PREFIX$name")

        else
        -> initialContent
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

    val splitContent = content.split(DEFAULT_FUNCTION_PREFIX)

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
        .replace("useSelect<OptionValue, Multiple extends boolean = false>", "<OptionValue, Multiple> useSelect")
        .replace("useOption<Value>", "<Value> useOption")
        .replace("useMediaQuery<Theme = unknown>", "<Theme : Any> useMediaQuery")
        .replace("?: T)", ": T? = definedExternally)")
        .replace("?: UseButtonParameters", ": UseButtonParameters? = definedExternally")
        .replace("?: UseMenuParameters", ": UseMenuParameters? = definedExternally")
        .replace("?: UseMenuButtonParameters", ": UseMenuButtonParameters? = definedExternally")
        .replace("?: UseMediaQueryOptions", ": UseMediaQueryOptions? = definedExternally")
        .replace("?: UseScrollTriggerOptions", ": UseScrollTriggerOptions? = definedExternally")
        .replace("?: UseInputParameters", ": UseInputParameters? = definedExternally")
        .replace("?: UseSnackbarParameters", ": UseSnackbarParameters? = definedExternally")
        .replace(": boolean", ": Boolean")
        .replace(": string", ": String")
        .replace(": number", ": Number")
        .replace(": ((theme: Theme) => string)", ": (theme: Theme) -> String")

    if ("()" !in declaration && "(\n" !in declaration)
        declaration = declaration.replaceFirst("(", "(\n")
            .replace(", ", ",\n")

    return comment +
            "@JsName(\"default\")\n" +
            "external fun $declaration"
}
