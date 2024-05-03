package karakum.mui

internal const val MUI = "Mui"
internal const val MUI_BASE = "Base"

internal val MUI_COMMON_CLASSES = listOf(
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
    keys = MUI_COMMON_CLASSES,
    getValue = { "$MUI-$it" },
    type = "ClassName",
)
