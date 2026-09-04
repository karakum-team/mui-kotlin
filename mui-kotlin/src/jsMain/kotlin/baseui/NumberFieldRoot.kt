// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.Ref
import web.dom.ElementId
import web.html.HTMLInputElement

external interface NumberFieldRootProps :
    BaseUiDivProps {
    /**
     * The id of the input element.
     */
    var id: ElementId?

    /**
     * The minimum value of the input element.
     */
    var min: Double?

    /**
     * The maximum value of the input element.
     */
    var max: Double?

    /**
     * When true, direct text entry may be outside the `min`/`max` range without clamping,
     * so native range underflow/overflow validation can occur.
     * Step-based interactions (keyboard arrows, buttons, wheel, scrub) still clamp.
     * @default false
     */
    var allowOutOfRange: Boolean?

    /**
     * The small step value of the input element when incrementing while the alt key is held.
     * Snaps to multiples of this value when `snapOnStep` is enabled.
     * @default 0.1
     */
    var smallStep: Number?

    /**
     * Amount to increment and decrement with the buttons and arrow keys, or to scrub with pointer movement in the scrub area.
     * To always enable step validation on form submission, specify the `min` prop explicitly in conjunction with this prop.
     * Specify `step="any"` to always disable step validation; interactive stepping then uses a base amount of `1`, while the alt and shift keys still step by `smallStep` and `largeStep`.
     * @default 1
     */
    var step: Any? /* number | 'any' */

    /**
     * The large step value of the input element when incrementing while the shift key is held.
     * Snaps to multiples of this value when `snapOnStep` is enabled.
     * @default 10
     */
    var largeStep: Number?

    /**
     * Whether the user must enter a value before submitting a form.
     * @default false
     */
    var required: Boolean?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * Whether the user should be unable to change the field value.
     * @default false
     */
    var readOnly: Boolean?

    /**
     * Identifies the field when a form is submitted.
     */
    var name: String?

    /**
     * Identifies the form that owns the hidden input.
     * Useful when the number field is rendered outside the form.
     */
    var form: String?

    /**
     * The raw numeric value of the field.
     */
    var value: Number?

    /**
     * The uncontrolled value of the field when it's initially rendered.
     *
     * To render a controlled number field, use the `value` prop instead.
     */
    var defaultValue: Number?

    /**
     * Whether to allow the user to scrub the input value with the mouse wheel while focused and
     * hovering over the input.
     * @default false
     */
    var allowWheelScrub: Boolean?

    /**
     * Whether the value should snap to the nearest step when incrementing or decrementing.
     * @default false
     */
    var snapOnStep: Boolean?

    /**
     * Options to format the input value.
     */
    var format: Any? /* Intl.NumberFormatOptions */

    /**
     * Callback fired when the number value changes.
     *
     * The `eventDetails.reason` indicates what triggered the change:
     * - `'input-change'` for parseable typing or programmatic text updates
     * - `'input-clear'` when the field becomes empty
     * - `'input-blur'` when formatting (and clamping, if enabled) occurs on blur
     * - `'input-paste'` for paste interactions
     * - `'keyboard'` for arrow-key/Home/End stepping (typing digits uses `'input-change'`/`'input-clear'`)
     * - `'increment-press'` / `'decrement-press'` for button presses on the increment and decrement controls
     * - `'wheel'` for wheel-based scrubbing
     * - `'scrub'` for scrub area drags
     */
    var onValueChange: ((value: Number?, eventDetails: NumberFieldRootChangeEventDetails) -> Unit)?

    /**
     * Callback function that is fired when the value is committed.
     * It runs later than `onValueChange`, when:
     * - The input is blurred after typing a value.
     * - The pointer is released after scrubbing or pressing the increment/decrement buttons.
     *
     * It runs simultaneously with `onValueChange` when interacting with the keyboard or the
     * mouse wheel.
     *
     * **Warning**: This is a generic event not a change event.
     */
    var onValueCommitted: ((value: Number?, eventDetails: NumberFieldRootCommitEventDetails) -> Unit)?

    /**
     * The locale of the input element.
     * Defaults to the user's runtime locale.
     */
    var locale: Any? /* Intl.LocalesArgument */

    /**
     * A ref to access the hidden input element.
     */
    var inputRef: Ref<HTMLInputElement>?
}

external interface NumberFieldRootState : FieldRootState {
    /**
     * The raw numeric value of the field.
     */
    var value: Number?

    /**
     * The formatted string value presented in the input element.
     */
    var inputValue: String

    /**
     * Whether the user must enter a value before submitting a form.
     */
    var required: Boolean

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the user should be unable to change the field value.
     */
    var readOnly: Boolean

    /**
     * Whether the user is currently scrubbing the field.
     */
    var scrubbing: Boolean
}

external interface NumberFieldRootChangeEventDetails :
    BaseUIChangeEventDetails,
    ChangeEventCustomProperties

external interface NumberFieldRootCommitEventDetails : BaseUIGenericEventDetails
