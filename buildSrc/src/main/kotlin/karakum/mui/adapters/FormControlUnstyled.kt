package karakum.mui.adapters

// TODO: Remove when `useFormControlUnstyledContext` will be supported
fun String.adaptFormControlUnstyled(): String {
    return replace(
        """
export interface UseFormControlUnstyledContextReturnValue extends FormControlUnstyledState {
}
""",
        "",
    )
}

