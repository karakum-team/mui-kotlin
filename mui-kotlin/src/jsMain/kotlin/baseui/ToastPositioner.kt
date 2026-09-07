// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface ToastPositionerProps :
    BaseUiDivProps,
    UseAnchorPositioningSharedParameters {
    /**
     * An element to position the toast against.
     */
    var anchor: Any? /* Element */

    /**
     * Which side of the anchor element to align the toast against.
     * May automatically change to avoid collisions.
     * @default 'top'
     */
    var side: Side?

    /**
     * The toast object associated with the positioner.
     */
    var toast: Any? /* ToastObject<any> */
}

external interface ToastPositionerState {
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
}
