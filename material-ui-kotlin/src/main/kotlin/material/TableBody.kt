// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableBody")
@file:JsNonModule

package material

external interface TableBodyProps : react.RProps {
    /**
     * The content of the component, normally `TableRow`.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Tables](https://material-ui.com/components/tables/)
 *
 * API:
 *
 * - [TableBody API](https://material-ui.com/api/table-body/)
 */
@JsName("default")
external val TableBody: react.FC<TableBodyProps>
