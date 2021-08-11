// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/DialogContent")
@file:JsNonModule

package material

external interface DialogContentProps : react.PropsWithChildren {
    /**
     * The content of the component.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: DialogContentClasses

    /**
     * Display the top and bottom dividers.
     * @default false
     */
    var dividers: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Dialogs](https://material-ui.com/components/dialogs/)
 *
 * API:
 *
 * - [DialogContent API](https://material-ui.com/api/dialog-content/)
 */
@JsName("default")
external val DialogContent: react.FC<DialogContentProps>
