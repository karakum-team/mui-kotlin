package playground.samples.baseui

import baseui.Orientation
import baseui.Toolbar
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
import web.cssom.None.Companion.none
import web.cssom.Outline
import web.cssom.number
import web.cssom.pct
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val ToolbarSample: Sample = Sample(
    id = "toolbar",
    library = Libraries.BaseUi,
    title = "Toolbar",
    kind = Kind.Check,
    covers = listOf(
        "Toolbar.Root/Button/Input/Link/Group/Separator (6 parts)",
        "ToolbarSeparator reusing SeparatorProps / SeparatorState",
        "Orientation (horizontal / vertical)",
        "ToolbarRootProps.disabled / loopFocus",
        "className/style/render state helpers",
    ),
    fileName = "ToolbarSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Toolbar", "https://base-ui.com/react/components/toolbar"),
    ),
    notes = "Namespace component export from @base-ui/react/toolbar. Accessible container for grouping " +
        "toolbar buttons, inputs, links, and separators with keyboard navigation.",
    content = FC<Props> {
        var orientation by useState(Orientation.horizontal)
        var toolbarDisabled by useState(false)
        var activeAction by useState("none")

        Global {
            styles {
                ".bui-toolbar-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-toolbar-root" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 8.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 6.px
                    backgroundColor = Color("#f8f9fa")
                    marginTop = 12.px
                    marginBottom = 12.px
                }
                ".bui-toolbar-root--vertical" {
                    flexDirection = FlexDirection.column
                    alignItems = AlignItems.stretch
                    width = 160.px
                }
                ".bui-toolbar-btn" {
                    padding = 6.px
                    fontSize = 14.px
                    fontWeight = FontWeight.bold
                    color = Color("#344054")
                    backgroundColor = NamedColor.white
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 4.px
                    cursor = Cursor.pointer
                }
                ".bui-toolbar-btn:hover" {
                    backgroundColor = Color("#f2f4f7")
                }
                ".bui-toolbar-btn[data-disabled]" {
                    color = Color("#98a2b3")
                    backgroundColor = Color("#eaecf0")
                    borderColor = Color("#eaecf0")
                    cursor = Cursor.notAllowed
                }
                ".bui-toolbar-input" {
                    padding = 6.px
                    fontSize = 14.px
                    width = 110.px
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 4.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                }
                ".bui-toolbar-input[data-disabled]" {
                    backgroundColor = Color("#eaecf0")
                    color = Color("#98a2b3")
                    cursor = Cursor.notAllowed
                }
                ".bui-toolbar-link" {
                    fontSize = 14.px
                    color = Color("#175cd3")
                    textDecoration = none
                    padding = 6.px
                }
                ".bui-toolbar-sep" {
                    backgroundColor = Color("#d0d5dd")
                    width = 1.px
                    height = 24.px
                }
                ".bui-toolbar-sep--horizontal" {
                    width = 100.pct
                    height = 1.px
                }
                ".bui-toolbar-controls" {
                    display = Display.flex
                    gap = 8.px
                    marginTop = 12.px
                }
                ".bui-toolbar-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-toolbar-sample button:focus-visible, .bui-toolbar-btn:focus-visible, .bui-toolbar-input:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-toolbar-sample")
            className = ClassName("bui-toolbar-sample")

            CheckList {
                id = "toolbar-checks"
                checks = listOf(
                    Check("Toolbar namespace is loaded", jsTypeOf(Toolbar) == "object"),
                    Check("Toolbar.Root is loaded", jsTypeOf(Toolbar.Root) == "object" || jsTypeOf(Toolbar.Root) == "function"),
                    Check("Toolbar.Button is loaded", jsTypeOf(Toolbar.Button) == "object" || jsTypeOf(Toolbar.Button) == "function"),
                    Check("Toolbar.Input is loaded", jsTypeOf(Toolbar.Input) == "object" || jsTypeOf(Toolbar.Input) == "function"),
                    Check("Toolbar.Link is loaded", jsTypeOf(Toolbar.Link) == "object" || jsTypeOf(Toolbar.Link) == "function"),
                    Check("Toolbar.Separator is loaded", jsTypeOf(Toolbar.Separator) == "object" || jsTypeOf(Toolbar.Separator) == "function"),
                )
            }

            StatusLine {
                id = "toolbar-status"
                fields = listOf(
                    "orientation" to if (orientation == Orientation.horizontal) "horizontal" else "vertical",
                    "disabled" to toolbarDisabled.toString(),
                    "activeAction" to activeAction,
                )
            }

            p { +"Interactive Toolbar with Buttons, Separator, Input, and Link:" }

            Toolbar.Root {
                id = ElementId("bui-toolbar-container")
                this.orientation = orientation
                disabled = toolbarDisabled
                loopFocus = true
                className { state ->
                    ClassName(if (state.orientation == Orientation.vertical) "bui-toolbar-root bui-toolbar-root--vertical" else "bui-toolbar-root")
                }

                Toolbar.Button {
                    id = ElementId("toolbar-bold-btn")
                    className = ClassName("bui-toolbar-btn")
                    onClick = { activeAction = "bold" }
                    +"Bold"
                }

                Toolbar.Button {
                    id = ElementId("toolbar-italic-btn")
                    className = ClassName("bui-toolbar-btn")
                    onClick = { activeAction = "italic" }
                    +"Italic"
                }

                Toolbar.Separator {
                    id = ElementId("toolbar-sep-1")
                    className { state ->
                        ClassName(if (state.orientation == Orientation.horizontal) "bui-toolbar-sep bui-toolbar-sep--horizontal" else "bui-toolbar-sep")
                    }
                }

                Toolbar.Input {
                    id = ElementId("toolbar-search-input")
                    className = ClassName("bui-toolbar-input")
                    placeholder = "Filter..."
                    defaultValue = ""
                }

                Toolbar.Separator {
                    id = ElementId("toolbar-sep-2")
                    className = ClassName("bui-toolbar-sep")
                }

                Toolbar.Link {
                    id = ElementId("toolbar-docs-link")
                    className = ClassName("bui-toolbar-link")
                    href = "https://base-ui.com"
                    +"Docs ↗"
                }
            }

            div {
                className = ClassName("bui-toolbar-controls")
                button {
                    id = ElementId("toolbar-toggle-orientation-btn")
                    type = ButtonType.button
                    onClick = {
                        orientation = if (orientation == Orientation.horizontal) Orientation.vertical else Orientation.horizontal
                    }
                    +"Toggle orientation"
                }
                button {
                    id = ElementId("toolbar-toggle-disabled-btn")
                    type = ButtonType.button
                    onClick = { toolbarDisabled = !toolbarDisabled }
                    +if (toolbarDisabled) "Enable Toolbar" else "Disable Toolbar"
                }
            }
        }
    }
)
