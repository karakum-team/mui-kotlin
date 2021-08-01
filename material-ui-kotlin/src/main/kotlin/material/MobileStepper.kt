// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/MobileStepper")
@file:JsNonModule

package material

external interface MobileStepperProps : react.RProps {
    /**
     * Set the active step (zero based index).
     * Defines which dot is highlighted when the variant is 'dots'.
     * @default 0
     */
    var activeStep: Number

    /**
     * A back button element. For instance, it can be a `Button` or an `IconButton`.
     */
    var backButton: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: MobileStepperClasses

    /**
     * Props applied to the `LinearProgress` element.
     */
    var LinearProgressProps: LinearProgressProps

    /**
     * A next button element. For instance, it can be a `Button` or an `IconButton`.
     */
    var nextButton: react.ReactNode

    /**
     * Set the positioning type.
     * @default 'bottom'
     */
    var position: Union /* 'bottom' | 'top' | 'static' */

    /**
     * The total steps.
     */
    var steps: Number

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The variant to use.
     * @default 'dots'
     */
    var variant: Union /* 'text' | 'dots' | 'progress' */
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
