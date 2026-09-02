// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface CollapsibleRootProps :
    BaseUiDivProps {
    /**
     * Whether the collapsible panel is currently open.
     *
     * To render an uncontrolled collapsible, use the `defaultOpen` prop instead.
     */
    var open: Boolean?

    /**
     * Whether the collapsible panel is initially open.
     *
     * To render a controlled collapsible, use the `open` prop instead.
     * @default false
     */
    var defaultOpen: Boolean?

    /**
     * Event handler called when the panel is opened or closed.
     */
    var onOpenChange: ((open: Boolean, eventDetails: CollapsibleRootChangeEventDetails) -> Unit)?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?
}

external interface CollapsibleRootState : UseCollapsibleRootReturnValue

external interface CollapsibleRootChangeEventDetails : BaseUIChangeEventDetails
