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
    var variant: dynamic
}
