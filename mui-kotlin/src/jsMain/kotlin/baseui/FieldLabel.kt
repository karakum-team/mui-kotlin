// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface FieldLabelProps :
    BaseUiLabelProps {
    /**
     * Whether the component renders a native `<label>` element when replacing it via the `render` prop.
     * Set to `false` if the rendered element is not a label (for example, `<div>`).
     *
     * This is useful to avoid inheriting label behaviors on `<button>` controls (such as `<Select.Trigger>` and `<Combobox.Trigger>`), including avoiding `:hover` on the button when hovering the label, and preventing clicks on the label from firing on the button.
     * @default true
     */
    var nativeLabel: Boolean?
}

external interface FieldLabelState : FieldRootState
