package playground.samples.baseui

import baseui.DirectionalChangeReason
import baseui.NumberField
import baseui.NumberFieldRootChangeEventDetails
import baseui.NumberFieldRootCommitEventDetails
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState
import web.cssom.ClassName

val NumberFieldSample: Sample = Sample(
    id = "number-field",
    library = Libraries.BaseUi,
    title = "NumberField",
    kind = Kind.Check,
    covers = listOf(
        "NumberField.Root/ScrubArea/ScrubAreaCursor/Group/Decrement/Input/Increment (7 parts)",
        "NumberFieldRootChangeEventDetails (reason, direction)",
        "NumberFieldRootCommitEventDetails",
        "DirectionalChangeReason / Direction unions",
    ),
    fileName = "NumberFieldSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("1dfec5cd", "https://github.com/karakum-team/mui-kotlin/commit/1dfec5cd"),
    ),
    notes = "Mounts all seven parts, scrub area and cursor included. Pressing increment/decrement or using the " +
        "keyboard proves the change details carry `direction` from `ChangeEventCustomProperties` (an extra " +
        "file the generator had to translate), that the `DirectionalChangeReason` constants compare equal to " +
        "the reasons Base UI reports, and that the commit callback fires with its own details type.",
    content = FC<Props> {
        var currentValue by useState<Number?>(5)
        var lastReason by useState("-")
        var lastDirection by useState("-")
        var lastCommit by useState("-")

        div {
            className = ClassName("base-ui-number-field-sample")

            NumberField.Root {
                value = currentValue
                onValueChange = { value, details: NumberFieldRootChangeEventDetails ->
                    currentValue = value
                    lastReason = when (details.reason) {
                        DirectionalChangeReason.incrementPress.toString() -> "increment"
                        DirectionalChangeReason.decrementPress.toString() -> "decrement"
                        DirectionalChangeReason.keyboard.toString() -> "keyboard"
                        else -> details.reason
                    }
                    lastDirection = details.direction?.toString() ?: "-"
                }
                onValueCommitted = { value, details: NumberFieldRootCommitEventDetails ->
                    lastCommit = "$value/${details.reason}"
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
                        ariaLabel = "Quantity"
                    }

                    NumberField.Increment {
                        className = ClassName("increment")
                        +"+"
                    }
                }
            }

            StatusLine {
                id = "number-field-status"
                fields = listOf(
                    "value" to currentValue,
                    "reason" to lastReason,
                    "direction" to lastDirection,
                    "commit" to lastCommit,
                )
                className = ClassName("number-reason")
            }
        }
    },
)
