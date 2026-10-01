import baseui.Fieldset
import baseui.FieldsetLegendState
import baseui.FieldsetRootState
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
import react.dom.html.ReactHTML.fieldset
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useRef
import react.useState
import web.cssom.*
import web.cssom.LineStyle.Companion.solid
import web.dom.ElementId
import web.html.ButtonType
import web.html.HTMLDivElement
import web.html.HTMLFieldSetElement
import web.html.InputType
import web.html.button
import web.html.checkbox
import web.html.text

// Default Root/Legend retain Base UI's native fieldset and div-based automatic label association.
// The nested parts test custom rendering, ref/children forwarding, and inherited disabled state.
val BaseUiFieldset = FC<Props> {
    var fieldsetDisabled by useState(false)
    var displayName by useState("Ada")
    var updates by useState(false)
    var profileActions by useState(0)
    var deliveryNote by useState("Morning delivery")
    var deliveryActions by useState(0)
    var lastFocus by useState("none")
    var refReport by useState("Custom refs have not been checked.")
    val customRootRef = useRef<HTMLFieldSetElement>(null)
    val customLegendRef = useRef<HTMLDivElement>(null)

    Global {
        styles {
            ".bui-fieldset-sample" {
                // SliderStylization overlays the page, so this sample needs its own stacking context.
                position = Position.relative
                zIndex = integer(1)
                boxSizing = BoxSizing.borderBox
                width = 100.pct
                maxWidth = 480.px
                padding = 16.px
                marginBottom = 32.px
                fontSize = 16.px
                lineHeight = number(1.5)
                color = Color("#182230")
                backgroundColor = NamedColor.white
            }
            ".bui-fieldset-sample h2" {
                margin = 0.px
                fontSize = 20.px
            }
            ".bui-fieldset-root" {
                display = Display.flex
                flexDirection = FlexDirection.column
                boxSizing = BoxSizing.borderBox
                minWidth = 0.px
                gap = 12.px
                margin = 0.px
                padding = 16.px
                border = Border(1.px, solid, Color("#667085"))
                borderRadius = 8.px
            }
            ".bui-fieldset-legend" {
                fontWeight = FontWeight.bold
            }
            ".bui-fieldset-control" {
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 4.px
            }
            ".bui-fieldset-checkbox" {
                display = Display.flex
                alignItems = AlignItems.center
                gap = 8.px
                minHeight = 48.px
            }
            ".bui-fieldset-checkbox input" {
                width = 20.px
                height = 20.px
            }
            ".bui-fieldset-sample input[type='text'], .bui-fieldset-sample button" {
                boxSizing = BoxSizing.borderBox
                minWidth = 0.px
                minHeight = 48.px
                padding = 10.px
                border = Border(1.px, solid, Color("#667085"))
                borderRadius = 4.px
                fontSize = 16.px
            }
            ".bui-fieldset-sample input[type='text']" {
                width = 100.pct
            }
            ".bui-fieldset-sample button" {
                color = NamedColor.white
                backgroundColor = Color("#175cd3")
                cursor = Cursor.pointer
            }
            ".bui-fieldset-sample input:disabled, .bui-fieldset-sample button:disabled" {
                color = Color("#475467")
                backgroundColor = Color("#eaecf0")
                cursor = Cursor.notAllowed
            }
            ".bui-fieldset-sample input:focus-visible, .bui-fieldset-sample button:focus-visible" {
                outline = Outline(3.px, solid, Color("#175cd3"))
                outlineOffset = 3.px
            }
            ".bui-fieldset-help, .bui-fieldset-status" {
                margin = 0.px
                overflowWrap = OverflowWrap.anywhere
            }
            ".bui-fieldset-footer" {
                marginTop = 12.px
            }
        }
    }

    section {
        id = ElementId("bui-fieldset-sample")
        className = ClassName("bui-fieldset-sample")
        ariaLabelledBy = ElementId("bui-fieldset-heading")

        h2 {
            id = ElementId("bui-fieldset-heading")
            +"Base UI Fieldset"
        }
        p {
            id = ElementId("bui-fieldset-help")
            +"Edit or activate the controls, then disable the group. Tab skips disabled controls; the outside buttons stay available."
        }
        button {
            id = ElementId("bui-fieldset-toggle")
            type = ButtonType.button
            ariaPressed = if (fieldsetDisabled) AriaPressed.`true` else AriaPressed.`false`
            onClick = { fieldsetDisabled = !fieldsetDisabled }
            onFocus = { lastFocus = "outside toggle" }
            +if (fieldsetDisabled) "Enable fieldset" else "Disable fieldset"
        }
        p {
            id = ElementId("bui-fieldset-status")
            className = ClassName("bui-fieldset-status")
            ariaLive = AriaLive.polite
            +"Group ${if (fieldsetDisabled) "disabled" else "enabled"}; last focus: $lastFocus."
        }

        Fieldset.Root {
            id = ElementId("bui-fieldset-root")
            disabled = fieldsetDisabled
            ariaDescribedBy = ElementId("bui-fieldset-help")
            className { state: FieldsetRootState ->
                ClassName("bui-fieldset-root bui-fieldset-root--${if (state.disabled) "disabled" else "enabled"}")
            }
            style { state ->
                unsafeJso<CSSProperties> {
                    backgroundColor = if (state.disabled) Color("#f2f4f7") else NamedColor.white
                }
            }

            Fieldset.Legend {
                className { state: FieldsetLegendState ->
                    ClassName("bui-fieldset-legend bui-fieldset-legend--${if (state.disabled) "disabled" else "enabled"}")
                }
                style { state ->
                    unsafeJso<CSSProperties> {
                        color = if (state.disabled) Color("#475467") else Color("#182230")
                    }
                }
                +"Profile controls"
            }
            div {
                className = ClassName("bui-fieldset-control")
                label {
                    htmlFor = ElementId("bui-fieldset-name")
                    +"Display name"
                }
                input {
                    id = ElementId("bui-fieldset-name")
                    name = "displayName"
                    type = InputType.text
                    value = displayName
                    onChange = { displayName = it.currentTarget.value }
                    onFocus = { lastFocus = "display name" }
                }
            }
            div {
                className = ClassName("bui-fieldset-checkbox")
                input {
                    id = ElementId("bui-fieldset-updates")
                    name = "updates"
                    type = InputType.checkbox
                    checked = updates
                    onChange = { updates = it.currentTarget.checked }
                    onFocus = { lastFocus = "send updates" }
                }
                label {
                    htmlFor = ElementId("bui-fieldset-updates")
                    +"Send updates"
                }
            }
            button {
                id = ElementId("bui-fieldset-apply")
                type = ButtonType.button
                onClick = { profileActions += 1 }
                onFocus = { lastFocus = "profile action" }
                +"Apply profile"
            }

            Fieldset.Root {
                id = ElementId("bui-fieldset-nested")
                disabled = false
                ref = customRootRef
                className { state: FieldsetRootState ->
                    ClassName("bui-fieldset-root bui-fieldset-root--custom bui-fieldset-root--${if (state.disabled) "disabled" else "enabled"}")
                }
                style { state ->
                    unsafeJso<CSSProperties> {
                        borderColor = if (state.disabled) Color("#98a2b3") else Color("#175cd3")
                    }
                }
                render { props, state ->
                    fieldset.create {
                        // These props carry the merged ref, children, disabled state, and label id.
                        +props
                        title = "Custom root disabled=${state.disabled} (own disabled=false)"
                    }
                }

                Fieldset.Legend {
                    ref = customLegendRef
                    className { state: FieldsetLegendState ->
                        ClassName("bui-fieldset-legend bui-fieldset-legend--custom bui-fieldset-legend--${if (state.disabled) "disabled" else "enabled"}")
                    }
                    style { state ->
                        unsafeJso<CSSProperties> {
                            color = if (state.disabled) Color("#475467") else Color("#175cd3")
                        }
                    }
                    render { props, state ->
                        div.create {
                            +props
                            title = "Custom legend disabled=${state.disabled}"
                        }
                    }
                    +"Delivery options"
                }
                p {
                    className = ClassName("bui-fieldset-help")
                    +"This nested group sets disabled=false and inherits the parent state."
                }
                div {
                    className = ClassName("bui-fieldset-control")
                    label {
                        htmlFor = ElementId("bui-fieldset-note")
                        +"Delivery note"
                    }
                    input {
                        id = ElementId("bui-fieldset-note")
                        name = "deliveryNote"
                        type = InputType.text
                        value = deliveryNote
                        onChange = { deliveryNote = it.currentTarget.value }
                        onFocus = { lastFocus = "delivery note" }
                    }
                }
                button {
                    id = ElementId("bui-fieldset-nested-action")
                    type = ButtonType.button
                    onClick = { deliveryActions += 1 }
                    onFocus = { lastFocus = "delivery action" }
                    +"Test delivery action"
                }
            }
        }

        div {
            className = ClassName("bui-fieldset-footer")
            p {
                id = ElementId("bui-fieldset-values")
                className = ClassName("bui-fieldset-status")
                +"Name: $displayName; updates: $updates; profile actions: $profileActions. Note: $deliveryNote; delivery actions: $deliveryActions."
            }
            button {
                id = ElementId("bui-fieldset-after")
                type = ButtonType.button
                onClick = {
                    refReport = "Custom refs: root=${customRootRef.current?.tagName ?: "missing"}, legend=${customLegendRef.current?.tagName ?: "missing"}."
                }
                onFocus = { lastFocus = "outside ref check" }
                +"Check custom refs"
            }
            p {
                id = ElementId("bui-fieldset-refs")
                className = ClassName("bui-fieldset-status")
                ariaLive = AriaLive.polite
                +refReport
            }
        }
    }
}
