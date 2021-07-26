// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Pagination")
@file:JsNonModule

package material

external interface PaginationProps : react.RProps {
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
     * Accepts a function which returns a string value that provides a user-friendly name for the current page.
     * This is important for screen reader users.
     *
     * For localization purposes, you can use the provided [translations](/guides/localization/).
     * @param {string} type The link or button type to format ('page' | 'first' | 'last' | 'next' | 'previous'). Defaults to 'page'.
     * @param {number} page The page number to format.
     * @param {bool} selected If true, the current page is selected.
     * @returns {string}
     */
    var getItemAriaLabel: dynamic

    /**
     * Render the item.
     * @param {PaginationRenderItemParams} params The props to spread on a PaginationItem.
     * @returns {ReactNode}
     * @default (item) => <PaginationItem {...item} />
     */
    var renderItem: dynamic

    /**
     * The shape of the pagination items.
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
 * - [Pagination API](https://material-ui.com/api/pagination/)
 */
@JsName("default")
external val Pagination: react.FC<PaginationProps>
