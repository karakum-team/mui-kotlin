// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface SelectProps : react.RProps {
    /**
     * If `true`, the width of the popover will automatically be set according to the items inside the
     * menu, otherwise it will be at least the width of the select input.
     */
    var autoWidth: Boolean

    /**
     * The option elements to populate the select with.
     * Can be some `MenuItem` when `native` is false and `option` when `native` is true.
     *
     * ⚠️The `MenuItem` elements **must** be direct descendants when `native` is false.
     */
    var children: dynamic

    /**
     * The default element value. Use when the component is not controlled.
     * @document
     */
    var defaultValue: dynamic

    /**
     * If `true`, a value is displayed even if no items are selected.
     *
     * In order to display a meaningful value, a function should be passed to the `renderValue` prop which returns the value to be displayed when no items are selected.
     * You can only use it when the `native` prop is `false` (default).
     */
    var displayEmpty: Boolean

    /**
     * The icon that displays the arrow.
     */
    var IconComponent: dynamic

    /**
     * The `id` of the wrapper element or the `select` element when `native`.
     */
    var id: String

    /**
     * An `Input` element; does not have to be a material-ui specific `Input`.
     */
    var input: dynamic

    /**
     * [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Attributes) applied to the `input` element.
     * When `native` is `true`, the attributes are applied on the `select` element.
     */
    var inputProps: dynamic

    /**
     * See [OutlinedInput#label](/api/outlined-input/#props)
     */
    var label: dynamic

    /**
     * The ID of an element that acts as an additional label. The Select will
     * be labelled by the additional label and the selected value.
     */
    var labelId: String

    /**
     * See [OutlinedInput#label](/api/outlined-input/#props)
     */
    var labelWidth: Number

    /**
     * Props applied to the [`Menu`](/api/menu/) element.
     */
    var MenuProps: dynamic

    /**
     * If `true`, `value` must be an array and the menu will support multiple selections.
     */
    var multiple: Boolean

    /**
     * If `true`, the component will be using a native `select` element.
     */
    var native: Boolean

    /**
     * Callback function fired when a menu item is selected.
     *
     * @param {object} event The event source of the callback.
     * You can pull out the new value by accessing `event.target.value` (any).
     * @param {object} [child] The react element that was selected when `native` is `false` (default).
     * @document
     */
    var onChange: dynamic

    /**
     * Callback fired when the component requests to be closed.
     * Use in controlled mode (see open).
     *
     * @param {object} event The event source of the callback.
     */
    var onClose: dynamic

    /**
     * Callback fired when the component requests to be opened.
     * Use in controlled mode (see open).
     *
     * @param {object} event The event source of the callback.
     */
    var onOpen: dynamic

    /**
     * Control `select` open state.
     * You can only use it when the `native` prop is `false` (default).
     */
    var open: Boolean

    /**
     * Render the selected value.
     * You can only use it when the `native` prop is `false` (default).
     *
     * @param {any} value The `value` provided to the component.
     * @returns {ReactNode}
     */
    var renderValue: dynamic

    /**
     * Props applied to the clickable div element.
     */
    var SelectDisplayProps: dynamic

    /**
     * The input value. Providing an empty string will select no options.
     * This prop is required when the `native` prop is `false` (default).
     * Set to an empty string `''` if you don't want any of the available options to be selected.
     *
     * If the value is an object it must have reference equality with the option in order to be selected.
     * If the value is not an object, the string representation must match with the string representation of the option in order to be selected.
     * @document
     */
    var value: dynamic

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'standard' | 'outlined' | 'filled' */
}

/**
 *
 * Demos:
 *
 * - [Selects](https://material-ui.com/components/selects/)
 *
 * API:
 *
 * - [Select API](https://material-ui.com/api/select/)
 * - inherits [Input API](https://material-ui.com/api/input/)
 */
@JsName("default")
external val Select: react.FC<SelectProps>
