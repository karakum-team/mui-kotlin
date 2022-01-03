// Automatically generated - do not modify!

@file:JsModule("@mui/material/useMediaQuery")
@file:JsNonModule

package mui.material

external interface UseMediaQueryOptions {
    var defaultMatches: Boolean?

    var matchMedia: dynamic

    var noSsr: Boolean?

    var ssrMatchMedia: ((query: String) -> dynamic)?
}

@JsName("default")
external fun <Theme : Any> useMediaQuery(
    queryInput: String,
    options: UseMediaQueryOptions? = definedExternally,
): Boolean

@JsName("default")
external fun <Theme : Any> useMediaQuery(
    queryInput: (theme: Theme) -> String,
    options: UseMediaQueryOptions? = definedExternally,
): Boolean
