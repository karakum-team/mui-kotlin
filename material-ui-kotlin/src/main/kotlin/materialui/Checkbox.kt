// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Checkbox")
@file:JsNonModule

package materialui

external interface CheckboxProps : react.RProps {
    /**
     * If `true`, the component is checked.
     */
    var checked: dynamic

    /**
     * The icon to display when the component is checked.
     */
    var checkedIcon: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' | 'default' */

    /**
     * If `true`, the checkbox will be disabled.
     */
    var disabled: dynamic

    /**
     * If `true`, the ripple effect will be disabled.
     */
    var disableRipple: dynamic

    /**
     * The icon to display when the component is unchecked.
     */
    var icon: react.ReactNode

    /**
     * The id of the `input` element.
     */
    var id: dynamic

    /**
     * If `true`, the component appears indeterminate.
     * This does not set the native input element to indeterminate due
     * to inconsistent behavior across browsers.
     * However, we set a `data-indeterminate` attribute on the input.
     */
    var indeterminate: Boolean

    /**
     * The icon to display when the component is indeterminate.
     */
    var indeterminateIcon: react.ReactNode

    /**
     * [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Attributes) applied to the `input` element.
     */
    var inputProps: dynamic

    /**
     * Pass a ref to the `input` element.
     */
    var inputRef: dynamic

    /**
     * Callback fired when the state is changed.
     *
     * @param {object} event The event source of the callback.
     * You can pull out the new checked state by accessing `event.target.checked` (boolean).
     */
    var onChange: dynamic

    /**
     * If `true`, the `input` element will be required.
     */
    var required: dynamic

    /**
     * The size of the checkbox.
     * `small` is equivalent to the dense checkbox styling.
     */
    var size: dynamic /* 'small' | 'medium' */

    /**
     * The value of the component. The DOM API casts this to a string.
     * The browser uses "on" as the default value.
     */
    var value: dynamic
}

/**
 *
 * Demos:
 *
 * - [Checkboxes](https://material-ui.com/components/checkboxes/)
 * - [Transfer List](https://material-ui.com/components/transfer-list/)
 *
 * API:
 *
 * - [Checkbox API](https://material-ui.com/api/checkbox/)
 * - inherits [IconButton API](https://material-ui.com/api/icon-button/)
 */
@JsName("default")
external val Checkbox: react.FC<CheckboxProps>
