// Automatically generated - do not modify!

@file:JsModule("@mui/material/TableRow")

package mui.material

import mui.material.styles.Theme
import mui.system.PropsWithSx
import mui.system.SxProps
import mui.types.PropsWithComponent
import react.dom.html.HTMLAttributes

external interface TableRowProps :
    TableRowOwnProps,
    HTMLAttributes<web.html.HTMLTableRowElement>,
    PropsWithComponent

external interface TableRowOwnProps :
    react.PropsWithChildren,
    PropsWithSx {
    /**
     * Should be valid `<tr>` children such as `TableCell`.
     */
    override var children: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableRowClasses?

    /**
     * If `true`, the table row will shade on hover.
     * @default false
     */
    var hover: Boolean?

    /**
     * If `true`, the table row will have the selected shading.
     * @default false
     */
    var selected: Boolean?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

/**
 * Will automatically set dynamic row height
 * based on the material table element parent (head, body, etc).
 *
 * Demos:
 *
 * - [Table](https://v6.mui.com/material-ui/react-table/)
 *
 * API:
 *
 * - [TableRow API](https://v6.mui.com/material-ui/api/table-row/)
 */
@JsName("default")
external val TableRow: react.FC<TableRowProps>
