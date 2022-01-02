package com.github.turansky.mui

internal val UNION_PROPERTIES = setOf(
    "animation",
    "variant",
    "fontSize",

    "indicatorColor",
    "textColor",

    "actionPosition",
    "anchorPosition",
    "iconPosition",
    "loadingPosition",
    "position",

    "labelPlacement",
    "placement",

    "overlap",
    "scroll",
    "scrollButtons",
    "anchor",
    "implementation",
    "edge",
    "underline",
    "shape",
    "direction",

    "align",

    "alignItems",
    "margin",
    "padding",
    "textAlign",
)

internal fun findDefaultUnions(
    name: String,
    content: String,
): Pair<String, List<String>> {
    val unions = mutableListOf<String>()
    var newContent = content

    findUnionSource(newContent, "color") { original, source ->
        if (source.startsWith("'")) {
            val colorName = "${name}Color"
            newContent = newContent.replaceFirst(original, colorName)
            unions += convertUnion("$colorName = $source")!!
        } else if (source == "AlertColor") {
            newContent = newContent.replaceFirst(original, source)
        }
    }

    for (property in UNION_PROPERTIES) {
        findUnionSource(newContent, property) { original, source ->
            if (name == "TextField" && property == "variant")
                return@findUnionSource

            if (!source.startsWith("'"))
                return@findUnionSource

            var className = when (property) {
                "fontSize" -> "Size"
                else -> property.capitalize()
            }

            if ("P" !in property)
                className = name + className

            newContent = newContent
                .replaceFirst("\n$original", " $className")
                .replaceFirst(" $original", " $className")
            unions += convertUnion("$className = $source")!!
        }
    }

    findUnionSource(newContent, "size") { original, source ->
        if (source.startsWith("'")) {
            val sizeName = when (source) {
                "'small' | 'medium'" -> "BaseSize"
                "'small' | 'medium' | 'large'" -> "Size"
                else -> TODO()
            }
            newContent = newContent.replaceFirst(original, sizeName)
        }
    }

    return newContent to unions
}

private fun findUnionSource(
    content: String,
    property: String,
    callback: (String, String) -> Unit,
) {
    val original = sequenceOf(
        content.substringAfter("  $property?: ", ""),
        content.substringAfter("  $property?:\n", ""),
        content.substringAfter("  $property: ", ""),
    ).filter { it.isNotEmpty() }
        .map { it.substringBefore(";\n") }
        .firstOrNull()
        ?: return

    var source = original
        .substringBefore(",")
        .removePrefix("OverridableStringUnion<")
        // TODO: remove hardcode
        .replace("PropTypes.Color", "'inherit' | 'primary' | 'secondary' | 'default'")
        .trim()

    if (source.startsWith("| '"))
        source = source.removePrefix("| ")
            .splitToSequence(" | ")
            .map { it.trim() }
            .joinToString(" | ")

    callback(original, source)
}
