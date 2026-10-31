package playground.samples.material

import js.objects.unsafeJso
import kotlinx.coroutines.delay
import mui.material.SpeedDial
import mui.material.SpeedDialAction
import mui.material.SpeedDialActionSlotProps
import mui.material.SpeedDialIcon
import mui.material.Tooltip
import mui.material.TooltipPlacement
import mui.material.TooltipProps
import mui.material.speedDialActionClasses
import mui.material.tooltipClasses
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.useEffectOnce
import react.useState
import web.dom.ElementId
import web.dom.document

private const val TOOLTIP_ID = "tooltip-placement-tooltip"
// SpeedDial rewrites its actions' ids (`<ariaLabel>-action-N`), so the action is found inside this wrapper.
private const val DIAL_ID = "tooltip-placement-dial"

val TooltipPlacementSample: Sample = Sample(
    id = "tooltip-placement",
    library = Libraries.Material,
    title = "Tooltip placement",
    kind = Kind.Check,
    covers = listOf(
        "Tooltip.placement: TooltipPlacement",
        "SpeedDialActionSlotProps.tooltip.placement",
        "tooltipClasses.tooltipPlacementTop",
        "speedDialActionClasses.tooltipPlacementRight",
    ),
    fileName = "TooltipPlacementSample.kt",
    notes = "Only running this proves that a `TooltipPlacement` companion value reaches MUI as the placement " +
            "string: the open tooltip gets `tooltipPlacementTop`, and the SpeedDial action's static tooltip, " +
            "which derives its class from `slotProps.tooltip.placement`, gets `tooltipPlacementRight`.",
    content = FC<Props> {
        // Null until the poppers have mounted: the tooltip opens only once its anchor node is attached.
        var classes by useState<Pair<Boolean, Boolean>?>(null)

        useEffectOnce {
            delay(100)
            val tooltip = document.getElementById(ElementId(TOOLTIP_ID))
                ?.querySelector(".${tooltipClasses.tooltipPlacementTop}")
            val actionRight = document.getElementById(ElementId(DIAL_ID))
                ?.querySelector(".${speedDialActionClasses.staticTooltip}.${speedDialActionClasses.tooltipPlacementRight}")
            classes = (tooltip != null) to (actionRight != null)
        }

        Tooltip {
            id = ElementId(TOOLTIP_ID)
            open = true
            placement = TooltipPlacement.top
            title = ReactNode("top")
            children = span.create { +"Anchor of an always-open tooltip" }
        }

        div {
            id = ElementId(DIAL_ID)

            SpeedDial {
                ariaLabel = "Tooltip placement"
                open = true
                icon = SpeedDialIcon.create()

                SpeedDialAction {
                    icon = ReactNode("A")
                    slotProps = unsafeJso<SpeedDialActionSlotProps> {
                        tooltip = unsafeJso<TooltipProps> {
                            open = true
                            placement = TooltipPlacement.right
                            title = ReactNode("right")
                        }
                    }
                }
            }
        }

        classes?.let { (tooltipTop, actionRight) ->
            CheckList {
                id = "tooltip-placement-checks"
                checks = listOf(
                    Check("Tooltip placement = top → tooltipPlacementTop", tooltipTop),
                    Check("SpeedDialAction tooltip placement = right → tooltipPlacementRight", actionRight),
                )
            }
        }
    },
)
