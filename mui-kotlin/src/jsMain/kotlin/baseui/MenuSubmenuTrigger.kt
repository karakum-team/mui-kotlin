// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId

external interface MenuSubmenuTriggerProps :
    NonNativeButtonProps,
    BaseUiDivProps {
    var onClick: Any? /* BaseUiDivProps['onClick'] */

    /**
     * Overrides the text label to use when the item is matched during keyboard text navigation.
     */
    var label: String?

    /**
     * @ignore
     */
    var id: ElementId?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

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

external interface MenuSubmenuTriggerState {
    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the item is highlighted.
     */
    var highlighted: Boolean

    /**
     * Whether the menu is currently open.
     */
    var open: Boolean
}
