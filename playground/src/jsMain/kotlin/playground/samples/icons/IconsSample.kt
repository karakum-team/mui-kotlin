package playground.samples.icons

import mui.icons.material.Abc
import mui.icons.material.SouthEast
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.div

val IconsSample: Sample = Sample(
    id = "icons",
    library = Libraries.Icons,
    title = "Icons",
    kind = Kind.Showcase,
    covers = listOf("Abc", "SouthEast"),
    fileName = "IconsSample.kt",
    notes = "Icons are named exports of the `@mui/icons-material` root module; a renamed or missing export " +
            "is `undefined` at runtime and fails to render here, which kotlinc cannot see.",
    content = FC<Props> {
        div {
            Abc()
            code { +"Abc" }
        }
        div {
            SouthEast()
            code { +"SouthEast" }
        }
    },
)
