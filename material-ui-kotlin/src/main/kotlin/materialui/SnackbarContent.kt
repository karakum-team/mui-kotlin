// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface SnackbarContentProps : react.RProps {
    /**
     * The action to display. It renders after the message, at the end of the snackbar.
     */
    var action: dynamic

    /**
     * The message to display.
     */
    var message: dynamic

    /**
     * The ARIA role attribute of the element.
     */
    var role: dynamic
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
