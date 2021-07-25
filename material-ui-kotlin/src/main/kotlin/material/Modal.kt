// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Modal")
@file:JsNonModule

package material

external interface ModalProps : react.RProps {
    var BackdropComponent: dynamic

    var BackdropProps: dynamic

    var children: dynamic

    var closeAfterTransition: Boolean

    var container: dynamic

    var disableAutoFocus: Boolean

    /**
     * If `true`, clicking the backdrop will not fire the `onClose` callback.
     * @deprecated Use the onClose prop with the `reason` argument to filter the `backdropClick` events.
     */
    var disableBackdropClick: Boolean

    var disableEnforceFocus: Boolean

    var disableEscapeKeyDown: Boolean

    var disablePortal: dynamic

    var disableRestoreFocus: Boolean

    var disableScrollLock: Boolean

    var hideBackdrop: Boolean

    var keepMounted: Boolean

    var manager: dynamic

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
     * Callback fired when the escape key is pressed,
     * `disableKeyboard` is false and the modal is in focus.
     * @deprecated Use the onClose prop with the `reason` argument to handle the `escapeKeyDown` events.
     */
    var onEscapeKeyDown: dynamic

    /**
     * Callback fired once the children has been mounted into the `container`.
     * It signals that the `open={true}` prop took effect.
     *
     * This prop will be removed in v5, the ref can be used instead.
     * @deprecated Use the ref instead.
     */
    var onRendered: dynamic

    /**
     * If `true`, the modal is open.
     */
    var open: Boolean
}

/**
 * Modal is a lower-level construct that is leveraged by the following components:
 *
 * -   [Dialog](https://material-ui.com/api/dialog/)
 * -   [Drawer](https://material-ui.com/api/drawer/)
 * -   [Menu](https://material-ui.com/api/menu/)
 * -   [Popover](https://material-ui.com/api/popover/)
 *
 * If you are creating a modal dialog, you probably want to use the [Dialog](https://material-ui.com/api/dialog/) component
 * rather than directly using Modal.
 *
 * This component shares many concepts with [react-overlays](https://react-bootstrap.github.io/react-overlays/#modals).
 * Demos:
 *
 * - [Modal](https://material-ui.com/components/modal/)
 *
 * API:
 *
 * - [Modal API](https://material-ui.com/api/modal/)
 */
@JsName("default")
external val Modal: react.ComponentType<ModalProps>
