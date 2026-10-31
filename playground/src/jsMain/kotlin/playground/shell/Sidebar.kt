package playground.shell

import react.ChildrenBuilder
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

            navLink("#/", "Index", route == Route.Index)
            navLink("#/all", "All samples", route == Route.All)
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
                        val active = route == Route.Single(sample.library.id, sample.id)
                        navLink(sample.hash, sample.title, active)
                    }
                }
            }
        }
    }
}

private fun ChildrenBuilder.navLink(
    href: String,
    label: String,
    active: Boolean,
) {
    li {
        key = Key(href)
        a {
            this.href = href
            if (active) ariaCurrent = AriaCurrent.page
            +label
        }
    }
}
