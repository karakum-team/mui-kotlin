package karakum.mui

internal const val MUI = "Mui"

private val MUI_CLASSES = listOf(
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
    keys = MUI_CLASSES,
    getValue = { "$MUI-$it" },
    type = "ClassName",
)
