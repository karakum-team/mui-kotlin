// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface FieldErrorProps :
    BaseUiDivProps {
    /**
     * Determines whether to show the error message according to the field's
     * [ValidityState](https://developer.mozilla.org/en-US/docs/Web/API/ValidityState).
     * Specifying `true` will always show the error message, and lets external libraries
     * control the visibility.
     */
    var match: Any? /* boolean | keyof ValidityState */
}

external interface FieldErrorState : FieldRootState {
    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?
}
