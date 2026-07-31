// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface MenuArrowProps : BaseUiDivProps {}

external interface MenuArrowState {
    /** Whether the menu is currently open. */
    var open: Boolean

    /** The side of the anchor the component is placed on. */
    var side: Any? /* Side */

    /** The alignment of the component relative to the anchor. */
    var align: Align

    /** Whether the arrow cannot be centered on the anchor. */
    var uncentered: Boolean
}
