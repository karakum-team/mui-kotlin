// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/MenuItem")
@file:JsNonModule

package material

external interface MenuItemProps : react.RProps {
    /**
     * If `true`, the list item is focused during the first mount.
     * Focus will also be triggered if the value changes from false to true.
     * @default false
     */
    var autoFocus: Boolean

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: MenuItemClasses

    /**
     * If `true`, compact vertical padding designed for keyboard and mouse input is used.
     * The prop defaults to the value inherited from the parent Menu component.
     * @default false
     */
    var dense: Boolean

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean

    /**
     * If `true`, the left and right padding is removed.
     * @default false
     */
    var disableGutters: Boolean

    /**
     * If `true`, a 1px light border is added to the bottom of the menu item.
     * @default false
     */
    var divider: Boolean

    /**
     * Use to apply selected styling.
     * @default false
     */
    var selected: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Menus](https://material-ui.com/components/menus/)
 *
 * API:
 *
 * - [MenuItem API](https://material-ui.com/api/menu-item/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val MenuItem: react.FC<MenuItemProps>
