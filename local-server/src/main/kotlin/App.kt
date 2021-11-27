import kotlinx.browser.document
import mui.material.SvgIcon
import react.dom.render

fun App() {}

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    render(container) {
        SvgIcon {

        }
    }
}
