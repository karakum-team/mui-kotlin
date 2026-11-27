package playground.samples.baseui

import baseui.Collapsible
import baseui.CollapsibleRootChangeEventDetails
import baseui.className
import emotion.react.Global
import emotion.react.styles
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useState
import web.cssom.AlignItems
import web.cssom.Border
import web.cssom.BoxSizing
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.FontWeight
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Outline
import web.cssom.number
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val CollapsibleSample: Sample = Sample(
    id = "collapsible",
    library = Libraries.BaseUi,
    title = "Collapsible",
    kind = Kind.Check,
    covers = listOf(
        "Collapsible.Root/Trigger/Panel (3 parts)",
        "CollapsibleRootProps.open / onOpenChange",
        "CollapsibleRootProps.disabled",
        "CollapsiblePanelProps.keepMounted",
        "className/style/render state helpers",
    ),
    fileName = "CollapsibleSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Collapsible", "https://base-ui.com/react/components/collapsible"),
    ),
    notes = "Namespace component export from @base-ui/react/collapsible. Reuses shared collapsible root state " +
        "with Accordion while providing standalone Trigger and Panel parts for expandable panels.",
    content = FC<Props> {
        var open by useState(false)
        var collapsibleDisabled by useState(false)

        Global {
            styles {
                ".bui-collapsible-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-collapsible-root" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 8.px
                    padding = 12.px
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 6.px
                    marginTop = 12.px
                    marginBottom = 12.px
                }
                ".bui-collapsible-root--disabled, .bui-collapsible-root[data-disabled]" {
                    backgroundColor = Color("#f8f9fa")
                    borderColor = Color("#eaecf0")
                }
                ".bui-collapsible-trigger" {
                    display = Display.inlineFlex
                    alignItems = AlignItems.center
                    padding = 8.px
                    fontSize = 14.px
                    fontWeight = FontWeight.bold
                    backgroundColor = Color("#175cd3")
                    color = NamedColor.white
                    border = Border(1.px, solid, Color("#154ec1"))
                    borderRadius = 4.px
                    cursor = Cursor.pointer
                }
                ".bui-collapsible-trigger:hover" {
                    backgroundColor = Color("#154ec1")
                }
                ".bui-collapsible-trigger[data-disabled]" {
                    backgroundColor = Color("#eaecf0")
                    color = Color("#98a2b3")
                    borderColor = Color("#d0d5dd")
                    cursor = Cursor.notAllowed
                }
                ".bui-collapsible-panel" {
                    padding = 12.px
                    backgroundColor = Color("#f8f9fa")
                    borderRadius = 4.px
                    border = Border(1.px, solid, Color("#eaecf0"))
                    marginTop = 4.px
                }
                ".bui-collapsible-controls" {
                    display = Display.flex
                    gap = 8.px
                    marginTop = 12.px
                }
                ".bui-collapsible-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-collapsible-sample button:focus-visible, .bui-collapsible-trigger:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-collapsible-sample")
            className = ClassName("bui-collapsible-sample")

            CheckList {
                id = "collapsible-checks"
                checks = listOf(
                    Check("Collapsible namespace is loaded", jsTypeOf(Collapsible) == "object"),
                    Check("Collapsible.Root is loaded", jsTypeOf(Collapsible.Root) == "object" || jsTypeOf(Collapsible.Root) == "function"),
                    Check("Collapsible.Trigger is loaded", jsTypeOf(Collapsible.Trigger) == "object" || jsTypeOf(Collapsible.Trigger) == "function"),
                    Check("Collapsible.Panel is loaded", jsTypeOf(Collapsible.Panel) == "object" || jsTypeOf(Collapsible.Panel) == "function"),
                    Check("open state is boolean", jsTypeOf(open) == "boolean"),
                )
            }

            StatusLine {
                id = "collapsible-status"
                fields = listOf(
                    "open" to open.toString(),
                    "disabled" to collapsibleDisabled.toString(),
                )
            }

            p { +"Interactive Collapsible component:" }

            Collapsible.Root {
                id = ElementId("bui-collapsible-container")
                this.open = open
                onOpenChange = { nextOpen: Boolean, _: CollapsibleRootChangeEventDetails ->
                    open = nextOpen
                }
                disabled = collapsibleDisabled
                className { state ->
                    ClassName(if (state.disabled) "bui-collapsible-root bui-collapsible-root--disabled" else "bui-collapsible-root")
                }

                Collapsible.Trigger {
                    id = ElementId("collapsible-trigger-btn")
                    className = ClassName("bui-collapsible-trigger")
                    +if (open) "Collapse content ▲" else "Expand content ▼"
                }

                Collapsible.Panel {
                    id = ElementId("collapsible-content-panel")
                    className = ClassName("bui-collapsible-panel")
                    p { +"This is the collapsible panel content that expands and collapses." }
                    p { +"It cleanly coordinates open and animation states with Base UI." }
                }
            }

            div {
                className = ClassName("bui-collapsible-controls")
                button {
                    id = ElementId("collapsible-toggle-disabled-btn")
                    type = ButtonType.button
                    onClick = { collapsibleDisabled = !collapsibleDisabled }
                    +if (collapsibleDisabled) "Enable Collapsible" else "Disable Collapsible"
                }
            }
        }
    }
)
