// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface StepIconProps : react.RProps {
    /**
     * Whether this step is active.
     */
    var active: Boolean

    /**
     * Mark the step as completed. Is passed to child components.
     */
    var completed: Boolean

    /**
     * Mark the step as failed.
     */
    var error: Boolean

    /**
     * The label displayed in the step icon.
     */
    var icon: dynamic
}

/**
 *
 * Demos:
 *
 * - [Steppers](https://material-ui.com/components/steppers/)
 *
 * API:
 *
 * - [StepIcon API](https://material-ui.com/api/step-icon/)
 */
@JsName("default")
external val StepIcon: react.FC<StepIconProps>
