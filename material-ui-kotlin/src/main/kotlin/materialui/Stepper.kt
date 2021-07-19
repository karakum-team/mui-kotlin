// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface StepperProps : react.RProps {
    /**
     * Set the active step (zero based index).
     * Set to -1 to disable all the steps.
     */
    var activeStep: Number

    /**
     * If set to 'true' and orientation is horizontal,
     * then the step label will be positioned under the icon.
     */
    var alternativeLabel: Boolean

    /**
     * Two or more `<Step />` components.
     */
    var children: dynamic

    /**
     * An element to be placed between each step.
     */
    var connector: dynamic

    /**
     * If set the `Stepper` will not assist in controlling steps for linear flow.
     */
    var nonLinear: Boolean

    /**
     * The stepper orientation (layout flow direction).
     */
    var orientation: dynamic
}
