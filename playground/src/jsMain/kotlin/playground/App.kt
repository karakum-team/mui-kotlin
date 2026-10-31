package playground

import playground.shell.Shell
import react.create
import react.dom.client.createRoot
import web.dom.document
import web.html.HtmlTagName.div

private fun main() {
    val container = document.createElement(div)
    document.body.appendChild(container)

    createRoot(container)
        .render(Shell.create())
}
