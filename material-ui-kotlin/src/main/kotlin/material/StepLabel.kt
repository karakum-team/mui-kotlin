// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/StepLabel")
@file:JsNonModule

package material

external interface StepLabelProps : react.RProps {
    /**
     * In most cases will simply be a string containing a title for the label.
     */
    var children: react.ReactNode

    /**
     * Mark the step as disabled, will also disable the button if
     * `StepLabelButton` is a child of `StepLabel`. Is passed to child components.
     */
    var disabled: Boolean

    /**
     * Mark the step as failed.
     */
    var error: Boolean

    /**
     * Override the default label of the step icon.
     */
    var icon: react.ReactNode

    /**
     * The optional node to display.
     */
    var optional: react.ReactNode

    /**
     * The component to render in place of the [`StepIcon`](/api/step-icon/).
     */
    var StepIconComponent: dynamic

    /**
     * Props applied to the [`StepIcon`](/api/step-icon/) element.
     */
    var StepIconProps: dynamic
}

/**
 *
 * Demos:
 *
 * - [Steppers](https://material-ui.com/components/steppers/)
 *
 * API:
 *
 * - [StepLabel API](https://material-ui.com/api/step-label/)
 */
@JsName("default")
external val StepLabel: react.FC<StepLabelProps>
