package karakum.mui

import karakum.mui.adapters.OWN_SLOTS_AND_SLOT_PROPS_COMPONENTS

internal fun fixOverrides(
    name: String,
    content: String,
): String =
    fixComponentOverrides(
        name = name,
        content = if (name in OWN_SLOTS_AND_SLOT_PROPS_COMPONENTS) content.overrideOwnSlots(name) else content,
    )

// `adaptOwnSlotsAndSlotProps` re-declares `slots`/`slotProps` in `XxxProps`; both parents declare them too.
// Scoped to that interface: the file's `XxxSlotsAndSlotProps` declares the same members as their origin.
// Keyed on the `XxxSlotsAndSlotProps` parent, because `@mui/base` has same-named files (Menu, Switch, …).
// Re-declared inherited properties must override; suppress only genuine type mismatches.
private fun String.overrideOwnSlots(
    name: String,
): String {
    val start = indexOf("external interface ${name}Props")
    if (start < 0 || "${name}SlotsAndSlotProps" !in substring(start, indexOf("{", start)))
        return this

    val end = indexOf("\n}\n", start)
    val inheritedProps = when (name) {
        "Checkbox" -> setOf(
            "checked", "checkedIcon", "classes", "disabled", "disableRipple", "icon", "id", "onChange", "required", "value",
        )
        "Dialog" -> setOf("ariaDescribedBy", "ariaLabelledBy", "ariaModal", "classes", "role")
        "Drawer" -> setOf("classes", "open")
        "Menu" -> setOf("anchorEl", "autoFocus", "classes", "onClose", "open", "transitionDuration")
        "OutlinedInput", "Popover" -> setOf("classes")
        "Radio" -> setOf("checkedIcon", "classes", "disabled", "icon")
        "SwipeableDrawer" -> setOf("onClose")
        "Switch" -> setOf("checkedIcon", "classes", "disabled", "icon", "value")
        else -> error("Missing own-slot property overrides for $name")
    } + setOf("slots", "slotProps")
    // Kotlin's mutable properties are invariant. Preserve upstream's own types and optionality,
    // suppressing only the differences confirmed against the inherited declarations.
    val mismatchedProps = setOf("slots", "slotProps", "classes") + when (name) {
        "Checkbox", "Radio", "Switch" -> setOf("checkedIcon", "icon")
        "Dialog" -> setOf("role")
        "Drawer" -> setOf("open")
        "Menu" -> setOf("anchorEl")
        "SwipeableDrawer" -> setOf("onClose")
        else -> emptySet()
    }
    val props = substring(start, end)
        .replace(Regex("""(?m)^var (\w+):""")) { match ->
            val propertyName = match.groupValues[1]
            if (propertyName in inheritedProps) {
                val annotation = if (propertyName in mismatchedProps)
                    "@Suppress(\"VAR_TYPE_MISMATCH_ON_OVERRIDE\")\n"
                else ""
                "${annotation}override ${match.value}"
            } else match.value
        }
        // These names are already mapped by AriaAttributes; @JsName is prohibited on an override.
        .replace(Regex("""(?m)^@JsName\("aria-(?:describedby|labelledby|modal)"\)\n(?=override var )"""), "")
    return substring(0, start) + props + substring(end)
}

private fun fixComponentOverrides(
    name: String,
    content: String,
): String =
    when (name) {
        "AlertTitle",
            -> content
            .override("classes")

        // v9: ExportedDateCalendarProps now extends ExportedDayCalendarProps (restored), which also
        // declares renderLoading — the own redeclaration must override.
        "DateCalendar",
            -> content
            .override("renderLoading")

        "Autocomplete",
            -> content
            // AutocompleteProps inherits disabled/readOnly from UseAutocompleteProps (restored as a
            // parent for v7, see ParentType.kt) — own redeclarations must override.
            .override("disabled")
            .override("readOnly")
            // onKeyDown is redeclared over the one inherited via StandardProps → HTMLAttributes → DOMAttributes.
            .override("onKeyDown")
            .replace("var key: Number", "override var key: react.Key? /* Key */")

        "Rating",
            -> content
            // `defaultValue: Number?` in RatingOwnProps clashes with `defaultValue: Any?` inherited
            // from react.dom.html.HTMLAttributes via RatingProps. Widen to Any? to match.
            .replace("var defaultValue: Number?", "var defaultValue: Any? /* Number */")

        "ButtonBase",
            -> content
            // v9 added `type?: string` to ButtonBaseOwnProps. In ButtonBaseProps it collides with
            // `type: ButtonType?` inherited from ButtonHTMLAttributes. The HTML attribute already
            // provides it correctly, so drop the OwnProps redeclaration — together with its leading
            // JSDoc block, which a bare `var type` removal would otherwise leave orphaned before `}`.
            .replace(
                Regex("""\n? *(?:/\*\*(?:(?!\*/)[\s\S])*?\*/)?\s*var type: String\?"""),
                "",
            )

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
            // TS slot props are `DrawerSlotsAndSlotProps & …`: the Drawer slots extend the own ones
            // (`SwipeableDrawerSlots : DrawerSlots` comes from `adaptOwnSlotsAndSlotProps`).
            .replace(Regex("""external interface SwipeableDrawerSlotProps: (?:react\.)?Props \{"""), "external interface SwipeableDrawerSlotProps: DrawerSlotProps {")

        "OutlinedInput",
            -> content
            // TS slots are `CreateSlotsAndSlotProps<…> & { slots?: InputBaseProps['slots']; slotProps?: … }`:
            // InputBase's own slot interfaces (nested in `InputBaseProps`) extend the outlined ones.
            .replace("external interface OutlinedInputSlots {", "external interface OutlinedInputSlots: InputBaseProps.Slots {")
            .replace(Regex("""external interface OutlinedInputSlotProps: (?:react\.)?Props \{"""), "external interface OutlinedInputSlotProps: InputBaseProps.SlotProps {")

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
            // v9: TreeItemProps's parents (`Omit<UseTreeItemParameters,'rootRef'>` and
            // `Omit<React.HTMLAttributes<HTMLLIElement>,'onFocus'>`) are both dropped, so there is no
            // supertype to override — onKeyDown/onFocus must be PLAIN members (like onBlur already is).
            // onFocus is `null` in TS ("This prop isn't supported") → keep the @Deprecated note, no override.
            .replace(
                "var onFocus: Nothing?",
                "@Deprecated(\"See documentation\")\nvar onFocus: react.dom.events.FocusEventHandler<web.html.HTMLLIElement>?"
            )

        "SpeedDial",
            -> content
            // SpeedDial.js reads `props.ariaLabel` (camelCase), while HTMLAttributes → AriaAttributes
            // declares `@JsName("aria-label") var ariaLabel: String?`. Prefix with `mui` and bind
            // `@JsName("ariaLabel")` so it does not collide with `AriaAttributes.ariaLabel`.
            .replace(
                "var ariaLabel: String",
                "@JsName(\"ariaLabel\")\nvar muiAriaLabel: String",
            )
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

        // The material theme (`createThemeNoVars.d.ts`, see `adapters/CreateThemeNoVars.kt`) re-declares these
        // `mui.system.Theme` / `ThemeOptions` members without the generator noticing the parent has them.
        "createThemeNoVars",
            -> content
            .override("unstable_sxConfig", all = true)
            .override("unstable_sx")
            .override("applyStyles")
            .override("shape", all = true)

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
): String =
    override(name, "var", last, all)
        .override(name, "val", last, all)

private fun String.override(
    name: String,
    modifier: String,
    last: Boolean,
    all: Boolean,
): String {
    val oldValue = "$modifier $name:"
    val newValue = "override $modifier $name:"

    return when {
        all -> replace(oldValue, newValue)
        last -> replaceLast(oldValue, newValue)
        else -> replaceFirst(oldValue, newValue)
    }
}

private fun String.replaceLast(oldValue: String, newValue: String): String =
    replaceFirst("(?s)(.*)$oldValue".toRegex(), "$1$newValue")
