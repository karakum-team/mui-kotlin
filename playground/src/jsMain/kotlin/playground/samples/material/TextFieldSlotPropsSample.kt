package playground.samples.material

import js.objects.unsafeJso
import mui.material.FormHelperTextProps
import mui.material.InputAdornment
import mui.material.InputAdornmentPosition
import mui.material.InputBaseComponentProps
import mui.material.InputLabelProps
import mui.material.OutlinedInputProps
import mui.material.SelectProps
import mui.material.TextField
import mui.material.TextFieldSlotProps
import mui.material.TextFieldSlots
import mui.material.inputAdornmentClasses
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.option
import react.useEffectOnce
import react.useState
import web.cssom.ClassName
import web.dom.ElementId
import web.dom.document

private const val ROOT_ID = "textfield-slots"
private const val INPUT_ID = "textfield-slots-input"
private const val HELPER_ID = "textfield-slots-helper"
private const val SELECT_ID = "textfield-slots-select"
private const val LABEL_CLASS = "textfield-slots-label"
private const val HTML_INPUT_CLASS = "textfield-slots-html-input"

val TextFieldSlotPropsSample: Sample = Sample(
    id = "textfield-slot-props",
    library = Libraries.Material,
    title = "TextField slots/slotProps",
    kind = Kind.Check,
    covers = listOf(
        "TextField.slots: TextFieldSlots.formHelperText",
        "TextField.slotProps: TextFieldSlotProps",
        "TextFieldSlotProps.input (OutlinedInputProps as InputBaseProps)",
        "TextFieldSlotProps.inputLabel/htmlInput/formHelperText",
        "TextFieldSlotProps.select: SelectProps<*>",
    ),
    fileName = "TextFieldSlotPropsSample.kt",
    notes = "Only running this proves that each `slotProps` entry reaches its inner component — v9 removed " +
            "`InputProps`/`inputProps`/`InputLabelProps`/`FormHelperTextProps`/`SelectProps`, so these slots are the " +
            "only way in: the input renders the start adornment, the label and the `<input>` carry their classes, " +
            "the helper text is a `<div>` via `slots`, and `select.native` renders a real `<select>`.",
    content = FC<Props> {
        // Null until the first commit: the checks read the rendered DOM.
        var checks by useState<List<Check>?>(null)

        useEffectOnce {
            val root = document.getElementById(ElementId(ROOT_ID))
            checks = listOf(
                Check(
                    "slotProps.input.startAdornment is rendered",
                    root?.querySelector(".${inputAdornmentClasses.root}") != null,
                ),
                Check(
                    "slotProps.inputLabel.className reaches the <label>",
                    root?.querySelector("label.$LABEL_CLASS") != null,
                ),
                Check(
                    "slotProps.htmlInput.className reaches the <input>",
                    document.getElementById(ElementId(INPUT_ID))?.classList?.contains(ClassName(HTML_INPUT_CLASS)) == true,
                ),
                Check(
                    "slots.formHelperText = div renders the helper text as <div>",
                    document.getElementById(ElementId(HELPER_ID))?.tagName == "DIV",
                ),
                Check(
                    "slotProps.select.native renders a <select>",
                    document.getElementById(ElementId(SELECT_ID))?.tagName == "SELECT",
                ),
            )
        }

        div {
            id = ElementId(ROOT_ID)

            TextField {
                id = ElementId(INPUT_ID)
                label = ReactNode("Price")
                helperText = ReactNode("Helper text rendered as a div")
                slots = unsafeJso<TextFieldSlots> {
                    formHelperText = div
                }
                slotProps = unsafeJso<TextFieldSlotProps> {
                    // A variant-specific props type is assignable to the `InputBaseProps` slot.
                    input = unsafeJso<OutlinedInputProps> {
                        notched = true
                        startAdornment = InputAdornment.create {
                            position = InputAdornmentPosition.start
                            +"$"
                        }
                    }
                    inputLabel = unsafeJso<InputLabelProps> {
                        className = ClassName(LABEL_CLASS)
                        shrink = true
                    }
                    htmlInput = unsafeJso<InputBaseComponentProps> {
                        className = ClassName(HTML_INPUT_CLASS)
                    }
                    formHelperText = unsafeJso<FormHelperTextProps> {
                        id = ElementId(HELPER_ID)
                    }
                }
            }

            TextField {
                id = ElementId(SELECT_ID)
                label = ReactNode("Native select")
                select = true
                slotProps = unsafeJso<TextFieldSlotProps> {
                    select = unsafeJso<SelectProps<String>> {
                        native = true
                    }
                }

                option { +"One" }
                option { +"Two" }
            }
        }

        checks?.let {
            CheckList {
                id = "textfield-slot-props-checks"
                this.checks = it
            }
        }
    },
)
