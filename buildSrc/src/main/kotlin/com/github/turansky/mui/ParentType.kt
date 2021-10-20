package com.github.turansky.mui

internal fun findParentType(
    content: String,
): String? {
    if (" extends " !in content)
        return null

    val parentSource = content
        .substringAfter(" extends ")
        .substringBefore(" {\n")

    return when {
        "<TDate>" in parentSource -> null

        parentSource.startsWith("StandardProps<") -> {
            sequenceOf(
                "mui.system.StandardProps",
                parentSource
                    .removeSurrounding("StandardProps<", ">")
                    .substringBefore(",")
                    .toTypeParameter()
            ).joinToString(",\n", "\n")
        }

        parentSource.startsWith("Omit<") -> {
            parentSource
                .removeSurrounding("Omit<", ">")
                .substringBefore(",")
                .toTypeParameter()
        }

        parentSource == "ListProps"
        -> parentSource

        parentSource == "HTMLDivProps"
        -> "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>"

        parentSource == "TransitionProps"
        -> parentSource.toTypeParameter()

        parentSource == "React.HTMLAttributes<HTMLSpanElement>"
        -> parentSource.toTypeParameter()

        else -> null
    }
}
