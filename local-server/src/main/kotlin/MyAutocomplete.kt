import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.TextField
import react.FC
import react.Props
import react.ReactNode
import react.create

val MyAutocomplete = FC<Props> {
    @Suppress("UPPER_BOUND_VIOLATED")
    Autocomplete<AutocompleteProps<String>> {
        options = arrayOf("foo", "bar", "baz")

        disablePortal = true

        renderInput = { params ->
            TextField.create {
                +params

                label = ReactNode("Choose value")
                variant = "outlined"
            }
        }
    }
}
