package karakum.mui

private const val PROMISE = "Promise"

internal const val DYNAMIC = "dynamic"
internal const val UNION = "mui.system.Union"

private const val ELEMENT_TYPE = "react.ElementType"

private val CREATE_TRANSITION = """
(
  props: string | string[],
  options?: Partial<{ duration: number | string; easing: string; delay: number | string }>,
) => string
""".removePrefix("\n").removeSuffix("\n")

private val USE_TAB_PANEL_RETURN_VALUE_GET_ROOT_PROPS = """
() => {
    'aria-labelledby': string | undefined;
    hidden: boolean;
    id: string | undefined;
}""".removePrefix("\n").removeSuffix("\n")

private val SWIPEABLE_DRAWER_PROPS_ALLOW_SWIPE_IN_CHILDREN = """

  | boolean
  | ((e: TouchEvent, swipeArea: HTMLDivElement, paper: HTMLDivElement) => boolean)
""".removePrefix("\n").removeSuffix("\n")

private val KNOWN_TYPES = setOf(
    "T",
    "Value",
    "TDate",
    "TValue",
    "TSectionValue",
    "TOption",
    "OptionValue",
    // MUI-X v9 named model types (defined as aliases/interface in PICKERS_STUBS) — keep their NAME on
    // members (e.g. `var value: PickerValidDate?`) instead of falling back to `Any? /* PickerValidDate */`.
    "PickerValidDate",
    "PickerValue",
    "PickerOwnerState",
    "ItemValue",
    "CustomActionContext",
    "TLibFormatToken",
    "ReadonlyArray<T>",
    "ReadonlyArray<Value>",
    "PickerOnChangeFn",
    "CalendarPickerView",

    "AlertColor",
    "GridDirection",
    "GridWrap",
    "Orientation",
    // Base UI's `Side`, generated from `utils/useAnchorPositioning.d.ts`. Unlike `Align`, the name is
    // not covered by KNOWN_TYPE_SUFFIXES (which is seeded with the capitalized UNION_PROPERTIES, and
    // `align` is one of those while `side` is not), so without this entry every `side` prop and state
    // member widens to `Any?`. No IMPORTED_FQNS entry: it resolves package-locally inside `baseui`,
    // and no MUI declaration uses the bare name.
    "Side",
    "PopoverReference",
    "PopperProps",

    "Breakpoints",
    "BreakpointsOptions",
    "Direction",
    "Shape",
    "ShapeOptions",
    "Spacing",

    "CSSProperties",

    "Mixins",
    "Palette",
    "Transitions",
    "ZIndex",

    // Material v9 additions (createMotion.d.ts): the `Motion` interface (createMotion.kt) and its
    // `ReducedMotionMode` string-union (createMotion.ext.kt) are both generated in mui.material.styles —
    // keep their NAMES on members instead of widening `motion` to `Any?` / inlining the literal union on
    // `reducedMotion`. (Motion is new in v9, so it was missing from this set.)
    "Motion",
    "ReducedMotionMode",

    "MixinsOptions",
    "PaletteOptions",
    "TransitionsOptions",
    // "ZIndexOptions",

    "CommonColors",
    "TypeText",
    "TypeAction",
    "TypeBackground",

    "SimplePaletteColorOptions",
    "CommonColorsOptions",

    "Easing",
    "Duration",

    "SxProps<Theme>",
)

private val KNOWN_TYPE_SUFFIXES = setOf(
    "Props",
    "Actions",
    "Origin",
    "Position",
    "Variant",
    "Color",
    "Size",
    "Channel",
) + UNION_PROPERTIES
    .map {
        @Suppress("DEPRECATION")
        it.capitalize()
    }

private val KNOWN_TYPE_PREFIX_MAP = mapOf(
    "SlotComponentProps" to "react.Props",
)

private val NUMBER_AS_INT_PROPERTIES = setOf(
    // Animation delays (ms)
    "enterDelay", "enterNextDelay", "enterTouchDelay",
    "leaveDelay", "leaveTouchDelay",
    // Auto-hide timings (ms)
    "autoHideDuration", "resumeHideDuration",
    // Pagination
    "count", "page", "rowsPerPage", "defaultPage",
    "boundaryCount", "siblingCount",
    // Steppers
    "activeStep", "steps",
    // Counters
    "limitTags",
    "itemsAfterCollapse", "itemsBeforeCollapse", "maxItems",
    "total",
    // Grid layout
    "cols", "defaultColumns",
    // Elevation (0–24)
    "elevation",
    // Date pickers
    "yearsPerRow", "monthsPerRow",
    // Minute increment for time pickers/clocks (@default 1)
    "minutesStep",
    // Fixed week count rendered by DayCalendar (e.g. 6 for Gregorian)
    "fixedWeekNumber",
    // Slider internal index
    "focusedThumbIndex",
    // Textarea row count (minRows/maxRows handled separately)
    "rows",
)

private val NUMBER_AS_DOUBLE_PROPERTIES = setOf(
    // Opacity ratios (0.0–1.0)
    "hoverOpacity", "selectedOpacity", "disabledOpacity",
    "focusOpacity", "activatedOpacity",
    // Palette contrast threshold (WCAG ratio)
    "contrastThreshold",
    // SwipeableDrawer velocity/ratio
    "hysteresis",
    // Rating precision (0.5 or 1.0)
    "precision",
    // CircularProgress stroke width
    "thickness",
    // Continuous value bounds (Circular/LinearProgress, Slider) — may be fractional
    "min", "max",
)

private val STANDARD_TYPE_MAP = mapOf(
    "any" to "Any",
    "object" to "Any",
    // MUI-X v9 internal transition slot type (from DateCalendar/PickersSlideTransition) — not a public
    // API type; widen to Any (it surfaces only on the internal DayCalendar's `TransitionProps` slot).
    "SlideTransitionProps" to "Any",
    // MUI-X v9 `TimeViewWithMeridiem = TimeView | 'meridiem'` is a string union → String.
    "TimeViewWithMeridiem" to "String /* 'hours' | 'minutes' | 'seconds' | 'meridiem' */",
    // NB: `PickerValidDate` / `PickerValue` / `PickerOwnerState` are kept as NAMED types (PICKERS_STUBS),
    // not widened here — see Generator.PICKERS_STUBS. Generic params `TDate` / `TView` / `TSectionValue`
    // are preserved on their declaring interfaces, not widened to Any.
    "string | number" to "Any /* String or Number */",
    "string | number | false" to "Any /* String or Number or Boolean /* false */ */",
    "string | number | null" to "Any /* String or Number */",

    // TODO: Probably need to replace all " | undefined" to " | null"
    "FormControlState | undefined" to "Any?",
    "string | undefined" to "String",

    "boolean" to "Boolean",
    "number" to "Number",
    "string" to "String",

    "void" to "Unit",
    "null" to "Nothing?",

    "false" to "Boolean /* false */",
    "true" to "Boolean /* true */",

    "Readonly<boolean>" to "Boolean",
    "string[]" to "ReadonlyArray<String>",
    "TValue[]" to "ReadonlyArray<TValue>",
    "ItemValue[]" to "ReadonlyArray<ItemValue>",
    "OptionValue[]" to "ReadonlyArray<OptionValue>",
    "TOption[]" to "ReadonlyArray<TOption>",
    "boolean | string" to "Boolean",
    "3 | 4" to "Number /* 3 | 4 */",

    "Date" to "kotlin.js.Date",

    "keyof HTMLElementTagNameMap" to "web.dom.TagName<out HTMLElement>",

    "DateView" to "String /* 'year' | 'month' | 'day' */",
    "PickersSectionElement[]" to "ReadonlyArray<PickersSectionElement>",
    "readonly CalendarPickerView[]" to "ReadonlyArray<CalendarPickerView>",
    "readonly DateOrTimeView[]" to "ReadonlyArray<String /* 'year' | 'month' | 'day' | 'hours' | 'minutes' | 'seconds' */>",
    "readonly DateView[]" to "ReadonlyArray<String /* 'year' | 'month' | 'day' */>",
    "readonly TimeView[]" to "ReadonlyArray<String /* 'hours' | 'minutes' | 'seconds' */>",
    "readonly 'hours'[]" to "ReadonlyArray<String /* 'hours' */>",
    "readonly TimeViewWithMeridiem[]" to "ReadonlyArray<String /* 'hours' | 'minutes' | 'seconds' | 'meridiem' */>",
    // v9 TimeClock `views?: readonly TView[]` where `TView extends TimeViewWithMeridiem` (string union).
    "readonly TView[]" to "ReadonlyArray<String /* 'hours' | 'minutes' | 'seconds' | 'meridiem' */>",
    "Breakpoint[]" to "ReadonlyArray<Breakpoint>",
    "PickersActionBarAction[]" to "ReadonlyArray<PickersActionBarAction>",
    "UsePaginationItem[]" to "ReadonlyArray<UsePaginationItem>",

    "HTMLElement" to "web.html.HTMLElement",
    "HTMLDivElement" to "web.html.HTMLDivElement",
    "HTMLInputElement" to "web.html.HTMLInputElement",
    "HTMLTextAreaElement" to "web.html.HTMLTextAreaElement",

    "Element | (() => Element | null) | null" to "Element",
    "Partial<OptionsGeneric<any>>" to "popper.core.Options",
    "Partial<PaperProps<React.ElementType>>" to "PaperProps",
    "React.CSSProperties" to "react.CSSProperties",
    "React.Ref<PickersSectionListRef>" to "react.Ref<PickersSectionListRef>",
    "React.Ref<Instance>" to "react.Ref<popper.core.Instance>",
    "React.Ref<Element>" to "react.Ref<web.dom.Element>",
    "React.Ref<HTMLElement>" to "react.Ref<web.html.HTMLElement>",
    "React.Ref<HTMLLIElement>" to "react.Ref<web.html.HTMLLIElement>",
    "React.Ref<HTMLInputElement | HTMLTextAreaElement>" to "react.Ref<web.html.HTMLInputElement /* or web.html.HTMLTextAreaElement*/>",
    "React.ElementType<TableCellBaseProps>" to "react.ElementType<*>",
    "React.RefCallback<Element>" to "react.RefCallback<web.dom.Element>",
    "React.RefCallback<HTMLInputElement>" to "react.RefCallback<web.html.HTMLInputElement>",
    "React.RefCallback<HTMLInputElement | HTMLTextAreaElement>" to "react.RefCallback<web.html.HTMLInputElement /* or web.html.HTMLTextAreaElement*/>",

    "{\n  bivarianceHack(event: {}, reason: 'backdropClick' | 'escapeKeyDown'): void;\n}['bivarianceHack']" to
            "(event: Any, reason: String) -> Unit",

    "React.ReactNode" to "react.ReactNode",
    "NonNullable<React.ReactNode>" to "react.ReactNode",
    "string | React.ReactNode" to "react.ReactNode",
    "string | React.ReactElement" to "react.ReactNode",
    "string | React.ReactElement<any>" to "react.ReactElement<*>",
    "string | number | React.ReactElement" to "react.ReactNode",
    "React.ReactNode | ((state: FormControlState) => React.ReactNode)" to "react.ReactNode",
    "React.HTMLAttributes<HTMLDivElement>['children']" to "react.ReactNode",
    "React.LabelHTMLAttributes<HTMLLabelElement>['children']" to "react.ReactNode",
    "FormControlProps['children']" to "react.ReactNode",

    "React.Dispatch<React.SetStateAction<boolean>>" to "react.StateSetter<Boolean>",

    "React.ReactElement" to "react.ReactElement<*>",
    "React.ReactElement<any>" to "react.ReactElement<*>",
    "React.ReactElement<any, any>" to "react.ReactElement<*>",
    "React.ReactElement<unknown>" to "react.ReactElement<*>",
    "NonNullable<React.ReactElement>" to "react.ReactElement<*>",

    "React.ElementType" to "$ELEMENT_TYPE<*>",

    "React.Ref<unknown>" to "react.Ref<*>",
    "React.Ref<any>" to "react.Ref<*>",

    "SimpleTreeViewApiRef" to "react.Ref<*>",
    "RichTreeViewApiRef" to "react.Ref<*>",

    "React.AriaRole" to "react.dom.aria.AriaRole",

    "PaletteMode" to "mui.material.PaletteMode",
    "TransitionProps" to "mui.material.transitions.TransitionProps",
    "ClickAwayListenerProps" to "mui.base.ClickAwayListenerProps",
    "Partial<BaseModalClasses>" to "ModalClasses",
    "ChipProps<ChipComponent>" to "ChipProps",

    "React.InputHTMLAttributes<HTMLInputElement>" to "react.dom.html.InputHTMLAttributes<web.html.HTMLInputElement>",
    "React.ImgHTMLAttributes<HTMLImageElement> & {\n  sx?: SxProps<Theme>;\n}" to "react.dom.html.ImgHTMLAttributes<web.html.HTMLImageElement>",
    "React.ImgHTMLAttributes<HTMLImageElement>" to "react.dom.html.ImgHTMLAttributes<web.html.HTMLImageElement>",
    "React.HTMLAttributes<HTMLDivElement>" to "react.dom.html.HTMLAttributes<web.html.HTMLDivElement>",
    "Partial<React.HTMLAttributes<HTMLDivElement>>" to "react.dom.html.HTMLAttributes<web.html.HTMLDivElement>",
    "React.HTMLAttributes<HTMLElement>" to "react.dom.html.HTMLAttributes<web.html.HTMLElement>",
    "React.HTMLAttributes<HTMLSpanElement>" to "react.dom.html.HTMLAttributes<web.html.HTMLSpanElement>",
    "React.ButtonHTMLAttributes<HTMLButtonElement>" to "react.dom.html.ButtonHTMLAttributes<web.html.HTMLButtonElement>",
    "React.AnchorHTMLAttributes<HTMLAnchorElement>" to "react.dom.html.AnchorHTMLAttributes<web.html.HTMLAnchorElement>",
    "React.LabelHTMLAttributes<HTMLLabelElement>" to "react.dom.html.LabelHTMLAttributes<web.html.HTMLLabelElement>",
    "React.LiHTMLAttributes<HTMLLIElement>" to "react.dom.html.LiHTMLAttributes<web.html.HTMLLIElement>",
    "React.FieldsetHTMLAttributes<HTMLFieldSetElement>" to "react.dom.html.FieldsetHTMLAttributes<web.html.HTMLFieldSetElement>",
    "React.FormHTMLAttributes<HTMLFormElement>" to "react.dom.html.FormHTMLAttributes<web.html.HTMLFormElement>",
    "TreeViewCancellableEventHandler<React.KeyboardEvent<HTMLLIElement>>" to "react.dom.events.KeyboardEventHandler<web.html.HTMLLIElement>",
    "TreeViewCancellableEventHandler<React.KeyboardEvent<HTMLInputElement>>" to "react.dom.events.KeyboardEventHandler<web.html.HTMLInputElement>",
    "TreeViewCancellableEventHandler<React.FocusEvent<HTMLInputElement>>" to "react.dom.events.FocusEventHandler<web.html.HTMLInputElement>",

    "NonNullable<React.HTMLAttributes<any>['tabIndex']>" to "Int",
    "React.InputHTMLAttributes<unknown>['type']" to "InputType",
    "React.InputHTMLAttributes<HTMLInputElement>['type']" to "InputType",
    "React.ButtonHTMLAttributes<HTMLButtonElement>['type']" to "ButtonType",

    "React.ChangeEvent" to "react.dom.events.ChangeEvent<*, *>",
    "React.MouseEvent" to "react.dom.events.MouseEvent<*, *>",

    "React.ReactEventHandler" to "react.dom.events.ReactEventHandler<*>",
    "React.FocusEventHandler" to "react.dom.events.FocusEventHandler<*>",
    "React.KeyboardEventHandler" to "react.dom.events.KeyboardEventHandler<*>",
    "React.MouseEventHandler" to "react.dom.events.MouseEventHandler<*>",
    "React.MouseEventHandler | undefined" to "react.dom.events.MouseEventHandler<*>",
    "React.MouseEventHandler<HTMLElement>" to "react.dom.events.MouseEventHandler<web.html.HTMLElement>",

    "Node | Window" to "web.events.EventTarget /* web.dom.Node? or web.window.Window? */",

    "null | HTMLElement" to "web.html.HTMLElement?",
    "null | Element | ((element: Element) => Element)" to "Element? /* null | Element | ((element: Element) => Element) */",
    "string | ((value: number, index: number) => React.ReactNode)" to "String /* or (value: Number, index: Number) -> react.ReactNode*/",

    "DisableClearable" to "Boolean",
    "FreeSolo" to "Boolean",

    "SelectionMode" to "mui.system.Union /* 'none' | 'single' | 'multiple' */",

    // MUI v6 Grid2/PigmentGrid use GridSize but each .d.ts defines own variant; we keep only Grid's
    // declaration and route the others through this opaque alias.
    "GridSize" to "mui.system.Union /* 'auto' | 'grow' | number | false */",

    "{ [key in Breakpoint]: number }" to "Record<Breakpoint, Number>",
    "Record<string, any>" to "Record<String, *>",
    "Record<string, any> & { mode: 'light' | 'dark' }" to "Record<String, *>",

    CREATE_TRANSITION to "(props: ReadonlyArray<String>, options: TransitionCreateOptions?) -> web.cssom.Transition",
    SWIPEABLE_DRAWER_PROPS_ALLOW_SWIPE_IN_CHILDREN to "Boolean /* or (e: TouchEvent, swipeArea: HTMLDivElement, paper: HTMLDivElement) -> Boolean*/",
    USE_TAB_PANEL_RETURN_VALUE_GET_ROOT_PROPS to "() -> UseTabPanelRootSlotProps",

    "'horizontal' | 'vertical'" to "Orientation",
    "'vertical' | 'horizontal'" to "Orientation",

    "typeof window.matchMedia" to "(query: String) -> web.cssom.MediaQueryList",

    "PopperPlacementType" to "popper.core.Placement",

    "typeof create" to "(props: ReadonlyArray<String>, options: TransitionCreateOptions?) -> web.cssom.Transition",
    "typeof getAutoHeightDuration" to "(height: Number) -> Number",

    "TabsDirection" to "mui.system.Direction",

    "MenuContextType" to "Any /* mui.base.MenuContextType */",
    "<ExternalProps extends Record<string, unknown> = {}>(externalProps?: ExternalProps) => UseMenuListboxSlotProps" to
            "Any /* <ExternalProps extends Record<string, unknown> = {}>(externalProps?: ExternalProps) => UseMenuListboxSlotProps */",

    "<ExternalProps extends Record<string, unknown> = {}>(externalProps?: ExternalProps) => UseMenuButtonRootSlotProps<ExternalProps>" to
            "Any /* <ExternalProps extends Record<string, unknown> = {}>(externalProps?: ExternalProps) => UseMenuButtonRootSlotProps<ExternalProps> */",

    "StateChangeCallback<State>" to "Any /* StateChangeCallback<State> */",
    "PopupProps['anchor']" to "Any /* PopupProps['anchor'] */",

    "TreeViewItemsReorderingAction" to "TreeItem2DragAndDropOverlayAction",

    // LocalizationProvider
    "Partial<AdapterFormats>" to "Any /* Partial<AdapterFormats> */",
    "TLocale" to "Any /* TLocale */",
    "PickersInputLocaleText<TDate>" to "Any /* PickersInputLocaleText<TDate> */",
)

/**
 * Whether [kotlinType] would resolve this bare name to a type of its own instead of leaving it unknown.
 *
 * Both tables it consults were built for the MUI target, and a Base UI declaration's *type parameter*
 * can happen to share a name with one of their entries — `Value` is `Autocomplete`'s value parameter
 * here and `SliderRootProps`' in Base UI. Since type parameters are dropped from the emitted declaration
 * (BASE_UI_TODO.md gap 4), such a name resolves to the MUI type and the reference does not compile.
 * See `substituteTypeParameterBounds` in BaseUi.kt, which is the only caller.
 */
internal fun isKnownTypeName(
    name: String,
): Boolean =
    name in KNOWN_TYPES || KNOWN_TYPE_SUFFIXES.any { name.endsWith(it) }

internal fun kotlinType(
    type: String,
    name: String? = null,
): String {
    // v7 appends an explicit `| undefined` to optional members (callbacks, unions, etc.).
    // Optionality is already encoded by the `?:` marker (see MemberConverter.convertProperty),
    // so drop the redundant trailing union member. This both fixes function types like
    // `((…) => void) | undefined` and restores the v6-shaped strings the rules below match on.
    // The function was wrapped in grouping parens only to attach `| undefined` (`((…) => …) | undefined`);
    // once that's gone the outer pair is redundant and would otherwise double up as `(((…)->…))?`.
    if (type.endsWith(" | undefined"))
        return kotlinType(unwrapRedundantParens(type.removeSuffix(" | undefined")), name)

    if (type in KNOWN_TYPES)
        return type

    if (type == "number" && name == "tabIndex")
        return "Int"

    if (type == "number" && name in NUMBER_AS_INT_PROPERTIES)
        return "Int"

    if (type == "number" && name in NUMBER_AS_DOUBLE_PROPERTIES)
        return "Double"

    if (type == "string" && name != null && name.endsWith("ClassName"))
        return "ClassName"

    // TODO: Need to support "unknown" -> "Any" for all others
    if (("unknown" == type || type == "Value") && name == "value")
        return "Any"

    // For `RegularBreakpoints` of `Grid` component
    if (name in setOf("lg", "md", "sm", "xl", "xs") && type == "boolean | GridSize")
        return "Any /* boolean | 'auto' | number */"

    // For `FormControl.FormControlOwnProps`
    if (name == "defaultValue" && type == "unknown")
        return "Any"

    // For `FormControl.FormControlOwnProps`
    if (name == "anchorEl" && "null" in type && "Element" in type && "(() => Element)" in type && "PopoverVirtualElement" in type && "(() => PopoverVirtualElement)" in type)
        return "Element? /* null | Element | (() => Element) | PopoverVirtualElement | (() => PopoverVirtualElement) */"

    // For `useList.UseListReturnValue`
    if (name == "getRootProps" && type == "<TOther extends EventHandlers = {}>(otherHandlers?: TOther) => UseListRootSlotProps<TOther>")
        return "Any /* $type */"

    // For `useAutocomplete`
    if (
        (name == "getTagProps" && type == "AutocompleteGetTagProps")
        || (name == "value" && type == "AutocompleteValue<Value, Multiple, DisableClearable, FreeSolo>")
        || (name == "groupedOptions" && type == "Value[] | Array<AutocompleteGroupedOption<Value>>")
    )
        return "Any /* $type */"

    // For `Input.InputBaseProps`
    if (name == "type" && type == "undefined")
        return "InputType"

    // For `Snackbar.SnackbarClickAwayListenerSlotProps`
    if (name == "ownerState" && type == "SnackbarOwnerState")
        return "Any"

    // For `UseListParameters`
    if (name == "stateReducer" && type == "(state: State, action: ActionWithContext<ListAction<ItemValue> | CustomAction, ListActionContext<ItemValue> & CustomActionContext>) => State")
        return "Any /* $type */"

    // For `Select` (see `Select` in flst for `SelectValue<OptionValue, Multiple>` replacement)
    if (
        (name == "defaultValue" && type == "SelectValue<OptionValue, Multiple>")
        || (name == "value" && type == "SelectValue<OptionValue, Multiple>")
        || (name == "multiple" && type == "Multiple")
        || (name == "getSerializedValue" && type == "(option: SelectValue<SelectOption<OptionValue>, Multiple>) => React.InputHTMLAttributes<HTMLInputElement>['value']")
        || (name == "onChange" && type == "(event: React.MouseEvent | React.KeyboardEvent | React.FocusEvent | null, value: SelectValue<OptionValue, Multiple>) => void")
        || (name == "onHighlightChange" && type == "(event: React.MouseEvent<Element, MouseEvent> | React.KeyboardEvent<Element> | React.FocusEvent<Element, Element> | null, highlighted: OptionValue | null) => void")
        || (name == "renderValue" && type == "(option: SelectValue<SelectOption<OptionValue>, Multiple>) => React.ReactNode")
    )
        return "Any /* $type */"
    if (name == "popper" && type == "React.ComponentType<WithOptionalOwnerState<SelectPopperSlotProps<OptionValue, Multiple>>>")
        return "react.ComponentType<*>"

    // For `useListbox`
    if (name == "stateReducer" && type == "ListboxReducer<TOption>")
        return "Any /* ListboxReducer<TOption> */"

    // For `useMenu`
    if (name == "menuItems" && type == "Record<string, MenuItemMetadata>")
        return "Any /* Record<string, MenuItemMetadata> */"

    // For `useTabs`
    if (name == "tabsContextValue" && type == "TabsContextValue")
        return "Any /* TabsContextValue */"

    // For system theme interfaces
    // v7 widened this to `Record<string, any> | undefined`; strip the optional suffix
    // before the map lookup and fall back to the raw type so we never throw on a miss.
    if (name == "palette" && type.startsWith("Record<")) {
        val baseType = type.removeSuffix(" | undefined")
        return "Any? /* ${STANDARD_TYPE_MAP[baseType] ?: baseType} */"
    }

    if (name == "dateAdapter")
        return "$DATE_ADAPTER /* $type */"

    // `createTransitions` `TransitionsOptions.create`: v7 reformats the inline signature (single-line
    // params, multi-line `Partial<{…}>`, trailing `| undefined`) so the exact `CREATE_TRANSITION`
    // literal in STANDARD_TYPE_MAP no longer matches; without this it falls to `toFunctionType()`
    // which degrades `Partial<{…}>` → `Any?` and `string` → `String`. Match by shape instead.
    // (`Transitions.create` uses the `typeof create` map entry and is unaffected.)
    if (name == "create" && "=> string" in type &&
        "duration" in type && "easing" in type && "delay" in type
    )
        return "(props: ReadonlyArray<String>, options: TransitionCreateOptions?) -> web.cssom.Transition"

    // v7 types some props-bag members as a bare `object`/`any` (e.g. Autocomplete `InputProps` in
    // AutocompleteRenderInputParams). A `*Props`-named member is a props bag — prefer `react.Props`
    // over the generic `object → Any` STANDARD_TYPE_MAP entry below.
    if (name != null && name.endsWith("Props") && (type == "object" || type == "any"))
        return "react.Props"

    STANDARD_TYPE_MAP[type]
        ?.also { return it }

    // v7 inline object-literal member types (e.g. Breadcrumbs/StepLabel/FormControlLabel `slotProps`):
    // no Kotlin structural equivalent — widen to `Any?`. Strip inner doc-comments and collapse
    // whitespace so we never emit a nested (and therefore unclosed) `/* … */`.
    // EXCEPTION: `classes`/`components`/`componentsProps`/`slots`/`slotProps` have dedicated
    // nested-interface handlers below; and a `*Props`-named member (e.g. useSlider `axisProps`) is a
    // props bag — let it reach the `react.Props` handler instead of widening to `Any?`.
    if (type.startsWith("{") &&
        name !in STRUCTURED_INLINE_MEMBER_NAMES &&
        !(name != null && name.endsWith("Props"))
    ) {
        val oneLine = type
            .replace(Regex("""/\*.*?\*/""", RegexOption.DOT_MATCHES_ALL), "")
            .replace(Regex("""\s+"""), " ")
            .trim()
        return "Any? /* $oneLine */"
    }

    type.toFunctionType()
        ?.also { return it }

    // Generic `T[]` → `ReadonlyArray<T>` for simple identifier element types
    // (point-cases like `Breakpoint[]` are still in STANDARD_TYPE_MAP and take precedence above).
    // Covers `number[]`, `Value[]`, `MyType[]` etc. that the explicit map didn't list.
    Regex("""^(\w+)\[\]$""").matchEntire(type)?.let { m ->
        val elem = when (val raw = m.groupValues[1]) {
            "number" -> "Number"
            "string" -> "String"
            "boolean" -> "Boolean"
            else -> raw
        }
        return "ReadonlyArray<$elem>"
    }

    // `(X | null)[]`, `(A | B)[]`, etc. — an array whose element type is parenthesized (unions,
    // nullable elements). The bare-identifier regex above only covers `\w+[]`; here we strip the
    // trailing `[]`, unwrap the redundant outer parens around the element type, and recursively
    // convert it — so e.g. `(PickerValidDate | null)[]` becomes `ReadonlyArray<PickerValidDate?>`
    // instead of falling through to the `Any? /* … */` catch-all below.
    if (type.endsWith("[]")) {
        val element = type.removeSuffix("[]")
        val unwrapped = unwrapRedundantParens(element)
        if (unwrapped != element)
            return "ReadonlyArray<${kotlinType(unwrapped, name)}>"
    }

    if ((name == "minRows" || name == "maxRows") && type == "string | number")
        return "Int"

    if (type.endsWith(" | null")) {
        val t = kotlinType(type.removeSuffix(" | null"))
        return when {
            t == DYNAMIC -> t
            "? /*" in t -> t // already nullable from Any?/* TS-source */ fallback
            t.endsWith("?") -> t
            else -> "$t?"
        }
    }

    if (KNOWN_TYPE_SUFFIXES.any { type.endsWith(it) } && " | " !in type && type != "Color")
        return type

    val prefixToType = KNOWN_TYPE_PREFIX_MAP.filter { (prefix) -> type.startsWith(prefix) }
    if (prefixToType.isNotEmpty())
        return prefixToType.values.single()

    val promiseResult = type.removeSurrounding("Promise<", ">")
    if (promiseResult != type)
        return "$PROMISE<${kotlinType(promiseResult)}>"

    val styleValueResult = type.removeSurrounding("ResponsiveStyleValue<", ">")
    if (styleValueResult != type) {
        // ResponsiveStyleValue<T : Any> requires non-null. Drop `?` from fallback so the bound is satisfied.
        val inner = kotlinType(styleValueResult)
        val nonNullInner = when {
            inner.startsWith("Any? /*") -> "Any" + inner.removePrefix("Any?")
            else -> inner
        }
        return "mui.system.ResponsiveStyleValue<$nonNullInner>"
    }

    val refResult = type.removeSurrounding("React.Ref<", ">")
    if (refResult != type) {
        // Ref<in T : Any> requires non-null, same as ResponsiveStyleValue above. An inner type that
        // falls through to the `Any? /* … */` convention would violate the bound and fail to compile.
        val inner = kotlinType(refResult)
        val nonNullInner = when {
            inner.startsWith("Any? /*") -> "Any" + inner.removePrefix("Any?")
            else -> inner
        }
        return "react.Ref<$nonNullInner>"
    }

    if (type.startsWith("TreeViewExperimentalFeatures<"))
        return "Any? /* $type */"

    if (type.startsWith("React.ElementType<"))
        return type.replace("React.ElementType", ELEMENT_TYPE)
            .replace("<TransitionProps>", "<mui.material.transitions.TransitionProps>")
            .replace(
                "React.HTMLAttributes<HTMLDivElement>",
                "react.dom.html.HTMLAttributes<web.html.HTMLDivElement>"
            )

    if (type.startsWith("React.") && "Handler<" in type) {
        var handlerType = type.removePrefix("React.")
            .replace("<any>", "<*>")
            .replace("<{}>", "<*>")
            .replace("<HTMLInputElement | HTMLTextAreaElement>", "<web.html.HTMLElement>")
            .replace("<HTMLTextAreaElement | HTMLInputElement>", "<web.html.HTMLElement>")
            .replace("<HTMLInputElement>", "<web.html.HTMLInputElement>")

        // FormEventHandler was removed from react-dom wrappers; use ReactEventHandler.
        if (handlerType.startsWith("FormEventHandler<"))
            handlerType = handlerType.replaceFirst("FormEventHandler<", "ReactEventHandler<")

        // ChangeEventHandler<T> gained a second target-element type parameter.
        if (handlerType.startsWith("ChangeEventHandler<") && !handlerType.contains(","))
            handlerType = handlerType.removeSuffix(">") + ", *>"

        return "react.dom.events.$handlerType"
    }

    // `React.JSXElementConstructor<X>` and `React.ComponentType<X>` (the former is normalized to the
    // latter for const declarations, but member types like Dialog `TransitionComponent` reach here as
    // either). v7 often intersects the arg (`<TransitionProps & { children: … }>`); dropInlineIntersections
    // already reduced it to the base props type. Emit `react.ComponentType<X>`, falling back to `<*>`.
    val ctorPrefix = listOf("React.JSXElementConstructor<", "React.ComponentType<")
        .firstOrNull { type.startsWith(it) }
    if (ctorPrefix != null) {
        val propsType = type.removeSurrounding(ctorPrefix, ">")
        val typeParameter = propsType
            .takeIf { it.endsWith("Props") }
            ?.let { STANDARD_TYPE_MAP[it] ?: it }
            ?: "*"

        return "react.ComponentType<$typeParameter>"
    }

    val partialResult = type.removeSurrounding("Partial<", ">")
    if (partialResult != type) {
        if (partialResult.endsWith("Props")) {
            return when (partialResult) {
                "TouchRippleProps",
                "NativeSelectInputProps",
                    -> "Any? /* Partial<$partialResult> */"

                "StandardInputProps",
                    -> "InputProps"

                "SelectProps",
                    -> "SelectProps<*>"

                else -> STANDARD_TYPE_MAP[partialResult] ?: partialResult
            }
        } else if (partialResult.endsWith("Classes")) {
            return partialResult
        } else if (partialResult in KNOWN_TYPES) {
            return partialResult
        }
    }

    if (type.startsWith("'")) {
        // TODO: Don't understand why need this check. Should work without. Try to remove
        if (name == "overlap" && type == "'rectangular' | 'circular'") {
            return "BadgeOverlap"
        }

        return "$UNION /* $type */"
    }

    if (type.startsWith("\n  | '")) {
        val t = type.removePrefix("\n")
            .trimIndent()
            .replace("\n", " ")
            .removePrefix("| ")

        return "$UNION /* $t */"
    }

    if (type.startsWith("OverridableStringUnion<")) {
        val comment = type.removeSurrounding("OverridableStringUnion<", ">")
            .splitToSequence("\n")
            .filter { it.isNotEmpty() }
            .map { it.trimStart() }
            .joinToString(" ")

        // v7 renamed the first union member `Variant` → `TypographyVariant`; accept both.
        if (comment == "Variant | 'inherit', TypographyPropsVariantOverrides" ||
            comment == "TypographyVariant | 'inherit', TypographyPropsVariantOverrides"
        )
            return "mui.material.styles.TypographyVariant"

        // TODO: Don't understand why need this check. Should work without. Try to remove
        if (name == "variant" && comment == "'standard' | 'dot', BadgePropsVariantOverrides")
            return "BadgeVariant"

        return "$UNION /* $comment */"
    }

    if (type.startsWith("TypographyProps<"))
        return "TypographyProps"

    type.toAlias()
        ?.also { return it }

    if (type.endsWith("']") || type.endsWith("'] | 'auto'"))
        return "Any /* $type */"

    if (name == "classes" && type.contains("{\n")) {
        val interfaceName = name.replaceFirstChar(Char::titlecase)
        return interfaceName + "\n\n" + convertInlineClasses(interfaceName, type)
    }

    // TODO: Remove when MUI completes migration to slots
    if ((name == "components" || name == "componentsProps") && type.startsWith("{\n")) {
        val interfaceName = name.replaceFirstChar(Char::titlecase)
        val defaultType = if (name == "components") "react.ElementType<*>" else "react.Props"
        return interfaceName + "\n\n" + componentInterface(interfaceName, type, defaultType)
    }

    // TODO: Need to process `SlotProps` interface separately from parent interface
    if (name == "slots" || name == "slotProps") {
        return if (!type.startsWith("{\n")) {
            type
                .replace("<OptionValue, Multiple>", "")
                .replace("<TValue>", "")
        } else {
            // TODO: Else branch should die when MUI fully migrates to named slot types
            val interfaceName = name.replaceFirstChar(Char::titlecase)
            val defaultType = if (name == "slots") "react.ElementType<*>" else "react.Props"
            interfaceName + "\n\n" + componentInterface(interfaceName, type, defaultType)
        }
    }

    if (name != null && name.endsWith("Props") && name != "componentsProps") {
        val comment = type.split("\n").joinToString(" ") { it.trim() }

        return "react.Props /* $comment */"
    }

    return "Any? /* $type */"
}

// Member names with a dedicated nested-interface handler in kotlinType — their inline-object value
// must NOT be widened to `Any?` by the generic `{`-fallback.
private val STRUCTURED_INLINE_MEMBER_NAMES = setOf(
    "classes", "components", "componentsProps", "slots", "slotProps",
)

// If `type` is fully wrapped in one redundant outer paren pair, unwrap it once. v7 wraps members in
// grouping parens to attach `| undefined` — `((args) => ret)` (function) or `(ImgHTMLAttributes<…>)`
// (after the `& { sx }` intersection is dropped). Once the `| undefined` is gone the pair is redundant
// and would otherwise double up (`(((…)->…))?`) or hide the base type from STANDARD_TYPE_MAP lookup.
// Detects the wrapper by checking the first `(` matches the last char; leaves `(args) => ret`
// (first `(` closes mid-string) untouched.
private fun unwrapRedundantParens(type: String): String {
    if (!type.startsWith("(") || !type.endsWith(")")) return type
    var depth = 0
    for (i in type.indices) {
        when (type[i]) {
            '(' -> depth++
            ')' -> {
                depth--
                // First `(` closed before the end → not a whole-string wrapper.
                if (depth == 0 && i != type.lastIndex) return type
            }
        }
    }
    return type.substring(1, type.length - 1)
}

// Remove JSDoc blocks (`/** … */`) and any now-empty lines from an inline-object source, so the
// member-by-member parsers below (`split("?: ")` etc.) see only `name?: type;` lines. v7 added docs
// to many inline slot/component objects that v6 didn't have.
private fun stripInlineDocs(source: String): String =
    source
        .replace(Regex("""/\*\*.*?\*/""", RegexOption.DOT_MATCHES_ALL), "")
        .replace(Regex("""\n\s*\n"""), "\n")

private fun componentInterface(
    sourceName: String,
    source: String,
    defaultType: String,
): String {
    val body = stripInlineDocs(source)
        .removeSurrounding("{\n", ";\n}")
        .trimIndent()
        .replace(";\n}", "\n}")
        .replace(";\n  ", "\n  ")
        .splitToSequence(";\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .mapNotNull { line ->
            // Members are `name?: type` (optional) or, rarely, `name: type`. Split on the first
            // separator; skip anything without one (residual blank/brace lines after doc-stripping).
            val sep = when {
                "?: " in line -> "?: "
                ": " in line -> ": "
                else -> return@mapNotNull null
            }
            val name = line.substringBefore(sep)
            val typeSource = line.substringAfter(sep)

            // Resolve through the shared resolver so v7's trailing ` | undefined`, `Partial<XxxProps>`,
            // unions, `React.ElementType`, etc. are handled exactly as for ordinary members — instead
            // of collapsing every slot type to the generic `defaultType` (the old STANDARD_TYPE_MAP +
            // `\w+Props` mini-resolver missed `IconButtonProps | undefined`, `Partial<…>`, …).
            // Only a genuinely-unresolvable type falls back to `defaultType` (`react.Props` for
            // slotProps/componentsProps, `react.ElementType<*>` for slots/components).
            val resolved = kotlinType(typeSource, name).let {
                if (it.startsWith("Any? /*")) defaultType + "?" + it.removePrefix("Any?") else it
            }
            val type = when {
                resolved.endsWith("?") -> resolved
                "? /*" in resolved -> resolved
                resolved.endsWith("*/") -> resolved.replace(" /*", "? /*")
                resolved.startsWith("(") -> "($resolved)?"
                else -> "$resolved?"
            }

            "var $name: $type"
        }
        .joinToString("\n")

    return "interface $sourceName {\n$body\n}"
}
