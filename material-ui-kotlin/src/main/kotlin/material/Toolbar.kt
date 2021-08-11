// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Toolbar")
@file:JsNonModule

package material

external interface ToolbarProps : react.Props {
    /**
     * The Toolbar children, usually a mixture of `IconButton`, `Button` and `Typography`.
     * The Toolbar is a flex container, allowing flex item properites to be used to lay out the children.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: ToolbarClasses

    /**
     * If `true`, disables gutter padding.
     * @default false
     */
    var disableGutters: Boolean

    /**
     * The variant to use.
     * @default 'regular'
     */
    var variant: Union /* 'regular' | 'dense', ToolbarPropsVariantOverrides */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [App Bar](https://material-ui.com/components/app-bar/)
 *
 * API:
 *
 * - [Toolbar API](https://material-ui.com/api/toolbar/)
 */
@JsName("default")
external val Toolbar: react.FC<ToolbarProps>
