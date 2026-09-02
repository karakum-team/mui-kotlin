// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface AccordionItemProps :
    BaseUiDivProps,
    UseCollapsibleRootParameters {
    /**
     * A unique value that identifies this accordion item.
     * If no value is provided, a unique ID will be generated automatically.
     * Use when controlling the accordion programmatically, or to set an initial
     * open state.
     * @example
     * ```tsx
     * <Accordion.Root value={['a']}>
     *   <Accordion.Item value="a" /> // initially open
     *   <Accordion.Item value="b" /> // initially closed
     * </Accordion.Root>
     * ```
     */
    var value: Any?

    /**
     * Event handler called when the panel is opened or closed.
     */
    var onOpenChange: ((open: Boolean, eventDetails: AccordionItemChangeEventDetails) -> Unit)?
}

external interface AccordionItemState : AccordionRootState {
    /**
     * Whether the accordion item's panel is currently hidden.
     */
    var hidden: Boolean

    /**
     * The item index.
     */
    var index: Number

    /**
     * Whether the component is open.
     */
    var open: Boolean
}

external interface AccordionItemChangeEventDetails : BaseUIChangeEventDetails
