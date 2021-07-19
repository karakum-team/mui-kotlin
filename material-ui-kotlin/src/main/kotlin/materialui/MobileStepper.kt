// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface MobileStepperProps : react.RProps {
    /**
     * Set the active step (zero based index).
     * Defines which dot is highlighted when the variant is 'dots'.
     */
    var activeStep: Number

    /**
     * A back button element. For instance, it can be a `Button` or an `IconButton`.
     */
    var backButton: react.ReactNode

    /**
     * Props applied to the `LinearProgress` element.
     */
    var LinearProgressProps: dynamic

    /**
     * A next button element. For instance, it can be a `Button` or an `IconButton`.
     */
    var nextButton: react.ReactNode

    /**
     * Set the positioning type.
     */
    var position: dynamic /* 'bottom' | 'top' | 'static' */

    /**
     * The total steps.
     */
    var steps: Number

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'text' | 'dots' | 'progress' */
}

/**
 *
 * Demos:
 *
 * - [Steppers](https://material-ui.com/components/steppers/)
 *
 * API:
 *
 * - [MobileStepper API](https://material-ui.com/api/mobile-stepper/)
 * - inherits [Paper API](https://material-ui.com/api/paper/)
 */
@JsName("default")
external val MobileStepper: react.FC<MobileStepperProps>
