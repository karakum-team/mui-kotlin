// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/checkbox-group")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import js.array.ReadonlyArray
import react.FC

external interface CheckboxGroupProps :
    BaseUiDivProps {
    /**
     * Names of the checkboxes in the group that should be ticked.
     *
     * To render an uncontrolled checkbox group, use the `defaultValue` prop instead.
     */
    var value: ReadonlyArray<String>?

    /**
     * Names of the checkboxes in the group that should be initially ticked.
     *
     * To render a controlled checkbox group, use the `value` prop instead.
     */
    var defaultValue: ReadonlyArray<String>?

    /**
     * Event handler called when a checkbox in the group is ticked or unticked.
     * Provides the new value as an argument.
     */
    var onValueChange: ((value: ReadonlyArray<String>, eventDetails: CheckboxGroupChangeEventDetails) -> Unit)?

    /**
     * Names of all checkboxes in the group. Use this when creating a parent checkbox.
     */
    var allValues: ReadonlyArray<String>?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?
}

external interface CheckboxGroupState : FieldRootState {
    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean
}

external interface CheckboxGroupChangeEventDetails : BaseUIChangeEventDetails

/**
 * Provides a shared state to a series of checkboxes.
 *
 * Documentation: [Base UI Checkbox Group](https://base-ui.com/react/components/checkbox-group)
 */
external val CheckboxGroup: FC<CheckboxGroupProps>
