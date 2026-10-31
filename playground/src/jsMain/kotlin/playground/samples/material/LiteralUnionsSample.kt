package playground.samples.material

import mui.material.Grid
import mui.material.GridSize
import mui.material.Skeleton
import mui.material.SkeletonAnimation
import mui.material.SortDirection
import mui.material.Table
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableRow
import mui.material.skeletonClasses
import mui.system.Breakpoint.Companion.md
import mui.system.Breakpoint.Companion.xs
import mui.system.ResponsiveStyleValue
import mui.system.responsive
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.div
import react.useEffectOnce
import react.useState
import web.dom.ElementId
import web.dom.document

private const val SKELETON_ID = "literal-unions-skeleton"
private const val SORTED_CELL_ID = "literal-unions-sorted-cell"
private const val UNSORTED_CELL_ID = "literal-unions-unsorted-cell"

private class GridCase(
    val id: String,
    val size: ResponsiveStyleValue<GridSize>,
    val label: String,
    val expectedClasses: List<String>,
    val unexpectedClasses: List<String> = emptyList(),
)

private val GRID_CASES = listOf(
    GridCase("literal-unions-grid-number", responsive(GridSize(6)), "GridSize(6)", listOf("grid-xs-6")),
    GridCase("literal-unions-grid-auto", responsive(GridSize.auto), "GridSize.auto", listOf("grid-xs-auto")),
    GridCase("literal-unions-grid-grow", responsive(GridSize.grow), "GridSize.grow", listOf("grid-xs-grow")),
    GridCase(
        "literal-unions-grid-breakpoints",
        responsive(xs to GridSize(12), md to GridSize(4)),
        "xs to GridSize(12), md to GridSize(4)",
        listOf("grid-xs-12", "grid-md-4"),
    ),
    // `false` must reach MUI as the boolean: the string "false" would add a `grid-md-false` class.
    GridCase(
        "literal-unions-grid-false",
        responsive(xs to GridSize(12), md to GridSize.`false`),
        "xs to GridSize(12), md to GridSize.false",
        listOf("grid-xs-12"),
        listOf("grid-md-false"),
    ),
)

val LiteralUnionsSample: Sample = Sample(
    id = "literal-unions",
    library = Libraries.Material,
    title = "Number and boolean literal unions",
    kind = Kind.Check,
    covers = listOf(
        "Grid.size: ResponsiveStyleValue<GridSize>",
        "GridSize(Number) / GridSize.auto / grow / false",
        "TableCell.sortDirection: SortDirection.false / asc",
        "Skeleton.animation: SkeletonAnimation.false",
    ),
    fileName = "LiteralUnionsSample.kt",
    notes = "Only running this proves that boolean and number members of a literal union reach MUI as JS values " +
            "(`@JsRawValue`), not strings: `GridSize.false` adds no `grid-md-false` class, `SortDirection.false` leaves " +
            "the cell without `aria-sort` (the truthy string \"false\" would make it `descending`), and " +
            "`SkeletonAnimation.false` switches the animation off. `GridSize(6)` is the numeric size the enum used to lose.",
    content = FC<Props> {
        // Null until the first commit: the checks read the rendered DOM.
        var checks by useState<List<Check>?>(null)

        useEffectOnce {
            val gridChecks = GRID_CASES.map { case ->
                // `GridProps` carries no `id` (see MUI_V6_TODO.md), so each item sits alone in a wrapped container.
                val item = document.getElementById(ElementId(case.id))?.firstElementChild?.firstElementChild
                val classes = item?.className?.toString().orEmpty()
                Check(
                    "Grid size = ${case.label}: ${(case.expectedClasses + case.unexpectedClasses.map { "no $it" }).joinToString()}",
                    case.expectedClasses.all { classes.contains(it) } && case.unexpectedClasses.none { classes.contains(it) },
                )
            }
            val skeleton = document.getElementById(ElementId(SKELETON_ID))
            checks = gridChecks + listOf(
                Check(
                    "SortDirection.asc: aria-sort is ascending",
                    document.getElementById(ElementId(SORTED_CELL_ID))?.getAttribute("aria-sort") == "ascending",
                ),
                Check(
                    "SortDirection.false: no aria-sort",
                    document.getElementById(ElementId(UNSORTED_CELL_ID))?.hasAttribute("aria-sort") == false,
                ),
                Check(
                    "SkeletonAnimation.false: neither pulse nor wave",
                    skeleton != null &&
                            !skeleton.classList.contains(skeletonClasses.pulse) &&
                            !skeleton.classList.contains(skeletonClasses.wave),
                ),
            )
        }

        for (case in GRID_CASES) {
            div {
                key = Key(case.id)
                id = ElementId(case.id)

                Grid {
                    container = true

                    Grid {
                        size = case.size
                        +case.label
                    }
                }
            }
        }

        Table {
            TableBody {
                TableRow {
                    TableCell {
                        id = ElementId(SORTED_CELL_ID)
                        sortDirection = SortDirection.asc
                        +"Sorted ascending"
                    }
                    TableCell {
                        id = ElementId(UNSORTED_CELL_ID)
                        sortDirection = SortDirection.`false`
                        +"Not sorted"
                    }
                }
            }
        }

        Skeleton {
            id = ElementId(SKELETON_ID)
            animation = SkeletonAnimation.`false`
        }

        checks?.let {
            CheckList {
                id = "literal-unions-checks"
                this.checks = it
            }
        }
    },
)
