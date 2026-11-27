// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/input")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.FC

external interface InputProps :
    BaseUiInputProps {
    /**
     * Callback fired when the `value` changes. Use when controlled.
     */
    var onValueChange: ((value: String, eventDetails: InputChangeEventDetails) -> Unit)?

    /**
     * The default value of the input. Use when uncontrolled.
     */
    var defaultValue: Any? /* FieldControlProps['defaultValue'] */

    /**
     * The value of the input. Use when controlled.
     */
    var value: Any? /* React.ComponentProps<'input'>['value'] */
}

external interface InputState : FieldControlState

external interface InputChangeEventDetails : FieldControlChangeEventDetails

/**
 * A native input element that automatically works with [Field](https://base-ui.com/react/components/field).
 * Renders an `<input>` element.
 *
 * Documentation: [Base UI Input](https://base-ui.com/react/components/input)
 */
external val Input: FC<InputProps>
