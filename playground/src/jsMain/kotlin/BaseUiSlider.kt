import baseui.Orientation
import baseui.Slider
import baseui.SliderRootChangeEventDetails
import baseui.SliderRootCommitEventDetails
import baseui.className
import baseui.render
import baseui.style
import emotion.react.Global
import emotion.react.styles
import js.array.ReadonlyArray
import js.objects.unsafeJso
import react.CSSProperties
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.useState
import web.cssom.AlignItems
import web.cssom.Border
import web.cssom.Color
import web.cssom.ClassName
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.FontVariantNumeric
import web.cssom.JustifyContent
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Position
import web.cssom.integer
import web.cssom.number
import web.cssom.pct
import web.cssom.px

// Live check of the generated `baseui` slider declarations, and the only usage reference for them in
// the repository. Same contract as `BaseUiMenu`: every member of the `Slider` namespace object is
// mounted, so a key that stops existing at runtime takes the page down instead of failing at some
// user's call site, and the state-typed `className` / `style` / `render` helpers from `<Part>.ext.kt`
// are exercised at call sites, which is the only way to prove they resolve to the extension function
// rather than to the `Any?` property they shadow.
//
// Four things here can only be checked by running it, each of them a defect this module found:
//  - `Slider.Value`'s `children` is a *formatting callback*, not React children. Assigning a lambda
//    has to reach the redeclared `Any?` member rather than the `ReactNode?` one inherited from the
//    per-tag parent.
//  - `SliderRootState` extends `FieldRootState`, which lives in a module that is not generated as a
//    module. `state.dirty` below is that inheritance, and it is unreachable if the dependency is lost.
//  - `SliderRootChangeEventDetails` carries `activeThumbIndex` through a second type argument, and
//    `SliderRootCommitEventDetails` exists at all only because every `EventDetails` alias is converted.
//    Both are read in the handlers.
//  - The five `<Part>State` types that upstream declares with an empty body inherit `SliderRootState`;
//    `state.dragging` / `state.orientation` are those parents.
//
// Class names are literal and `bui-slider-` prefixed so they can be asserted against in the DOM.
//
// Running this logs one Base UI warning, and it is expected: Base UI rejects a `render` callback whose
// function name starts with a capital, on the assumption that it is a React component passed by mistake.
// Kotlin/JS names a lambda after the declaration enclosing it, and a component `val` is `PascalCase` by
// convention, so every Kotlin call site of the generated `render` helper trips the heuristic. The
// callback really is a plain render callback and calls no hooks. See BASE_UI_TODO.md.
val BaseUiSlider = FC<Props> {
    var volume by useState(40)
    var lastChange by useState("-")
    var lastCommit by useState("-")
    var lastRange by useState("-")

    Global {
        styles {
            // A sibling sample (`SliderStylization`) lays an absolutely positioned element over the whole
            // viewport, so anything interactive needs a stacking context of its own to stay draggable.
            ".bui-slider-status, .bui-slider-root" {
                position = Position.relative
                zIndex = integer(1)
            }

            ".bui-slider-root" {
                display = Display.flex
                flexDirection = FlexDirection.column
                width = 240.px
                marginBottom = 24.px
                fontSize = 13.px
            }

            ".bui-slider-label" { color = Color("#666666") }
            ".bui-slider-label--dragging" { color = Color("#111111") }

            ".bui-slider-value" { fontVariantNumeric = FontVariantNumeric.tabularNums }

            ".bui-slider-control" {
                display = Display.flex
                alignItems = AlignItems.center
                width = 100.pct
                height = 20.px
                cursor = Cursor.pointer
            }
            ".bui-slider-control--vertical" {
                width = 20.px
                height = 120.px
                justifyContent = JustifyContent.center
            }
            ".bui-slider-control--dirty" { cursor = Cursor.grab }

            ".bui-slider-track" {
                position = Position.relative
                width = 100.pct
                height = 4.px
                borderRadius = 2.px
                backgroundColor = Color("#dddddd")
            }
            ".bui-slider-track--vertical" {
                width = 4.px
                height = 100.pct
            }

            ".bui-slider-indicator" {
                position = Position.absolute
                borderRadius = 2.px
                backgroundColor = Color("#111111")
            }

            ".bui-slider-thumb" {
                width = 14.px
                height = 14.px
                borderRadius = 50.pct
                backgroundColor = NamedColor.white
                border = Border(2.px, solid, Color("#111111"))
            }
            ".bui-slider-thumb--dragging" { backgroundColor = Color("#0a7d00") }
        }
    }

    p {
        className = ClassName("bui-slider-status")
        +"volume=$volume change=$lastChange commit=$lastCommit range=$lastRange"
    }

    // Controlled, single thumb. Everything the module got back is read from here.
    Slider.Root {
        // Value arm of `className`: `SliderRootProps` has state helpers too, and this proves the plain
        // property is still assignable alongside them.
        className = ClassName("bui-slider-root")

        value = volume
        min = 0.0
        max = 100.0
        step = 5
        largeStep = 20
        orientation = Orientation.horizontal

        // `onValueChange` is `Any?` — the parameter's conditional type has no Kotlin form, so the whole
        // callback is widened and the parameter types are stated here instead. `activeThumbIndex` is the
        // custom-properties arm of `BaseUIChangeEventDetails<Reason, CustomProperties>`.
        onValueChange = { next: Any?, details: SliderRootChangeEventDetails ->
            volume = (next as Number).toInt()
            lastChange = "${details.reason}@${details.activeThumbIndex}"
        }

        // `SliderRootCommitEventDetails` is a `BaseUIGenericEventDetails`, which has `reason` and
        // `event` but no `cancel()` — a different base from the change details above.
        onValueCommitted = { _: Any?, details: SliderRootCommitEventDetails ->
            lastCommit = details.reason
        }

        Slider.Label {
            className { state ->
                if (state.dragging) ClassName("bui-slider-label bui-slider-label--dragging")
                else ClassName("bui-slider-label")
            }
            +"Volume"
        }

        Slider.Value {
            className = ClassName("bui-slider-value")

            // Not React children: Base UI calls this and renders what it returns, ignoring a plain node
            // entirely. Both parameters are read so a change to either arity would fail here.
            children = { formatted: ReadonlyArray<String>, values: ReadonlyArray<Double> ->
                "${formatted[0]} of 100 (raw ${values[0]})"
            }
        }

        Slider.Control {
            // `dirty` is declared by `FieldRootState`, two declarations up and in another module, and
            // reaching it at all is what
            // this line is for. It stays `false` at runtime outside a `Field.Root`, so the modifier
            // class is not expected to appear here.
            className { state ->
                if (state.dirty) ClassName("bui-slider-control bui-slider-control--dirty")
                else ClassName("bui-slider-control")
            }

            Slider.Track {
                className = ClassName("bui-slider-track")

                // Callback arm of `render`. `+props` is what applies the ref, the `data-*` state
                // attributes and the `className` above — drop it and the track renders unstyled and
                // zero-width, which is exactly how the omission shows up. No children in the builder,
                // since `+props` already carries them.
                render { props, _ -> div.create { +props } }

                Slider.Indicator {
                    className = ClassName("bui-slider-indicator")

                    // Callback arm of `style`, over the part's own state. Base UI's own inline sizing
                    // has to survive alongside it.
                    style { state ->
                        unsafeJso<CSSProperties> { opacity = if (state.dragging) number(1.0) else number(0.75) }
                    }
                }

                Slider.Thumb {
                    className { state ->
                        if (state.dragging) ClassName("bui-slider-thumb bui-slider-thumb--dragging")
                        else ClassName("bui-slider-thumb")
                    }
                    getAriaLabel = { _ -> "Volume" }
                    getAriaValueText = { formattedValue, _, _ -> "$formattedValue percent" }
                }
            }
        }
    }

    // Uncontrolled, vertical, two thumbs — the range shape, and the only place `index` matters.
    Slider.Root {
        className = ClassName("bui-slider-root")

        defaultValue = arrayOf(20, 70)
        min = 0.0
        max = 100.0
        minStepsBetweenValues = 10
        orientation = Orientation.vertical

        // `mui.system.Union` is `String`: the string-literal unions Base UI declares inline are not
        // sealed types yet, so the raw value is what a call site writes. See BASE_UI_TODO.md gap 8.
        thumbCollisionBehavior = "swap"

        // A range slider's value is a JS array, which reaches Kotlin as `Array<*>` through the widened
        // `Any?`. `toString()` on it would give `[...]`.
        onValueChange = { next: Any?, _: SliderRootChangeEventDetails ->
            lastRange = (next as Array<*>).joinToString(" – ")
        }

        Slider.Label {
            className = ClassName("bui-slider-label")
            +"Range"
        }

        Slider.Value {
            className = ClassName("bui-slider-value")
            children = { formatted: ReadonlyArray<String>, _: ReadonlyArray<Double> ->
                formatted.joinToString(" – ")
            }
        }

        Slider.Control {
            // `orientation` is read off the state rather than repeated: it is the seskar union assigned
            // to `Slider.Root` above, and reading it yields the bare JavaScript string.
            className { state ->
                ClassName("bui-slider-control bui-slider-control--${state.orientation}")
            }

            Slider.Track {
                className { state ->
                    ClassName("bui-slider-track bui-slider-track--${state.orientation}")
                }

                Slider.Indicator { className = ClassName("bui-slider-indicator") }

                Slider.Thumb {
                    index = 0
                    className = ClassName("bui-slider-thumb")
                    getAriaLabel = { index -> "Range start ($index)" }
                }
                Slider.Thumb {
                    index = 1
                    className = ClassName("bui-slider-thumb")
                    getAriaLabel = { index -> "Range end ($index)" }
                }
            }
        }
    }
}
