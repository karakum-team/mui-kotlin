package playground.shell

import react.FC
import react.Key
import react.Props
import react.dom.aria.AriaCurrent
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.nav
import react.dom.html.ReactHTML.small
import react.dom.html.ReactHTML.ul
import web.cssom.ClassName

external interface SidebarProps : Props {
    var route: Route
}

val Sidebar = FC<SidebarProps> { props ->
    val route = props.route

    nav {
        className = ClassName("pg-sidebar")
        ariaLabel = "Playground samples"

        ul {
            className = ClassName("pg-nav-top")

            NavLink {
                href = "#/"
                label = "Index"
                active = route == Route.Index
            }
            NavLink {
                href = "#/all"
                label = "All samples"
                active = route == Route.All
            }
        }

        for (library in Libraries.all) {
            val librarySamples = samplesOf(library)

            h3 {
                className = ClassName("pg-nav-library")
                +library.title
                small {
                    +"${library.npmPackage}@${library.version ?: "pinned"}"
                }
            }

            if (librarySamples.isEmpty()) {
                small {
                    className = ClassName("pg-nav-empty")
                    +"no samples yet"
                }
            } else {
                ul {
                    for (sample in librarySamples) {
                        NavLink {
                            key = Key(sample.key)
                            href = sample.hash
                            label = sample.title
                            active = route == Route.Single(sample.library.id, sample.id)
                        }
                    }
                }
            }
        }
    }
}

private external interface NavLinkProps : Props {
    var href: String
    var label: String
    var active: Boolean
}

private val NavLink = FC<NavLinkProps> { props ->
    li {
        a {
            href = props.href
            if (props.active) ariaCurrent = AriaCurrent.page
            +props.label
        }
    }
}
