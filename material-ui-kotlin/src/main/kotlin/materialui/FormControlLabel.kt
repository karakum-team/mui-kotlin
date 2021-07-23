// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/FormControlLabel")
@file:JsNonModule

package materialui

external interface FormControlLabelProps : react.RProps {
    /**
     * If `true`, the component appears selected.
     */
    var checked: Boolean

    /**
     * A control element. For instance, it can be be a `Radio`, a `Switch` or a `Checkbox`.
     */
    var control: dynamic

    /**
     * If `true`, the control will be disabled.
     */
    var disabled: Boolean

    /**
     * Pass a ref to the `input` element.
     */
    var inputRef: dynamic

    /**
     * The text to be used in an enclosing label element.
     */
    var label: react.ReactNode

    /**
     * The position of the label.
     */
    var labelPlacement: dynamic /* 'end' | 'start' | 'top' | 'bottom' */

    var name: String

    /**
     * Callback fired when the state is changed.
     *
     * @param {object} event The event source of the callback.
     * You can pull out the new checked state by accessing `event.target.checked` (boolean).
     */
    var onChange: dynamic

    /**
     * The value of the component.
     */
    var value: dynamic
}

/**
 * Drop in replacement of the `Radio`, `Switch` and `Checkbox` component.
 * Use this component if you want to display an extra label.
 * Demos:
 *
 * - [Checkboxes](https://material-ui.com/components/checkboxes/)
 * - [Radio Buttons](https://material-ui.com/components/radio-buttons/)
 * - [Switches](https://material-ui.com/components/switches/)
 *
 * API:
 *
 * - [FormControlLabel API](https://material-ui.com/api/form-control-label/)
 */
@JsName("default")
external val FormControlLabel: react.FC<FormControlLabelProps>
