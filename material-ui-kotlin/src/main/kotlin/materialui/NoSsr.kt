// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/NoSsr")
@file:JsNonModule

package materialui

external interface NoSsrProps : react.RProps {
    /**
     * You can wrap a node.
     */
    var children: react.ReactNode

    /**
     * If `true`, the component will not only prevent server-side rendering.
     * It will also defer the rendering of the children into a different screen frame.
     */
    var defer: Boolean

    /**
     * The fallback content to display.
     */
    var fallback: react.ReactNode
}

/**
 * NoSsr purposely removes components from the subject of Server Side Rendering (SSR).
 *
 * This component can be useful in a variety of situations:
 *
 * -   Escape hatch for broken dependencies not supporting SSR.
 * -   Improve the time-to-first paint on the client by only rendering above the fold.
 * -   Reduce the rendering time on the server.
 * -   Under too heavy server load, you can turn on service degradation.
 * Demos:
 *
 * - [No Ssr](https://material-ui.com/components/no-ssr/)
 *
 * API:
 *
 * - [NoSsr API](https://material-ui.com/api/no-ssr/)
 */
@JsName("default")
external val NoSsr: react.FC<NoSsrProps>
