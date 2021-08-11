// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/BottomNavigationAction")
@file:JsNonModule

package material

external interface BottomNavigationActionProps : react.Props {
    /**
     * This prop isn't supported.
     * Use the `component` prop if you need to change the children structure.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: BottomNavigationActionClasses

    /**
     * The icon to display.
     */
    var icon: react.ReactNode

    /**
     * The label element.
     */
    var label: react.ReactNode

    /**
     * If `true`, the `BottomNavigationAction` will show its label.
     * By default, only the selected `BottomNavigationAction`
     * inside `BottomNavigation` will show its label.
     *
     * The prop defaults to the value (`false`) inherited from the parent BottomNavigation component.
     */
    var showLabel: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

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
