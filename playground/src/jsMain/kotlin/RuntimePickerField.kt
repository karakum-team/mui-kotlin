import js.array.jsArrayOf
import js.objects.unsafeJso
import muix.pickers.DatePicker
import muix.pickers.DatePickerSlots
import muix.pickers.PickersSectionList
import muix.pickers.PickersSectionListRef
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useRef
import web.dom.ElementId

// The section-list component needs the private picker context supplied by DatePicker. A small field
// slot mounts the generated alias directly, so this test does not accidentally exercise MUI's own
// internal import of the component instead of the repaired Kotlin binding.
private val RuntimePickerField = FC<Props> {
    val sectionRef = useRef<PickersSectionListRef>(null)
    div {
        id = ElementId("runtime-picker-sections")
        PickersSectionList {
            elements = jsArrayOf()
            sectionListRef = sectionRef
            contentEditable = false
        }
    }
}

// DatePickerSlots is currently an empty generated interface; only refine the one field this fixture
// needs, without changing the generator's unrelated slot-type coverage.
private external interface RuntimePickerSlots : DatePickerSlots {
    var field: FC<Props>
}

val RuntimePicker = FC<Props> {
    DatePicker {
        slots = unsafeJso<RuntimePickerSlots> { field = RuntimePickerField }
    }
}
