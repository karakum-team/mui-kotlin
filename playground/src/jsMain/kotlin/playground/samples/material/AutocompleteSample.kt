package playground.samples.material

import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.FormControlVariant
import mui.material.TextField
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import react.FC
import react.Props
import react.ReactNode
import react.create

val AutocompleteSample: Sample = Sample(
    id = "autocomplete",
    library = Libraries.Material,
    title = "Autocomplete",
    kind = Kind.Showcase,
    covers = listOf(
        "Autocomplete<AutocompleteProps<String>>.options/disablePortal/renderInput",
        "TextField.label/variant",
        "Props?.unaryPlus (+params)",
    ),
    fileName = "AutocompleteSample.kt",
    refs = listOf(
        Ref(
            "docs/plans/base-ui.md — Open gaps (`+props`)",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md#open-gaps-in-the-generated-output",
        ),
    ),
    notes = "Only running this proves that `+params` copies the ref and input props `renderInput` receives onto " +
            "`TextField`: focusing and typing into the field must open the options list and filter it.",
    content = FC<Props> {
        Autocomplete<AutocompleteProps<String>> {
            options = arrayOf("foo", "bar", "baz")

            disablePortal = true

            renderInput = { params ->
                TextField.create {
                    +params

                    label = ReactNode("Choose value")
                    variant = FormControlVariant.outlined
                }
            }
        }
    },
)
