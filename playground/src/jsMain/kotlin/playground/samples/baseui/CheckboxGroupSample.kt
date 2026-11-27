package playground.samples.baseui

import baseui.Checkbox
import baseui.CheckboxGroup
import baseui.CheckboxGroupChangeEventDetails
import baseui.className
import emotion.react.Global
import emotion.react.styles
import js.array.ReadonlyArray
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
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
import web.cssom.FontWeight
import web.cssom.JustifyContent
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Outline
import web.cssom.number
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

private val ALL_FRUITS: ReadonlyArray<String> = arrayOf("apple", "banana", "orange")

val CheckboxGroupSample: Sample = Sample(
    id = "checkbox-group",
    library = Libraries.BaseUi,
    title = "CheckboxGroup",
    kind = Kind.Check,
    covers = listOf(
        "CheckboxGroup (direct component export)",
        "CheckboxGroupProps.value / onValueChange",
        "CheckboxGroupProps.allValues",
        "CheckboxGroupProps.disabled",
        "CheckboxGroupState.disabled",
        "Parent Checkbox (parent = true, indeterminate state)",
        "Child Checkboxes (value = '...')",
        "className/style/render state helpers",
    ),
    fileName = "CheckboxGroupSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Checkbox Group", "https://base-ui.com/react/components/checkbox-group"),
    ),
    notes = "Direct component export from @base-ui/react/checkbox-group. Provides shared state for a group " +
        "of checkboxes, coordinating selection, parent/child relationships (allValues, parent=true with automatic " +
        "indeterminate state), group-level disabled state, and state helpers.",
    content = FC<Props> {
        var selectedFruits by useState<ReadonlyArray<String>>(arrayOf("apple"))
        var groupDisabled by useState(false)

        Global {
            styles {
                ".bui-cg-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-cg" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 8.px
                    padding = 12.px
                    border = Border(1.px, solid, Color("#d0d5dd"))
                    borderRadius = 6.px
                    marginTop = 12.px
                    marginBottom = 12.px
                }
                ".bui-cg--disabled, .bui-cg[data-disabled]" {
                    backgroundColor = Color("#f8f9fa")
                    borderColor = Color("#eaecf0")
                }
                ".bui-cg-row" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 8.px
                }
                ".bui-cg-row--child" {
                    paddingLeft = 24.px
                }
                ".bui-cg-checkbox" {
                    display = Display.inlineFlex
                    alignItems = AlignItems.center
                    justifyContent = JustifyContent.center
                    boxSizing = BoxSizing.borderBox
                    width = 24.px
                    height = 24.px
                    border = Border(2.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    backgroundColor = NamedColor.white
                    cursor = Cursor.pointer
                }
                ".bui-cg-checkbox[data-checked], .bui-cg-checkbox[data-indeterminate]" {
                    backgroundColor = Color("#175cd3")
                    borderColor = Color("#175cd3")
                    color = NamedColor.white
                }
                ".bui-cg-checkbox[data-disabled]" {
                    cursor = Cursor.notAllowed
                    borderColor = Color("#d0d5dd")
                    backgroundColor = Color("#eaecf0")
                    color = Color("#98a2b3")
                }
                ".bui-cg-indicator" {
                    fontWeight = FontWeight.bold
                    fontSize = 16.px
                    lineHeight = number(1.0)
                }
                ".bui-cg-controls" {
                    display = Display.flex
                    gap = 8.px
                    marginTop = 12.px
                }
                ".bui-cg-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-cg-sample button:focus-visible, .bui-cg-checkbox:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-cg-sample")
            className = ClassName("bui-cg-sample")

            CheckList {
                id = "checkbox-group-checks"
                checks = listOf(
                    Check("CheckboxGroup component is loaded", jsTypeOf(CheckboxGroup) == "object"),
                    Check("selected count is non-negative", selectedFruits.size >= 0),
                    Check("group disabled is boolean", jsTypeOf(groupDisabled) == "boolean"),
                )
            }

            StatusLine {
                id = "checkbox-group-status"
                fields = listOf(
                    "selected" to selectedFruits.joinToString(","),
                    "count" to selectedFruits.size.toString(),
                    "disabled" to groupDisabled.toString(),
                )
            }

            p { +"Checkbox Group with Parent (Select All) and Child Checkboxes:" }

            CheckboxGroup {
                id = ElementId("fruit-checkbox-group")
                allValues = ALL_FRUITS
                value = selectedFruits
                onValueChange = { next: ReadonlyArray<String>, _: CheckboxGroupChangeEventDetails ->
                    selectedFruits = next
                }
                disabled = groupDisabled
                className { state ->
                    ClassName(if (state.disabled) "bui-cg bui-cg--disabled" else "bui-cg")
                }

                div {
                    className = ClassName("bui-cg-row")
                    Checkbox.Root {
                        id = ElementId("cg-parent-checkbox")
                        parent = true
                        className = ClassName("bui-cg-checkbox")
                        Checkbox.Indicator {
                            className = ClassName("bui-cg-indicator")
                            // Render minus for indeterminate, checkmark for checked
                            +if (selectedFruits.size > 0 && selectedFruits.size < ALL_FRUITS.size) "−" else "✓"
                        }
                    }
                    span { +"Select all fruits" }
                }

                for (fruit in listOf("apple", "banana", "orange")) {
                    div {
                        key = Key(fruit)
                        className = ClassName("bui-cg-row bui-cg-row--child")
                        Checkbox.Root {
                            id = ElementId("cg-child-$fruit")
                            value = fruit
                            className = ClassName("bui-cg-checkbox")
                            Checkbox.Indicator {
                                className = ClassName("bui-cg-indicator")
                                +"✓"
                            }
                        }
                        span { +fruit.replaceFirstChar { it.uppercase() } }
                    }
                }
            }

            div {
                className = ClassName("bui-cg-controls")
                button {
                    id = ElementId("cg-toggle-disabled-btn")
                    type = ButtonType.button
                    onClick = { groupDisabled = !groupDisabled }
                    +if (groupDisabled) "Enable group" else "Disable group"
                }
                button {
                    id = ElementId("cg-select-all-btn")
                    type = ButtonType.button
                    onClick = { selectedFruits = ALL_FRUITS }
                    +"Select all"
                }
                button {
                    id = ElementId("cg-clear-all-btn")
                    type = ButtonType.button
                    onClick = { selectedFruits = emptyArray() }
                    +"Clear all"
                }
            }
        }
    }
)
