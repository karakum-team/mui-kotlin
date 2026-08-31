// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface MenuPositionerProps : UseAnchorPositioningSharedParameters, BaseUiDivProps {}

external interface MenuPositionerState {
    /** Whether the menu is currently open. */
    var open: Boolean

    /** The side of the anchor the component is placed on. */
    var side: Side

    /** The alignment of the component relative to the anchor. */
    var align: Align

    /** Whether the anchor element is hidden. */
    var anchorHidden: Boolean

    /** Whether the component is nested. */
    var nested: Boolean

    /** Whether CSS transitions should be disabled. */
    var instant: String
}
