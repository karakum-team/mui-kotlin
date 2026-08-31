// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.events.Event
import mui.system.Union

external interface SliderRootProps :
    BaseUiDivProps {
    /**
     * The uncontrolled value of the slider when it's initially rendered.
     *
     * To render a controlled slider, use the `value` prop instead.
     */
    var defaultValue: Any? /* number | readonly number[] */

    /**
     * Whether the slider should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * Options to format the input value.
     */
    var format: Any? /* Intl.NumberFormatOptions */

    /**
     * The locale used by `Intl.NumberFormat` when formatting the value.
     * Defaults to the user's runtime locale.
     */
    var locale: Any? /* Intl.LocalesArgument */

    /**
     * The maximum allowed value of the slider.
     * Should not be equal to min.
     * @default 100
     */
    var max: Double?

    /**
     * The minimum allowed value of the slider.
     * Should not be equal to max.
     * @default 0
     */
    var min: Double?

    /**
     * The minimum steps between values in a range slider.
     * @default 0
     */
    var minStepsBetweenValues: Number?

    /**
     * Identifies the field when a form is submitted.
     */
    var name: String?

    /**
     * Identifies the form that owns the slider inputs.
     * Useful when the slider is rendered outside the form.
     */
    var form: String?

    /**
     * The component orientation.
     * @default 'horizontal'
     */
    var orientation: Orientation?

    /**
     * The granularity with which the slider can step through values. (A "discrete" slider.)
     * The `min` prop serves as the origin for the valid values.
     * We recommend (max - min) to be evenly divisible by the step.
     * @default 1
     */
    var step: Number?

    /**
     * The granularity with which the slider can step through values when using Page Up/Page Down or Shift + Arrow Up/Arrow Down.
     * @default 10
     */
    var largeStep: Number?

    /**
     * How the thumb(s) are aligned relative to `Slider.Control` when the value is at `min` or `max`:
     * - `center`: The center of the thumb is aligned with the control edge
     * - `edge`: The thumb is inset within the control such that its edge is aligned with the control edge
     * - `edge-client-only`: Same as `edge` but renders after React hydration on the client, reducing bundle size in return
     * @default 'center'
     */
    var thumbAlignment: Union? /* 'center' | 'edge' | 'edge-client-only' */

    /**
     * Controls how thumbs behave when they collide during pointer interactions.
     *
     * - `'push'` (default): Thumbs push each other without restoring their previous positions when dragged back.
     * - `'swap'`: Thumbs swap places when dragged past each other.
     * - `'none'`: Thumbs cannot move past each other; excess movement is ignored.
     *
     * @default 'push'
     */
    var thumbCollisionBehavior: Union? /* 'push' | 'swap' | 'none' */

    /**
     * The value of the slider.
     * For ranged sliders, provide an array with two values.
     */
    var value: Any? /* number | readonly number[] */

    /**
     * Callback function that is fired when the slider's value changed.
     * You can pull out the new value by accessing `event.target.value` (any).
     *
     * The `eventDetails.reason` indicates what triggered the change:
     *
     * - `'input-change'` when the hidden range input emits a change event (for example, via form integration)
     * - `'track-press'` when the control track is pressed
     * - `'drag'` while dragging a thumb
     * - `'keyboard'` for keyboard input
     * - `'none'` when the change is triggered without a specific interaction
     */
    var onValueChange: Any? /* (value: Value extends number ? number : Value, eventDetails: SliderRootChangeEventDetails) => void */

    /**
     * Callback function that is fired when a value change is committed.
     * Does not fire if the value did not change, or if the change was canceled.
     * **Warning**: This is a generic event, not a change event.
     *
     * The `eventDetails.reason` indicates what triggered the commit:
     *
     * - `'drag'` while dragging a thumb
     * - `'track-press'` when the control track is pressed
     * - `'keyboard'` for keyboard input
     * - `'input-change'` when the hidden range input emits a change event (for example, via form integration)
     * - `'none'` when the commit occurs without a specific interaction
     */
    var onValueCommitted: Any? /* (value: Value extends number ? number : Value, eventDetails: SliderRootCommitEventDetails) => void */
}

external interface SliderRootState : FieldRootState {
    /**
     * The index of the active thumb.
     */
    var activeThumbIndex: Number

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the thumb is currently being dragged.
     */
    var dragging: Boolean

    /**
     * The maximum value.
     */
    var max: Double

    /**
     * The minimum value.
     */
    var min: Double

    /**
     * The minimum steps between values in a range slider.
     * @default 0
     */
    var minStepsBetweenValues: Number

    /**
     * The component orientation.
     */
    var orientation: Orientation

    /**
     * The step increment of the slider when incrementing or decrementing. It will snap
     * to multiples of this value. Decimal values are supported.
     * @default 1
     */
    var step: Number

    /**
     * The raw number value of the slider.
     */
    var values: Any? /* readonly number[] */
}

external interface SliderRootChangeEventCustomProperties {
    /**
     * The index of the active thumb at the time of the change.
     */
    var activeThumbIndex: Number
}

external interface SliderRootChangeEventDetails :
    BaseUIChangeEventDetails,
    SliderRootChangeEventCustomProperties

external interface SliderRootCommitEventDetails : BaseUIGenericEventDetails
