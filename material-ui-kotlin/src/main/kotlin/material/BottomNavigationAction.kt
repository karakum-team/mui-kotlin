// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/BottomNavigationAction")
@file:JsNonModule

package material

external interface BottomNavigationActionProps : react.RProps {
    /**
     * This prop isn't supported.
     * Use the `component` prop if you need to change the children structure.
     */
    var children: react.ReactNode

    /**
     * The icon element.
     */
    var icon: react.ReactNode

    /**
     * The label element.
     */
    var label: react.ReactNode

    var onChange: dynamic

    var onClick: dynamic

    var selected: Boolean

    /**
     * If `true`, the `BottomNavigationAction` will show its label.
     * By default, only the selected `BottomNavigationAction`
     * inside `BottomNavigation` will show its label.
     */
    var showLabel: Boolean

    /**
     * You can provide your own value. Otherwise, we fallback to the child position index.
     */
    var value: Any
}

/**
 *
 * Demos:
 *
 * - [Bottom Navigation](https://material-ui.com/components/bottom-navigation/)
 *
 * API:
 *
 * - [BottomNavigationAction API](https://material-ui.com/api/bottom-navigation-action/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val BottomNavigationAction: react.FC<BottomNavigationActionProps>
