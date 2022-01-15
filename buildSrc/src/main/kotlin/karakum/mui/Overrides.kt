package karakum.mui

internal fun fixOverrides(
    name: String,
    content: String,
): String =
    when (name) {
        "Backdrop",
        "Badge",
        "Slider",
        -> content
            .override("classes")

        "Box",
        -> content
            .override("component")

        "ButtonBase",
        -> content
            .override("disabled")
            .override("tabIndex")

        "Dialog",
        -> content
            .override("disableEscapeKeyDown")
            .override("onBackdropClick")
            .override("onClose")
            .override("open")
            .override("sx")
            .replaceFirst("override var children:", "    /* override */ var children:")

        "Drawer",
        -> content
            .override("onClose")
            .override("sx")
            .replaceFirst("override var children:", "    /* override */ var children:")

        "Popover",
        -> content
            .override("container")
            .override("onClose")
            .override("open")
            .override("sx")
            .replaceFirst("override var children:", "    /* override */ var children:")

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
            .override("open")
            .replace("open: Boolean", "open: Boolean?")

        "TableCell",
        -> content
            .override("align")
            .override("scope")

        "TreeItemContent",
        "Collapse",
        -> content
            .override("className")

        "SpeedDial",
        -> content
            .override("hidden")

        "InputUnstyled",
        -> content
            .replaceFirst("*/\nvar value: ", "*/\noverride var value: ")

        "PopperUnstyled",
        -> content
            .override("children")

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
