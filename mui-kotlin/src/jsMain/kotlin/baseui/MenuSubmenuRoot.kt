// Automatically generated - do not modify!

@file:Suppress(
"VIRTUAL_MEMBER_HIDDEN",
"VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.events.Event
import react.PropsWithChildren
import react.ReactNode

external interface MenuSubmenuRootProps: 
MenuRootProps,
PropsWithChildren {
/**
 * Event handler called when the menu is opened or closed.
 */
var onOpenChange: ((open: Boolean, eventDetails: MenuSubmenuRootChangeEventDetails)->Unit)?

/**
 * When in a submenu, determines whether pressing the Escape key
 * closes the entire menu, or only the current child menu.
 * @default false
 */
var closeParentOnEsc: Boolean?

/**
 * The content of the submenu.
 */
override var children: ReactNode?
}

external interface MenuSubmenuRootState

external interface MenuSubmenuRootChangeEventDetails : BaseUIChangeEventDetails
