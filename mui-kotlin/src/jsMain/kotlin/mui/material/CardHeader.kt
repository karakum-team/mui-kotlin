// Automatically generated - do not modify!

@file:JsModule("@mui/material/CardHeader")

@file:Suppress(
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface CardHeaderProps :
    CardHeaderOwnProps,
    react.dom.html.HTMLAttributes<web.html.HTMLDivElement>

external interface CardHeaderRootSlotPropsOverrides

external interface CardHeaderAvatarSlotPropsOverrides

external interface CardHeaderActionSlotPropsOverrides

external interface CardHeaderContentSlotPropsOverrides

external interface CardHeaderTitleSlotPropsOverrides

external interface CardHeaderSubheaderSlotPropsOverrides

external interface CardHeaderSlots {
    /**
     * The component that renders the root slot.
     * @default 'div'
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the avatar slot.
     * @default 'div'
     */
    var avatar: react.ElementType<*>

    /**
     * The component that renders the action slot.
     * @default 'div'
     */
    var action: react.ElementType<*>

    /**
     * The component that renders the content slot.
     * @default 'div'
     */
    var content: react.ElementType<*>

    /**
     * The component that renders the title slot (as long as disableTypography is not `true`).
     * [Follow this guide](https://mui.com/material-ui/api/typography/#props) to learn more about the requirements for this component.
     * @default Typography
     */
    var title: react.ElementType<*>

    /**
     * The component that renders the subheader slot (as long as disableTypography is not `true`).
     * [Follow this guide](https://mui.com/material-ui/api/typography/#props) to learn more about the requirements for this component.
     * @default Typography
     */
    var subheader: react.ElementType<*>
}

external interface CardHeaderOwnProps :
    mui.system.PropsWithSx,
    react.dom.html.HTMLAttributes<web.html.HTMLDivElement> {
    /**
     * The action to display in the card header.
     */
    var action: react.ReactNode?

    /**
     * The Avatar element to display.
     */
    var avatar: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: CardHeaderClasses?

    /**
     * If `true`, `subheader` and `title` won't be wrapped by a Typography component.
     * This can be useful to render an alternative Typography variant by wrapping
     * the `title` text, and optional `subheader` text
     * with the Typography component.
     * @default false
     */
    var disableTypography: Boolean?

    /**
     * The content of the component.
     */
    var subheader: react.ReactNode?

    /**
     * These props will be forwarded to the subheader
     * (as long as disableTypography is not `true`).
     * @deprecated Use `slotProps.subheader` instead. This prop will be removed in v7. See [Migrating from deprecated APIs](/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var subheaderTypographyProps: TypographyProps?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?

    /**
     * The content of the component.
     */
    override var title: react.ReactNode?

    /**
     * These props will be forwarded to the title
     * (as long as disableTypography is not `true`).
     * @deprecated Use `slotProps.title` instead. This prop will be removed in v7. See [Migrating from deprecated APIs](/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var titleTypographyProps: TypographyProps?
}

external interface CardHeaderOwnerState

/**
 *
 * Demos:
 *
 * - [Card](https://v6.mui.com/material-ui/react-card/)
 *
 * API:
 *
 * - [CardHeader API](https://v6.mui.com/material-ui/api/card-header/)
 */
@JsName("default")
external val CardHeader: react.FC<CardHeaderProps>
