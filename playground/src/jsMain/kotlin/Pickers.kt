import muix.pickers.CalendarPicker
import react.Fragment
import react.VFC
import react.create

val Pickers = VFC {
    Fragment.create {
        CalendarPicker {
            onChange = { _, _ -> }
        }
    }
}
