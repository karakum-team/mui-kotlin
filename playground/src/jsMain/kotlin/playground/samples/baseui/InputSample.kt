package playground.samples.baseui

import baseui.Field
import baseui.Input
import baseui.InputChangeEventDetails
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
import web.cssom.pct
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val InputSample: Sample = Sample(
    id = "input",
    library = Libraries.BaseUi,
    title = "Input",
    kind = Kind.Check,
    covers = listOf(
        "Input direct component export",
        "InputProps.onValueChange / InputChangeEventDetails",
        "InputState extending FieldControlState",
        "Field.Root integration with Field.Label",
        "className/style/render state helpers",
    ),
    fileName = "InputSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Input", "https://base-ui.com/react/components/input"),
    ),
    notes = "Direct component export from @base-ui/react/input. Native HTML input element with state tracking, " +
        "cross-module Field.Control integration, and typed change events.",
    content = FC<Props> {
        var standaloneValue by useState("")
        var lastEventDetails by useState<InputChangeEventDetails?>(null)
        var inputDisabled by useState(false)

        Global {
            styles {
                ".bui-input-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-input-control" {
                    width = 100.pct
                    maxWidth = 320.px
                    padding = 8.px
                    fontSize = 14.px
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 4.px
                    outline = Outline(0.px, solid, Color("transparent"))
                    boxSizing = BoxSizing.borderBox
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                }
                ".bui-input-control:focus" {
                    borderColor = Color("#175cd3")
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 1.px
                }
                ".bui-input-control[data-disabled]" {
                    backgroundColor = Color("#f2f4f7")
                    color = Color("#98a2b3")
                    borderColor = Color("#eaecf0")
                    cursor = Cursor.notAllowed
                }
                ".bui-field-container" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 4.px
                    marginTop = 16.px
                    marginBottom = 16.px
                }
                ".bui-field-label" {
                    fontSize = 14.px
                    fontWeight = FontWeight.bold
                    color = Color("#344054")
                }
                ".bui-field-description" {
                    fontSize = 12.px
                    color = Color("#475467")
                }
                ".bui-input-controls" {
                    display = Display.flex
                    gap = 8.px
                    marginTop = 12.px
                }
                ".bui-input-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-input-sample button:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-input-sample")
            className = ClassName("bui-input-sample")

            CheckList {
                id = "input-checks"
                checks = listOf(
                    Check("Input component is loaded", jsTypeOf(Input) == "object" || jsTypeOf(Input) == "function"),
                    Check("Input direct component export exists", jsTypeOf(Input) != "undefined"),
                )
            }

            StatusLine {
                id = "input-status"
                fields = listOf(
                    "value" to standaloneValue,
                    "disabled" to inputDisabled.toString(),
                    "hasEventDetails" to (lastEventDetails != null).toString(),
                )
            }

            p { +"1. Standalone controlled Input:" }

            div {
                Input {
                    id = ElementId("bui-standalone-input")
                    value = standaloneValue
                    onValueChange = { next, details ->
                        standaloneValue = next
                        lastEventDetails = details
                    }
                    disabled = inputDisabled
                    placeholder = "Type something here..."
                    className { state ->
                        ClassName(if (state.disabled) "bui-input-control bui-input-control--disabled" else "bui-input-control")
                    }
                }
            }

            p { +"2. Input within Field.Root:" }

            Field.Root {
                id = ElementId("bui-input-field-root")
                className = ClassName("bui-field-container")

                Field.Label {
                    id = ElementId("bui-input-field-label")
                    className = ClassName("bui-field-label")
                    +"Username"
                }

                Input {
                    id = ElementId("bui-field-integrated-input")
                    defaultValue = "alex"
                    className = ClassName("bui-input-control")
                    placeholder = "Enter username"
                }

                Field.Description {
                    id = ElementId("bui-input-field-description")
                    className = ClassName("bui-field-description")
                    +"This username will be displayed publicly."
                }
            }

            div {
                className = ClassName("bui-input-controls")
                button {
                    id = ElementId("input-toggle-disabled-btn")
                    type = ButtonType.button
                    onClick = { inputDisabled = !inputDisabled }
                    +if (inputDisabled) "Enable Input" else "Disable Input"
                }
                button {
                    id = ElementId("input-clear-btn")
                    type = ButtonType.button
                    onClick = {
                        standaloneValue = ""
                        lastEventDetails = null
                    }
                    +"Clear Input"
                }
            }
        }
    }
)
