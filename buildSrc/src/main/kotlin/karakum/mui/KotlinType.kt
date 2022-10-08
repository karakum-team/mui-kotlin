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

private val KNOWN_TYPES = setOf(
    "T",
    "TDate",
    "TValue",
    "TLibFormatToken",
    "ReadonlyArray<T>",
    "PickerOnChangeFn<TDate>",
    "CalendarPickerView",

    "AlertColor",
    "GridDirection",
    "GridWrap",
    "Orientation",
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
) + UNION_PROPERTIES.map { it.capitalize() }

private val STANDARD_TYPE_MAP = mapOf(
    "any" to "Any",
    "object" to "Any",

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

    "Date" to "kotlin.js.Date",

    "readonly CalendarPickerView[]" to "ReadonlyArray<CalendarPickerView>",
    "Breakpoint[]" to "ReadonlyArray<Breakpoint>",
    "UsePaginationItem[]" to "ReadonlyArray<UsePaginationItem>",

    "HTMLDivElement" to "dom.html.HTMLDivElement",
    "HTMLInputElement" to "dom.html.HTMLInputElement",
    "HTMLTextAreaElement" to "dom.html.HTMLTextAreaElement",

    "Element | (() => Element | null) | null" to "dom.Element",
    "Partial<OptionsGeneric<any>>" to "popper.core.Options",
    "React.Ref<Instance>" to "react.Ref<popper.core.Instance>",
    "React.ElementType<TableCellBaseProps>" to "react.ElementType<*>",

    "{\n  bivarianceHack(event: {}, reason: 'backdropClick' | 'escapeKeyDown'): void;\n}['bivarianceHack']" to
            "(event: $DYNAMIC, reason: String) -> Unit",

    "React.ReactNode" to "react.ReactNode",
    "NonNullable<React.ReactNode>" to "react.ReactNode",
    "string | React.ReactElement" to "react.ReactNode",
    "string | number | React.ReactElement" to "react.ReactNode",

    "React.ReactElement" to "react.ReactElement<*>",
    "React.ReactElement<any, any>" to "react.ReactElement<*>",
    "NonNullable<React.ReactElement>" to "react.ReactElement<*>",

    "React.ElementType" to "$ELEMENT_TYPE<*>",

    "React.Ref<unknown>" to "react.Ref<*>",
    "React.Ref<any>" to "react.Ref<*>",

    "PaletteMode" to "mui.material.PaletteMode",
    "TransitionProps" to "mui.material.transitions.TransitionProps",
    "ClickAwayListenerProps" to "mui.base.ClickAwayListenerProps",
    "ChipProps<ChipComponent>" to "ChipProps",

    "React.InputHTMLAttributes<HTMLInputElement>" to "react.dom.html.InputHTMLAttributes<dom.html.HTMLInputElement>",
    "React.ImgHTMLAttributes<HTMLImageElement> & {\n  sx?: SxProps<Theme>;\n}" to "react.dom.html.ImgHTMLAttributes<dom.html.HTMLImageElement>",
    "React.ImgHTMLAttributes<HTMLImageElement>" to "react.dom.html.ImgHTMLAttributes<dom.html.HTMLImageElement>",
    "React.HTMLAttributes<HTMLDivElement>" to "react.dom.html.HTMLAttributes<dom.html.HTMLDivElement>",
    "Partial<React.HTMLAttributes<HTMLDivElement>>" to "react.dom.html.HTMLAttributes<dom.html.HTMLDivElement>",
    "React.HTMLAttributes<HTMLElement>" to "react.dom.html.HTMLAttributes<dom.html.HTMLElement>",

    "NonNullable<React.HTMLAttributes<any>['tabIndex']>" to "Int",
    "React.InputHTMLAttributes<unknown>['type']" to "react.dom.html.InputType",
    "React.InputHTMLAttributes<HTMLInputElement>['type']" to "react.dom.html.InputType",

    "React.ReactEventHandler" to "react.dom.events.ReactEventHandler<*>",
    "React.FocusEventHandler" to "react.dom.events.FocusEventHandler<*>",
    "React.MouseEventHandler" to "react.dom.events.MouseEventHandler<*>",
    "React.MouseEventHandler<HTMLElement>" to "react.dom.events.MouseEventHandler<dom.html.HTMLElement>",

    "null | Element | ((element: Element) => Element)" to "dom.Element? /* null | Element | ((element: Element) => Element) */",

    "DisableClearable" to "Boolean",
    "FreeSolo" to "Boolean",

    "{ [key in Breakpoint]: number }" to "Record<Breakpoint, Number>",
    "Record<string, any>" to "Record<String, *>",
    "Record<string, any> & { mode: 'light' | 'dark' }" to "Record<String, *>",

    CREATE_TRANSITION to "(props: ReadonlyArray<String>, options: TransitionCreateOptions?) -> csstype.Transition",

    "'horizontal' | 'vertical'" to "mui.material.Orientation",
    "'vertical' | 'horizontal'" to "mui.material.Orientation",

    "typeof window.matchMedia" to "(query: String) -> cssom.MediaQueryList",

    "PopperPlacementType" to "popper.core.Placement",

    "typeof create" to "(props: ReadonlyArray<String>, options: TransitionCreateOptions?) -> csstype.Transition",
    "typeof getAutoHeightDuration" to "(height: Number) -> Number",

    "TabsUnstyledDirection" to "mui.system.Direction",
)

internal fun kotlinType(
    type: String,
    name: String? = null,
): String {
    if (type in KNOWN_TYPES)
        return type

    if (type == "number" && name == "tabIndex")
        return "Int"

    if (type == "string" && name != null && name.endsWith("ClassName"))
        return "ClassName"

    // For system theme interfaces
    if (name == "palette" && type.startsWith("Record<"))
        return "$DYNAMIC /* ${STANDARD_TYPE_MAP.getValue(type)} */"

    if (name == "dateAdapter")
        return "$DATE_ADAPTER /* $type */"

    STANDARD_TYPE_MAP[type]
        ?.also { return it }

    type.toFunctionType()
        ?.also { return it }

    if ((name == "minRows" || name == "maxRows") && type == "string | number")
        return "Int"

    if (type.endsWith(" | null")) {
        val t = kotlinType(type.removeSuffix(" | null"))
        return if (t == DYNAMIC) t else "$t?"
    }

    if (KNOWN_TYPE_SUFFIXES.any { type.endsWith(it) } && " | " !in type && type != "Color")
        return type

    val promiseResult = type.removeSurrounding("Promise<", ">")
    if (promiseResult != type)
        return "$PROMISE<${kotlinType(promiseResult)}>"

    val styleValueResult = type.removeSurrounding("ResponsiveStyleValue<", ">")
    if (styleValueResult != type)
        return "mui.system.ResponsiveStyleValue<${kotlinType(styleValueResult)}>"

    val refResult = type.removeSurrounding("React.Ref<", ">")
    if (refResult != type)
        return "react.Ref<${kotlinType(refResult)}>"

    if (type.startsWith("React.ElementType<"))
        return type.replace("React.ElementType", ELEMENT_TYPE)
            .replace("<TransitionProps>", "<mui.material.transitions.TransitionProps>")
            .replace(
                "React.HTMLAttributes<HTMLDivElement>",
                "react.dom.html.HTMLAttributes<dom.html.HTMLDivElement>"
            )

    if (type.startsWith("React.") && "Handler<" in type) {
        val handlerType = type.removePrefix("React.")
            .replace("<any>", "<*>")
            .replace("<{}>", "<*>")
            .replace("<HTMLInputElement | HTMLTextAreaElement>", "<dom.html.HTMLElement>")
            .replace("<HTMLTextAreaElement | HTMLInputElement>", "<dom.html.HTMLElement>")
            .replace("<HTMLInputElement>", "<dom.html.HTMLInputElement>")

        return "react.dom.events.$handlerType"
    }

    val propsType = type.removeSurrounding("React.JSXElementConstructor<", ">")
    if (propsType != type) {
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
                -> DYNAMIC

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

    if (type.startsWith("'"))
        return "$UNION /* $type */"

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

        if (comment == "Variant | 'inherit', TypographyPropsVariantOverrides")
            return "mui.material.styles.TypographyVariant"

        return "$UNION /* $comment */"
    }

    if (type.startsWith("TypographyProps<"))
        return "TypographyProps"

    type.toAlias()
        ?.also { return it }

    if (type.endsWith("']") || type.endsWith("'] | 'auto'"))
        return "$DYNAMIC /* $type */"

    if ((name == "components" || name == "componentsProps") && type.startsWith("{\n") && "/**" !in type) {
        val interfaceName = name.capitalize()
        val defaultType = if (name == "components") "react.ElementType<*>" else "react.Props"
        return interfaceName + "\n\n" + componentInterface(interfaceName, type, defaultType)
    }

    if (name != null && name.endsWith("Props") && name != "componentsProps") {
        val comment = type.split("\n")
            .map { it.trim() }
            .joinToString(" ")

        return "react.Props /* $comment */"
    }

    return DYNAMIC
}

private fun componentInterface(
    sourceName: String,
    source: String,
    defaultType: String,
): String {
    val body = source
        .removeSurrounding("{\n", ";\n}")
        .trimIndent()
        .replace(";\n}", "\n}")
        .replace(";\n  ", "\n  ")
        .splitToSequence(";\n")
        .joinToString("\n") { line ->
            val (name, typeSource) = line.split("?: ")
            val type = STANDARD_TYPE_MAP[typeSource]
                ?.let { "$it?" }
                ?: "$defaultType? /* $typeSource */"

            "var $name: $type"
        }

    return "interface $sourceName {\n$body\n}"
}
