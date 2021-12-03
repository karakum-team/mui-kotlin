import kotlinext.js.Object
import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.BaseTextFieldProps
import mui.material.TextField
import react.Props
import react.ReactNode
import react.buildElement
import react.fc

inline var <T : Any> AutocompleteProps<T>.options: Array<T>
    get() = asDynamic().options
    set(value) {
        asDynamic().options = value
    }

val MyAutocomplete = fc<Props>("MyAutocomplete") {
    Autocomplete<AutocompleteProps<String>> {
        attrs {
            options = arrayOf("foo", "bar", "baz")

            disablePortal = true

            renderInput = { params ->
                buildElement {
                    TextField {
                        attrs {
                            Object.assign(this, params)

                            @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
                            this as BaseTextFieldProps
                            label = ReactNode("Choose value")
                            variant = "outlined"
                        }
                    }
                }
            }
        }
    }
}
