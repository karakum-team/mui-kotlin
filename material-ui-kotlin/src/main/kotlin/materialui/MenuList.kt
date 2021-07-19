// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface MenuListProps : react.RProps {
    /**
     * If `true`, will focus the `[role="menu"]` container and move into tab order.
     */
    var autoFocus: Boolean

    /**
     * If `true`, will focus the first menuitem if `variant="menu"` or selected item
     * if `variant="selectedMenu"`.
     */
    var autoFocusItem: Boolean

    /**
     * MenuList contents, normally `MenuItem`s.
     */
    var children: dynamic

    /**
     * If `true`, will allow focus on disabled items.
     */
    var disabledItemsFocusable: Boolean

    /**
     * If `true`, the menu items will not wrap focus.
     */
    var disableListWrap: Boolean

    /**
     * The variant to use. Use `menu` to prevent selected items from impacting the initial focus
     * and the vertical alignment relative to the anchor element.
     */
    var variant: dynamic /* 'menu' | 'selectedMenu' */
}

/**
 * A permanently displayed menu following <https://www.w3.org/TR/wai-aria-practices/#menubutton>.
 * It's exposed to help customization of the [`Menu`](https://material-ui.com/api/menu/) component. If you
 * use it separately you need to move focus into the component manually. Once
 * the focus is placed inside the component it is fully keyboard accessible.
 * Demos:
 *
 * - [Menus](https://material-ui.com/components/menus/)
 *
 * API:
 *
 * - [MenuList API](https://material-ui.com/api/menu-list/)
 * - inherits [List API](https://material-ui.com/api/list/)
 */
@JsName("default")
external val MenuList: react.FC<MenuListProps>
