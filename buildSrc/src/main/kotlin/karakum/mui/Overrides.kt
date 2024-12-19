package karakum.mui

internal fun fixOverrides(
    name: String,
    content: String,
): String =
    when (name) {
        "AlertTitle",
            -> content
            .override("classes")

        "Autocomplete",
            -> content
            .override("disabled")
            .override("readOnly")
            .replaceFirst("var key: String", "override var key: react.Key? /* Key */")

        "BottomNavigationAction",
            -> content
            .override("classes")
            .override("value")

        "StepButton",
            -> content
            .override("classes")

        "Button",
            -> content
            .override("rootElementName")

        "CardActionArea",
            -> content
            .override("classes")
            .override("focusVisibleClassName")

        "Fab",
            -> content
            .override("classes")
            .override("disabled")
            .override("disableRipple")

        "IconButton",
            -> content
            .override("classes")
            .override("disabled")

        "CardHeader",
            -> content
            .override("title")

        "Dialog",
            -> content
            .override("disableEscapeKeyDown")
            .override("onBackdropClick")
            .override("onClose")
            .override("open")

        "DialogTitle",
        "DialogContentText",
            -> content
            .override("classes")

        "Drawer",
            -> content
            .override("onClose")

        "Popover",
            -> content
            .override("container")
            .override("onClose")
            .override("open")

        "ToggleButton",
            -> content
            .override("disabled")
            .override("value")

        "SwipeableDrawer",
            -> content
            .override("open")

        "MultiSelect",
            -> content
            .override("disabled")
            .replace("disabled: Boolean", "disabled: Boolean?")
            .replace("var component: dynamic", "var component: react.ElementType<*>?")

        "Option",
            -> content
            .replace("var component: dynamic", "var component: react.ElementType<*>?")

        "Select",
            -> content
            .replace("var component: dynamic", "var component: react.ElementType<*>?")

        "TableCell",
            -> content
            .override("align")
            .override("scope")

        "SpeedDial",
            -> content
            .override("ariaLabel")
            .override("hidden")

        "Tab",
            -> content
            .override("value")
            .override("slots")
            .replace("slots: TabSlots?", "slots: ButtonSlots? /* TabSlots? */")
            .override("slotProps")
            .replace(": SlotProps?", ": ButtonOwnProps.SlotProps?")

        "MenuItem",
            -> content
            .override("autoFocus")
            .override("onClick")
            .replace(
                "onClick: react.dom.events.MouseEventHandler<web.html.HTMLElement>?",
                "onClick: react.dom.events.MouseEventHandler<web.html.HTMLLIElement>?"
            )

        "TreeItem",
            -> content
            .override("onKeyDown")
            .replace(
                "var onFocus: Nothing?",
                "@Deprecated(\"See documentation\")\noverride var onFocus: react.dom.events.FocusEventHandler<web.html.HTMLLIElement>?"
            )

        "TabScrollButton",
            -> content
            .override("disabled")
            .override("classes")

        "Tabs",
            -> content
            .override("centered")
            .replaceFirst("centered: Boolean", "centered: Boolean?")

        "createTheme",
            -> {
            if ("mui.system.ThemeOptions" !in content) {
                content
            } else {
                content
                    .override("unstable_sxConfig", all = true)
                    .override("unstable_sx")
            }
        }

        else -> content
    }

private fun String.override(
    name: String,
    last: Boolean = false,
    all: Boolean = false,
): String {
    val oldValue = "var $name:"
    val newValue = "override var $name:"

    return when {
        all -> replace(oldValue, newValue)
        last -> replaceLast(oldValue, newValue)
        else -> replaceFirst(oldValue, newValue)
    }
}

private fun String.replaceLast(oldValue: String, newValue: String): String =
    replaceFirst("(?s)(.*)$oldValue".toRegex(), "$1$newValue")
