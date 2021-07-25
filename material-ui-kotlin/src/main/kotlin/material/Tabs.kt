// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Tabs")
@file:JsNonModule

package material

external interface TabsProps : react.RProps {
    /**
     * Callback fired when the component mounts.
     * This is useful when you want to trigger an action programmatically.
     * It supports two actions: `updateIndicator()` and `updateScrollButtons()`
     *
     * @param {object} actions This object contains all possible actions
     * that can be triggered programmatically.
     */
    var action: dynamic

    /**
     * The label for the Tabs as a string.
     */
    // var `aria-label`: String

    /**
     * An id or list of ids separated by a space that label the Tabs.
     */
    // var `aria-labelledby`: String

    /**
     * If `true`, the tabs will be centered.
     * This property is intended for large views.
     */
    var centered: Boolean

    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Determines the color of the indicator.
     */
    var indicatorColor: dynamic /* 'secondary' | 'primary' */

    /**
     * Callback fired when the value changes.
     *
     * @param {object} event The event source of the callback
     * @param {any} value We default to the index of the child (number)
     */
    var onChange: dynamic

    /**
     * The tabs orientation (layout flow direction).
     */
    var orientation: dynamic /* 'horizontal' | 'vertical' */

    /**
     * The component used to render the scroll buttons.
     */
    var ScrollButtonComponent: dynamic

    /**
     * Determine behavior of scroll buttons when tabs are set to scroll:
     *
     * - `auto` will only present them when not all the items are visible.
     * - `desktop` will only present them on medium and larger viewports.
     * - `on` will always present them.
     * - `off` will never present them.
     */
    var scrollButtons: dynamic /* 'auto' | 'desktop' | 'on' | 'off' */

    /**
     * If `true` the selected tab changes on focus. Otherwise it only
     * changes on activation.
     */
    var selectionFollowsFocus: Boolean

    /**
     * Props applied to the tab indicator element.
     */
    var TabIndicatorProps: dynamic

    /**
     * Props applied to the [`TabScrollButton`](/api/tab-scroll-button/) element.
     */
    var TabScrollButtonProps: dynamic

    /**
     * Determines the color of the `Tab`.
     */
    var textColor: dynamic /* 'secondary' | 'primary' | 'inherit' */

    /**
     * The value of the currently selected `Tab`.
     * If you don't want any selected `Tab`, you can set this property to `false`.
     */
    var value: Any

    /**
     *  Determines additional display behavior of the tabs:
     *
     *  - `scrollable` will invoke scrolling properties and allow for horizontally
     *  scrolling (or swiping) of the tab bar.
     *  -`fullWidth` will make the tabs grow to use all the available space,
     *  which should be used for small views, like on mobile.
     *  - `standard` will render the default state.
     */
    var variant: dynamic /* 'standard' | 'scrollable' | 'fullWidth' */
}

/**
 *
 * Demos:
 *
 * - [Tabs](https://material-ui.com/components/tabs/)
 *
 * API:
 *
 * - [Tabs API](https://material-ui.com/api/tabs/)
 */
@JsName("default")
external val Tabs: react.FC<TabsProps>
