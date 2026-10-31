package playground.shell

import playground.dataNotFound
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.footer
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useEffect
import web.cssom.ClassName
import web.dom.document
import web.window.window

val Shell = FC<Props> {
    val route = useRoute()
    val single = (route as? Route.Single)?.let { findSample(it.libraryId, it.sampleId) }

    useEffect(route) {
        window.scrollTo(0.0, 0.0)
        document.title = "Playground — ${routeTitle(route, single)}"
    }

    ShellStyles()

    div {
        className = ClassName("pg-shell")

        Sidebar {
            this.route = route
        }

        main {
            className = ClassName("pg-main")

            when (route) {
                Route.Index -> IndexPage()
                Route.All -> AllPage()
                is Route.Single ->
                    if (single != null) {
                        SampleFrame {
                            key = Key(single.key)
                            sample = single
                            single.content()
                        }
                    } else {
                        NotFound {
                            hash = "#/${route.libraryId}/${route.sampleId}"
                        }
                    }

                is Route.NotFound -> NotFound {
                    hash = route.hash
                }
            }

            footer {
                className = ClassName("pg-footer")
                +"Kotlin ${Versions.of("kotlin")} · kotlin-wrappers ${Versions.of("kotlin.wrappers")}"
            }
        }
    }
}

private fun routeTitle(route: Route, single: Sample?): String =
    when (route) {
        Route.Index -> "Index"
        Route.All -> "All samples"
        is Route.Single -> single?.let { "${it.library.title} · ${it.title}" } ?: "Not found"
        is Route.NotFound -> "Not found"
    }

private external interface NotFoundProps : Props {
    var hash: String
}

private val NotFound = FC<NotFoundProps> { props ->
    section {
        className = ClassName("pg-not-found")
        dataNotFound = props.hash

        h1 { +"Not found" }
        p {
            +"No sample matches "
            code { +props.hash }
            +". Pick one from the index below."
        }
    }
    IndexPage()
}
