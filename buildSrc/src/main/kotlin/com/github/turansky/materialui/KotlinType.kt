package com.github.turansky.materialui

private const val PROMISE = "kotlin.js.Promise"

private val CLASS_REGEX = Regex("""[\w\d]+""")

internal const val DYNAMIC = "dynamic"
internal const val UNION = "Union"

// use ElementType after update
private const val ELEMENT_TYPE = "react.ComponentType"

private val KNOWN_TYPES = setOf(
    "AlertColor",
    "GridDirection",
    "GridWrap",
    "Orientation",
    "PopoverReference",

    "SxProps<Theme>",
    "TransitionProps",
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

    "React.ReactElement" to "react.ReactElement",
    "React.ReactElement<any, any>" to "react.ReactElement",

    "React.ElementType" to "${ELEMENT_TYPE}<*>",

    "React.Ref<unknown>" to "react.Ref<*>",
    "React.Ref<any>" to "react.Ref<*>",

    "(event: React.SyntheticEvent) => void" to "(event: org.w3c.dom.events.Event) -> Unit",
    "(event: React.SyntheticEvent, checked: boolean) => void" to "(event: org.w3c.dom.events.Event, checked: Boolean) -> Unit",

    "(event: React.SyntheticEvent<{}>, reason: CloseReason) => void" to "(event: org.w3c.dom.events.Event, reason: CloseReason) -> Unit",
    "(event: React.SyntheticEvent<{}>, reason: OpenReason) => void" to "(event: org.w3c.dom.events.Event, reason: OpenReason) -> Unit",
)

internal fun kotlinType(
    type: String,
    name: String? = null,
): String {
    if (type in KNOWN_TYPES)
        return type

    STANDARD_TYPE_MAP[type]
        ?.also { return it }

    if (type.endsWith("Props") || type.endsWith("Origin") || type.endsWith("Position"))
        return type

    val promiseResult = type.removeSurrounding("Promise<", ">")
    if (promiseResult != type)
        return "$PROMISE<${kotlinType(promiseResult)}>"

    val styleValueResult = type.removeSurrounding("ResponsiveStyleValue<", ">")
    if (styleValueResult != type)
        return "ResponsiveStyleValue<${kotlinType(styleValueResult)}>"

    val refResult = type.removeSurrounding("React.Ref<", ">")
    if (refResult != type)
        return "react.Ref<${kotlinType(refResult)}>"

    if (type.startsWith("React.ElementType<"))
        return type.replace("React.ElementType", ELEMENT_TYPE)

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

    println(type)

    return DYNAMIC
}
