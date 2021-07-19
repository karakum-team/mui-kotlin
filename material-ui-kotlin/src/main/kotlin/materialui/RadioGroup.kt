// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface RadioGroupProps : react.RProps {
    /**
     * The default `input` element value. Use when the component is not controlled.
     */
    var defaultValue: dynamic

    /**
     * The name used to reference the value of the control.
     * If you don't provide this prop, it falls back to a randomly generated name.
     */
    var name: String

    /**
     * Callback fired when a radio button is selected.
     *
     * @param {object} event The event source of the callback.
     * You can pull out the new value by accessing `event.target.value` (string).
     */
    var onChange: dynamic

    /**
     * Value of the selected radio button. The DOM API casts this to a string.
     */
    var value: Any
}

/**
 *
 * Demos:
 *
 * - [Radio Buttons](https://material-ui.com/components/radio-buttons/)
 *
 * API:
 *
 * - [RadioGroup API](https://material-ui.com/api/radio-group/)
 * - inherits [FormGroup API](https://material-ui.com/api/form-group/)
 */
@JsName("default")
external val RadioGroup: react.FC<RadioGroupProps>
