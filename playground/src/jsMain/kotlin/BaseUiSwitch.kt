import baseui.Field
import baseui.Switch
import baseui.SwitchRootChangeEventDetails
import baseui.SwitchRootState
import baseui.SwitchThumbState
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
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.span
import react.useRef
import react.useState
import web.cssom.*
import web.cssom.LineStyle.Companion.solid
import web.dom.ElementId
import web.form.FormData
import web.html.ButtonType
import web.html.HTMLInputElement
import web.html.button
import web.html.submit

// Root/Thumb helpers consume their own state, including inherited Field state.
// The render callbacks forward props so keyboard handlers, refs, children and ARIA survive.
val BaseUiSwitch = FC<Props> {
    var switchChecked by useState(false)
    var uncontrolledChecked by useState(true)
    var switchDisabled by useState(false)
    var switchReadOnly by useState(false)
    var lastChange by useState("none")
    var submitted by useState("Submit to inspect the form values and input ref.")
    val hiddenInput = useRef<HTMLInputElement>(null)

    Global {
        styles {
            ".bui-switch-sample" {
                position = Position.relative
                zIndex = integer(1) // Stay above the existing SliderStylization overlay.
                maxWidth = 480.px
                padding = 16.px
                marginBottom = 24.px
                fontSize = 16.px
                lineHeight = number(1.5)
                backgroundColor = NamedColor.white
                color = Color("#182230")
            }
            ".bui-switch-sample h2" { margin = 0.px; fontSize = 20.px }
            ".bui-switch-field" { marginBottom = 16.px }
            ".bui-switch-label" { display = Display.block; fontWeight = FontWeight.bold }
            ".bui-switch-description" { margin = 0.px; color = Color("#475467") }
            ".bui-switch-root" {
                display = Display.inlineFlex
                alignItems = AlignItems.center
                boxSizing = BoxSizing.borderBox
                width = 56.px
                height = 32.px
                padding = 3.px
                marginTop = 8.px
                marginBottom = 8.px
                border = Border(1.px, solid, Color("#475467"))
                borderRadius = 16.px
                backgroundColor = Color("#eaecf0")
                cursor = Cursor.pointer
            }
            ".bui-switch-root[data-checked]" { backgroundColor = Color("#175cd3") }
            ".bui-switch-root[data-disabled], .bui-switch-root[data-readonly]" { cursor = Cursor.default }
            ".bui-switch-thumb" {
                display = Display.block
                width = 24.px
                height = 24.px
                borderRadius = 50.pct
                backgroundColor = NamedColor.white
            }
            ".bui-switch-thumb[data-checked]" { marginLeft = 22.px }
            ".bui-switch-sample button" {
                minHeight = 48.px
                marginRight = 8.px
                marginBottom = 8.px
                padding = 8.px
                fontSize = 16.px
            }
            ".bui-switch-root:focus-visible, .bui-switch-sample button:focus-visible" {
                outline = Outline(3.px, solid, Color("#175cd3"))
                outlineOffset = 3.px
            }
            ".bui-switch-status" { margin = 0.px; overflowWrap = OverflowWrap.anywhere }
        }
    }

    section {
        id = ElementId("bui-switch-sample")
        className = ClassName("bui-switch-sample")
        ariaLabelledBy = ElementId("bui-switch-heading")
        h2 { id = ElementId("bui-switch-heading"); +"Base UI Switch" }
        p { +"Use Tab and Space, or click a label. On and off values are included in the form snapshot." }
        button {
            id = ElementId("bui-switch-disabled-toggle")
            type = ButtonType.button
            onClick = { switchDisabled = !switchDisabled }
            +if (switchDisabled) "Enable switches" else "Disable switches"
        }
        button {
            id = ElementId("bui-switch-readonly-toggle")
            type = ButtonType.button
            onClick = { switchReadOnly = !switchReadOnly }
            +if (switchReadOnly) "Make switches editable" else "Make switches read-only"
        }

        form {
            id = ElementId("bui-switch-form")
            onSubmit = { event ->
                event.preventDefault()
                val data = FormData(event.currentTarget)
                val input = hiddenInput.current
                submitted = "notifications=${data.get("notifications") ?: "omitted"}; " +
                    "localPreview=${data.get("localPreview") ?: "omitted"}; " +
                    "input ref: ${input?.type}/${input?.name}, id=${input?.id}, checked=${input?.checked}"
            }
            Field.Root {
                className = ClassName("bui-switch-field")
                name = "notifications"
                disabled = switchDisabled
                // Native label activation reaches the hidden checkbox; Root uses the label as its ARIA name.
                Field.Label { className = ClassName("bui-switch-label"); nativeLabel = true; +"Notifications (controlled)" }
                Field.Description {
                    className = ClassName("bui-switch-description")
                    +"Send notifications when this switch is on."
                }
                Switch.Root {
                    id = ElementId("bui-switch-controlled-input")
                    checked = switchChecked
                    readOnly = switchReadOnly
                    nativeButton = false
                    inputRef = hiddenInput
                    value = "enabled"
                    uncheckedValue = "disabled"
                    className { state: SwitchRootState ->
                        ClassName("bui-switch-root bui-switch-controlled " +
                            "bui-switch-dirty-${state.dirty} bui-switch-touched-${state.touched} " +
                            "bui-switch-filled-${state.filled} bui-switch-focused-${state.focused} " +
                            "bui-switch-valid-${state.valid}")
                    }
                    style { state: SwitchRootState -> unsafeJso<CSSProperties> { opacity = number(if (state.disabled) 0.5 else 1.0) } }
                    render { props, state: SwitchRootState ->
                        span.create { +props; title = "checked=${state.checked}, readOnly=${state.readOnly}, required=${state.required}" }
                    }
                    onCheckedChange = { next: Boolean, details: SwitchRootChangeEventDetails ->
                        switchChecked = next
                        lastChange = "controlled=$next/${details.reason}/${details.event.type}"
                    }
                    Switch.Thumb {
                        className { state: SwitchThumbState ->
                            ClassName("bui-switch-thumb bui-switch-thumb-filled-${state.filled}")
                        }
                        style { state: SwitchThumbState -> unsafeJso<CSSProperties> { marginLeft = if (state.checked) 22.px else 0.px } }
                        render { props, state: SwitchThumbState ->
                            span.create { +props; title = "checked=${state.checked}, disabled=${state.disabled}, dirty=${state.dirty}" }
                        }
                    }
                }
            }
            Field.Root {
                className = ClassName("bui-switch-field")
                name = "localPreview"
                Field.Label { className = ClassName("bui-switch-label"); nativeLabel = true; +"Local preview (uncontrolled)" }
                Field.Description { className = ClassName("bui-switch-description"); +"Starts on; Base UI owns its checked state." }
                Switch.Root {
                    id = ElementId("bui-switch-uncontrolled-input")
                    className = ClassName("bui-switch-root bui-switch-uncontrolled")
                    defaultChecked = true
                    disabled = switchDisabled
                    readOnly = switchReadOnly
                    value = "on"
                    uncheckedValue = "off"
                    onCheckedChange = { next: Boolean, details: SwitchRootChangeEventDetails ->
                        uncontrolledChecked = next
                        lastChange = "uncontrolled=$next/${details.reason}/${details.event.type}"
                    }
                    Switch.Thumb { className = ClassName("bui-switch-thumb") }
                }
            }
            button { id = ElementId("bui-switch-submit"); type = ButtonType.submit; +"Inspect form values" }
        }
        p {
            id = ElementId("bui-switch-status")
            className = ClassName("bui-switch-status")
            ariaLive = AriaLive.polite
            +"Controlled=$switchChecked; uncontrolled=$uncontrolledChecked; disabled=$switchDisabled; read-only=$switchReadOnly; change=$lastChange."
        }
        p {
            id = ElementId("bui-switch-form-status")
            className = ClassName("bui-switch-status")
            ariaLive = AriaLive.polite
            +submitted
        }
    }
}
