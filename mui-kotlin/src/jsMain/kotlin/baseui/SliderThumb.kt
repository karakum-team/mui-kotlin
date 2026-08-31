// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.Ref
import react.dom.events.FocusEventHandler
import react.dom.events.KeyboardEventHandler
import web.html.HTMLInputElement

external interface SliderThumbProps :
    BaseUiDivProps {
    /**
     * Whether the thumb should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * A string value forwarded to the [`aria-valuetext`](https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Reference/Attributes/aria-valuetext) attribute of the `input`.
     * Ignored when `getAriaValueText` is provided.
     */
    @JsName("aria-valuetext")
    var ariaValueText: Any? /* React.AriaAttributes['aria-valuetext'] */

    /**
     * A function which returns a string value for the [`aria-label`](https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Reference/Attributes/aria-label) attribute of the `input`.
     */
    var getAriaLabel: (((index: Number) -> String)?)?

    /**
     * A function which returns a string value for the [`aria-valuetext`](https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Reference/Attributes/aria-valuetext) attribute of the `input`.
     * This is important for screen reader users.
     */
    var getAriaValueText: (((formattedValue: String, value: Number, index: Number) -> String)?)?

    /**
     * The index of the thumb which corresponds to the index of its value in the
     * `value` or `defaultValue` array.
     * This prop is required to support server-side rendering for range sliders
     * with multiple thumbs.
     * @example
     * ```tsx
     * <Slider.Root value={[10, 20]}>
     *   <Slider.Thumb index={0} />
     *   <Slider.Thumb index={1} />
     * </Slider.Root>
     * ```
     */
    var index: Number?

    /**
     * A ref to access the nested input element.
     */
    var inputRef: Ref<HTMLInputElement>?

    /**
     * A blur handler forwarded to the `input`.
     */
    var onBlur: FocusEventHandler<HTMLInputElement>?

    /**
     * A focus handler forwarded to the `input`.
     */
    var onFocus: FocusEventHandler<HTMLInputElement>?

    /**
     * A keydown handler forwarded to the `input`.
     */
    var onKeyDown: KeyboardEventHandler<HTMLInputElement>?

    /**
     * Optional tab index attribute forwarded to the `input`.
     */
    var tabIndex: Int?
}

external interface ThumbMetadata {
    var inputId: Any /* LabelableContext['controlId'] */
}

external interface SliderThumbState : SliderRootState
