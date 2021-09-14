// Automatically generated - do not modify!

@file:JsModule("@mui/material/Modal")
@file:JsNonModule

package mui.material

external interface ModalProps : react.Props {
    /**
     * A backdrop component. This prop enables custom backdrop rendering.
     * @default styled(Backdrop, {
     *   name: 'MuiModal',
     *   slot: 'Backdrop',
     *   overridesResolver: (props, styles) => {
     *     return styles.backdrop;
     *   },
     * })({
     *   zIndex: -1,
     * })
     */
    var BackdropComponent: react.ElementType<BackdropProps>

    /**
     * Props applied to the [`Backdrop`](/api/backdrop/) element.
     */
    var BackdropProps: BackdropProps

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 * Modal is a lower-level construct that is leveraged by the following components:
 *
 * *   [Dialog](https://material-ui.com/api/dialog/)
 * *   [Drawer](https://material-ui.com/api/drawer/)
 * *   [Menu](https://material-ui.com/api/menu/)
 * *   [Popover](https://material-ui.com/api/popover/)
 *
 * If you are creating a modal dialog, you probably want to use the [Dialog](https://material-ui.com/api/dialog/) component
 * rather than directly using Modal.
 *
 * This component shares many concepts with [react-overlays](https://react-bootstrap.github.io/react-overlays/#modals).
 *
 * Demos:
 *
 * - [Modal](https://material-ui.com/components/modal/)
 *
 * API:
 *
 * - [Modal API](https://material-ui.com/api/modal/)
 */
@JsName("default")
external val Modal: react.FC<ModalProps>
