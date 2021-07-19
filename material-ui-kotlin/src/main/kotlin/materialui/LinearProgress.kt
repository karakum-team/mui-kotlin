// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface LinearProgressProps : react.RProps {
    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' */

    /**
     * The value of the progress indicator for the determinate and buffer variants.
     * Value between 0 and 100.
     */
    var value: Number

    /**
     * The value for the buffer variant.
     * Value between 0 and 100.
     */
    var valueBuffer: Number

    /**
     * The variant to use.
     * Use indeterminate or query when there is no progress value.
     */
    var variant: dynamic /* 'determinate' | 'indeterminate' | 'buffer' | 'query' */
}

/**
 * ## ARIA
 *
 * If the progress bar is describing the loading progress of a particular region of a page,
 * you should use `aria-describedby` to point to the progress bar, and set the `aria-busy`
 * attribute to `true` on that region until it has finished loading.
 * Demos:
 *
 * - [Progress](https://material-ui.com/components/progress/)
 *
 * API:
 *
 * - [LinearProgress API](https://material-ui.com/api/linear-progress/)
 */
@JsName("default")
external val LinearProgress: react.FC<LinearProgressProps>
