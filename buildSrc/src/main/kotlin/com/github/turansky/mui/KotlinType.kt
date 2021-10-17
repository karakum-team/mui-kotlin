package com.github.turansky.mui

private const val PROMISE = "kotlin.js.Promise"

internal const val DYNAMIC = "dynamic"
internal const val UNION = "mui.system.Union"

private const val ELEMENT_TYPE = "react.ElementType"

private val KNOWN_TYPES = setOf(
    "AlertColor",
    "GridDirection",
    "GridWrap",
    "Orientation",
    "PopoverReference",
)

private val STANDARD_TYPE_MAP = mapOf(
    "any" to "Any",
    "object" to "Any",

    "boolean" to "Boolean",
    "number" to "Number",
    "string" to "String",

    "void" to "Unit",
    "null" to "Nothing?",

    "Date" to "kotlin.js.Date",

    "HTMLDivElement" to "org.w3c.dom.HTMLDivElement",
    "HTMLInputElement" to "org.w3c.dom.HTMLInputElement",
    "HTMLTextAreaElement" to "org.w3c.dom.HTMLTextAreaElement",

    "React.ReactNode" to "react.ReactNode",
    "NonNullable<React.ReactNode>" to "react.ReactNode",

    "React.ReactElement" to "react.ReactElement",
    "React.ReactElement<any, any>" to "react.ReactElement",

    "React.ElementType" to "$ELEMENT_TYPE<*>",

    "React.Ref<unknown>" to "react.Ref<*>",
    "React.Ref<any>" to "react.Ref<*>",

    "SxProps<Theme>" to "mui.system.SxProps<mui.system.Theme>",
    "TransitionProps" to "mui.material.transitions.TransitionProps",

    "React.InputHTMLAttributes<HTMLInputElement>" to "react.dom.html.InputHTMLAttributes<org.w3c.dom.HTMLInputElement>",
    "React.ImgHTMLAttributes<HTMLImageElement>" to "react.dom.html.ImgHTMLAttributes<org.w3c.dom.HTMLImageElement>",
    "React.HTMLAttributes<HTMLDivElement>" to "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLDivElement>",
    "React.HTMLAttributes<HTMLElement>" to "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLElement>",

    "NonNullable<React.HTMLAttributes<any>['tabIndex']>" to "Int",
    "React.InputHTMLAttributes<unknown>['type']" to "react.dom.html.InputType",
    "React.InputHTMLAttributes<HTMLInputElement>['type']" to "react.dom.html.InputType",

    "null | Element | ((element: Element) => Element)" to "(element: org.w3c.dom.Element) -> org.w3c.dom.Element",

    "(event: React.SyntheticEvent) => void" to "(event: react.dom.events.SyntheticEvent<*, *>) -> Unit",
    "(event: React.SyntheticEvent, checked: boolean) => void" to "(event: react.dom.events.SyntheticEvent<*, *>, checked: Boolean) -> Unit",
    "(event: React.SyntheticEvent, value: any) => void" to "(event: react.dom.events.SyntheticEvent<*, *>, value: $DYNAMIC) -> Unit",

    "(event: React.SyntheticEvent<{}>, reason: CloseReason) => void" to "(event: react.dom.events.SyntheticEvent<*, *>, reason: CloseReason) -> Unit",
    "(event: React.SyntheticEvent<{}>, reason: OpenReason) => void" to "(event: react.dom.events.SyntheticEvent<*, *>, reason: OpenReason) -> Unit",
)

internal fun kotlinType(
    type: String,
    name: String? = null,
): String {
    if (type in KNOWN_TYPES)
        return type

    STANDARD_TYPE_MAP[type]
        ?.also { return it }

    if (type.endsWith(" | null")) {
        val t = kotlinType(type.removeSuffix(" | null"))
        return if (t == DYNAMIC) t else "$t?"
    }

    if (type.endsWith("Props") || type.endsWith("Origin") || type.endsWith("Position"))
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

                else -> partialResult
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

    if (type.endsWith("']") || type.endsWith("'] | 'auto'"))
        return "$DYNAMIC /* $type */"

    return DYNAMIC
}
