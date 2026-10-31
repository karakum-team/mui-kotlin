import mui.icons.material.Abc
import mui.icons.material.SouthEast
import mui.material.SvgIcon
import react.FC
import react.Props
import react.dom.svg.ReactSVG.path

val AppIcons = FC<Props> {
    SvgIcon {
        path {
            d = "M9 16.17 4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
        }
    }

    Abc()
    SouthEast()
}
