import mui.material.TableCell
import react.Fragment
import react.create
import react.dom.html.ReactHTML

fun cell() {
    val application = Fragment.create {
        TableCell {
            component = ReactHTML.td
            component = ReactHTML.th
        }
    }
}
