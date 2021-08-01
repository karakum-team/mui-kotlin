// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/FormHelperText")
@file:JsNonModule

package material

external interface FormHelperTextProps : react.RProps {
    /**
     * The content of the component.
     *
     * If `' '` is provided, the component reserves one line height for displaying a future message.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: FormHelperTextClasses

    /**
     * If `true`, the helper text should be displayed in a disabled state.
     */
    var disabled: Boolean

    /**
     * If `true`, helper text should be displayed in an error state.
     */
    var error: Boolean

    /**
     * If `true`, the helper text should use filled classes key.
     */
    var filled: Boolean

    /**
     * If `true`, the helper text should use focused classes key.
     */
    var focused: Boolean

    /**
     * If `dense`, will adjust vertical spacing. This is normally obtained via context from
     * FormControl.
     */
    var margin: Union /* 'dense' */

    /**
     * If `true`, the helper text should use required classes key.
     */
    var required: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The variant to use.
     */
    var variant: Union /* 'standard' | 'outlined' | 'filled' */
}

/**
 *
 * Demos:
 *
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [FormHelperText API](https://material-ui.com/api/form-helper-text/)
 */
@JsName("default")
external val FormHelperText: react.FC<FormHelperTextProps>
