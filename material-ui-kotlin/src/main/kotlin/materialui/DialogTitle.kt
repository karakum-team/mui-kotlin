// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/DialogTitle")
@file:JsNonModule

package materialui

external interface DialogTitleProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * If `true`, the children won't be wrapped by a typography component.
     * For instance, this can be useful to render an h4 instead of the default h2.
     */
    var disableTypography: Boolean
}

/**
 *
 * Demos:
 *
 * - [Dialogs](https://material-ui.com/components/dialogs/)
 *
 * API:
 *
 * - [DialogTitle API](https://material-ui.com/api/dialog-title/)
 */
@JsName("default")
external val DialogTitle: react.FC<DialogTitleProps>
