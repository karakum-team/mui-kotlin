package playground.samples.material

import js.objects.unsafeJso
import mui.material.Grid
import mui.material.GridOffset
import mui.material.GridSize
import mui.system.responsive
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.CSSProperties
import react.FC
import react.Props
import react.useEffectOnce
import react.useState
import web.cssom.ClassName
import web.cssom.getComputedStyle
import web.cssom.LineStyle
import web.cssom.NamedColor
import web.dom.ElementId
import web.dom.document
import mui.system.Grid as SystemGrid
import mui.system.GridOffset as SystemGridOffset
import mui.system.GridSize as SystemGridSize
import mui.system.Stack as SystemStack

private const val ITEM_ID = "grid-sample-item"
private const val ITEM_CLASS = "grid-sample-item-class"
private const val AUTO_ID = "grid-sample-auto"
private const val SYSTEM_GRID_ID = "grid-sample-system-grid"
private const val SYSTEM_STACK_ID = "grid-sample-system-stack"

val GridSample: Sample = Sample(
    id = "grid",
    library = Libraries.Material,
    title = "Grid attributes and offset",
    kind = Kind.Check,
    covers = listOf(
        "GridProps : HTMLAttributes<HTMLDivElement> (id / className / style / title)",
        "Grid.offset: ResponsiveStyleValue<GridOffset> (GridOffset(Number), GridOffset.auto)",
        "mui.system.Grid / Stack : HTMLAttributes<HTMLDivElement>",
    ),
    fileName = "GridSample.kt",
    notes = "Only running this proves that the root `<div>` attributes a `Grid` / system `Stack` now inherits reach " +
            "the element, and that `GridOffset(2)` / `GridOffset.auto` push the item right — a left margin appears.",
    content = FC<Props> {
        // Null until the first commit: the checks read the rendered DOM and computed styles.
        var checks by useState<List<Check>?>(null)

        useEffectOnce {
            val item = document.getElementById(ElementId(ITEM_ID))
            fun marginLeft(id: String): String? =
                document.getElementById(ElementId(id))?.let { getComputedStyle(it).marginLeft }

            checks = listOf(
                Check("Grid.id reaches the <div>", item?.tagName == "DIV"),
                Check("Grid.className is merged", item?.classList?.contains(ClassName(ITEM_CLASS)) == true),
                Check("Grid.title reaches the <div>", item?.getAttribute("title") == "Grid item"),
                Check("Grid.style reaches the <div>", "${item?.let { getComputedStyle(it).outlineStyle }}" == "dashed"),
                Check("GridOffset(2) adds a left margin", marginLeft(ITEM_ID).let { it != null && it != "0px" }),
                Check("GridOffset.auto adds a left margin", marginLeft(AUTO_ID).let { it != null && it != "0px" }),
                Check("mui.system.Grid.title reaches the <div>", document.getElementById(ElementId(SYSTEM_GRID_ID))?.getAttribute("title") == "System Grid"),
                Check("mui.system.Stack.title reaches the <div>", document.getElementById(ElementId(SYSTEM_STACK_ID))?.getAttribute("title") == "System Stack"),
            )
        }

        Grid {
            container = true

            Grid {
                id = ElementId(ITEM_ID)
                className = ClassName(ITEM_CLASS)
                title = "Grid item"
                style = unsafeJso<CSSProperties> {
                    outlineStyle = LineStyle.dashed
                    outlineColor = NamedColor.gray
                }
                size = responsive(GridSize(4))
                offset = responsive(GridOffset(2))
                +"size 4, offset 2"
            }
        }

        Grid {
            container = true

            Grid {
                id = ElementId(AUTO_ID)
                size = responsive(GridSize(4))
                offset = responsive(GridOffset.auto)
                +"size 4, offset auto"
            }
        }

        SystemGrid {
            container = true

            SystemGrid {
                id = ElementId(SYSTEM_GRID_ID)
                title = "System Grid"
                size = responsive(SystemGridSize(6))
                offset = responsive(SystemGridOffset(3))
                +"mui.system.Grid"
            }
        }

        SystemStack {
            id = ElementId(SYSTEM_STACK_ID)
            title = "System Stack"
            +"mui.system.Stack"
        }

        checks?.let {
            CheckList {
                id = "grid-checks"
                this.checks = it
            }
        }
    },
)
