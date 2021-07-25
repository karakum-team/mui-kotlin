// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Slider")
@file:JsNonModule

package material

external interface SliderProps : react.RProps {
    // var `aria-label`: String

    // var `aria-labelledby`: String

    // var `aria-valuetext`: String

    var color: dynamic /* 'primary' | 'secondary' */

    var defaultValue: dynamic

    var disabled: Boolean

    var getAriaLabel: dynamic

    var getAriaValueText: dynamic

    var marks: dynamic

    var max: Number

    var min: Number

    var name: String

    var onChange: dynamic

    var onChangeCommitted: dynamic

    var orientation: dynamic /* 'horizontal' | 'vertical' */

    var step: dynamic

    var scale: dynamic

    var ThumbComponent: dynamic

    var track: dynamic /* 'normal' | false | 'inverted' */

    var value: dynamic

    var ValueLabelComponent: dynamic

    var valueLabelDisplay: dynamic /* 'on' | 'auto' | 'off' */

    var valueLabelFormat: dynamic
}

/**
 *
 * Demos:
 *
 * - [Slider](https://material-ui.com/components/slider/)
 *
 * API:
 *
 * - [Slider API](https://material-ui.com/api/slider/)
 */
@JsName("default")
external val Slider: react.FC<SliderProps>
