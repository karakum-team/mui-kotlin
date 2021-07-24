// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TextField")
@file:JsNonModule

package material

external interface TextFieldProps : react.RProps

/**
 * The `TextField` is a convenience wrapper for the most common cases (80%).
 * It cannot be all things to all people, otherwise the API would grow out of control.
 *
 * ## Advanced Configuration
 *
 * It's important to understand that the text field is a simple abstraction
 * on top of the following components:
 *
 * -   [FormControl](https://material-ui.com/api/form-control/)
 * -   [InputLabel](https://material-ui.com/api/input-label/)
 * -   [FilledInput](https://material-ui.com/api/filled-input/)
 * -   [OutlinedInput](https://material-ui.com/api/outlined-input/)
 * -   [Input](https://material-ui.com/api/input/)
 * -   [FormHelperText](https://material-ui.com/api/form-helper-text/)
 *
 * If you wish to alter the props applied to the `input` element, you can do so as follows:
 *
 * ```jsx
 * const inputProps = {
 *   step: 300,
 * };
 *
 * return <TextField id="time" type="time" inputProps={inputProps} />;
 * ```
 *
 * For advanced cases, please look at the source of TextField by clicking on the
 * "Edit this page" button above. Consider either:
 *
 * -   using the upper case props for passing values directly to the components
 * -   using the underlying components directly as shown in the demos
 * Demos:
 *
 * - [Autocomplete](https://material-ui.com/components/autocomplete/)
 * - [Pickers](https://material-ui.com/components/pickers/)
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [TextField API](https://material-ui.com/api/text-field/)
 * - inherits [FormControl API](https://material-ui.com/api/form-control/)
 */
@JsName("default")
external val TextField: react.FC<TextFieldProps>
