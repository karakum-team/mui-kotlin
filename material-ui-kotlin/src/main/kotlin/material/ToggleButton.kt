// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ToggleButton")
@file:JsNonModule

package material

external interface ToggleButtonProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the button when it is in an active state.
     * @default 'standard'
     */
    var color: dynamic /* 'standard' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' */

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean

    /**
     * If `true`, the  keyboard focus ripple is disabled.
     * @default false
     */
    var disableFocusRipple: Boolean

    /**
     * If `true`, the button will take up the full width of its container.
     * @default false
     */
    var fullWidth: Boolean

    /**
     * If `true`, the button is rendered in an active state.
     */
    var selected: Boolean

    /**
     * The size of the component.
     * The prop defaults to the value inherited from the parent ToggleButtonGroup component.
     * @default 'medium'
     */
    var size: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic

    /**
     * The value to associate with the button when selected in a
     * ToggleButtonGroup.
     */
    var value: dynamic
}

/**
 *
 * Demos:
 *
 * - [Toggle Button](https://material-ui.com/components/toggle-button/)
 *
 * API:
 *
 * - [ToggleButton API](https://material-ui.com/api/toggle-button/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val ToggleButton: react.FC<ToggleButtonProps>
