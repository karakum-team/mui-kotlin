// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface OutlinedInputProps : react.RProps {
    /**
     * The label of the input. It is only used for layout. The actual labelling
     * is handled by `InputLabel`. If specified `labelWidth` is ignored.
     */
    var label: react.ReactNode

    /**
     * The width of the label. Is ignored if `label` is provided. Prefer `label`
     * if the input label appears with a strike through.
     */
    var labelWidth: Number

    /**
     * If `true`, the outline is notched to accommodate the label.
     */
    var notched: Boolean
}

/**
 *
 * Demos:
 *
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [OutlinedInput API](https://material-ui.com/api/outlined-input/)
 * - inherits [InputBase API](https://material-ui.com/api/input-base/)
 */
@JsName("default")
external val OutlinedInput: react.FC<OutlinedInputProps>
