// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface MenuCheckboxItemIndicatorProps :
    BaseUiSpanProps {
    /**
     * Whether to keep the HTML element in the DOM when the checkbox item is not checked.
     * @default false
     */
    var keepMounted: Boolean?
}

external interface MenuCheckboxItemIndicatorState {
    /**
     * Whether the checkbox item is currently ticked.
     */
    var checked: Boolean

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the item is highlighted.
     */
    var highlighted: Boolean

    /**
     * The transition status of the component.
     */
    var transitionStatus: Any? /* TransitionStatus */
}
