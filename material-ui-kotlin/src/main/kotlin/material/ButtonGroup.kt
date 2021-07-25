// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ButtonGroup")
@file:JsNonModule

package material

external interface ButtonGroupProps : react.RProps {
    /**
     * The content of the button group.
     */
    var children: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic

    /**
     * If `true`, the buttons will be disabled.
     */
    var disabled: Boolean

    /**
     * If `true`, no elevation is used.
     */
    var disableElevation: Boolean

    /**
     * If `true`, the button keyboard focus ripple will be disabled.
     */
    var disableFocusRipple: Boolean

    /**
     * If `true`, the button ripple effect will be disabled.
     */
    var disableRipple: Boolean

    /**
     * If `true`, the buttons will take up the full width of its container.
     */
    var fullWidth: Boolean

    /**
     * The group orientation (layout flow direction).
     */
    var orientation: dynamic /* 'vertical' | 'horizontal' */

    /**
     * The size of the button.
     * `small` is equivalent to the dense button styling.
     */
    var size: dynamic /* 'small' | 'medium' | 'large' */

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'text' | 'outlined' | 'contained' */
}

/**
 *
 * Demos:
 *
 * - [Button Group](https://material-ui.com/components/button-group/)
 *
 * API:
 *
 * - [ButtonGroup API](https://material-ui.com/api/button-group/)
 */
@JsName("default")
external val ButtonGroup: react.FC<ButtonGroupProps>
