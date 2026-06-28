package karakum.mui.adapters

// v7 parameterizes the Slider props by the value type:
//   export interface SliderOwnProps<Value extends number | readonly number[]> { … }
// The generator drops that type parameter from the emitted `SliderOwnProps`/`SliderProps`, so the
// members that reference bare `Value` (`defaultValue`, `value`, the `onChange`/`onChangeCommitted`
// callbacks) become unresolved. `Value` is constrained to `number | readonly number[]`, so collapse
// it back to that concrete union — semantically the wrapper-level type anyway.
//
// Guarded on the Slider-specific interface header so the broad `Value` token is only touched inside
// `Slider.d.ts` (it is also a generic parameter on Select/Option, which must NOT be rewritten).
fun String.adaptSlider(): String {
    if ("export interface SliderOwnProps<Value extends number | readonly number[]>" !in this)
        return this

    return replace(": Value | undefined", ": number | number[] | undefined") // defaultValue, value
        .replace("value: Value", "value: number | number[]") // onChange / onChangeCommitted callbacks
}
