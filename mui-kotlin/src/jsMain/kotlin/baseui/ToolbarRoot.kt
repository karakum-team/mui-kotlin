// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface ToolbarRootProps :
    BaseUiDivProps {
    var disabled: Boolean?

    /**
     * The orientation of the toolbar.
     * @default 'horizontal'
     */
    var orientation: Orientation?

    /**
     * If `true`, using keyboard navigation will wrap focus to the other end of the toolbar once the end is reached.
     *
     * @default true
     */
    var loopFocus: Boolean?
}

external interface ToolbarRootItemMetadata {
    var disabled: Boolean

    var focusableWhenDisabled: Boolean
}

external interface ToolbarRootState {
    /**
     * Whether the component is disabled.
     */
    var disabled: Boolean

    /**
     * The component orientation.
     */
    var orientation: Orientation
}
