package playground.samples.icons

import mui.icons.material.Abc
import mui.icons.material.SouthEast
import mui.material.SvgIcon
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props
import react.dom.svg.ReactSVG.path

val IconsSample: Sample = Sample(
    id = "icons",
    library = Libraries.Icons,
    title = "Icons",
    kind = Kind.Showcase,
    covers = listOf("SvgIcon + path", "Abc", "SouthEast"),
    fileName = "IconsSample.kt",
    content = FC<Props> {
        SvgIcon {
            path {
                d = "M9 16.17 4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
            }
        }

        Abc()
        SouthEast()
    },
)
