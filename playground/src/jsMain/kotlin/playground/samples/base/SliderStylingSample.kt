package playground.samples.base

import emotion.react.css
import emotion.styled.styled
import mui.base.Slider
import mui.base.sliderClasses
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import react.FC
import react.Props
import react.dom.events.KeyboardEvent
import react.dom.html.ReactHTML.div
import web.cssom.*
import web.cssom.LineStyle.Companion.solid
import web.cssom.None.Companion.none

val SliderStylingSample: Sample = Sample(
    id = "slider-styling",
    library = Libraries.Base,
    title = "Slider styling",
    kind = Kind.ClassesCoverage,
    covers = listOf(
        "Slider.disableSwap/min/max/value/onKeyDown",
        "Slider.styled",
        "sliderClasses.rail/track/thumb",
    ),
    fileName = "SliderStylingSample.kt",
    refs = listOf(
        Ref(
            "BASE_UI_TODO.md — Group 5 (retirement)",
            "https://github.com/karakum-team/mui-kotlin/blob/main/BASE_UI_TODO.md#group-5--final--retirement-remove-the-old-target-after-its-consumers-move",
        ),
        Ref("916e234d", "https://github.com/karakum-team/mui-kotlin/commit/916e234d"),
        Ref("adbb1c2c", "https://github.com/karakum-team/mui-kotlin/commit/adbb1c2c"),
    ),
    notes = "Only running this proves that the typed `sliderClasses` keys match the class names the legacy " +
            "`@mui/base` Slider renders: the rail, the hidden track and the 36px thumb (shrinking on hover) must " +
            "appear styled. It stays until the sample migrates to Base UI's Slider.",
    content = FC<Props> {
        // `RangeSliderBase` is absolutely positioned at 100% width and height, so this wrapper gives it a
        // positioned box of its own to fill, sized to the 36px thumb.
        div {
            css {
                position = Position.relative
                height = 36.px
            }

            RangeSliderBase {
                disableSwap = true

                min = 0.0
                max = 100.0

                value = 50.0

                onKeyDown = KeyboardEvent<*>::preventDefault
            }
        }
    },
)

private val RangeSliderBase = Slider.styled {
    position = Position.absolute
    top = 0.px
    width = 100.pct
    left = (-36 / 2).px
    height = 100.pct

    display = Display.inlineFlex
    alignItems = AlignItems.center
    cursor = Cursor.pointer
    touchAction = none

    sliderClasses.rail {
        display = Display.block
        position = Position.absolute
        width = 100.pct
        height = 4.px
        borderRadius = 6.px
        backgroundColor = Color.currentColor
        opacity = number(0.3)
    }

    sliderClasses.track {
        display = none
    }

    sliderClasses.thumb {
        display = Display.flex
        alignItems = AlignItems.center
        justifyContent = JustifyContent.center
        position = Position.absolute
        width = 36.px
        height = 36.px
        boxSizing = BoxSizing.borderBox
        borderRadius = 50.pct
        outline = Outline(0.px, solid)
        backgroundColor = Color("#007FFF")
        transitionProperty = many(PropertyName.boxShadow, PropertyName.transform)
        transitionTimingFunction = TransitionTimingFunction.ease
        transitionDuration = 120.ms
        transformOrigin = "center".unsafeCast<TransformOrigin>()
    }

    ":hover, :active" {
        sliderClasses.thumb {
            width = 24.px
            height = 24.px
            margin = ((36 - 24) / 2).px
            border = Border(2.px, solid, NamedColor.white)
        }
    }
}
