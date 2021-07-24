// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Menu")
@file:JsNonModule

package material

external interface MenuProps : react.RProps {
    /**
     * A HTML element, or a function that returns it.
     * It's used to set the position of the menu.
     * @document
     */
    var anchorEl: dynamic

    /**
     * If `true` (Default) will focus the `[role="menu"]` if no focusable child is found. Disabled
     * children are not focusable. If you set this prop to `false` focus will be placed
     * on the parent modal container. This has severe accessibility implications
     * and should only be considered if you manage focus otherwise.
     */
    var autoFocus: Boolean

    /**
     * Menu contents, normally `MenuItem`s.
     */
    var children: react.ReactNode

    /**
     * When opening the menu will not focus the active item but the `[role="menu"]`
     * unless `autoFocus` is also set to `false`. Not using the default means not
     * following WAI-ARIA authoring practices. Please be considerate about possible
     * accessibility implications.
     */
    var disableAutoFocusItem: Boolean

    /**
     * Props applied to the [`MenuList`](/api/menu-list/) element.
     */
    var MenuListProps: dynamic

    /**
     * Callback fired when the component requests to be closed.
     *
     * @param {object} event The event source of the callback.
     * @param {string} reason Can be: `"escapeKeyDown"`, `"backdropClick"`, `"tabKeyDown"`.
     */
    var onClose: dynamic

    /**
     * Callback fired before the Menu enters.
     * @deprecated Use the `TransitionProps` prop instead.
     * @document
     */
    var onEnter: dynamic

    /**
     * Callback fired when the Menu has entered.
     * @deprecated Use the `TransitionProps` prop instead.
     * @document
     */
    var onEntered: dynamic

    /**
     * Callback fired when the Menu is entering.
     * @deprecated Use the `TransitionProps` prop instead.
     * @document
     */
    var onEntering: dynamic

    /**
     * Callback fired before the Menu exits.
     * @deprecated Use the `TransitionProps` prop instead.
     * @document
     */
    var onExit: dynamic

    /**
     * Callback fired when the Menu has exited.
     * @deprecated Use the `TransitionProps` prop instead.
     * @document
     */
    var onExited: dynamic

    /**
     * Callback fired when the Menu is exiting.
     * @deprecated Use the `TransitionProps` prop instead.
     * @document
     */
    var onExiting: dynamic

    /**
     * If `true`, the menu is visible.
     */
    var open: Boolean

    /**
     * `classes` prop applied to the [`Popover`](/api/popover/) element.
     */
    var PopoverClasses: dynamic

    /**
     * The length of the transition in `ms`, or 'auto'
     */
    var transitionDuration: dynamic

    /**
     * Props applied to the transition element.
     * By default, the element is based on this [`Transition`](http://reactcommunity.org/react-transition-group/transition) component.
     */
    var TransitionProps: dynamic

    /**
     * The variant to use. Use `menu` to prevent selected items from impacting the initial focus
     * and the vertical alignment relative to the anchor element.
     */
    var variant: dynamic /* 'menu' | 'selectedMenu' */
}

/**
 *
 * Demos:
 *
 * - [App Bar](https://material-ui.com/components/app-bar/)
 * - [Menus](https://material-ui.com/components/menus/)
 *
 * API:
 *
 * - [Menu API](https://material-ui.com/api/menu/)
 * - inherits [Popover API](https://material-ui.com/api/popover/)
 */
@JsName("default")
external val Menu: react.FC<MenuProps>
