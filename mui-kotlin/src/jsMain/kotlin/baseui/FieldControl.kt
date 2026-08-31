// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface FieldControlProps :
    BaseUiInputProps {
    /**
     * Callback fired when the `value` changes. Use when controlled.
     */
    var onValueChange: ((value: String, eventDetails: FieldControlChangeEventDetails) -> Unit)?

    var defaultValue: Any? /* React.ComponentProps<'input'>['defaultValue'] */
}

external interface FieldControlState : FieldRootState

external interface FieldControlChangeEventDetails : BaseUIChangeEventDetails
