// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Divider")
@file:JsNonModule

package material

external interface DividerProps : react.RProps {
    /**
     * Absolutely position the element.
     * @default false
     */
    var absolute: Boolean

    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * If `true`, a vertical divider will have the correct height when used in flex container.
     * (By default, a vertical divider will have a calculated height of `0px` if it is the child of a flex container.)
     * @default false
     */
    var flexItem: Boolean

    /**
     * If `true`, the divider will have a lighter color.
     * @default false
     */
    var light: Boolean

    /**
     * The component orientation.
     * @default 'horizontal'
     */
    var orientation: dynamic /* 'horizontal' | 'vertical' */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The text alignment.
     * @default 'center'
     */
    var textAlign: dynamic /* 'center' | 'right' | 'left' */

    /**
     * The variant to use.
     * @default 'fullWidth'
     */
    var variant: dynamic
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
