// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.PropsWithChildren
import react.ReactNode

external interface MenuRadioGroupProps :
    BaseUiDivProps,
    PropsWithChildren {
    /**
     * The content of the component.
     */
    override var children: ReactNode?

    /**
     * The controlled value of the radio item that should be currently selected.
     *
     * To render an uncontrolled radio group, use the `defaultValue` prop instead.
     */
    var value: Any?

    /**
     * The uncontrolled value of the radio item that should be initially selected.
     *
     * To render a controlled radio group, use the `value` prop instead.
     */
    var defaultValue: Any?

    /**
     * Function called when the selected value changes.
     */
    var onValueChange: ((value: Any, eventDetails: MenuRadioGroupChangeEventDetails) -> Unit)?

    /**
     * Whether the component should ignore user interaction.
     *
     * @default false
     */
    var disabled: Boolean?
}

external interface MenuRadioGroupState {
    /**
     * Whether the component is disabled.
     */
    var disabled: Boolean
}

external interface MenuRadioGroupChangeEventDetails : MenuRootChangeEventDetails
