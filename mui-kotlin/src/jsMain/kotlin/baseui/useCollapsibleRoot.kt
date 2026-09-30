// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.StateSetter

external interface UseCollapsibleRootParameters {
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
    var onOpenChange: (open: Boolean, eventDetails: CollapsibleRootChangeEventDetails) -> Unit

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean
}

external interface UseCollapsibleRootReturnValue {
    var defaultPanelId: String?

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    var handleTrigger: Any? /* (event: React.MouseEvent | React.KeyboardEvent) => void */

    /**
     * Whether the collapsible panel is mounted for transition and hidden-state
     * purposes. This can be `false` while the element remains in the DOM when
     * `keepMounted` or `hiddenUntilFound` is enabled.
     */
    var mounted: Boolean

    /**
     * Whether the collapsible panel is currently open.
     */
    var open: Boolean

    var panelId: String?

    var setMounted: (nextMounted: Boolean) -> Unit

    var setOpen: (open: Boolean) -> Unit

    var setPanelIdState: StateSetter<String?>

    var transitionStatus: TransitionStatus?
}

external interface UseCollapsibleRootState
