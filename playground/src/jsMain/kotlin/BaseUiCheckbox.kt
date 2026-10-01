import baseui.Checkbox
import baseui.CheckboxIndicatorState
import baseui.CheckboxRootChangeEventDetails
import baseui.CheckboxRootState
import baseui.Field
import baseui.className
import baseui.render
import baseui.style
import emotion.react.Global
import emotion.react.styles
import js.objects.unsafeJso
import react.CSSProperties
import react.FC
import react.Props
import react.create
import react.dom.aria.AriaLive
import react.dom.aria.AriaPressed
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.span
import react.useRef
import react.useState
import web.cssom.*
import web.cssom.LineStyle.Companion.solid
import web.dom.ElementId
import web.html.ButtonType
import web.html.HTMLSpanElement
import web.html.button

// Root/Indicator exercise their own state and inherited FieldRootState without manual ARIA wiring.
val BaseUiCheckbox = FC<Props> {
    var selection by useState(0) // unchecked, checked, indeterminate
    var fieldDisabled by useState(false)
    var lastChange by useState("No checkbox changes yet.")
    val checkboxRef = useRef<HTMLSpanElement>(null)
    val selectionName = when (selection) {
        1 -> "checked"
        2 -> "indeterminate"
        else -> "unchecked"
    }

    Global {
        styles {
            ".bui-checkbox-sample" {
                // SliderStylization overlays the page; controls need their own stacking context.
                position = Position.relative
                zIndex = integer(1)
                maxWidth = 400.px
                marginBottom = 24.px
                padding = 12.px
                color = Color("#182230")
                backgroundColor = NamedColor.white
                fontSize = 16.px
                lineHeight = number(1.5)
            }
            ".bui-checkbox-sample h2, .bui-checkbox-sample p" { margin = 0.px }
            ".bui-checkbox-field, .bui-checkbox-actions" {
                display = Display.flex
                gap = 8.px
                marginTop = 8.px
            }
            ".bui-checkbox-field" { flexDirection = FlexDirection.column }
            ".bui-checkbox-row" {
                display = Display.flex
                alignItems = AlignItems.center
                gap = 8.px
            }
            ".bui-checkbox-root" {
                display = Display.inlineFlex
                alignItems = AlignItems.center
                justifyContent = JustifyContent.center
                boxSizing = BoxSizing.borderBox
                width = 48.px
                height = 48.px
                border = Border(2.px, solid, Color("#475467"))
                borderRadius = 6.px
                cursor = Cursor.pointer
            }
            ".bui-checkbox-root[data-disabled]" { cursor = Cursor.notAllowed }
            ".bui-checkbox-label" { cursor = Cursor.pointer }
            ".bui-checkbox-description" { color = Color("#475467") }
            ".bui-checkbox-indicator" {
                fontWeight = FontWeight.bold
                fontSize = 24.px
            }
            ".bui-checkbox-actions" { flexWrap = FlexWrap.wrap }
            ".bui-checkbox-sample button" {
                minHeight = 48.px
                padding = 8.px
                border = Border(1.px, solid, Color("#475467"))
                borderRadius = 4.px
                fontSize = 16.px
                cursor = Cursor.pointer
            }
            ".bui-checkbox-root:focus-visible, .bui-checkbox-sample button:focus-visible" {
                outline = Outline(3.px, solid, Color("#175cd3"))
                outlineOffset = 3.px
            }
        }
    }

    section {
        id = ElementId("bui-checkbox-sample")
        className = ClassName("bui-checkbox-sample")
        ariaLabelledBy = ElementId("bui-checkbox-heading")
        h2 {
            id = ElementId("bui-checkbox-heading")
            +"Base UI Checkbox"
        }

        Field.Root {
            className = ClassName("bui-checkbox-field")
            name = "notifications"
            disabled = fieldDisabled

            div {
                className = ClassName("bui-checkbox-row")
                Checkbox.Root {
                    ref = checkboxRef
                    checked = selection == 1
                    indeterminate = selection == 2
                    disabled = false // Field.Root's disabled state takes precedence.
                    readOnly = false
                    required = false
                    nativeButton = false
                    value = "enabled"
                    uncheckedValue = "disabled"
                    className { state: CheckboxRootState ->
                        ClassName("bui-checkbox-root bui-checkbox-root--${if (state.dirty) "dirty" else "pristine"}")
                    }
                    style { state ->
                        unsafeJso<CSSProperties> {
                            backgroundColor = when {
                                state.disabled -> Color("#eaecf0")
                                state.checked || state.indeterminate -> Color("#175cd3")
                                else -> NamedColor.white
                            }
                            color = if (state.disabled) Color("#475467") else NamedColor.white
                        }
                    }
                    render { props, state ->
                        span.create {
                            // Includes merged ref, children, handlers, and accessibility attributes.
                            +props
                            title = "checked=${state.checked}, mixed=${state.indeterminate}, touched=${state.touched}"
                        }
                    }
                    onCheckedChange = { next: Boolean, details: CheckboxRootChangeEventDetails ->
                        selection = if (next) 1 else 0
                        lastChange = "checked=$next; reason=${details.reason}"
                    }

                    Checkbox.Indicator {
                        keepMounted = true
                        className { state: CheckboxIndicatorState ->
                            ClassName("bui-checkbox-indicator bui-checkbox-indicator--${if (state.indeterminate) "mixed" else "checked"}")
                        }
                        style { state ->
                            unsafeJso<CSSProperties> {
                                opacity = if (state.checked || state.indeterminate) number(1.0) else number(0.0)
                                color = if (state.disabled) Color("#475467") else NamedColor.white
                            }
                        }
                        render { props, state ->
                            span.create {
                                +props
                                title = "transition=${state.transitionStatus}; focused=${state.focused}"
                            }
                        }
                        +if (selection == 2) "−" else "✓"
                    }
                }
                Field.Label {
                    className = ClassName("bui-checkbox-label")
                    +"Send notifications"
                }
            }
            Field.Description {
                className = ClassName("bui-checkbox-description")
                +"Click the label or press Space to toggle. Cycle also shows the mixed state."
            }
        }

        div {
            className = ClassName("bui-checkbox-actions")
            button {
                type = ButtonType.button
                disabled = fieldDisabled
                onClick = { selection = (selection + 1) % 3 }
                +"Cycle state"
            }
            button {
                type = ButtonType.button
                ariaPressed = if (fieldDisabled) AriaPressed.`true` else AriaPressed.`false`
                onClick = { fieldDisabled = !fieldDisabled }
                +if (fieldDisabled) "Enable checkbox" else "Disable checkbox"
            }
            button {
                type = ButtonType.button
                disabled = fieldDisabled
                onClick = { checkboxRef.current?.focus() }
                +"Focus checkbox"
            }
        }
        p {
            id = ElementId("bui-checkbox-status")
            ariaLive = AriaLive.polite
            +"$selectionName; ${if (fieldDisabled) "disabled" else "enabled"}. $lastChange"
        }
    }
}
