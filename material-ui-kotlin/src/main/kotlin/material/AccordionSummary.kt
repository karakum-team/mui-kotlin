// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/AccordionSummary")
@file:JsNonModule

package material

external interface AccordionSummaryProps : react.RProps {
    /**
     * The content of the accordion summary.
     */
    var children: react.ReactNode

    /**
     * The icon to display as the expand indicator.
     */
    var expandIcon: react.ReactNode

    /**
     * Props applied to the `IconButton` element wrapping the expand icon.
     */
    var IconButtonProps: dynamic
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
