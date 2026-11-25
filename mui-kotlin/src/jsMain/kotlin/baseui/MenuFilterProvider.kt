// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.PropsWithChildren
import react.ReactNode

external interface MenuFilterProviderProps :
    MenuFilterProviderOptions,
    PropsWithChildren {
    /**
     * The `<Menu.Root>` or `<Menu.SubmenuRoot>` to make filterable.
     */
    override var children: ReactNode?
}

external interface MenuFilterProviderState

external interface MenuFilterProviderChangeEventDetails : BaseUIChangeEventDetails
