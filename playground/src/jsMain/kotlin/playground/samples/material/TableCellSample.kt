package playground.samples.material

import mui.material.Table
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableHead
import mui.material.TableRow
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.th

val TableCellSample: Sample = Sample(
    id = "table-cell",
    library = Libraries.Material,
    title = "TableCell",
    kind = Kind.Showcase,
    covers = listOf("Table", "TableHead", "TableBody", "TableRow", "TableCell.component"),
    fileName = "TableCellSample.kt",
    notes = "`TableCell.component` accepts intrinsic elements: the header row renders `<th>` cells and the body " +
            "row `<td>` cells.",
    content = FC<Props> {
        Table {
            TableHead {
                TableRow {
                    TableCell {
                        component = th
                        +"Library"
                    }
                    TableCell {
                        component = th
                        +"Kotlin package"
                    }
                }
            }
            TableBody {
                TableRow {
                    TableCell {
                        component = td
                        +"Material UI"
                    }
                    TableCell {
                        component = td
                        +"mui.material"
                    }
                }
            }
        }
    },
)
