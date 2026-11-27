// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface TabsIndicatorProps :
    BaseUiSpanProps {
    /**
     * Whether to render itself before React hydrates.
     * This minimizes the time that the indicator isn't visible after server-side rendering.
     * @default false
     */
    var renderBeforeHydration: Boolean?
}

external interface TabsIndicatorState : TabsRootState {
    /**
     * The active tab position.
     */
    var activeTabPosition: TabsTabPosition?

    /**
     * The active tab size.
     */
    var activeTabSize: TabsTabSize?

    /**
     * The component orientation.
     */
    var orientation: Orientation
}
