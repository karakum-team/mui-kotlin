// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Link")
@file:JsNonModule

package material

external interface LinkProps : react.PropsWithChildren {
    /**
     * The content of the component.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: LinkClasses

    /**
     * The color of the link.
     * @default 'primary'
     */
    var color: dynamic /* TypographyProps['color'] */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * `classes` prop applied to the [`Typography`](/api/typography/) element.
     */
    var TypographyClasses: dynamic /* TypographyProps['classes'] */

    /**
     * Controls when the link should have an underline.
     * @default 'always'
     */
    var underline: Union /* 'none' | 'hover' | 'always' */

    /**
     * Applies the theme typography styles.
     * @default 'inherit'
     */
    var variant: dynamic /* TypographyProps['variant'] */
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
