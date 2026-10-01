// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.Ref
import web.dom.ElementId
import web.html.HTMLInputElement

external interface CheckboxRootProps :
    NonNativeButtonProps,
    BaseUiSpanProps {
    /**
     * The id of the input element.
     */
    var id: ElementId?

    /**
     * Identifies the field when a form is submitted.
     * @default undefined
     */
    var name: String?

    /**
     * Identifies the form that owns the hidden input.
     * Useful when the checkbox is rendered outside the form.
     */
    var form: String?

    /**
     * Whether the checkbox is currently ticked.
     *
     * To render an uncontrolled checkbox, use the `defaultChecked` prop instead.
     * @default undefined
     */
    var checked: Boolean?

    /**
     * Whether the checkbox is initially ticked.
     *
     * To render a controlled checkbox, use the `checked` prop instead.
     * @default false
     */
    var defaultChecked: Boolean?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * Event handler called when the checkbox is ticked or unticked.
     */
    var onCheckedChange: ((checked: Boolean, eventDetails: CheckboxRootChangeEventDetails) -> Unit)?

    /**
     * Whether the user should be unable to tick or untick the checkbox.
     * @default false
     */
    var readOnly: Boolean?

    /**
     * Whether the user must tick the checkbox before submitting a form.
     * @default false
     */
    var required: Boolean?

    /**
     * Whether the checkbox is in a mixed state: neither ticked, nor unticked.
     * @default false
     */
    var indeterminate: Boolean?

    /**
     * A ref to access the hidden `<input>` element.
     */
    var inputRef: Ref<HTMLInputElement>?

    /**
     * Whether the checkbox controls a group of child checkboxes.
     *
     * Must be used in a [Checkbox Group](https://base-ui.com/react/components/checkbox-group).
     * @default false
     */
    var parent: Boolean?

    /**
     * The value submitted with the form when the checkbox is unchecked.
     * By default, unchecked checkboxes do not submit any value, matching native checkbox behavior.
     */
    var uncheckedValue: String?

    /**
     * The checkbox's value. Identifies it within a [Checkbox Group](https://base-ui.com/react/components/checkbox-group), falling back to `name` when omitted.
     * When submitting a form, a checked box submits `value`; with no `value`, it submits the native "on".
     */
    var value: String?
}

external interface CheckboxRootState : FieldRootState {
    /**
     * Whether the checkbox is currently ticked.
     */
    var checked: Boolean

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the user should be unable to tick or untick the checkbox.
     */
    var readOnly: Boolean

    /**
     * Whether the user must tick the checkbox before submitting a form.
     */
    var required: Boolean

    /**
     * Whether the checkbox is in a mixed state: neither ticked, nor unticked.
     */
    var indeterminate: Boolean
}

external interface CheckboxRootChangeEventDetails : BaseUIChangeEventDetails
