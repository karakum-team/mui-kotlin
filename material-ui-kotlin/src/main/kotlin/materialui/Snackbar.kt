// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface SnackbarProps : react.RProps {
    /**
     * The action to display. It renders after the message, at the end of the snackbar.
     */
    var action: dynamic

    /**
     * The anchor of the `Snackbar`.
     */
    var anchorOrigin: dynamic

    /**
     * The number of milliseconds to wait before automatically calling the
     * `onClose` function. `onClose` should then set the state of the `open`
     * prop to hide the Snackbar. This behavior is disabled by default with
     * the `null` value.
     */
    var autoHideDuration: dynamic

    /**
     * Replace the `SnackbarContent` component.
     */
    var children: dynamic

    /**
     * Props applied to the `ClickAwayListener` element.
     */
    var ClickAwayListenerProps: dynamic

    /**
     * Props applied to the [`SnackbarContent`](/api/snackbar-content/) element.
     */
    var ContentProps: dynamic

    /**
     * If `true`, the `autoHideDuration` timer will expire even if the window is not focused.
     */
    var disableWindowBlurListener: Boolean

    /**
     * When displaying multiple consecutive Snackbars from a parent rendering a single
     * <Snackbar/>, add the key prop to ensure independent treatment of each message.
     * e.g. <Snackbar key={message} />, otherwise, the message may update-in-place and
     * features such as autoHideDuration may be canceled.
     * @document
     */
    var key: Any

    /**
     * The message to display.
     */
    var message: dynamic

    /**
     * Callback fired when the component requests to be closed.
     * Typically `onClose` is used to set state in the parent component,
     * which is used to control the `Snackbar` `open` prop.
     * The `reason` parameter can optionally be used to control the response to `onClose`,
     * for example ignoring `clickaway`.
     *
     * @param {object} event The event source of the callback.
     * @param {string} reason Can be: `"timeout"` (`autoHideDuration` expired), `"clickaway"`.
     */
    var onClose: dynamic

    /**
     * Callback fired before the transition is entering.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEnter: dynamic

    /**
     * Callback fired when the transition has entered.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEntered: dynamic

    /**
     * Callback fired when the transition is entering.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEntering: dynamic

    /**
     * Callback fired before the transition is exiting.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExit: dynamic

    /**
     * Callback fired when the transition has exited.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExited: dynamic

    /**
     * Callback fired when the transition is exiting.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExiting: dynamic

    var onMouseEnter: dynamic

    var onMouseLeave: dynamic

    /**
     * If `true`, `Snackbar` is open.
     */
    var open: Boolean

    /**
     * The number of milliseconds to wait before dismissing after user interaction.
     * If `autoHideDuration` prop isn't specified, it does nothing.
     * If `autoHideDuration` prop is specified but `resumeHideDuration` isn't,
     * we default to `autoHideDuration / 2` ms.
     */
    var resumeHideDuration: Number

    /**
     * The component used for the transition.
     * [Follow this guide](/components/transitions/#transitioncomponent-prop) to learn more about the requirements for this component.
     */
    var TransitionComponent: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     */
    var transitionDuration: dynamic

    /**
     * Props applied to the [`Transition`](http://reactcommunity.org/react-transition-group/transition#Transition-props) element.
     */
    var TransitionProps: dynamic
}

/**
 *
 * Demos:
 *
 * - [Snackbars](https://material-ui.com/components/snackbars/)
 *
 * API:
 *
 * - [Snackbar API](https://material-ui.com/api/snackbar/)
 */
@JsName("default")
external val Snackbar: react.FC<SnackbarProps>
