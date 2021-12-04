package com.github.turansky.mui

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

    if (parentSource.startsWith("Omit<"))
        return parentSource
            .removeSurrounding("Omit<", ">")
            .substringBefore(",")
            .toTypeParameter()

    if (parentSource.startsWith("UseAutocompleteProps<")) {
        val (first, second) = parentSource.split(",\n    ")
        return sequenceOf(
            "mui.base." + first.replace(", Multiple, DisableClearable, FreeSolo", ""),
            parseStandardProps(second)
                .replace(",\n", "\n    /* ") + " */",
        ).joinToString(",", "\n")
    }

    return when (parentSource) {
        "BaseTextFieldProps",
        "ListProps",
        "TreeViewPropsBase",
        -> parentSource

        "HTMLDivProps",
        -> "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>"

        "TransitionProps",
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
