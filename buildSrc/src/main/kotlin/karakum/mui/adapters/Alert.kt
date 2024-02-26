package karakum.mui.adapters

fun String.adaptAlert(): String = this
    .replace(
        oldValue = "export interface AlertOwnerState extends AlertProps {}",
        newValue = "",
    ).replace(
        oldValue = "AlertProps & AlertSlotsAndSlotProps",
        newValue = "AlertProps"
    )
