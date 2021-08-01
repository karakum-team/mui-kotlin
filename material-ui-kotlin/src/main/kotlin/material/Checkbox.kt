// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Checkbox")
@file:JsNonModule

package material

external interface CheckboxProps : react.RProps {
    /**
     * If `true`, the component is checked.
     */
    var checked: dynamic

    /**
     * The icon to display when the component is checked.
     * @default <CheckBoxIcon />
     */
    var checkedIcon: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'primary'
     */
    var color: dynamic

    /**
     * If `true`, the component is disabled.
     */
    var disabled: dynamic

    /**
     * If `true`, the ripple effect is disabled.
     */
    var disableRipple: dynamic

    /**
     * The icon to display when the component is unchecked.
     * @default <CheckBoxOutlineBlankIcon />
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
     * However, we set a `data-indeterminate` attribute on the `input`.
     * @default false
     */
    var indeterminate: Boolean

    /**
     * The icon to display when the component is indeterminate.
     * @default <IndeterminateCheckBoxIcon />
     */
    var indeterminateIcon: react.ReactNode

    /**
     * [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Attributes) applied to the `input` element.
     */
    var inputProps: dynamic

    /**
     * Pass a ref to the `input` element.
     */
    var inputRef: react.Ref<org.w3c.dom.HTMLInputElement>

    /**
     * Callback fired when the state is changed.
     *
     * @param {React.ChangeEvent<HTMLInputElement>} event The event source of the callback.
     * You can pull out the new checked state by accessing `event.target.checked` (boolean).
     */
    var onChange: dynamic

    /**
     * If `true`, the `input` element is required.
     */
    var required: dynamic

    /**
     * The size of the component.
     * `small` is equivalent to the dense checkbox styling.
     * @default 'medium'
     */
    var size: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

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
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val Checkbox: react.FC<CheckboxProps>
