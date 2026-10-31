package playground.samples.material

import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.FormControlVariant
import mui.material.TextField
import playground.shell.Kind
import playground.shell.Libraries
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
    covers = listOf("Autocomplete", "TextField"),
    fileName = "AutocompleteSample.kt",
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
