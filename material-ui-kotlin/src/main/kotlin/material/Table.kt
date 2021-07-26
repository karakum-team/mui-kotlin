// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Table")
@file:JsNonModule

package material

external interface TableProps : react.RProps {
    /**
     * The content of the table, normally `TableHead` and `TableBody`.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * Allows TableCells to inherit padding of the Table.
     * @default 'normal'
     */
    var padding: dynamic /* 'normal' | 'checkbox' | 'none' */

    /**
     * Allows TableCells to inherit size of the Table.
     * @default 'medium'
     */
    var size: dynamic

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
    var sx: dynamic
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
