// Automatically generated - do not modify!

@file:JsModule("@mui/material/PaginationItem")

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface PaginationItemProps :
    PaginationItemOwnProps,
    react.dom.html.HTMLAttributes<web.html.HTMLDivElement>,
    mui.types.PropsWithComponent

external interface PaginationItemPropsVariantOverrides

external interface PaginationItemPropsSizeOverrides

external interface PaginationItemPropsColorOverrides

external interface PaginationItemSlots {
    var first: react.ElementType<*>

    var last: react.ElementType<*>

    var next: react.ElementType<*>

    var previous: react.ElementType<*>
}

external interface PaginationItemOwnerState

external interface PaginationItemOwnProps : mui.system.PropsWithSx {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: PaginationItemClasses?

    /**
     * The active color.
     * It supports both default and custom theme colors, which can be added as shown in the
     * [palette customization guide](https://mui.com/material-ui/customization/palette/#custom-colors).
     * @default 'standard'
     */
    var color: PaginationItemColor?

    /**
     * The components used for each slot inside.
     *
     * This prop is an alias for the `slots` prop.
     * It's recommended to use the `slots` prop instead.
     *
     * @default {}
     * @deprecated use the `slots` prop instead. This prop will be removed in v7. See [Migrating from deprecated APIs](https://mui.com/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var components: Components?

    interface Components {
        var first: react.ElementType<*>?
        var last: react.ElementType<*>?
        var next: react.ElementType<*>?
        var previous: react.ElementType<*>?
    }

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean?

    /**
     * The current page number.
     */
    var page: react.ReactNode?

    /**
     * If `true` the pagination item is selected.
     * @default false
     */
    var selected: Boolean?

    /**
     * The shape of the pagination item.
     * @default 'circular'
     */
    var shape: PaginationItemShape?

    /**
     * The size of the component.
     * @default 'medium'
     */
    var size: Size?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?

    /**
     * The type of pagination item.
     * @default 'page'
     */
    var type: UsePaginationItemType?

    /**
     * The variant to use.
     * @default 'text'
     */
    var variant: PaginationItemVariant?
}

/**
 *
 * Demos:
 *
 * - [Pagination](https://v6.mui.com/material-ui/react-pagination/)
 *
 * API:
 *
 * - [PaginationItem API](https://v6.mui.com/material-ui/api/pagination-item/)
 */
@JsName("default")
external val PaginationItem: react.FC<PaginationItemProps>
