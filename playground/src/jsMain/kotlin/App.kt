import mui.icons.material.Abc
import mui.icons.material.SouthEast
import mui.material.SvgIcon
import react.FC
import react.create
import react.dom.client.createRoot
import react.dom.svg.ReactSVG.path
import web.dom.document
import web.html.HtmlTagName.div

private val App = FC {
    SvgIcon {
        path {
            d = "M9 16.17 4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
        }
    }

    Abc()
    SouthEast()

    MyAutocomplete()
    Pickers()
    SliderStylization()
}

private fun main() {
    val container = document.createElement(div)
    document.body.appendChild(container)

    createRoot(container)
        .render(App.create())
}
