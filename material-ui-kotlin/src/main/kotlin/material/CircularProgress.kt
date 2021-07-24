// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/CircularProgress")
@file:JsNonModule

package material

external interface CircularProgressProps : react.RProps {
    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' | 'inherit' */

    /**
     * If `true`, the shrink animation is disabled.
     * This only works if variant is `indeterminate`.
     */
    var disableShrink: Boolean

    /**
     * The size of the circle.
     * If using a number, the pixel unit is assumed.
     * If using a string, you need to provide the CSS unit, e.g '3rem'.
     */
    var size: dynamic

    /**
     * The thickness of the circle.
     */
    var thickness: Number

    /**
     * The value of the progress indicator for the determinate variant.
     * Value between 0 and 100.
     */
    var value: Number

    /**
     * The variant to use.
     * Use indeterminate when there is no progress value.
     */
    var variant: dynamic /* 'determinate' | 'indeterminate' | 'static' */
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
 * - [CircularProgress API](https://material-ui.com/api/circular-progress/)
 */
@JsName("default")
external val CircularProgress: react.FC<CircularProgressProps>
