import baseui.NumberField
import baseui.NumberFieldRootChangeEventDetails
import baseui.NumberFieldRootCommitEventDetails
import baseui.className
import react.FC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.useState
import web.cssom.ClassName

val BaseUiNumberField = FC {
    var currentValue by useState<Number?>(5)

    div {
        className = ClassName("base-ui-number-field-sample")

        NumberField.Root {
            value = currentValue
            onValueChange = { value, details: NumberFieldRootChangeEventDetails ->
                console.log("NumberField change:", value, details.reason, details.direction)
                currentValue = value
            }
            onValueCommitted = { value, details: NumberFieldRootCommitEventDetails ->
                console.log("NumberField commit:", value, details.reason)
            }
            min = 0.0
            max = 10.0
            step = 1.0

            NumberField.ScrubArea {
                className = ClassName("scrub-area")
                NumberField.ScrubAreaCursor {
                    className = ClassName("scrub-cursor")
                }
            }

            NumberField.Group {
                className = ClassName("number-group")

                NumberField.Decrement {
                    className = ClassName("decrement")
                    +"-"
                }

                NumberField.Input {
                    className = ClassName("number-input")
                }

                NumberField.Increment {
                    className = ClassName("increment")
                    +"+"
                }
            }
        }
    }
}