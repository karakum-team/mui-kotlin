// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.PropsWithChildren
import react.ReactNode

external interface MenuTriggerProps :
    NativeButtonProps,
    BaseUiButtonProps,
    PropsWithChildren {
    override var children: ReactNode?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * A handle to associate the trigger with a menu.
     */
    var handle: Any? /* MenuHandle<Payload> */

    /**
     * A payload to pass to the menu when it is opened.
     */
    var payload: Any? /* Payload */

    /**
     * How long to wait before the menu may be opened on hover. Specified in milliseconds.
     *
     * Requires the `openOnHover` prop.
     * @default 100
     */
    var delay: Number?

    /**
     * How long to wait before closing the menu that was opened on hover.
     * Specified in milliseconds.
     *
     * Requires the `openOnHover` prop.
     * @default 0
     */
    var closeDelay: Number?

    /**
     * Whether the menu should also open when the trigger is hovered.
     */
    var openOnHover: Boolean?
}

external interface MenuTriggerState {
    /**
     * Whether the menu is currently open and was opened by this trigger.
     */
    var open: Boolean

    /**
     * Whether the trigger is disabled.
     */
    var disabled: Boolean
}
