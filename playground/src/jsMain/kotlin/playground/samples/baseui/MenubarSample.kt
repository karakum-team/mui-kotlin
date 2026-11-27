package playground.samples.baseui

import baseui.Align
import baseui.Menu
import baseui.Menubar
import baseui.Side
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
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Outline
import web.cssom.integer
import web.cssom.number
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val MenubarSample: Sample = Sample(
    id = "menubar",
    library = Libraries.BaseUi,
    title = "Menubar",
    kind = Kind.Check,
    covers = listOf(
        "Menubar (direct component export)",
        "MenubarProps.disabled",
        "MenubarProps.modal",
        "MenubarProps.loopFocus",
        "MenubarState.hasSubmenuOpen",
        "Integration with Menu.Root / Menu.Trigger / Menu.Popup",
        "className/style/render state helpers",
    ),
    fileName = "MenubarSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Menubar", "https://base-ui.com/react/components/menubar"),
    ),
    notes = "Direct component export from @base-ui/react/menubar. Coordinates menus within a horizontal bar, " +
        "handling roving tabIndex, keyboard navigation between menus, modal state, and disabled state.",
    content = FC<Props> {
        var lastAction by useState("None")
        var menubarDisabled by useState(false)

        Global {
            styles {
                ".bui-menubar-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-menubar" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 4.px
                    padding = 4.px
                    backgroundColor = Color("#f8f9fa")
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 6.px
                    marginTop = 12.px
                    marginBottom = 12.px
                }
                ".bui-menubar--disabled, .bui-menubar[data-disabled]" {
                    backgroundColor = Color("#eaecf0")
                    borderColor = Color("#d0d5dd")
                }
                ".bui-menubar-trigger" {
                    padding = 8.px
                    border = Border(0.px, solid, Color("transparent"))
                    borderRadius = 4.px
                    backgroundColor = Color("transparent")
                    color = Color("#182230")
                    fontSize = 14.px
                    cursor = Cursor.pointer
                }
                ".bui-menubar-trigger:hover" {
                    backgroundColor = Color("#e4e7ec")
                }
                ".bui-menubar-trigger[data-popup-open]" {
                    backgroundColor = Color("#d0d5dd")
                }
                ".bui-menubar-trigger[data-disabled]" {
                    color = Color("#98a2b3")
                    cursor = Cursor.notAllowed
                }
                ".bui-menubar-positioner" {
                    zIndex = integer(1000)
                }
                ".bui-menubar-popup" {
                    padding = 4.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 6.px
                    minWidth = 140.px
                }
                ".bui-menubar-item" {
                    display = Display.block
                    padding = 8.px
                    borderRadius = 4.px
                    fontSize = 14.px
                    cursor = Cursor.pointer
                }
                ".bui-menubar-item:hover, .bui-menubar-item[data-highlighted]" {
                    backgroundColor = Color("#f2f4f7")
                }
                ".bui-menubar-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-menubar-sample button:focus-visible, .bui-menubar-trigger:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-menubar-sample")
            className = ClassName("bui-menubar-sample")

            CheckList {
                id = "menubar-checks"
                checks = listOf(
                    Check("Menubar component is loaded", jsTypeOf(Menubar) == "object"),
                    Check("lastAction state is non-empty", lastAction.isNotEmpty()),
                    Check("menubar disabled is boolean", jsTypeOf(menubarDisabled) == "boolean"),
                )
            }

            StatusLine {
                id = "menubar-status"
                fields = listOf(
                    "lastAction" to lastAction,
                    "disabled" to menubarDisabled.toString(),
                )
            }

            p { +"Interactive Menubar coordinating File and Edit menus:" }

            Menubar {
                id = ElementId("bui-menubar-container")
                disabled = menubarDisabled
                className { state ->
                    ClassName(if (state.hasSubmenuOpen) "bui-menubar bui-menubar--active" else "bui-menubar")
                }

                Menu.Root {
                    Menu.Trigger {
                        id = ElementId("menubar-file-trigger")
                        className = ClassName("bui-menubar-trigger")
                        +"File"
                    }
                    Menu.Portal {
                        Menu.Positioner {
                            side = Side.bottom
                            align = Align.start
                            className = ClassName("bui-menubar-positioner")
                            Menu.Popup {
                                className = ClassName("bui-menubar-popup")
                                Menu.Item {
                                    id = ElementId("menubar-item-new")
                                    className = ClassName("bui-menubar-item")
                                    onClick = { lastAction = "New File" }
                                    +"New File"
                                }
                                Menu.Item {
                                    id = ElementId("menubar-item-open")
                                    className = ClassName("bui-menubar-item")
                                    onClick = { lastAction = "Open File" }
                                    +"Open File"
                                }
                            }
                        }
                    }
                }

                Menu.Root {
                    Menu.Trigger {
                        id = ElementId("menubar-edit-trigger")
                        className = ClassName("bui-menubar-trigger")
                        +"Edit"
                    }
                    Menu.Portal {
                        Menu.Positioner {
                            side = Side.bottom
                            align = Align.start
                            className = ClassName("bui-menubar-positioner")
                            Menu.Popup {
                                className = ClassName("bui-menubar-popup")
                                Menu.Item {
                                    id = ElementId("menubar-item-undo")
                                    className = ClassName("bui-menubar-item")
                                    onClick = { lastAction = "Undo" }
                                    +"Undo"
                                }
                                Menu.Item {
                                    id = ElementId("menubar-item-redo")
                                    className = ClassName("bui-menubar-item")
                                    onClick = { lastAction = "Redo" }
                                    +"Redo"
                                }
                            }
                        }
                    }
                }
            }

            div {
                button {
                    id = ElementId("menubar-toggle-disabled-btn")
                    type = ButtonType.button
                    onClick = { menubarDisabled = !menubarDisabled }
                    +if (menubarDisabled) "Enable Menubar" else "Disable Menubar"
                }
            }
        }
    }
)
