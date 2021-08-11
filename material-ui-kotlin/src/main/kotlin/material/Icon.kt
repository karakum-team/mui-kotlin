// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Icon")
@file:JsNonModule

package material

external interface IconProps : react.PropsWithChildren {
    /**
     * The base class applied to the icon. Defaults to 'material-icons', but can be changed to any
     * other base class that suits the icon font you're using (e.g. material-icons-rounded, fas, etc).
     * @default 'material-icons'
     */
    var baseClassName: String

    /**
     * The name of the icon font ligature.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: IconClasses

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'inherit'
     */
    var color: Union /* | 'inherit' | 'action' | 'disabled' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning', IconPropsColorOverrides */

    /**
     * The fontSize applied to the icon. Defaults to 24px, but can be configure to inherit font size.
     * @default 'medium'
     */
    var fontSize: Union /* 'inherit' | 'large' | 'medium' | 'small', IconPropsSizeOverrides */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Icons](https://material-ui.com/components/icons/)
 * - [Material Icons](https://material-ui.com/components/material-icons/)
 *
 * API:
 *
 * - [Icon API](https://material-ui.com/api/icon/)
 */
@JsName("default")
external val Icon: react.FC<IconProps>
