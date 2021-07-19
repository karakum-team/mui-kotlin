// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface StepProps : react.RProps {
    /**
     * Sets the step as active. Is passed to child components.
     */
    var active: Boolean

    /**
     * Should be `Step` sub-components such as `StepLabel`, `StepContent`.
     */
    var children: dynamic

    /**
     * Mark the step as completed. Is passed to child components.
     */
    var completed: Boolean

    /**
     * Mark the step as disabled, will also disable the button if
     * `StepButton` is a child of `Step`. Is passed to child components.
     */
    var disabled: Boolean

    /**
     * Expand the step.
     */
    var expanded: Boolean
}

/**
 *
 * Demos:
 *
 * - [Steppers](https://material-ui.com/components/steppers/)
 *
 * API:
 *
 * - [Step API](https://material-ui.com/api/step/)
 */
@JsName("default")
external val Step: react.FC<StepProps>
