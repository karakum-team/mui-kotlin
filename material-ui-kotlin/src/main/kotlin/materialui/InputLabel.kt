// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/InputLabel")
@file:JsNonModule

package materialui

external interface InputLabelProps : react.RProps {
    /**
     * The contents of the `InputLabel`.
     */
    var children: react.ReactNode

    var color: dynamic

    /**
     * If `true`, the transition animation is disabled.
     */
    var disableAnimation: Boolean

    /**
     * If `true`, apply disabled class.
     */
    var disabled: Boolean

    /**
     * If `true`, the label will be displayed in an error state.
     */
    var error: Boolean

    /**
     * If `true`, the input of this label is focused.
     */
    var focused: Boolean

    /**
     * If `dense`, will adjust vertical spacing. This is normally obtained via context from
     * FormControl.
     */
    var margin: dynamic /* 'dense' */

    /**
     * if `true`, the label will indicate that the input is required.
     */
    var required: Boolean

    /**
     * If `true`, the label is shrunk.
     */
    var shrink: Boolean

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'standard' | 'outlined' | 'filled' */
}

/**
 *
 * Demos:
 *
 * - [Text Fields](https://material-ui.com/components/text-fields/)
 *
 * API:
 *
 * - [InputLabel API](https://material-ui.com/api/input-label/)
 * - inherits [FormLabel API](https://material-ui.com/api/form-label/)
 */
@JsName("default")
external val InputLabel: react.FC<InputLabelProps>
