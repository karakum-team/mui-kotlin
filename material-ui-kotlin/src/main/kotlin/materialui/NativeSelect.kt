// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface NativeSelectProps : react.RProps {
    /**
     * The option elements to populate the select with.
     * Can be some `<option>` elements.
     */
    var children: react.ReactNode

    /**
     * The icon that displays the arrow.
     */
    var IconComponent: dynamic

    /**
     * An `Input` element; does not have to be a material-ui specific `Input`.
     */
    var input: dynamic

    /**
     * Attributes applied to the `select` element.
     */
    var inputProps: dynamic

    /**
     * Callback function fired when a menu item is selected.
     *
     * @param {object} event The event source of the callback.
     * You can pull out the new value by accessing `event.target.value` (string).
     * @document
     */
    var onChange: dynamic

    /**
     * The input value. The DOM API casts this to a string.
     * @document
     */
    var value: dynamic

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'standard' | 'outlined' | 'filled' */
}

/**
 * An alternative to `<Select native />` with a much smaller bundle size footprint.
 * Demos:
 *
 * - [Selects](https://material-ui.com/components/selects/)
 *
 * API:
 *
 * - [NativeSelect API](https://material-ui.com/api/native-select/)
 * - inherits [Input API](https://material-ui.com/api/input/)
 */
@JsName("default")
external val NativeSelect: react.FC<NativeSelectProps>
