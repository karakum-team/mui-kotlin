// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Table")
@file:JsNonModule

package material

external interface TableProps : react.Props {
    /**
     * The content of the table, normally `TableHead` and `TableBody`.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableClasses

    /**
     * Allows TableCells to inherit padding of the Table.
     * @default 'normal'
     */
    var padding: Union /* 'normal' | 'checkbox' | 'none' */

    /**
     * Allows TableCells to inherit size of the Table.
     * @default 'medium'
     */
    var size: Union /* 'small' | 'medium', TablePropsSizeOverrides */

    /**
     * Set the header sticky.
     *
     * ⚠️ It doesn't work with IE11.
     * @default false
     */
    var stickyHeader: Boolean

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
 * - [Table API](https://material-ui.com/api/table/)
 */
@JsName("default")
external val Table: react.FC<TableProps>
