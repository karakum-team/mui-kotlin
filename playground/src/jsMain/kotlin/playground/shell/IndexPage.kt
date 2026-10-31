package playground.shell

import playground.dataIndexRow
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import web.cssom.ClassName

val IndexPage = FC<Props> {
    h1 { +"Playground" }
    p {
        +"Verification stand for the generated declarations. Open a sample by its route, or "
        a {
            href = "#/all"
            +"#/all"
        }
        +" to mount every sample at once."
    }

    table {
        className = ClassName("pg-index")

        thead {
            tr {
                th { +"Library" }
                th { +"Sample" }
                th { +"Kind" }
                th { +"Covers" }
                th { +"Source path" }
            }
        }
        tbody {
            for (sample in samples) {
                tr {
                    key = Key(sample.key)
                    dataIndexRow = sample.key

                    td { +sample.library.title }
                    td {
                        a {
                            href = sample.hash
                            +sample.title
                        }
                    }
                    td { +sample.kind.name }
                    td { +sample.covers.joinToString(", ") }
                    td { code { +sample.sourcePath } }
                }
            }
        }
    }
}
