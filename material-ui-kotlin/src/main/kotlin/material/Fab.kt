// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Fab")
@file:JsNonModule

package material

external interface FabProps : react.RProps {
    /**
     * The content of the button.
     */
    var children: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic

    /**
     * If `true`, the button will be disabled.
     */
    var disabled: Boolean

    /**
     * If `true`, the  keyboard focus ripple will be disabled.
     */
    var disableFocusRipple: Boolean

    /**
     * If `true`, the ripple effect will be disabled.
     */
    var disableRipple: Boolean

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
     * The variant to use.
     * 'round' is deprecated, use 'circular' instead.
     */
    var variant: dynamic /* 'circular' | 'extended' | 'round' */
}

/**
 *
 * Demos:
 *
 * - [Floating Action Button](https://material-ui.com/components/floating-action-button/)
 *
 * API:
 *
 * - [Fab API](https://material-ui.com/api/fab/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val Fab: react.FC<FabProps>
