// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface MenuPositionerProps :
    UseAnchorPositioningSharedParameters,
    BaseUiDivProps {
    /**
     * How to align the popup relative to the specified side.
     *
     * Submenus and menubars default to `'start'`.
     * @default 'center'
     */
    var align: Align?

    /**
     * Which side of the anchor element to align the popup against.
     * May automatically change to avoid collisions.
     *
     * Submenus and vertical menubars default to `'inline-end'`.
     * @default 'bottom'
     */
    var side: Side?
}

external interface MenuPositionerState {
    /**
     * Whether the menu is currently open.
     */
    var open: Boolean

    /**
     * The side of the anchor the component is placed on.
     */
    var side: Side

    /**
     * The alignment of the component relative to the anchor.
     */
    var align: Align

    /**
     * Whether the anchor element is hidden.
     */
    var anchorHidden: Boolean

    /**
     * Whether the component is nested.
     */
    var nested: Boolean

    /**
     * Whether CSS transitions should be disabled.
     */
    var instant: String
}
