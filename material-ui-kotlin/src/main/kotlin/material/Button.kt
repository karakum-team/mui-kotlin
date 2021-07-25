// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Button")
@file:JsNonModule

package material

external interface ButtonProps : react.RProps {
    /**
     * The content of the button.
     */
    var children: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic

    /**
     * If `true`, the button will be disabled.
     */
    var disabled: Boolean

    /**
     * If `true`, no elevation is used.
     */
    var disableElevation: Boolean

    /**
     * If `true`, the  keyboard focus ripple will be disabled.
     */
    var disableFocusRipple: Boolean

    /**
     * Element placed after the children.
     */
    var endIcon: react.ReactNode

    /**
     * If `true`, the button will take up the full width of its container.
     */
    var fullWidth: Boolean

    /**
     * The URL to link to when the button is clicked.
     * If defined, an `a` element will be used as the root node.
     */
    var href: String

    /**
     * The size of the button.
     * `small` is equivalent to the dense button styling.
     */
    var size: dynamic /* 'small' | 'medium' | 'large' */

    /**
     * Element placed before the children.
     */
    var startIcon: react.ReactNode

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
 * - [Buttons](https://material-ui.com/components/buttons/)
 *
 * API:
 *
 * - [Button API](https://material-ui.com/api/button/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val Button: react.FC<ButtonProps>
