import muix.pickers.MonthCalendar
import react.FC
import react.Fragment
import react.create

val Pickers = FC {
    Fragment.create {
        MonthCalendar {
            onChange = { _ -> }
        }
    }
}
