package karakum.mui

internal fun findParentType(
    content: String,
): String? {
    if (" extends " !in content)
        return null

    val parentSource = content
        .substringAfter(" extends ")
        .substringBefore(" {\n")
        .substringAfter("\n> extends ")

    if ("<TDate>" in parentSource)
        return null

    if (parentSource.startsWith("StandardProps<"))
        return parseStandardProps(parentSource)

    if (parentSource.startsWith("Omit<")) {
        var result = parentSource
            .removeSurrounding("Omit<", ">")
            .substringBefore(",")
            .toTypeParameter()

        if (result == "SystemThemeOptions")
            result = "mui.system.ThemeOptions"

        return result
    }

    if (parentSource.startsWith("UseAutocompleteProps<")) {
        val (first, second) = parentSource.split(",\n    ")
        return sequenceOf(
            "mui.base." + first.replace(", Multiple, DisableClearable, FreeSolo", ""),
            parseStandardProps(second),
        ).joinToString(",", "\n")
    }

    if (parentSource.startsWith("UsePaginationProps")) {
        val (first, second) = parentSource.split(",\n    ")
        return sequenceOf(
            first,
            parseStandardProps(second),
        ).joinToString(",", "\n")
    }

    return when (parentSource) {
        "BaseTextFieldProps",
        "ListProps",
        "TreeViewPropsBase",
        "UseInputProps",
        "UseSwitchProps",
        "UsePaginationItem",

        "BaseTheme",

        "SelectUnstyledCommonProps",
        "MultiSelectUnstyledProps<TValue>",
        -> parentSource

        "SystemTheme",
        -> "mui.system.Theme"

        "ButtonUnstyledOwnProps",
        -> "mui.base.$parentSource"

        "HTMLDivProps",
        -> "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>"

        "TransitionProps",
        "React.HTMLAttributes<HTMLDivElement>",
        "React.HTMLAttributes<HTMLSpanElement>",
        "React.HTMLAttributes<HTMLInputElement | HTMLTextAreaElement>",
        -> parentSource.toTypeParameter()

        else -> null
    }
}

private fun parseStandardProps(
    source: String,
): String =
    sequenceOf(
        "mui.system.StandardProps",
        source
            .removeSurrounding("StandardProps<", ">")
            .substringBefore(",")
            .trim()
            .toTypeParameter()
    ).joinToString(",\n", "\n")
