package playground.shell

import react.useEffectOnce
import react.useState
import web.events.invoke
import web.location.location
import web.window.hashChangeEvent
import web.window.window

sealed interface Route {
    data object Index : Route
    data object All : Route
    data class Single(val libraryId: String, val sampleId: String) : Route
    data class NotFound(val hash: String) : Route
}

fun parseRoute(hash: String): Route {
    val path = hash.removePrefix("#").removePrefix("/").removeSuffix("/")
    if (path.isEmpty())
        return Route.Index

    if (path == "all")
        return Route.All

    val parts = path.split("/")
    if (parts.size == 2 && parts.all { it.isNotEmpty() })
        return Route.Single(libraryId = parts[0], sampleId = parts[1])

    return Route.NotFound(hash)
}

fun useRoute(): Route {
    val (route, setRoute) = useState { parseRoute(location.hash) }

    useEffectOnce {
        window.hashChangeEvent().collect {
            setRoute(parseRoute(location.hash))
        }
    }

    return route
}
