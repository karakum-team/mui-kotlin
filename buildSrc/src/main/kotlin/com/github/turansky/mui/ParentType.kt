package com.github.turansky.mui

internal fun findParentType(
    content: String,
): String? {
    if (" extends " !in content)
        return null

    val parentSource = content
        .substringAfter(" extends ")
        .substringBefore(" {\n")


    if ("<TDate>" in parentSource)
        return null

    if (parentSource.startsWith("StandardProps<"))
        return sequenceOf(
            "mui.system.StandardProps",
            parentSource
                .removeSurrounding("StandardProps<", ">")
                .substringBefore(",")
                .toTypeParameter()
        ).joinToString(",\n", "\n")

    if (parentSource.startsWith("Omit<"))
        return parentSource
            .removeSurrounding("Omit<", ">")
            .substringBefore(",")
            .toTypeParameter()

    return when (parentSource) {
        "ListProps",
        "BaseTextFieldProps",
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
