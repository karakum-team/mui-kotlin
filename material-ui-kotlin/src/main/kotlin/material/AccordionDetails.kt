// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/AccordionDetails")
@file:JsNonModule

package material

external interface AccordionDetailsProps : react.PropsWithChildren {
    /**
     * The content of the component.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: AccordionDetailsClasses

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
 * - [AccordionDetails API](https://material-ui.com/api/accordion-details/)
 */
@JsName("default")
external val AccordionDetails: react.FC<AccordionDetailsProps>
