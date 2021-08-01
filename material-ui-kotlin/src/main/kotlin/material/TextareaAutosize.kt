// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TextareaAutosize")
@file:JsNonModule

package material

external interface TextareaAutosizeProps : react.RProps {
    var ref: react.Ref<org.w3c.dom.HTMLTextAreaElement>

    /**
     * Maximum number of rows to display.
     */
    var maxRows: dynamic

    /**
     * Minimum number of rows to display.
     * @default 1
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
