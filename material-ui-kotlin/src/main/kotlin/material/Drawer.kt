// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Drawer")
@file:JsNonModule

package material

external interface DrawerProps : react.RProps {
    /**
     * Side from which the drawer will appear.
     */
    var anchor: dynamic /* 'left' | 'top' | 'right' | 'bottom' */

    /**
     * The contents of the drawer.
     */
    var children: react.ReactNode

    /**
     * The elevation of the drawer.
     */
    var elevation: Number

    /**
     * Props applied to the [`Modal`](/api/modal/) element.
     */
    var ModalProps: dynamic

    /**
     * Callback fired when the component requests to be closed.
     *
     * @param {object} event The event source of the callback.
     */
    var onClose: dynamic

    /**
     * If `true`, the drawer is open.
     */
    var open: Boolean

    /**
     * Props applied to the [`Paper`](/api/paper/) element.
     */
    var PaperProps: dynamic

    /**
     * Props applied to the [`Slide`](/api/slide/) element.
     */
    var SlideProps: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     */
    var transitionDuration: dynamic

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'permanent' | 'persistent' | 'temporary' */
}

/**
 * The props of the [Modal](https://material-ui.com/api/modal/) component are available
 * when `variant="temporary"` is set.
 * Demos:
 *
 * - [Drawers](https://material-ui.com/components/drawers/)
 *
 * API:
 *
 * - [Drawer API](https://material-ui.com/api/drawer/)
 */
@JsName("default")
external val Drawer: react.FC<DrawerProps>
