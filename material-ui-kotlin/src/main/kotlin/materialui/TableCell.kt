// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface TableCellProps : react.RProps {
    /**
     * Set the text-align on the table cell content.
     *
     * Monetary or generally number fields **should be right aligned** as that allows
     * you to add them up quickly in your head without having to worry about decimals.
     */
    var align: dynamic /* 'inherit' | 'left' | 'center' | 'right' | 'justify' */

    /**
     * The table cell contents.
     */
    var children: dynamic

    /**
     * The component used for the root node.
     * Either a string to use a HTML element or a component.
     */
    var component: dynamic

    /**
     * Sets the padding applied to the cell.
     * By default, the Table parent component set the value (`normal`).
     * `default` is deprecated, use `normal` instead.
     */
    var padding: dynamic

    /**
     * Set scope attribute.
     */
    var scope: dynamic

    /**
     * Specify the size of the cell.
     * By default, the Table parent component set the value (`medium`).
     */
    var size: dynamic

    /**
     * Set aria-sort direction.
     */
    var sortDirection: dynamic

    /**
     * Specify the cell type.
     * By default, the TableHead, TableBody or TableFooter parent component set the value.
     */
    var variant: dynamic /* 'head' | 'body' | 'footer' */
}

/**
 * The component renders a `<th>` element when the parent context is a header
 * or otherwise a `<td>` element.
 * Demos:
 *
 * - [Tables](https://material-ui.com/components/tables/)
 *
 * API:
 *
 * - [TableCell API](https://material-ui.com/api/table-cell/)
 */
@JsName("default")
external val TableCell: react.FC<TableCellProps>
