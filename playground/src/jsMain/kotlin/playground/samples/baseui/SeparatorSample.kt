package playground.samples.baseui

import baseui.Orientation
import baseui.Separator
import baseui.className
import baseui.render
import baseui.style
import emotion.react.Global
import emotion.react.styles
import js.objects.unsafeJso
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.span
import react.useState
import web.cssom.AlignItems
import web.cssom.Border
import web.cssom.BoxSizing
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.JustifyContent
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Outline
import web.cssom.number
import web.cssom.pct
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val SeparatorSample: Sample = Sample(
    id = "separator",
    library = Libraries.BaseUi,
    title = "Separator",
    kind = Kind.Check,
    covers = listOf(
        "Separator (direct component export)",
        "SeparatorProps.orientation (Orientation.horizontal / Orientation.vertical)",
        "className/style/render state helpers",
        "SeparatorState.orientation",
        "render prop with element replacement (hr)",
    ),
    fileName = "SeparatorSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Separator", "https://base-ui.com/react/components/separator"),
    ),
    notes = "Separator is a standalone direct component export from @base-ui/react/separator. " +
        "It renders an accessible separator with role='separator' and data-orientation matching " +
        "the orientation prop (horizontal by default, or vertical). " +
        "The sample tests both orientations, dynamic orientation toggle, state-dependent " +
        "className and style helpers, and custom element rendering via the render prop.",
    content = FC<Props> {
        var dynamicOrientation by useState(Orientation.horizontal)
        var toggleCount by useState(0)

        Global {
            styles {
                ".bui-separator-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-separator-section" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 8.px
                    marginTop = 16.px
                    marginBottom = 16.px
                }
                ".bui-separator-toolbar" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 12.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 6.px
                }
                ".bui-separator" {
                    backgroundColor = Color("#d0d5dd")
                    boxSizing = BoxSizing.borderBox
                }
                ".bui-separator--horizontal, .bui-separator[data-orientation='horizontal']" {
                    width = 100.pct
                    height = 1.px
                    margin = 0.px
                }
                ".bui-separator--vertical, .bui-separator[data-orientation='vertical']" {
                    width = 1.px
                    height = 24.px
                    margin = 0.px
                }
                ".bui-separator-dynamic-box" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    justifyContent = JustifyContent.center
                    height = 64.px
                    padding = 12.px
                    backgroundColor = Color("#f8f9fa")
                    borderRadius = 6.px
                    border = Border(1.px, solid, Color("#eaecf0"))
                }
                ".bui-separator-hr" {
                    border = Border(0.px, solid, Color("transparent"))
                    borderTop = Border(2.px, solid, Color("#175cd3"))
                    margin = 0.px
                    width = 100.pct
                }
                ".bui-separator-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-separator-sample button:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-separator-sample")
            className = ClassName("bui-separator-sample")

            CheckList {
                id = "separator-checks"
                checks = listOf(
                    Check("Separator component is loaded", jsTypeOf(Separator) == "object"),
                    Check("Orientation.horizontal is distinct from vertical", Orientation.horizontal != Orientation.vertical),
                    Check("dynamicOrientation initialized", dynamicOrientation == Orientation.horizontal || dynamicOrientation == Orientation.vertical),
                )
            }

            StatusLine {
                id = "separator-status"
                fields = listOf(
                    "orientation" to if (dynamicOrientation == Orientation.horizontal) "horizontal" else "vertical",
                    "toggles" to toggleCount.toString(),
                )
            }

            div {
                className = ClassName("bui-separator-section")
                p { +"1. Horizontal Separator (default orientation):" }
                p { +"Content above the horizontal separator" }
                Separator {
                    id = ElementId("separator-horizontal")
                    className { state ->
                        ClassName("bui-separator bui-separator--" + if (state.orientation == Orientation.horizontal) "horizontal" else "vertical")
                    }
                }
                p { +"Content below the horizontal separator" }
            }

            div {
                className = ClassName("bui-separator-section")
                p { +"2. Vertical Separator in a toolbar row:" }
                div {
                    className = ClassName("bui-separator-toolbar")
                    span { +"Item A" }
                    Separator {
                        id = ElementId("separator-vertical-1")
                        orientation = Orientation.vertical
                        className { state ->
                            ClassName("bui-separator bui-separator--" + if (state.orientation == Orientation.vertical) "vertical" else "horizontal")
                        }
                    }
                    span { +"Item B" }
                    Separator {
                        id = ElementId("separator-vertical-2")
                        orientation = Orientation.vertical
                        className { state ->
                            ClassName("bui-separator bui-separator--" + if (state.orientation == Orientation.vertical) "vertical" else "horizontal")
                        }
                    }
                    span { +"Item C" }
                }
            }

            div {
                className = ClassName("bui-separator-section")
                p { +"3. Interactive Dynamic Orientation Separator:" }
                button {
                    id = ElementId("separator-toggle-btn")
                    type = ButtonType.button
                    onClick = {
                        dynamicOrientation = if (dynamicOrientation == Orientation.horizontal) {
                            Orientation.vertical
                        } else {
                            Orientation.horizontal
                        }
                        toggleCount++
                    }
                    +"Toggle orientation"
                }
                div {
                    className = ClassName("bui-separator-dynamic-box")
                    Separator {
                        id = ElementId("separator-dynamic")
                        orientation = dynamicOrientation
                        className { state ->
                            ClassName("bui-separator bui-separator--" + if (state.orientation == Orientation.horizontal) "horizontal" else "vertical")
                        }
                        style { state ->
                            unsafeJso {
                                backgroundColor = if (state.orientation == Orientation.horizontal) Color("#175cd3") else Color("#7a5af8")
                            }
                        }
                    }
                }
            }

            div {
                className = ClassName("bui-separator-section")
                p { +"4. Custom element rendering via render prop helper (hr):" }
                Separator {
                    id = ElementId("separator-custom-hr")
                    orientation = Orientation.horizontal
                    render { props, _ ->
                        hr.create {
                            +props
                            className = ClassName("bui-separator-hr")
                        }
                    }
                }
            }
        }
    }
)
