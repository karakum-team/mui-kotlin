package karakum.mui

private val TOP_UNION_PROPERTIES = setOf(
    "matchFrom",
    "blurOnSelect",
)

internal val UNION_PROPERTIES = setOf(
    "animation",
    "variant",
    "fontSize",

    "indicatorColor",
    "textColor",

    "type",

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

    "vertical",
    "horizontal",

    "align",

    "alignItems",
    "margin",
    "padding",
    "textAlign",

    "mouseEvent",
    "touchEvent",

    "action",
) + TOP_UNION_PROPERTIES

internal fun findDefaultUnions(
    name: String,
    content: String,
): Pair<String, List<String>> {
    val unions = mutableListOf<String>()
    var newContent = content

    findUnionSource(newContent, "color") { original, source ->
        // Only build a `<Name>Color` enum from a clean string-literal union.
        // v7 Typography.color mixes literals with a template-literal member
        // (`` `text${Capitalize<keyof TypeText>}` ``); leave those for the
        // `OverridableStringUnion<…>` → `Any?` fallback in KotlinType instead.
        if (source.startsWith("'") && "`" !in source) {
            val colorName = "${name}Color"
            newContent = newContent.replaceFirst(original, colorName)
            unions += convertUnion("$colorName = $source")
                ?: error("convertUnion null (color): name=$name source=[$source]")
        } else if (source == "AlertColor") {
            newContent = newContent.replaceFirst(original, source)
        }
    }

    for (property in UNION_PROPERTIES) {
        findUnionSource(newContent, property) { original, source ->
            if (name == "TextField" && property == "variant")
                return@findUnionSource

            if (name != "usePagination" && property == "type")
                return@findUnionSource

            if (!source.startsWith("'"))
                return@findUnionSource

            if (source.endsWith(" | number"))
                return@findUnionSource

            var className = when (property) {
                "fontSize" -> "Size"
                "vertical", "horizontal" -> "Origin${property.replaceFirstChar(Char::titlecase)}"
                else -> property.replaceFirstChar(Char::titlecase)
            }

            if ("P" !in property && property !in TOP_UNION_PROPERTIES)
                className = name + className

            if (name == "usePagination" && property == "type")
                className = "UsePaginationItemType"

            newContent = newContent
                .replaceFirst("\n$original", " $className")
                .replaceFirst(" $original", " $className")
                .replaceFirst("<$original", "<$className")
            unions += convertUnion("$className = $source")
                ?: error("DIAG convertUnion null: name=$name property=$property className=$className source=[$source]")
        }
    }

    findUnionSource(newContent, "size") { original, source ->
        if (source.startsWith("'")) {
            val sizeName = when (source) {
                "'small' | 'medium'" -> "BaseSize"
                "'small' | 'medium' | 'large'" -> "Size"
                "'small' | 'normal'" -> "NormalSize"
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
        // v7 surfaces optional members as `… | undefined`. Strip it BEFORE unwrapping
        // `ResponsiveStyleValue<…>`, otherwise the string ends with `undefined` (not `>`) and the
        // unwrap silently no-ops — leaving the literal union unrecognized (e.g. Stack `direction`).
        .map { it.removeSuffix(" | undefined") }
        .map { it.removeSurrounding("ResponsiveStyleValue<", ">") }
        .firstOrNull()
        ?: return

    var source = original
        .substringBefore(",")
        .removePrefix("OverridableStringUnion<")
        // TODO: remove hardcode
        .replace("PropTypes.Color", "'inherit' | 'primary' | 'secondary' | 'default'")
        .replace("TreeViewItemsReorderingAction", "'reorder-above' | 'reorder-below' | 'make-child' | 'move-to-parent'")
        .replace(
            "ClickAwayMouseEventHandler",
            "'onClick' | 'onMouseDown' | 'onMouseUp' | 'onPointerDown' | 'onPointerUp'"
        )
        .replace("ClickAwayTouchEventHandler", "'onTouchStart' | 'onTouchEnd'")
        // v7 surfaces optional members explicitly (e.g. `'left' | 'right' | undefined`);
        // drop the `undefined` member so the generated union stays a clean literal enum.
        .replace(" | undefined", "")
        .trim()

    if (source.startsWith("| '"))
        source = source.removePrefix("| ")
            .splitToSequence(" | ")
            .map { it.trim() }
            .joinToString(" | ")

    callback(original, source)
}
