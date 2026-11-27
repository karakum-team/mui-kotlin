// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/button")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.FC

external interface ButtonProps :
    NativeButtonProps,
    BaseUiButtonProps {
    /**
     * Whether the button should be focusable when disabled.
     * @default false
     */
    var focusableWhenDisabled: Boolean?
}

external interface ButtonState {
    /**
     * Whether the button should ignore user interaction.
     */
    var disabled: Boolean
}

/**
 * A button component that can be used to trigger actions.
 * Renders a `<button>` element.
 *
 * Documentation: [Base UI Button](https://base-ui.com/react/components/button)
 */
external val Button: FC<ButtonProps>
