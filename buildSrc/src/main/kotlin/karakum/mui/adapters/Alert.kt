package karakum.mui.adapters

fun String.adaptAlert(): String = this
    .replace(
        oldValue = "export interface AlertOwnerState extends AlertProps {}",
        newValue = "",
    ).replace(
        oldValue = "StandardProps<PaperProps, 'variant'>, AlertSlotsAndSlotProps",
        newValue = "StandardProps<PaperProps, 'variant'>"
    )
