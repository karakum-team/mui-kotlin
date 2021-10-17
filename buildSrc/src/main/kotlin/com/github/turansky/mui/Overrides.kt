package com.github.turansky.mui

internal fun fixOverrides(
    name: String,
    content: String,
): String =
    when (name) {
        "ButtonBase",
        -> content
            .override("disabled")
            .override("tabIndex")

        "Button",
        "Fab",
        "IconButton",
        -> content
            .override("disabled")

        "ToggleButton",
        -> content
            .override("disabled")
            .override("value", false)

        "SwipeableDrawer",
        -> content
            .override("onClose", false)
            .override("open", false)

        else -> content
    }

private fun String.override(
    name: String,
    optional: Boolean = true, // remove
): String {
    val newValue = "override var $name:"
    val result = replaceFirst("var $name:", newValue)

    if (!optional)
        return result

    return result.substringBefore(newValue) +
            newValue +
            result.substringAfter(newValue)
                .replaceFirst("\n", "?\n")
}
