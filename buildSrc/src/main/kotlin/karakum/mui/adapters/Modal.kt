package karakum.mui.adapters

fun String.adaptModal(): String {
    return replace(
        " & Omit<ModalUnstyledTypeMap['props'], 'slotProps'>",
        "",
    )
}
