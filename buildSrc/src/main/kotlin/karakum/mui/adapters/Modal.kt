package karakum.mui.adapters

fun String.adaptModal(): String = this
    .replace(
        oldValue = " & Omit<BaseModalTypeMap['props'], 'slotProps'>",
        newValue = "",
    )
