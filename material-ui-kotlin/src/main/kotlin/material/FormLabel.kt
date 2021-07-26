// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/FormLabel")
@file:JsNonModule

package material

external interface FormLabelProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' */

    /**
     * If `true`, the label should be displayed in a disabled state.
     */
    var disabled: Boolean

    /**
     * If `true`, the label is displayed in an error state.
     */
    var error: Boolean

    /**
     * If `true`, the label should use filled classes key.
     */
    var filled: Boolean

    /**
     * If `true`, the input of this label is focused (used by `FormGroup` components).
     */
    var focused: Boolean

    /**
     * If `true`, the label will indicate that the `input` is required.
     */
    var required: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic
}

/**
 *
 * Demos:
 *
 * - [Checkboxes](https://material-ui.com/components/checkboxes/)
 * - [Radio Buttons](https://material-ui.com/components/radio-buttons/)
 * - [Switches](https://material-ui.com/components/switches/)
 *
 * API:
 *
 * - [FormLabel API](https://material-ui.com/api/form-label/)
 */
@JsName("default")
external val FormLabel: react.FC<FormLabelProps>
