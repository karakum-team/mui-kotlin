import emotion.styled.styled
import mui.base.Slider
import mui.base.sliderClasses
import react.FC
import react.Props
import react.dom.events.KeyboardEvent
import web.cssom.*
import web.cssom.LineStyle.Companion.solid
import web.cssom.None.Companion.none

val SliderStylization = FC<Props> {
    RangeSliderBase {
        disableSwap = true

        min = 0.0
        max = 100.0

        value = 50.0

        onKeyDown = KeyboardEvent<*>::preventDefault
    }
}

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
        backgroundColor = Color.currentcolor
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
