// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ButtonGroup")
@file:JsNonModule

package material

external interface ButtonGroupProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'primary'
     */
    var color: dynamic

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean

    /**
     * If `true`, no elevation is used.
     * @default false
     */
    var disableElevation: Boolean

    /**
     * If `true`, the button keyboard focus ripple is disabled.
     * @default false
     */
    var disableFocusRipple: Boolean

    /**
     * If `true`, the button ripple effect is disabled.
     * @default false
     */
    var disableRipple: Boolean

    /**
     * If `true`, the buttons will take up the full width of its container.
     * @default false
     */
    var fullWidth: Boolean

    /**
     * The component orientation (layout flow direction).
     * @default 'horizontal'
     */
    var orientation: dynamic /* 'vertical' | 'horizontal' */

    /**
     * The size of the component.
     * `small` is equivalent to the dense button styling.
     * @default 'medium'
     */
    var size: dynamic /* 'small' | 'medium' | 'large' */

    /**
     * The variant to use.
     * @default 'outlined'
     */
    var variant: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic
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
