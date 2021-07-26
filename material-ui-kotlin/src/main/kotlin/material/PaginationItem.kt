// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/PaginationItem")
@file:JsNonModule

package material

external interface PaginationItemProps : react.RProps {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The active color.
     * @default 'standard'
     */
    var color: dynamic

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean

    /**
     * The current page number.
     */
    var page: react.ReactNode

    /**
     * If `true` the pagination item is selected.
     * @default false
     */
    var selected: Boolean

    /**
     * The shape of the pagination item.
     * @default 'circular'
     */
    var shape: dynamic /* 'circular' | 'rounded' */

    /**
     * The size of the component.
     * @default 'medium'
     */
    var size: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic

    /**
     * The type of pagination item.
     * @default 'page'
     */
    var type: dynamic

    /**
     * The variant to use.
     * @default 'text'
     */
    var variant: dynamic
}

/**
 *
 * Demos:
 *
 * - [Pagination](https://material-ui.com/components/pagination/)
 *
 * API:
 *
 * - [PaginationItem API](https://material-ui.com/api/pagination-item/)
 */
@JsName("default")
external val PaginationItem: react.FC<PaginationItemProps>
