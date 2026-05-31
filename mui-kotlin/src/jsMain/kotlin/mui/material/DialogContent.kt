// Automatically generated - do not modify!

@file:JsModule("@mui/material/DialogContent")

package mui.material

import mui.material.styles.Theme
import mui.system.PropsWithSx
import mui.system.StandardProps
import mui.system.SxProps
import react.dom.html.HTMLAttributes
import web.html.HTMLDivElement

external interface DialogContentProps :
    StandardProps,
    HTMLAttributes<HTMLDivElement>,
    react.PropsWithChildren,
    PropsWithSx {
    /**
     * The content of the component.
     */
    override var children: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: DialogContentClasses?

    /**
     * Display the top and bottom dividers.
     * @default false
     */
    var dividers: Boolean?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

/**
 *
 * Demos:
 *
 * - [Dialog](https://v6.mui.com/material-ui/react-dialog/)
 *
 * API:
 *
 * - [DialogContent API](https://v6.mui.com/material-ui/api/dialog-content/)
 */
@JsName("default")
external val DialogContent: react.FC<DialogContentProps>
