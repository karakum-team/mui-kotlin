// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TextareaAutosize")
@file:JsNonModule

package material

external interface TextareaAutosizeProps : react.RProps {
    var ref: dynamic

    /**
     * Minimum number of rows to display.
     * @deprecated Use `rowsMin` instead.
     */
    var rows: dynamic

    /**
     * Maximum number of rows to display.
     * @deprecated Use `maxRows` instead.
     */
    var rowsMax: dynamic

    /**
     * Minimum number of rows to display.
     * @deprecated Use `minRows` instead.
     */
    var rowsMin: dynamic

    /**
     * Maximum number of rows to display.
     */
    var maxRows: dynamic

    /**
     * Minimum number of rows to display.
     */
    var minRows: dynamic
}

/**
 *
 * Demos:
 *
 * - [Textarea Autosize](https://material-ui.com/components/textarea-autosize/)
 *
 * API:
 *
 * - [TextareaAutosize API](https://material-ui.com/api/textarea-autosize/)
 */
@JsName("default")
external val TextareaAutosize: react.FC<TextareaAutosizeProps>
