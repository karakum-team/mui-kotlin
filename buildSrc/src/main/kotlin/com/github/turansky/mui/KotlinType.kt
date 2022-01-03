package com.github.turansky.mui

private const val PROMISE = "kotlin.js.Promise"

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
)

private val KNOWN_TYPE_SUFFIXES = setOf(
    "Props",
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

    "string[]" to "ReadonlyArray<String>",

    "Date" to "kotlin.js.Date",

    "readonly CalendarPickerView[]" to "ReadonlyArray<CalendarPickerView>",
    "Breakpoint[]" to "ReadonlyArray<Breakpoint>",
    "UsePaginationItem[]" to "ReadonlyArray<UsePaginationItem>",

    "HTMLDivElement" to "org.w3c.dom.HTMLDivElement",
    "HTMLInputElement" to "org.w3c.dom.HTMLInputElement",
    "HTMLTextAreaElement" to "org.w3c.dom.HTMLTextAreaElement",

    "React.ReactNode" to "react.ReactNode",
    "NonNullable<React.ReactNode>" to "react.ReactNode",
    "string | React.ReactElement" to "react.ReactNode",
    "string | number | React.ReactElement" to "react.ReactNode",

    "React.ReactElement" to "react.ReactElement",
    "React.ReactElement<any, any>" to "react.ReactElement",
    "NonNullable<React.ReactElement>" to "react.ReactElement",

    "React.ElementType" to "$ELEMENT_TYPE<*>",

    "React.Ref<unknown>" to "react.Ref<*>",
    "React.Ref<any>" to "react.Ref<*>",

    "SxProps<Theme>" to "mui.system.SxProps<mui.system.Theme>",
    "TransitionProps" to "mui.material.transitions.TransitionProps",
    "ClickAwayListenerProps" to "mui.base.ClickAwayListenerProps",

    "React.InputHTMLAttributes<HTMLInputElement>" to "react.dom.html.InputHTMLAttributes<org.w3c.dom.HTMLInputElement>",
    "React.ImgHTMLAttributes<HTMLImageElement>" to "react.dom.html.ImgHTMLAttributes<org.w3c.dom.HTMLImageElement>",
    "React.HTMLAttributes<HTMLDivElement>" to "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>",
    "Partial<React.HTMLAttributes<HTMLDivElement>>" to "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>",
    "React.HTMLAttributes<HTMLElement>" to "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLElement>",

    "NonNullable<React.HTMLAttributes<any>['tabIndex']>" to "Int",
    "React.InputHTMLAttributes<unknown>['type']" to "react.dom.html.InputType",
    "React.InputHTMLAttributes<HTMLInputElement>['type']" to "react.dom.html.InputType",

    "React.ReactEventHandler" to "react.dom.events.ReactEventHandler<*>",

    "null | Element | ((element: Element) => Element)" to "(element: org.w3c.dom.Element) -> org.w3c.dom.Element",

    "DisableClearable" to "Boolean",
    "FreeSolo" to "Boolean",

    "{ [key in Breakpoint]: number }" to "kotlinext.js.Record<Breakpoint, Number>",
    "Record<string, any>" to "kotlinext.js.Record<String, *>",
    "Record<string, any> & { mode: 'light' | 'dark' }" to "kotlinext.js.Record<String, *>",

    CREATE_TRANSITION to "(props: ReadonlyArray<String>?, options: $DYNAMIC) -> String",

    "'horizontal' | 'vertical'" to "Orientation",
    "'vertical' | 'horizontal'" to "Orientation",
)

internal fun kotlinType(
    type: String,
    name: String? = null,
): String {
    if (type in KNOWN_TYPES)
        return type

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
            .replace("React.HTMLAttributes<HTMLDivElement>", "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>")

    if (type.startsWith("React.") && "Handler<" in type) {
        val handlerType = type.removePrefix("React.")
            .replace("<any>", "<*>")
            .replace("<{}>", "<*>")
            .replace("<HTMLInputElement | HTMLTextAreaElement>", "<org.w3c.dom.HTMLElement>")
            .replace("<HTMLTextAreaElement | HTMLInputElement>", "<org.w3c.dom.HTMLElement>")

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

        return "$UNION /* $comment */"
    }

    if (type.startsWith("TypographyProps<"))
        return "TypographyProps"

    type.toAlias()
        ?.also { return it }

    if (type.endsWith("']") || type.endsWith("'] | 'auto'"))
        return "$DYNAMIC /* $type */"

    return DYNAMIC
}
