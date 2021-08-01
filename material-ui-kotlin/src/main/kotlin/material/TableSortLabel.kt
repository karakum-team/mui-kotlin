// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableSortLabel")
@file:JsNonModule

package material

external interface TableSortLabelProps : react.RProps {
    /**
     * If `true`, the label will have the active styling (should be true for the sorted column).
     * @default false
     */
    var active: Boolean

    /**
     * Label contents, the arrow will be appended automatically.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The current sort direction.
     * @default 'asc'
     */
    var direction: dynamic /* 'asc' | 'desc' */

    /**
     * Hide sort icon when active is false.
     * @default false
     */
    var hideSortIcon: Boolean

    /**
     * Sort icon to use.
     * @default ArrowDownwardIcon
     */
    var IconComponent: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 * A button based label for placing inside `TableCell` for column sorting.
 *
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
