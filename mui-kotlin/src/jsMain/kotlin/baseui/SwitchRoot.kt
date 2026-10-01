// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.Ref
import web.dom.ElementId
import web.html.HTMLInputElement

external interface SwitchRootProps :
    NonNativeButtonProps,
    BaseUiSpanProps {
    /**
     * The id of the hidden input element.
     *
     * When `nativeButton` is `true`, the id is applied to the root element.
     */
    var id: ElementId?

    /**
     * Whether the switch is currently active.
     *
     * To render an uncontrolled switch, use the `defaultChecked` prop instead.
     */
    var checked: Boolean?

    /**
     * Whether the switch is initially active.
     *
     * To render a controlled switch, use the `checked` prop instead.
     * @default false
     */
    var defaultChecked: Boolean?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * A ref to access the hidden `<input>` element.
     */
    var inputRef: Ref<HTMLInputElement>?

    /**
     * Identifies the field when a form is submitted.
     */
    var name: String?

    /**
     * Identifies the form that owns the hidden input.
     * Useful when the switch is rendered outside the form.
     */
    var form: String?

    /**
     * Event handler called when the switch is activated or deactivated.
     */
    var onCheckedChange: ((checked: Boolean, eventDetails: SwitchRootChangeEventDetails) -> Unit)?

    /**
     * Whether the user should be unable to activate or deactivate the switch.
     * @default false
     */
    var readOnly: Boolean?

    /**
     * Whether the user must activate the switch before submitting a form.
     * @default false
     */
    var required: Boolean?

    /**
     * The value submitted with the form when the switch is on.
     * By default, switch submits the "on" value, matching native checkbox behavior.
     */
    var value: String?

    /**
     * The value submitted with the form when the switch is off.
     * By default, unchecked switches do not submit any value, matching native checkbox behavior.
     */
    var uncheckedValue: String?
}

external interface SwitchRootState : FieldRootState {
    /**
     * Whether the switch is currently active.
     */
    var checked: Boolean

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the user should be unable to activate or deactivate the switch.
     */
    var readOnly: Boolean

    /**
     * Whether the user must activate the switch before submitting a form.
     */
    var required: Boolean
}

external interface SwitchRootChangeEventDetails : BaseUIChangeEventDetails
