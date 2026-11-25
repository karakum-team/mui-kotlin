// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId

external interface MenuLinkItemProps :
    BaseUiAProps {
    /**
     * Overrides the text used for keyboard text navigation and filtering.
     * Falls back to the rendered text when not provided.
     */
    var label: String?

    /**
     * @ignore
     */
    var id: ElementId?

    /**
     * Whether to close the menu when the item is clicked.
     * @default false
     */
    var closeOnClick: Boolean?
}

external interface MenuLinkItemState {
    /**
     * Whether the item is highlighted.
     */
    var highlighted: Boolean
}
