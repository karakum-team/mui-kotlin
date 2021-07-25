// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/IconButton")
@file:JsNonModule

package material

external interface IconButtonProps : react.RProps {
    var color: dynamic

    var disableFocusRipple: Boolean

    /**
     * If given, uses a negative margin to counteract the padding on one
     * side (this is often helpful for aligning the left or right
     * side of the icon with content above or below, without ruining the border
     * size and shape).
     */
    var edge: dynamic /* 'start' | 'end' | false */

    var size: dynamic /* 'small' | 'medium' */
}

/**
 * Refer to the [Icons](https://material-ui.com/components/icons/) section of the documentation
 * regarding the available icon options.
 * Demos:
 *
 * - [Buttons](https://material-ui.com/components/buttons/)
 *
 * API:
 *
 * - [IconButton API](https://material-ui.com/api/icon-button/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val IconButton: react.FC<IconButtonProps>
