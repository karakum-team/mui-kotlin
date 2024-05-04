package karakum.mui

internal const val MUI = "Mui"

internal val MUI_COMMON_CLASS_MODIFIERS = listOf(
    "active",
    "checked",
    "completed",
    "disabled",
    "error",
    "expanded",
    "focusVisible",
    "focused",
    "required",
    "selected",
)

internal val MUI_BODY = convertSealed(
    name = MUI,
    keys = MUI_COMMON_CLASS_MODIFIERS,
    getValue = { "$MUI-$it" },
    type = "ClassName",
)
