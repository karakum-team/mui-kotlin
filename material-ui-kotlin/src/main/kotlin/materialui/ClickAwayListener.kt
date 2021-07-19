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
    var mouseEvent: dynamic

    /**
     * Callback fired when a "click away" event is detected.
     */
    var onClickAway: dynamic

    /**
     * The touch event to listen to. You can disable the listener by providing `false`.
     */
    var touchEvent: dynamic
}
