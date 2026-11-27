// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId

external interface TabsPanelProps :
    BaseUiDivProps {
    /**
     * The value of the TabPanel. It will be shown when the Tab with the corresponding value is active.
     */
    var value: Any?

    /**
     * Whether to keep the HTML element in the DOM while the panel is hidden.
     * @default false
     */
    var keepMounted: Boolean?
}

external interface TabsPanelMetadata {
    var id: ElementId?

    var value: Any?
}

external interface TabsPanelState : TabsRootState {
    /**
     * Whether the component is hidden.
     */
    var hidden: Boolean

    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?
}
