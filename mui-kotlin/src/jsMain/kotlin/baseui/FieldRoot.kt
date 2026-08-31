// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import js.array.ReadonlyArray

external interface FieldRootProps :
    BaseUiDivProps {
    /**
     * Whether the component should ignore user interaction.
     * Takes precedence over the `disabled` prop on the `<Field.Control>` component.
     * @default false
     */
    var disabled: Boolean?

    /**
     * Identifies the field when a form is submitted.
     * Takes precedence over the `name` prop on the `<Field.Control>` component.
     */
    var name: String?

    /**
     * A function for custom validation. Return a string or an array of strings with
     * the error message(s) if the value is invalid, or `null` if the value is valid.
     * Asynchronous functions are supported, but they do not prevent form submission
     * when using `validationMode="onSubmit"`.
     */
    var validate: Any? /* (value: unknown, formValues: Form.Values) => string | string[] | null | Promise<string | string[] | null> */

    /**
     * Determines when the field should be validated.
     * This takes precedence over the `validationMode` prop on `<Form>`.
     *
     * - `onSubmit`: triggers validation when the form is submitted, and re-validates on change after submission.
     * - `onBlur`: triggers validation when the control loses focus.
     * - `onChange`: triggers validation on every change to the control value.
     *
     * @default 'onSubmit'
     */
    var validationMode: Any? /* Form.ValidationMode */

    /**
     * How long to wait between `validate` callbacks if
     * `validationMode="onChange"` is used. Specified in milliseconds.
     * @default 0
     */
    var validationDebounceTime: Number?

    /**
     * Whether the field is invalid.
     * Useful when the field state is controlled by an external library.
     */
    var invalid: Boolean?

    /**
     * Whether the field's value has been changed from its initial value.
     * Useful when the field state is controlled by an external library.
     */
    var dirty: Boolean?

    /**
     * Whether the field has been touched.
     * Useful when the field state is controlled by an external library.
     */
    var touched: Boolean?

    /**
     * A ref to imperative actions.
     * - `validate`: Validates the field when called.
     */
    var actionsRef: Any? /* React.RefObject<FieldRootActions | null> */
}

external interface FieldValidityData {
    var state: Any? /* { badInput: boolean; customError: boolean; patternMismatch: boolean; rangeOverflow: boolean; rangeUnderflow: boolean; stepMismatch: boolean; tooLong: boolean; tooShort: boolean; typeMismatch: boolean; valueMissing: boolean; valid: boolean | null; } */

    var error: String

    var errors: ReadonlyArray<String>

    var value: Any

    var initialValue: Any? /* unknown */
}

external interface FieldRootActions {
    fun validate()
}

external interface FieldRootState {
    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the field has been touched.
     */
    var touched: Boolean

    /**
     * Whether the field value has changed from its initial value.
     */
    var dirty: Boolean

    /**
     * Whether the field is valid.
     */
    var valid: Boolean?

    /**
     * Whether the field has a value.
     */
    var filled: Boolean

    /**
     * Whether the field is focused.
     */
    var focused: Boolean
}
