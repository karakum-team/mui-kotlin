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
            .override("value")

        "LoadingButton",
        -> content
            .override("classes")
            .override("sx")

        "SwipeableDrawer",
        -> content
            .override("onClose")
            .override("open")
            .replace("open: Boolean", "open: Boolean?")

        "TreeItemContent",
        "Collapse",
        -> content
            .override("className")

        "Dialog",
        "Drawer",
        "Popover",
        -> content
            .override("sx")

        "SpeedDial",
        -> content
            .override("hidden")

        "SliderUnstyled",
        -> content
            .override("defaultValue")
            .override("tabIndex")

        "TabUnstyled",
        -> content
            .override("components")
            .override("componentsProps")

        else -> content
    }

private fun String.override(
    name: String,
): String =
    replaceFirst("var $name:", "override var $name:")
