// Automatically generated - do not modify!

@file:JsModule("@mui/material/AccordionSummary")
@file:JsNonModule

package mui.material

external interface AccordionSummaryProps : react.PropsWithChildren {
    /**
     * The content of the component.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: AccordionSummaryClasses

    /**
     * The icon to display as the expand indicator.
     */
    var expandIcon: react.ReactNode

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Accordion](https://material-ui.com/components/accordion/)
 *
 * API:
 *
 * - [AccordionSummary API](https://material-ui.com/api/accordion-summary/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val AccordionSummary: react.FC<AccordionSummaryProps>
