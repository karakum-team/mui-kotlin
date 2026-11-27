// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/menubar")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.FC

external interface MenubarProps :
    BaseUiDivProps {
    /**
     * Whether the menubar is modal.
     * @default true
     */
    var modal: Boolean?

    /**
     * Whether the whole menubar is disabled.
     * @default false
     */
    var disabled: Boolean?

    /**
     * The orientation of the menubar.
     * @default 'horizontal'
     */
    var orientation: Any? /* MenuRootOrientation */

    /**
     * Whether to loop keyboard focus back to the first item
     * when the end of the list is reached while using the arrow keys.
     * @default true
     */
    var loopFocus: Boolean?
}

external interface MenubarState {
    /**
     * The orientation of the menubar.
     */
    var orientation: Any? /* MenuRootOrientation */

    /**
     * Whether the menubar is modal.
     */
    var modal: Boolean

    /**
     * Whether any submenu within the menubar is open.
     */
    var hasSubmenuOpen: Boolean
}

/**
 * The container for menus.
 *
 * Documentation: [Base UI Menubar](https://base-ui.com/react/components/menubar)
 */
external val Menubar: FC<MenubarProps>
