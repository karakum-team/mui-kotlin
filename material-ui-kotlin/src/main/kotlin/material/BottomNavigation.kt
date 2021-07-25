// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/BottomNavigation")
@file:JsNonModule

package material

external interface BottomNavigationProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Callback fired when the value changes.
     *
     * @param {object} event The event source of the callback.
     * @param {any} value We default to the index of the child.
     */
    var onChange: dynamic

    /**
     * If `true`, all `BottomNavigationAction`s will show their labels.
     * By default, only the selected `BottomNavigationAction` will show its label.
     */
    var showLabels: Boolean

    /**
     * The value of the currently selected `BottomNavigationAction`.
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
 * - [BottomNavigation API](https://material-ui.com/api/bottom-navigation/)
 */
@JsName("default")
external val BottomNavigation: react.FC<BottomNavigationProps>
