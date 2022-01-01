package com.github.turansky.mui

internal val UNION_PROPERTIES = setOf(
    "variant",

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
    "anchor",
    "implementation",
    // "edge",
    "underline",
    "shape",
    "direction",

    "align",
)

internal fun findDefaultUnions(
    name: String,
    content: String,
): Pair<String, List<String>> {
    val unions = mutableListOf<String>()
    var newContent = content

    findUnionSource(newContent, "color") { original, source ->
        when (source) {
            "AlertColor",
            -> newContent = newContent.replaceFirst(original, source)

            "PropTypes.Color",
            "PropTypes.Color | 'transparent'",
            -> newContent = newContent.replaceFirst(original, "csstype.Color")

            else -> if (source.startsWith("'")) {
                val colorName = "${name}Color"
                newContent = newContent.replaceFirst(original, colorName)
                unions += convertUnion("$colorName = $source")!!
            }
        }
    }

    for (property in UNION_PROPERTIES) {
        findUnionSource(newContent, property) { original, source ->
            if (name == "TextField" && property == "variant")
                return@findUnionSource

            if (!source.startsWith("'"))
                return@findUnionSource

            var className = property.capitalize()
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
        .trim()

    if (source.startsWith("| '"))
        source = source.removePrefix("| ")
            .splitToSequence(" | ")
            .map { it.trim() }
            .joinToString(" | ")

    callback(original, source)
}
