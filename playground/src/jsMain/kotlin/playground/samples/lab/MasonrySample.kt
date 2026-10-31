package playground.samples.lab

import mui.lab.Masonry
import mui.material.Paper
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
import web.cssom.px

// Masonry places each direct child separately, so tiles are rendered inline rather than through a wrapper FC.
private val tileHeights = listOf(60, 110, 80, 140, 70, 100, 90, 120)

val MasonrySample: Sample = Sample(
    id = "masonry",
    library = Libraries.Lab,
    title = "Masonry",
    kind = Kind.Showcase,
    covers = listOf("Masonry.columns (responsive map)", "Masonry.columns (single value)"),
    fileName = "MasonrySample.kt",
    notes = "The only usage of `mui.lab` in the playground. Both forms of `columns` must reach the component: " +
            "the first grid has 1 column below `md` and 4 from `md` up, the second always has 2.",
    content = FC<Props> {
        p { +"columns = responsive(xs to 1, md to 4)" }

        Masonry {
            @Suppress("INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
            columns = responsive(
                xs to 1,
                md to 4,
            )

            for ((index, tileHeight) in tileHeights.withIndex()) {
                Paper {
                    key = Key("tile-$index")
                    sx {
                        height = tileHeight.px
                        padding = 8.px
                    }
                    +"${index + 1}"
                }
            }
        }

        p { +"columns = responsive(2)" }

        Masonry {
            columns = responsive(2)

            for ((index, tileHeight) in tileHeights.withIndex()) {
                Paper {
                    key = Key("tile-$index")
                    sx {
                        height = tileHeight.px
                        padding = 8.px
                    }
                    +"${index + 1}"
                }
            }
        }
    },
)
