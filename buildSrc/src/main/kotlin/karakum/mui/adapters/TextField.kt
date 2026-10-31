package karakum.mui.adapters

// v9 removed `InputProps`/`inputProps`/`InputLabelProps`/`FormHelperTextProps`/`SelectProps` from TextField,
// so `slots`/`slotProps` are the only way to configure its inner components. Upstream declares them on a
// generic alias attached to each variant:
//   export type TextFieldSlotsAndSlotProps<InputPropsType> = CreateSlotsAndSlotProps<TextFieldSlots, { … }>;
//   export interface StandardTextFieldProps extends BaseTextFieldProps, TextFieldSlotsAndSlotProps<StandardInputProps>
// The alias converter only recognizes non-generic aliases, and Kotlin's `TextFieldProps` is `BaseTextFieldProps`
// (see `Converter.kt::findProps`), which carries neither member — so `TextField` had no `slotProps` at all.
//
// `InputPropsType` is only the `input` slot's props type: Input/FilledInput/OutlinedInput props all extend
// `InputBaseProps`, so resolve it to that (a variant-specific `OutlinedInputProps` is still assignable) and
// attach the now non-generic alias to `BaseTextFieldProps`. The variants' own `TextFieldSlotsAndSlotProps<…>`
// parents are stripped in `Converter.kt::adaptRawContent`; they inherit it through `BaseTextFieldProps`.
fun String.adaptTextField(): String {
    val alias = "export type TextFieldSlotsAndSlotProps<InputPropsType> = CreateSlotsAndSlotProps<"
    if (alias !in this)
        return this

    return replace(alias, "export type TextFieldSlotsAndSlotProps = CreateSlotsAndSlotProps<")
        .replace("React.ElementType<InputPropsType>", "React.ElementType<InputBaseProps>")
        .replace(
            "'onChange' | 'onBlur' | 'onFocus' | 'defaultValue'> {",
            "'onChange' | 'onBlur' | 'onFocus' | 'defaultValue'>, TextFieldSlotsAndSlotProps {",
        )
}
