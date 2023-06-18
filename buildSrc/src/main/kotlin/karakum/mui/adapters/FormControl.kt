package karakum.mui.adapters

// TODO: Remove when `useFormControlContext` will be supported
fun String.adaptFormControl(): String {
    return replace(
        """
export interface UseFormControlContextReturnValue extends FormControlState {
}
""",
        "",
    )
}

