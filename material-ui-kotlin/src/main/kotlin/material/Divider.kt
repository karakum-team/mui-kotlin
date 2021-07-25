// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Divider")
@file:JsNonModule

package material

external interface DividerProps : react.RProps {
    /**
     * Absolutely position the element.
     */
    var absolute: Boolean

    /**
     * If `true`, a vertical divider will have the correct height when used in flex container.
     * (By default, a vertical divider will have a calculated height of `0px` if it is the child of a flex container.)
     */
    var flexItem: Boolean

    /**
     * If `true`, the divider will have a lighter color.
     */
    var light: Boolean

    /**
     * The divider orientation.
     */
    var orientation: dynamic /* 'horizontal' | 'vertical' */

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'fullWidth' | 'inset' | 'middle' */
}

/**
 *
 * Demos:
 *
 * - [Dividers](https://material-ui.com/components/dividers/)
 * - [Lists](https://material-ui.com/components/lists/)
 *
 * API:
 *
 * - [Divider API](https://material-ui.com/api/divider/)
 */
@JsName("default")
external val Divider: react.FC<DividerProps>
