// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ListItemText")
@file:JsNonModule

package material

external interface ListItemTextProps : react.RProps {
    /**
     * Alias for the `primary` prop.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * If `true`, the children won't be wrapped by a Typography component.
     * This can be useful to render an alternative Typography variant by wrapping
     * the `children` (or `primary`) text, and optional `secondary` text
     * with the Typography component.
     * @default false
     */
    var disableTypography: Boolean

    /**
     * If `true`, the children are indented.
     * This should be used if there is no left avatar or left icon.
     * @default false
     */
    var inset: Boolean

    /**
     * The main content element.
     */
    var primary: react.ReactNode

    /**
     * These props will be forwarded to the primary typography component
     * (as long as disableTypography is not `true`).
     */
    var primaryTypographyProps: dynamic

    /**
     * The secondary content element.
     */
    var secondary: react.ReactNode

    /**
     * These props will be forwarded to the secondary typography component
     * (as long as disableTypography is not `true`).
     */
    var secondaryTypographyProps: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}
