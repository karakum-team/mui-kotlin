package karakum.mui

internal fun fixOverrides(
    name: String,
    content: String,
): String =
    when (name) {
        "Autocomplete",
        -> content
            .override("disabled")
            .override("readOnly")
            .replaceFirst("var key: String", "override var key: react.Key? /* Key */")

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
            .replaceFirst("override var children:", "    /* override */ var children:")

        "Drawer",
        -> content
            .override("onClose")
            .replaceFirst("override var children:", "    /* override */ var children:")

        "Popover",
        -> content
            .override("container")
            .override("onClose")
            .override("open")
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

        "SwipeableDrawer",
        -> content
            .override("open")
            .replace("open: Boolean", "open: Boolean?")

        "TableCell",
        -> content
            .override("align")
            .override("scope")

        "SpeedDial",
        -> content
            .override("hidden")

        "PopperUnstyled",
        -> content
            .override("children")

        "TabUnstyled",
        -> content
            .override("components")
            .override("componentsProps")
            .replace(": Components?", ": ButtonUnstyledOwnProps.Components?")
            .replace(": ComponentsProps?", ": ButtonUnstyledOwnProps.ComponentsProps?")

        "ModalUnstyled"
            -> content
            .replace("children: react.ReactElement<*>", "children: dynamic /* react.ReactElement<*> */")

        else -> content
    }

private fun String.override(
    name: String,
): String =
    replaceFirst("var $name:", "override var $name:")
