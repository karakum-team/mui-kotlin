// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface ListItemTextProps : react.RProps {
    /**
     * Alias for the `primary` prop.
     */
    var children: dynamic

    /**
     * If `true`, the children won't be wrapped by a Typography component.
     * This can be useful to render an alternative Typography variant by wrapping
     * the `children` (or `primary`) text, and optional `secondary` text
     * with the Typography component.
     */
    var disableTypography: Boolean

    /**
     * If `true`, the children will be indented.
     * This should be used if there is no left avatar or left icon.
     */
    var inset: Boolean

    /**
     * The main content element.
     */
    var primary: dynamic

    /**
     * These props will be forwarded to the primary typography component
     * (as long as disableTypography is not `true`).
     */
    var primaryTypographyProps: dynamic

    /**
     * The secondary content element.
     */
    var secondary: dynamic

    /**
     * These props will be forwarded to the secondary typography component
     * (as long as disableTypography is not `true`).
     */
    var secondaryTypographyProps: dynamic
}
