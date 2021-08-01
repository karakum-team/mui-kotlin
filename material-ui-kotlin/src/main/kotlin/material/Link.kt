// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Link")
@file:JsNonModule

package material

external interface LinkProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the link.
     * @default 'primary'
     */
    var color: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * `classes` prop applied to the [`Typography`](/api/typography/) element.
     */
    var TypographyClasses: dynamic

    /**
     * Controls when the link should have an underline.
     * @default 'always'
     */
    var underline: dynamic /* 'none' | 'hover' | 'always' */

    /**
     * Applies the theme typography styles.
     * @default 'inherit'
     */
    var variant: dynamic
}

/**
 *
 * Demos:
 *
 * - [Breadcrumbs](https://material-ui.com/components/breadcrumbs/)
 * - [Links](https://material-ui.com/components/links/)
 *
 * API:
 *
 * - [Link API](https://material-ui.com/api/link/)
 * - inherits [Typography API](https://material-ui.com/api/typography/)
 */
@JsName("default")
external val Link: react.FC<LinkProps>
