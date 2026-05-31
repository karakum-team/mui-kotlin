// Automatically generated - do not modify!

@file:JsModule("@mui/material/TableSortLabel")

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface TableSortLabelProps :
    TableSortLabelOwnProps,
    react.dom.html.HTMLAttributes<web.html.HTMLSpanElement>,
    mui.types.PropsWithComponent

external interface TableSortLabelRootSlotPropsOverrides

external interface TableSortLabelIconSlotPropsOverrides

external interface TableSortLabelSlots {
    /**
     * The component that renders the root slot.
     * @default span
     */
    var root: react.ElementType<*>?

    /**
     * The component that renders the icon slot.
     * @default ArrowDownwardIcon
     */
    var icon: react.ElementType<*>?
}

external interface TableSortLabelOwnerState

external interface TableSortLabelOwnProps :
    react.PropsWithChildren,
    mui.system.PropsWithSx {
    /**
     * If `true`, the label will have the active styling (should be true for the sorted column).
     * @default false
     */
    var active: Boolean?

    /**
     * Label contents, the arrow will be appended automatically.
     */
    override var children: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableSortLabelClasses?

    /**
     * The current sort direction.
     * @default 'asc'
     */
    var direction: TableSortLabelDirection?

    /**
     * Hide sort icon when active is false.
     * @default false
     */
    var hideSortIcon: Boolean?

    /**
     * Sort icon to use.
     * @default ArrowDownwardIcon
     */
    var IconComponent: react.ComponentType<*>?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

/**
 * A button based label for placing inside `TableCell` for column sorting.
 *
 * Demos:
 *
 * - [Table](https://v6.mui.com/material-ui/react-table/)
 *
 * API:
 *
 * - [TableSortLabel API](https://v6.mui.com/material-ui/api/table-sort-label/)
 * - inherits [ButtonBase API](https://v6.mui.com/material-ui/api/button-base/)
 */
@JsName("default")
external val TableSortLabel: react.FC<TableSortLabelProps>
