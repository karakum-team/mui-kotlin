package playground.samples.material

import mui.material.SvgIcon
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props
import react.dom.svg.ReactSVG.path

val SvgIconSample: Sample = Sample(
    id = "svg-icon",
    library = Libraries.Material,
    title = "SvgIcon",
    kind = Kind.Showcase,
    covers = listOf("SvgIcon", "ReactSVG.path.d"),
    fileName = "SvgIconSample.kt",
    notes = "A hand-drawn `path` child renders inside `SvgIcon`'s 24×24 viewBox as a check mark.",
    content = FC<Props> {
        SvgIcon {
            path {
                d = "M9 16.17 4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
            }
        }
    },
)
