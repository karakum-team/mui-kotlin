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
     * The message to display.
     */
    var message: react.ReactNode

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
