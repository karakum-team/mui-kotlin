// Automatically generated - do not modify!

@file:JsModule("@mui/material/TablePagination")

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface TablePaginationProps :
    TablePaginationOwnProps,
    mui.types.PropsWithComponent

external interface LabelDisplayedRowsArgs {
    var from: Number

    var to: Number

    var count: Number

    var page: Number
}

external interface TablePaginationRootSlotPropsOverrides

external interface TablePaginationToolbarSlotPropsOverrides

external interface TablePaginationSpacerSlotPropsOverrides

external interface TablePaginationSelectLabelSlotPropsOverrides

external interface TablePaginationSelectSlotPropsOverrides

external interface TablePaginationMenuItemSlotPropsOverrides

external interface TablePaginationDisplayedRowsSlotPropsOverrides

external interface TablePaginationActionsSlotPropsOverrides

external interface TablePaginationSlots {
    /**
     * The component that renders the root slot.
     * [Follow this guide](https://mui.com/material-ui/api/table-cell/#props) to learn more about the requirements for this component.
     * @default TableCell
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the toolbar slot.
     * [Follow this guide](https://mui.com/material-ui/api/toolbar/#props) to learn more about the requirements for this component.
     * @default Toolbar
     */
    var toolbar: react.ElementType<*>

    /**
     * The tag that renders the spacer slot.
     * @default 'div'
     */
    var spacer: react.ElementType<*>

    /**
     * The tag that renders the selectLabel slot.
     * @default 'p'
     */
    var selectLabel: react.ElementType<*>

    /**
     * The component that renders the select slot.
     * [Follow this guide](https://mui.com/material-ui/api/select/#props) to learn more about the requirements for this component.
     * @default Select
     */
    var select: react.ElementType<*>

    /**
     * The component that renders the select slot.
     * [Follow this guide](https://mui.com/material-ui/api/menu-item/#props) to learn more about the requirements for this component.
     * @default MenuItem
     */
    var menuItem: react.ElementType<*>

    /**
     * The tag that renders the displayedRows slot.
     * @default 'p'
     */
    var displayedRows: react.ElementType<*>

    /**
     * The slots that passed to the actions slot.
     */
    var actions: Any? /* TablePaginationActionsSlots */
}

external interface TablePaginationSlotProps : react.Props {
    /** TS: SlotProps< React.ElementType<TableCellProps>, TablePaginationRootSlotPropsOverrides, TablePaginationOwnerState > */
    var root: TableCellProps?

    /** TS: SlotProps< React.ElementType<ToolbarProps>, TablePaginationToolbarSlotPropsOverrides, TablePaginationOwnerState > */
    var toolbar: ToolbarProps?

    /** TS: SlotProps<'div', TablePaginationSpacerSlotPropsOverrides, TablePaginationOwnerState> */
    var spacer: react.dom.html.HTMLAttributes<web.html.HTMLDivElement>?

    /** TS: SlotProps< 'p', TablePaginationSelectLabelSlotPropsOverrides, TablePaginationOwnerState > */
    var selectLabel: Any?

    /** TS: Partial<SelectProps> & TablePaginationSelectSlotPropsOverrides */
    var select: Any?

    /** TS: SlotProps< React.ElementType<MenuItemProps>, TablePaginationMenuItemSlotPropsOverrides, TablePaginationOwnerState > */
    var menuItem: MenuItemProps?

    /** TS: SlotProps< 'p', TablePaginationDisplayedRowsSlotPropsOverrides, TablePaginationOwnerState > */
    var displayedRows: Any?

    /** TS: TablePaginationActionsProps['slotProps'] & TablePaginationActionsSlotPropsOverrides */
    var actions: Any?
}

external interface TablePaginationSlotsAndSlotProps : react.Props {
    var slots: TablePaginationSlots?

    var slotProps: TablePaginationSlotProps?
}

external interface TablePaginationOwnProps : mui.system.PropsWithSx {
    /**
     * The component used for displaying the actions.
     * Either a string to use a HTML element or a component.
     * @default TablePaginationActions
     */
    var ActionsComponent: react.ElementType<TablePaginationActionsProps>?

    /**
     * Props applied to the back arrow [`IconButton`](https://mui.com/material-ui/api/icon-button/) component.
     *
     * This prop is an alias for `slotProps.actions.previousButton` and will be overriden by it if both are used.
     * @deprecated Use `slotProps.actions.previousButton` instead.
     */
    var backIconButtonProps: IconButtonProps?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TablePaginationClasses?

    /**
     * The total number of rows.
     *
     * To enable server side pagination for an unknown number of items, provide -1.
     */
    var count: Number

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean?

    /**
     * Accepts a function which returns a string value that provides a user-friendly name for the current page.
     * This is important for screen reader users.
     *
     * For localization purposes, you can use the provided [translations](https://mui.com/material-ui/guides/localization/).
     * @param {string} type The link or button type to format ('first' | 'last' | 'next' | 'previous').
     * @returns {string}
     * @default function defaultGetAriaLabel(type) {
     *   return `Go to ${type} page`;
     * }
     */
    var getItemAriaLabel: ((type: mui.system.Union /* 'first' | 'last' | 'next' | 'previous' */) -> String)?

    /**
     * Customize the displayed rows label. Invoked with a `{ from, to, count, page }`
     * object.
     *
     * For localization purposes, you can use the provided [translations](https://mui.com/material-ui/guides/localization/).
     * @default function defaultLabelDisplayedRows({ from, to, count }) {
     *   return `${from}–${to} of ${count !== -1 ? count : `more than ${to}`}`;
     * }
     */
    var labelDisplayedRows: ((paginationInfo: LabelDisplayedRowsArgs) -> react.ReactNode)?

    /**
     * Customize the rows per page label.
     *
     * For localization purposes, you can use the provided [translations](https://mui.com/material-ui/guides/localization/).
     * @default 'Rows per page:'
     */
    var labelRowsPerPage: react.ReactNode?

    /**
     * Props applied to the next arrow [`IconButton`](https://mui.com/material-ui/api/icon-button/) element.
     *
     * This prop is an alias for `slotProps.actions.nextButton` and will be overriden by it if both are used.
     * @deprecated Use `slotProps.actions.nextButton` instead.
     */
    var nextIconButtonProps: IconButtonProps?

    /**
     * Callback fired when the page is changed.
     *
     * @param {React.MouseEvent<HTMLButtonElement> | null} event The event source of the callback.
     * @param {number} page The page selected.
     */
    var onPageChange: (event: react.dom.events.MouseEvent<web.html.HTMLButtonElement, *>?, page: Number) -> Unit

    /**
     * Callback fired when the number of rows per page is changed.
     *
     * @param {React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>} event The event source of the callback.
     */
    var onRowsPerPageChange: react.dom.events.ChangeEventHandler<web.html.HTMLElement, *>?

    /**
     * The zero-based index of the current page.
     */
    var page: Number

    /**
     * The number of rows per page.
     *
     * Set -1 to display all the rows.
     */
    var rowsPerPage: Number

    /**
     * Customizes the options of the rows per page select field. If less than two options are
     * available, no select field will be displayed.
     * Use -1 for the value with a custom label to show all the rows.
     * @default [10, 25, 50, 100]
     */
    var rowsPerPageOptions: Any? /* ReadonlyArray<number | { value: number; label: string }> */

    /**
     * Props applied to the rows per page [`Select`](https://mui.com/material-ui/api/select/) element.
     *
     * This prop is an alias for `slotProps.select` and will be overriden by it if both are used.
     * @deprecated Use `slotProps.select` instead.
     *
     * @default {}
     */
    var SelectProps: SelectProps<*>?

    /**
     * If `true`, show the first-page button.
     * @default false
     */
    var showFirstButton: Boolean?

    /**
     * If `true`, show the last-page button.
     * @default false
     */
    var showLastButton: Boolean?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

external interface TablePaginationOwnerState

/**
 * A `TableCell` based component for placing inside `TableFooter` for pagination.
 *
 * Demos:
 *
 * - [Pagination](https://v6.mui.com/material-ui/react-pagination/)
 * - [Table](https://v6.mui.com/material-ui/react-table/)
 *
 * API:
 *
 * - [TablePagination API](https://v6.mui.com/material-ui/api/table-pagination/)
 * - inherits [TableCell API](https://v6.mui.com/material-ui/api/table-cell/)
 */
@JsName("default")
external val TablePagination: react.FC<TablePaginationProps>
