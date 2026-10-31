package playground.samples.system

import mui.material.Box
import mui.system.Breakpoint.Companion.md
import mui.system.Breakpoint.Companion.xs
import mui.system.responsive
import mui.system.sx
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.p
import web.cssom.Display
import web.cssom.NamedColor
import web.cssom.None.Companion.none
import web.cssom.px

val ResponsiveSample: Sample = Sample(
    id = "responsive",
    library = Libraries.System,
    title = "Responsive values",
    kind = Kind.Showcase,
    covers = listOf("responsive(vararg Pair<Breakpoint, T>)", "Breakpoint.xs/md", "sx.display"),
    fileName = "ResponsiveSample.kt",
    notes = "A breakpoint map passed through `responsive(...)` must reach `sx` as a plain record: the three boxes " +
            "below are hidden below `md` (900px) and laid out as a grid from `md` up.",
    content = FC<Props> {
        p { +"display: none below md (900px), grid from md up — narrow the window to hide the boxes." }

        Box {
            sx {
                display = responsive(
                    xs to none,
                    md to Display.grid,
                )
                gap = 8.px
            }

            for (index in 1..3) {
                Box {
                    key = Key("box-$index")
                    sx {
                        padding = 8.px
                        backgroundColor = NamedColor.lavender
                    }
                    +"Box $index"
                }
            }
        }
    },
)
