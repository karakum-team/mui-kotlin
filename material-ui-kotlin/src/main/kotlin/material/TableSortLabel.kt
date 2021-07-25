// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableSortLabel")
@file:JsNonModule

package material

external interface TableSortLabelProps : react.RProps {
    var active: Boolean

    var direction: dynamic /* 'asc' | 'desc' */

    var hideSortIcon: Boolean

    var IconComponent: dynamic
}

/**
 * A button based label for placing inside `TableCell` for column sorting.
 * Demos:
 *
 * - [Tables](https://material-ui.com/components/tables/)
 *
 * API:
 *
 * - [TableSortLabel API](https://material-ui.com/api/table-sort-label/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val TableSortLabel: react.FC<TableSortLabelProps>
