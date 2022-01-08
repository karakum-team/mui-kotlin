import mui.material.TableCell
import react.Fragment
import react.create
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.th

fun cell() {
    Fragment.create {
        TableCell {
            component = td
            component = th
        }
    }
}
