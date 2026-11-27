package playground.samples.baseui

import baseui.Orientation
import baseui.Tabs
import baseui.TabsRootChangeEventDetails
import baseui.TabsTabActivationDirection
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
import web.cssom.Position
import web.cssom.number
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val TabsSample: Sample = Sample(
    id = "tabs",
    library = Libraries.BaseUi,
    title = "Tabs",
    kind = Kind.Check,
    covers = listOf(
        "Tabs.Root/List/Tab/Panel/Indicator (5 parts)",
        "TabsRootProps.orientation / onValueChange",
        "TabsTabActivationDirection (left/right/up/down/none)",
        "Tabs.Indicator moving indicator",
        "className/style/render state helpers",
    ),
    fileName = "TabsSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Tabs", "https://base-ui.com/react/components/tabs"),
    ),
    notes = "Namespace component export from @base-ui/react/tabs. Coordinates interactive tabs and tab panels, " +
        "supporting horizontal and vertical orientation, roving keyboard focus, animated indicators, and tab activation direction.",
    content = FC<Props> {
        var activeTab by useState<Any?>("overview")
        var orientation by useState(Orientation.horizontal)
        var settingsDisabled by useState(false)

        Global {
            styles {
                ".bui-tabs-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-tabs-root" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 12.px
                    marginTop = 12.px
                    marginBottom = 12.px
                }
                ".bui-tabs-root--vertical" {
                    flexDirection = FlexDirection.row
                }
                ".bui-tabs-list" {
                    display = Display.flex
                    gap = 4.px
                    borderBottom = Border(1.px, solid, Color("#d0d5dd"))
                    position = Position.relative
                }
                ".bui-tabs-list--vertical" {
                    flexDirection = FlexDirection.column
                    borderBottom = Border(0.px, solid, Color("transparent"))
                    borderRight = Border(1.px, solid, Color("#d0d5dd"))
                    minWidth = 120.px
                }
                ".bui-tabs-tab" {
                    padding = 8.px
                    fontSize = 14.px
                    backgroundColor = Color("transparent")
                    border = Border(0.px, solid, Color("transparent"))
                    borderRadius = 4.px
                    color = Color("#475467")
                    cursor = Cursor.pointer
                }
                ".bui-tabs-tab:hover" {
                    color = Color("#182230")
                    backgroundColor = Color("#f2f4f7")
                }
                ".bui-tabs-tab[data-selected]" {
                    color = Color("#175cd3")
                    fontWeight = FontWeight.bold
                }
                ".bui-tabs-tab[data-disabled]" {
                    color = Color("#98a2b3")
                    cursor = Cursor.notAllowed
                }
                ".bui-tabs-indicator" {
                    position = Position.absolute
                    bottom = (-1).px
                    height = 2.px
                    backgroundColor = Color("#175cd3")
                }
                ".bui-tabs-panel" {
                    padding = 12.px
                    backgroundColor = Color("#f8f9fa")
                    borderRadius = 6.px
                    border = Border(1.px, solid, Color("#eaecf0"))
                    minHeight = 60.px
                }
                ".bui-tabs-controls" {
                    display = Display.flex
                    gap = 8.px
                    marginTop = 12.px
                }
                ".bui-tabs-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-tabs-sample button:focus-visible, .bui-tabs-tab:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-tabs-sample")
            className = ClassName("bui-tabs-sample")

            CheckList {
                id = "tabs-checks"
                checks = listOf(
                    Check("Tabs namespace is loaded", jsTypeOf(Tabs) == "object"),
                    Check("Tabs.Root is loaded", jsTypeOf(Tabs.Root) == "object" || jsTypeOf(Tabs.Root) == "function"),
                    Check("Tabs.List is loaded", jsTypeOf(Tabs.List) == "object" || jsTypeOf(Tabs.List) == "function"),
                    Check("Tabs.Tab is loaded", jsTypeOf(Tabs.Tab) == "object" || jsTypeOf(Tabs.Tab) == "function"),
                    Check("Tabs.Panel is loaded", jsTypeOf(Tabs.Panel) == "object" || jsTypeOf(Tabs.Panel) == "function"),
                    Check("TabsTabActivationDirection values distinct", TabsTabActivationDirection.left != TabsTabActivationDirection.right),
                )
            }

            StatusLine {
                id = "tabs-status"
                fields = listOf(
                    "activeTab" to (activeTab?.toString() ?: "none"),
                    "orientation" to if (orientation == Orientation.horizontal) "horizontal" else "vertical",
                    "settingsDisabled" to settingsDisabled.toString(),
                )
            }

            p { +"Interactive Tabs with List, Tab, Panel, and Indicator:" }

            Tabs.Root {
                id = ElementId("bui-tabs-container")
                value = activeTab
                this.orientation = orientation
                onValueChange = { next, _: TabsRootChangeEventDetails ->
                    activeTab = next
                }
                className { state ->
                    ClassName(if (state.orientation == Orientation.vertical) "bui-tabs-root bui-tabs-root--vertical" else "bui-tabs-root")
                }

                Tabs.List {
                    id = ElementId("tabs-list")
                    className { state ->
                        ClassName(if (state.orientation == Orientation.vertical) "bui-tabs-list bui-tabs-list--vertical" else "bui-tabs-list")
                    }

                    Tabs.Tab {
                        id = ElementId("tab-overview")
                        value = "overview"
                        className = ClassName("bui-tabs-tab")
                        +"Overview"
                    }

                    Tabs.Tab {
                        id = ElementId("tab-details")
                        value = "details"
                        className = ClassName("bui-tabs-tab")
                        +"Details"
                    }

                    Tabs.Tab {
                        id = ElementId("tab-settings")
                        value = "settings"
                        disabled = settingsDisabled
                        className = ClassName("bui-tabs-tab")
                        +"Settings"
                    }

                    Tabs.Indicator {
                        className = ClassName("bui-tabs-indicator")
                    }
                }

                Tabs.Panel {
                    id = ElementId("panel-overview")
                    value = "overview"
                    className = ClassName("bui-tabs-panel")
                    p { +"Overview panel content: Welcome to Base UI Tabs." }
                }

                Tabs.Panel {
                    id = ElementId("panel-details")
                    value = "details"
                    className = ClassName("bui-tabs-panel")
                    p { +"Details panel content: Technical details and specifications." }
                }

                Tabs.Panel {
                    id = ElementId("panel-settings")
                    value = "settings"
                    className = ClassName("bui-tabs-panel")
                    p { +"Settings panel content: User preferences and configuration." }
                }
            }

            div {
                className = ClassName("bui-tabs-controls")
                button {
                    id = ElementId("tabs-toggle-orientation-btn")
                    type = ButtonType.button
                    onClick = {
                        orientation = if (orientation == Orientation.horizontal) Orientation.vertical else Orientation.horizontal
                    }
                    +"Toggle orientation"
                }
                button {
                    id = ElementId("tabs-toggle-settings-disabled-btn")
                    type = ButtonType.button
                    onClick = {
                        settingsDisabled = !settingsDisabled
                    }
                    +if (settingsDisabled) "Enable Settings Tab" else "Disable Settings Tab"
                }
            }
        }
    }
)
