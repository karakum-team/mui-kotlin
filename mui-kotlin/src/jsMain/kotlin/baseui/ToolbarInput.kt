// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface ToolbarInputProps :
    BaseUiInputProps {
    /**
     * When `true` the item is disabled.
     * @default false
     */
    var disabled: Boolean?

    /**
     * When `true` the item remains focusable when disabled.
     * @default true
     */
    var focusableWhenDisabled: Boolean?

    var defaultValue: Any? /* React.ComponentProps<'input'>['defaultValue'] */
}

external interface ToolbarInputState : ToolbarRootState {
    /**
     * Whether the component is disabled.
     */
    var disabled: Boolean

    /**
     * Whether the component remains focusable when disabled.
     */
    var focusable: Boolean
}
