// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId

external interface MenuItemProps : NonNativeButtonProps, BaseUiDivProps {
    /** The click handler for the menu item. */
    override var onClick: Any? /* BaseUiDivProps['onClick'] */

    /**
     * Whether the component should ignore user interaction.
     *
     * @default false
     */
    var disabled: Boolean?

    /** Overrides the text label to use when the item is matched during keyboard text navigation. */
    var label: String?

    /** @ignore */
    var id: ElementId?

    /**
     * Whether to close the menu when the item is clicked.
     *
     * @default true
     */
    var closeOnClick: Boolean?
}

external interface MenuItemState {
    /** Whether the item should ignore user interaction. */
    var disabled: Boolean

    /** Whether the item is highlighted. */
    var highlighted: Boolean
}
