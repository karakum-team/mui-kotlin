// Automatically generated - do not modify!

@file:JsModule("@mui/material/AccordionSummary")

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface AccordionSummaryProps :
    AccordionSummaryOwnProps,
    react.dom.html.HTMLAttributes<web.html.HTMLDivElement>,
    mui.types.PropsWithComponent

external interface AccordionSummarySlots {
    /**
     * The component that renders the root slot.
     * @default ButtonBase
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the content slot.
     * @default div
     */
    var content: react.ElementType<*>

    /**
     * The component that renders the expand icon wrapper slot.
     * @default div
     */
    var expandIconWrapper: react.ElementType<*>
}

external interface AccordionSummaryRootSlotPropsOverrides

external interface AccordionSummaryContentSlotPropsOverrides

external interface AccordionSummaryExpandIconWrapperSlotPropsOverrides

external interface AccordionSummaryOwnProps :
    react.PropsWithChildren,
    mui.system.PropsWithSx {
    override var children: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: AccordionSummaryClasses?

    /**
     * The icon to display as the expand indicator.
     */
    var expandIcon: react.ReactNode?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

external interface AccordionSummaryOwnerState

/**
 *
 * Demos:
 *
 * - [Accordion](https://v6.mui.com/material-ui/react-accordion/)
 *
 * API:
 *
 * - [AccordionSummary API](https://v6.mui.com/material-ui/api/accordion-summary/)
 * - inherits [ButtonBase API](https://v6.mui.com/material-ui/api/button-base/)
 */
@JsName("default")
external val AccordionSummary: react.FC<AccordionSummaryProps>
