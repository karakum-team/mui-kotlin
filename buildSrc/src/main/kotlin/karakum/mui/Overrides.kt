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
            .replace("var key: Number", "override var key: react.Key? /* Key */")

        "Rating",
            -> content
            // `defaultValue: Number?` in RatingOwnProps clashes with `defaultValue: Any?` inherited
            // from react.dom.html.HTMLAttributes via RatingProps. Widen to Any? to match.
            .replace("var defaultValue: Number?", "var defaultValue: Any? /* Number */")

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
            .override("title", last = true)

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

        "StepIcon",
            -> content
            // SvgIconOwnProps drags in HTMLAttributes<SVGSVGElement> which clashes with our
            // already-emitted HTMLAttributes<HTMLDivElement> parent. Drop it for v6 minimum.
            // The StepIconProps's `classes` field is its own (no parent has it after the drop),
            // so don't add an `override` modifier.
            .replace(",\nSvgIconOwnProps", "")

        "Switch",
            -> content
            // SwitchProps inherits handlers from BOTH UseSwitchParameters (typed `<*>`) and
            // HTMLAttributes<HTMLSpanElement> (typed `<HTMLSpanElement>`). Diamond inheritance,
            // Kotlin's invariant var can't reconcile. Drop UseSwitchParameters from SwitchOwnProps —
            // the resulting wrapper keeps HTMLAttributes typing but loses hook-typed onChange/onFocus/onBlur.
            .replace("UseSwitchParameters,\n", "")

        "createBreakpoints",
            // `unit` is defined on BOTH Breakpoints (line 75) and BreakpointsOptions (line 90).
            // BreakpointsOptions extends Partial<Breakpoints> → second declaration overrides.
            -> content.override("unit", last = true)

        "TreeItem",
            -> content
            .override("onKeyDown")
            .replace(
                "var onFocus: Nothing?",
                "@Deprecated(\"See documentation\")\noverride var onFocus: react.dom.events.FocusEventHandler<web.html.HTMLLIElement>?"
            )
            // TS source has `disabled: boolean` (non-optional) in TreeItem2OwnProps, but parent
            // TreeItemProps has `disabled?: boolean`. Align nullability AND add override.
            .replace("\nvar disabled: Boolean\n", "\noverride var disabled: Boolean?\n")

        "SpeedDial",
            -> content
            .override("ariaLabel").replace("override var ariaLabel: String", "/* override var ariaLabel: String */")
            .override("hidden")
            .replace("override var hidden: Boolean?", "override var hidden: Hidden?")

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
