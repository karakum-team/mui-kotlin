// Automatically generated - do not modify!

@file:JsModule("@mui/material/TableBody")
@file:JsNonModule

package material

external interface TableBodyProps : react.PropsWithChildren {
    /**
     * The content of the component, normally `TableRow`.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableBodyClasses

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
