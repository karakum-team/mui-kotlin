// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/FormControl")
@file:JsNonModule

package material

external interface FormControlProps : react.RProps {
    /**
     * The contents of the form control.
     */
    var children: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' */

    /**
     * If `true`, the label, input and helper text should be displayed in a disabled state.
     */
    var disabled: Boolean

    /**
     * If `true`, the label should be displayed in an error state.
     */
    var error: Boolean

    /**
     * If `true`, the component will take up the full width of its container.
     */
    var fullWidth: Boolean

    /**
     * If `true`, the component will be displayed in focused state.
     */
    var focused: Boolean

    /**
     * If `true`, the label will be hidden.
     * This is used to increase density for a `FilledInput`.
     * Be sure to add `aria-label` to the `input` element.
     */
    var hiddenLabel: Boolean

    /**
     * If `dense` or `normal`, will adjust vertical spacing of this and contained components.
     */
    var margin: dynamic

    /**
     * If `true`, the label will indicate that the input is required.
     */
    var required: Boolean

    /**
     * The size of the text field.
     */
    var size: dynamic /* 'small' | 'medium' */

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'standard' | 'outlined' | 'filled' */
}

/**
 * Provides context such as filled/focused/error/required for form inputs.
 * Relying on the context provides high flexibility and ensures that the state always stays
 * consistent across the children of the `FormControl`.
 * This context is used by the following components:
 *
 * -   FormLabel
 * -   FormHelperText
 * -   Input
 * -   InputLabel
 *
 * You can find one composition example below and more going to [the demos](https://material-ui.com/components/text-fields/#components).
 *
 * ```jsx
 * <FormControl>
 *   <InputLabel htmlFor="my-input">Email address</InputLabel>
 *   <Input id="my-input" aria-describedby="my-helper-text" />
 *   <FormHelperText id="my-helper-text">We'll never share your email.</FormHelperText>
 * </FormControl>
 * ```
 *
 * ⚠️Only one input can be used within a FormControl.
 * Demos:
 *
 * - [Checkboxes](https://material-ui.com/components/checkboxes/)
 * - [Radio Buttons](https://material-ui.com/components/radio-buttons/)
 * - [Switches](https://material-ui.com/components/switches/)
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [FormControl API](https://material-ui.com/api/form-control/)
 */
@JsName("default")
external val FormControl: react.FC<FormControlProps>
