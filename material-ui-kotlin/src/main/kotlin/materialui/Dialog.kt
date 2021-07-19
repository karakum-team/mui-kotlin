// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface DialogProps : react.RProps {
    /**
     * The id(s) of the element(s) that describe the dialog.
     */
// var `aria-describedby`: String

    /**
     * The id(s) of the element(s) that label the dialog.
     */
// var `aria-labelledby`: String

    /**
     * Dialog children, usually the included sub-components.
     */
    var children: react.ReactNode

    /**
     * If `true`, clicking the backdrop will not fire the `onClose` callback.
     * @deprecated Use the onClose prop with the `reason` argument to filter the `backdropClick` events.
     */
    var disableBackdropClick: Boolean

    /**
     * If `true`, hitting escape will not fire the `onClose` callback.
     */
    var disableEscapeKeyDown: Boolean

    /**
     * If `true`, the dialog will be full-screen
     */
    var fullScreen: Boolean

    /**
     * If `true`, the dialog stretches to `maxWidth`.
     *
     * Notice that the dialog width grow is limited by the default margin.
     */
    var fullWidth: Boolean

    /**
     * Determine the max-width of the dialog.
     * The dialog width grows with the size of the screen.
     * Set to `false` to disable `maxWidth`.
     */
    var maxWidth: dynamic /* 'xs' | 'sm' | 'md' | 'lg' | 'xl' | false */

    /**
     * Callback fired when the backdrop is clicked.
     * @deprecated Use the onClose prop with the `reason` argument to handle the `backdropClick` events.
     */
    var onBackdropClick: dynamic

    /**
     * Callback fired when the component requests to be closed.
     *
     * @param {object} event The event source of the callback.
     * @param {string} reason Can be: `"escapeKeyDown"`, `"backdropClick"`.
     */
    var onClose: dynamic

    /**
     * Callback fired before the dialog enters.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEnter: dynamic

    /**
     * Callback fired when the dialog has entered.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEntered: dynamic

    /**
     * Callback fired when the dialog is entering.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEntering: dynamic

    /**
     * Callback fired when the escape key is pressed,
     * `disableKeyboard` is false and the modal is in focus.
     * @deprecated Use the onClose prop with the `reason` argument to handle the `escapeKeyDown` events.
     */
    var onEscapeKeyDown: dynamic

    /**
     * Callback fired before the dialog exits.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExit: dynamic

    /**
     * Callback fired when the dialog has exited.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExited: dynamic

    /**
     * Callback fired when the dialog is exiting.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExiting: dynamic

    /**
     * If `true`, the Dialog is open.
     */
    var open: dynamic

    /**
     * The component used to render the body of the dialog.
     */
    var PaperComponent: dynamic

    /**
     * Props applied to the [`Paper`](/api/paper/) element.
     */
    var PaperProps: dynamic

    /**
     * Determine the container for scrolling the dialog.
     */
    var scroll: dynamic /* 'body' | 'paper' */

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
 * Dialogs are overlaid modal paper based components with a backdrop.
 * Demos:
 *
 * - [Dialogs](https://material-ui.com/components/dialogs/)
 *
 * API:
 *
 * - [Dialog API](https://material-ui.com/api/dialog/)
 * - inherits [Modal API](https://material-ui.com/api/modal/)
 */
@JsName("default")
external val Dialog: react.FC<DialogProps>
