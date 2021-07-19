// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface ClickAwayListenerProps : react.RProps {
    /**
     * The wrapped element.
     */
    var children: dynamic

    /**
     * If `true`, the React tree is ignored and only the DOM tree is considered.
     * This prop changes how portaled elements are handled.
     */
    var disableReactTree: Boolean

    /**
     * The mouse event to listen to. You can disable the listener by providing `false`.
     */
    var mouseEvent: dynamic /* 'onClick' | 'onMouseDown' | 'onMouseUp' | false */

    /**
     * Callback fired when a "click away" event is detected.
     */
    var onClickAway: dynamic

    /**
     * The touch event to listen to. You can disable the listener by providing `false`.
     */
    var touchEvent: dynamic /* 'onTouchStart' | 'onTouchEnd' | false */
}

/**
 * Listen for click events that occur somewhere in the document, outside of the element itself.
 * For instance, if you need to hide a menu when people click anywhere else on your page.
 * Demos:
 *
 * - [Click Away Listener](https://material-ui.com/components/click-away-listener/)
 * - [Menus](https://material-ui.com/components/menus/)
 *
 * API:
 *
 * - [ClickAwayListener API](https://material-ui.com/api/click-away-listener/)
 */
@JsName("default")
external val ClickAwayListener: react.FC<ClickAwayListenerProps>
