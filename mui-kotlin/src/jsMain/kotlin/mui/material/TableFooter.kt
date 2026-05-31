// Automatically generated - do not modify!

@file:JsModule("@mui/material/TableFooter")

package mui.material

import mui.material.styles.Theme
import mui.system.PropsWithSx
import mui.system.SxProps
import mui.types.PropsWithComponent
import react.dom.html.HTMLAttributes

external interface TableFooterProps :
    TableFooterOwnProps,
    HTMLAttributes<web.html.HTMLTableSectionElement>,
    PropsWithComponent

external interface TableFooterOwnProps :
    react.PropsWithChildren,
    PropsWithSx {
    /**
     * The content of the component, normally `TableRow`.
     */
    override var children: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableFooterClasses?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

/**
 *
 * Demos:
 *
 * - [Table](https://v6.mui.com/material-ui/react-table/)
 *
 * API:
 *
 * - [TableFooter API](https://v6.mui.com/material-ui/api/table-footer/)
 */
@JsName("default")
external val TableFooter: react.FC<TableFooterProps>
