// Automatically generated - do not modify!

@file:JsModule("@mui/material/Card")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
)

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface CardProps :
    CardOwnProps,
    react.dom.html.HTMLAttributes<web.html.HTMLDivElement>,
    mui.types.PropsWithComponent

external interface CardPropsColorOverrides

external interface CardOwnProps :
    mui.system.PropsWithSx,
    PaperOwnProps {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: CardClasses?

    /**
     * If `true`, the card will use raised styling.
     * @default false
     */
    var raised: Boolean?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

/**
 *
 * Demos:
 *
 * - [Card](https://v6.mui.com/material-ui/react-card/)
 *
 * API:
 *
 * - [Card API](https://v6.mui.com/material-ui/api/card/)
 * - inherits [Paper API](https://v6.mui.com/material-ui/api/paper/)
 */

@JsName("default")
external val Card: react.FC<CardProps>
