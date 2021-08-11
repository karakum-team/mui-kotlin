// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Input")
@file:JsNonModule

package material

external interface InputProps : react.Props {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: InputClasses

    /**
     * If `true`, the `input` will not have an underline.
     */
    var disableUnderline: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [Input API](https://material-ui.com/api/input/)
 * - inherits [InputBase API](https://material-ui.com/api/input-base/)
 */
@JsName("default")
external val Input: react.FC<InputProps>
