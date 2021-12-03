import kotlinext.js.Object
import kotlinext.js.ReadonlyArray
import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.TextField
import react.Props
import react.ReactNode
import react.buildElement
import react.fc

inline var <T : Any> AutocompleteProps<T>.options: ReadonlyArray<T>
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
                            // TODO: Use `unaryPlus` instead
                            Object.assign(this, params)

                            label = ReactNode("Choose value")
                            variant = "outlined"
                        }
                    }
                }
            }
        }
    }
}
