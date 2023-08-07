import muix.pickers.CalendarPicker
import react.FC
import react.Fragment
import react.create

val Pickers = FC {
    Fragment.create {
        CalendarPicker {
            onChange = { _, _ -> }
        }
    }
}
