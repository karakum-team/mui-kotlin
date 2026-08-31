// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.Props
import react.ReactNode

external interface FieldValidityProps : Props {
    /**
     * A function that accepts the field validity state as an argument.
     *
     * ```jsx
     * <Field.Validity>
     *   {(validity) => {
     *     return <div>...</div>
     *   }}
     * </Field.Validity>
     * ```
     */
    var children: (state: FieldValidityState) -> ReactNode
}

external interface FieldValidityState : FieldValidityData {
    /**
     * The validity state.
     */
    var validity: Any /* FieldValidityData['state'] */

    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?
}
