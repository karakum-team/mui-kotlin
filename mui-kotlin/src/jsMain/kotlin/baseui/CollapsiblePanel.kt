// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface CollapsiblePanelProps :
    BaseUiDivProps {
    /**
     * Allows the browser's built-in page search to find and expand the panel contents.
     *
     * Overrides the `keepMounted` prop and uses `hidden="until-found"`
     * to hide the element without removing it from the DOM.
     *
     * @default false
     */
    var hiddenUntilFound: Boolean?

    /**
     * Whether to keep the element in the DOM while the panel is hidden.
     * This prop is ignored when `hiddenUntilFound` is used.
     * @default false
     */
    var keepMounted: Boolean?
}

external interface CollapsiblePanelState : CollapsibleRootState {
    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?
}
