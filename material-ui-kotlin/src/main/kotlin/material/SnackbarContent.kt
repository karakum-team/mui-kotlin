// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/SnackbarContent")
@file:JsNonModule

package material

external interface SnackbarContentProps : react.RProps {
    /**
     * The action to display. It renders after the message, at the end of the snackbar.
     */
    var action: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The message to display.
     */
    var message: react.ReactNode

    /**
     * The ARIA role attribute of the element.
     * @default 'alert'
     */
    var role: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic
}

/**
 *
 * Demos:
 *
 * - [Snackbars](https://material-ui.com/components/snackbars/)
 *
 * API:
 *
 * - [SnackbarContent API](https://material-ui.com/api/snackbar-content/)
 * - inherits [Paper API](https://material-ui.com/api/paper/)
 */
@JsName("default")
external val SnackbarContent: react.FC<SnackbarContentProps>
