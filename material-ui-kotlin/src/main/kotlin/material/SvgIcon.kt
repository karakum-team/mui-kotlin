// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/SvgIcon")
@file:JsNonModule

package material

external interface SvgIconProps : react.RProps {
    /**
     * Node passed into the SVG element.
     */
    var children: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * You can use the `htmlColor` prop to apply a color attribute to the SVG element.
     */
    var color: dynamic /* 'inherit' | 'primary' | 'secondary' | 'action' | 'disabled' | 'error' */

    /**
     * The fontSize applied to the icon. Defaults to 24px, but can be configure to inherit font size.
     */
    var fontSize: dynamic /* 'default' | 'inherit' | 'large' | 'medium' | 'small' */

    /**
     * Applies a color attribute to the SVG element.
     */
    var htmlColor: String

    /**
     * The shape-rendering attribute. The behavior of the different options is described on the
     * [MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/shape-rendering).
     * If you are having issues with blurry icons you should investigate this prop.
     * @document
     */
    var shapeRendering: String

    /**
     * Provides a human-readable title for the element that contains it.
     * https://www.w3.org/TR/SVG-access/#Equivalent
     */
    var titleAccess: String

    /**
     * Allows you to redefine what the coordinates without units mean inside an SVG element.
     * For example, if the SVG element is 500 (width) by 200 (height), and you pass viewBox="0 0 50 20",
     * this means that the coordinates inside the SVG will go from the top left corner (0,0)
     * to bottom right (50,20) and each unit will be worth 10px.
     */
    var viewBox: String
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
 * - [SvgIcon API](https://material-ui.com/api/svg-icon/)
 */
@JsName("default")
external val SvgIcon: react.FC<SvgIconProps>
