// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ToggleButtonGroup")
@file:JsNonModule

package material

external interface ToggleButtonGroupProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: ToggleButtonGroupClasses

    /**
     * The color of a button when it is selected.
     * @default 'standard'
     */
    var color: Union /* 'standard' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' */

    /**
     * If `true`, only allow one of the child ToggleButton values to be selected.
     * @default false
     */
    var exclusive: Boolean

    /**
     * If `true`, the button group will take up the full width of its container.
     * @default false
     */
    var fullWidth: Boolean

    /**
     * Callback fired when the value changes.
     *
     * @param {React.MouseEvent<HTMLElement>} event The event source of the callback.
     * @param {any} value of the selected buttons. When `exclusive` is true
     * this is a single value; when false an array of selected values. If no value
     * is selected and `exclusive` is true the value is null; when false an empty array.
     */
    var onChange: dynamic

    /**
     * The component orientation (layout flow direction).
     * @default 'horizontal'
     */
    var orientation: Union /* 'horizontal' | 'vertical' */

    /**
     * The size of the component.
     * @default 'medium'
     */
    var size: Union /* 'small' | 'medium' | 'large', ToggleButtonGroupPropsSizeOverrides */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The currently selected value within the group or an array of selected
     * values when `exclusive` is false.
     *
     * The value must have reference equality with the option in order to be selected.
     */
    var value: Any
}

/**
 *
 * Demos:
 *
 * - [Toggle Button](https://material-ui.com/components/toggle-button/)
 *
 * API:
 *
 * - [ToggleButtonGroup API](https://material-ui.com/api/toggle-button-group/)
 */
@JsName("default")
external val ToggleButtonGroup: react.FC<ToggleButtonGroupProps>
