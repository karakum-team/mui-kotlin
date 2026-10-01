// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface CheckboxIndicatorProps :
    BaseUiSpanProps {
    /**
     * Whether to keep the element in the DOM when the checkbox is not checked.
     * @default false
     */
    var keepMounted: Boolean?
}

external interface CheckboxIndicatorState : CheckboxRootState {
    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?
}
