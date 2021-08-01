// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/InputBase")
@file:JsNonModule

package material

external interface InputBaseProps : react.RProps {
    // var `aria-describedby`: String

    /**
     * This prop helps users to fill forms faster, especially on mobile devices.
     * The name can be confusing, as it's more like an autofill.
     * You can learn more about it [following the specification](https://html.spec.whatwg.org/multipage/form-control-infrastructure.html#autofill).
     */
    var autoComplete: String

    /**
     * If `true`, the `input` element is focused during the first mount.
     */
    var autoFocus: Boolean

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * The prop defaults to the value (`'primary'`) inherited from the parent FormControl component.
     */
    var color: Union /* 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning', InputBasePropsColorOverrides */

    /**
     * The components used for each slot inside the InputBase.
     * Either a string to use a HTML element or a component.
     * @default {}
     */
    var components: dynamic

    /**
     * The props used for each slot inside the Input.
     * @default {}
     */
    var componentsProps: dynamic

    /**
     * The default value. Use when the component is not controlled.
     */
    var defaultValue: dynamic

    /**
     * If `true`, the component is disabled.
     * The prop defaults to the value (`false`) inherited from the parent FormControl component.
     */
    var disabled: Boolean

    /**
     * End `InputAdornment` for this component.
     */
    var endAdornment: react.ReactNode

    /**
     * If `true`, the `input` will indicate an error.
     * The prop defaults to the value (`false`) inherited from the parent FormControl component.
     */
    var error: Boolean

    /**
     * If `true`, the `input` will take up the full width of its container.
     * @default false
     */
    var fullWidth: Boolean

    /**
     * The id of the `input` element.
     */
    var id: String

    /**
     * The component used for the `input` element.
     * Either a string to use a HTML element or a component.
     * @default 'input'
     */
    var inputComponent: dynamic

    /**
     * [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Attributes) applied to the `input` element.
     * @default {}
     */
    var inputProps: InputBaseComponentProps

    /**
     * Pass a ref to the `input` element.
     */
    var inputRef: react.Ref<*>

    /**
     * If `dense`, will adjust vertical spacing. This is normally obtained via context from
     * FormControl.
     * The prop defaults to the value (`'none'`) inherited from the parent FormControl component.
     */
    var margin: Union /* 'dense' | 'none' */

    /**
     * If `true`, a `textarea` element is rendered.
     * @default false
     */
    var multiline: Boolean

    /**
     * Name attribute of the `input` element.
     */
    var name: String

    /**
     * Callback fired when the `input` is blurred.
     *
     * Notice that the first argument (event) might be undefined.
     */
    var onBlur: dynamic

    /**
     * Callback fired when the value is changed.
     *
     * @param {React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>} event The event source of the callback.
     * You can pull out the new value by accessing `event.target.value` (string).
     */
    var onChange: dynamic

    var onFocus: dynamic

    var onKeyDown: dynamic

    var onKeyUp: dynamic

    /**
     * The short hint displayed in the `input` before the user enters a value.
     */
    var placeholder: String

    /**
     * It prevents the user from changing the value of the field
     * (not from interacting with the field).
     */
    var readOnly: Boolean

    /**
     * If `true`, the `input` element is required.
     * The prop defaults to the value (`false`) inherited from the parent FormControl component.
     */
    var required: Boolean

    var renderSuffix: dynamic

    /**
     * Number of rows to display when multiline option is set to true.
     */
    var rows: dynamic

    /**
     * Maximum number of rows to display when multiline option is set to true.
     */
    var maxRows: dynamic

    /**
     * Minimum number of rows to display when multiline option is set to true.
     */
    var minRows: dynamic

    /**
     * The size of the component.
     */
    var size: Union /* 'small' | 'medium', InputBasePropsSizeOverrides */

    /**
     * Start `InputAdornment` for this component.
     */
    var startAdornment: react.ReactNode

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * Type of the `input` element. It should be [a valid HTML5 input type](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Form_%3Cinput%3E_types).
     * @default 'text'
     */
    var type: String

    /**
     * The value of the `input` element, required for a controlled component.
     */
    var value: dynamic
}

external interface InputBaseComponentProps : react.RProps

/**
 * `InputBase` contains as few styles as possible.
 * It aims to be a simple building block for creating an input.
 * It contains a load of style reset and some state logic.
 *
 * Demos:
 *
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [InputBase API](https://material-ui.com/api/input-base/)
 */
@JsName("default")
external val InputBase: react.FC<InputBaseProps>
