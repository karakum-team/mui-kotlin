package playground.samples.baseui

import baseui.Button
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
import react.dom.html.ReactHTML.a
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
import web.cssom.None.Companion.none
import web.cssom.Outline
import web.cssom.number
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val ButtonSample: Sample = Sample(
    id = "button",
    library = Libraries.BaseUi,
    title = "Button",
    kind = Kind.Check,
    covers = listOf(
        "Button (direct component export)",
        "ButtonProps.focusableWhenDisabled",
        "NativeButtonProps.nativeButton",
        "ButtonState.disabled",
        "className/style/render state helpers",
        "render prop with element replacement (a)",
    ),
    fileName = "ButtonSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Button", "https://base-ui.com/react/components/button"),
    ),
    notes = "Direct component export from @base-ui/react/button. Renders an accessible button " +
        "with state-dependent className, style, and render helpers. Supports focusableWhenDisabled " +
        "for accessible disabled buttons and element swapping via render.",
    content = FC<Props> {
        var clicks by useState(0)
        var btnDisabled by useState(false)
        var focusableWhenDisabled by useState(true)

        Global {
            styles {
                ".bui-button-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-button-section" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 8.px
                    marginTop = 16.px
                    marginBottom = 16.px
                }
                ".bui-button-row" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 12.px
                }
                ".bui-btn" {
                    boxSizing = BoxSizing.borderBox
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 6.px
                    fontSize = 14.px
                    backgroundColor = Color("#175cd3")
                    color = NamedColor.white
                    cursor = Cursor.pointer
                }
                ".bui-btn:hover" {
                    backgroundColor = Color("#154ec1")
                }
                ".bui-btn:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
                ".bui-btn[data-disabled], .bui-btn--disabled" {
                    backgroundColor = Color("#eaecf0")
                    color = Color("#98a2b3")
                    borderColor = Color("#d0d5dd")
                    cursor = Cursor.notAllowed
                }
                ".bui-btn--link" {
                    display = Display.inlineFlex
                    alignItems = AlignItems.center
                    textDecoration = none
                    backgroundColor = Color("#079455")
                    color = NamedColor.white
                }
                ".bui-btn--link:hover" {
                    backgroundColor = Color("#067647")
                }
                ".bui-button-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
            }
        }

        section {
            id = ElementId("bui-button-sample")
            className = ClassName("bui-button-sample")

            CheckList {
                id = "button-checks"
                checks = listOf(
                    Check("Button component is loaded", jsTypeOf(Button) == "object"),
                    Check("clicks state is non-negative", clicks >= 0),
                    Check("button disabled state is boolean", jsTypeOf(btnDisabled) == "boolean"),
                )
            }

            StatusLine {
                id = "button-status"
                fields = listOf(
                    "clicks" to clicks.toString(),
                    "disabled" to btnDisabled.toString(),
                    "focusable" to focusableWhenDisabled.toString(),
                )
            }

            div {
                className = ClassName("bui-button-section")
                p { +"1. Standard interactive Button:" }
                div {
                    className = ClassName("bui-button-row")
                    Button {
                        id = ElementId("button-primary")
                        disabled = btnDisabled
                        className { state ->
                            ClassName(if (state.disabled) "bui-btn bui-btn--disabled" else "bui-btn")
                        }
                        onClick = { clicks++ }
                        +"Click count: $clicks"
                    }
                    button {
                        id = ElementId("button-toggle-disabled")
                        type = ButtonType.button
                        onClick = { btnDisabled = !btnDisabled }
                        +if (btnDisabled) "Enable button" else "Disable button"
                    }
                }
            }

            div {
                className = ClassName("bui-button-section")
                p { +"2. focusableWhenDisabled Button:" }
                div {
                    className = ClassName("bui-button-row")
                    Button {
                        id = ElementId("button-focusable-disabled")
                        disabled = true
                        this.focusableWhenDisabled = focusableWhenDisabled
                        className { state ->
                            ClassName("bui-btn bui-btn--disabled")
                        }
                        onClick = { clicks++ }
                        +"Disabled (focusable: $focusableWhenDisabled)"
                    }
                    button {
                        id = ElementId("button-toggle-focusable")
                        type = ButtonType.button
                        onClick = { focusableWhenDisabled = !focusableWhenDisabled }
                        +if (focusableWhenDisabled) "Make non-focusable" else "Make focusable"
                    }
                }
            }

            div {
                className = ClassName("bui-button-section")
                p { +"3. Custom styling via style helper:" }
                Button {
                    id = ElementId("button-styled")
                    disabled = btnDisabled
                    className = ClassName("bui-btn")
                    style { state ->
                        unsafeJso {
                            opacity = if (state.disabled) number(0.4) else number(1.0)
                        }
                    }
                    onClick = { clicks++ }
                    +"Styled with style helper"
                }
            }

            div {
                className = ClassName("bui-button-section")
                p { +"4. Render prop with custom anchor element (nativeButton = false):" }
                Button {
                    id = ElementId("button-custom-link")
                    nativeButton = false
                    render { props, state ->
                        a.create {
                            +props
                            href = "#custom-link"
                            className = ClassName("bui-btn bui-btn--link")
                        }
                    }
                    +"Rendered as Anchor Link"
                }
            }
        }
    }
)
