package karakum.mui.adapters

fun String.adaptInitColorSchemeScript(): String {
    return replace(
        oldValue = "declare const _default: typeof SystemInitColorSchemeScript;",
        newValue = "",
    )
}
