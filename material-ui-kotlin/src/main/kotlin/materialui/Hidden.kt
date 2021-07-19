// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface HiddenProps : react.RProps {
    /**
     * Specify which implementation to use.  'js' is the default, 'css' works better for
     * server-side rendering.
     */
    var implementation: dynamic

    /**
     * You can use this prop when choosing the `js` implementation with server-side rendering.
     *
     * As `window.innerWidth` is unavailable on the server,
     * we default to rendering an empty component during the first mount.
     * You might want to use an heuristic to approximate
     * the screen width of the client browser screen width.
     *
     * For instance, you could be using the user-agent or the client-hints.
     * https://caniuse.com/#search=client%20hint
     */
    var initialWidth: dynamic

    /**
     * If `true`, screens this size and down will be hidden.
     */
    var lgDown: Boolean

    /**
     * If `true`, screens this size and up will be hidden.
     */
    var lgUp: Boolean

    /**
     * If `true`, screens this size and down will be hidden.
     */
    var mdDown: Boolean

    /**
     * If `true`, screens this size and up will be hidden.
     */
    var mdUp: Boolean

    /**
     * Hide the given breakpoint(s).
     */
    var only: dynamic

    /**
     * If `true`, screens this size and down will be hidden.
     */
    var smDown: Boolean

    /**
     * If `true`, screens this size and up will be hidden.
     */
    var smUp: Boolean

    /**
     * If `true`, screens this size and down will be hidden.
     */
    var xlDown: Boolean

    /**
     * If `true`, screens this size and up will be hidden.
     */
    var xlUp: Boolean

    /**
     * If `true`, screens this size and down will be hidden.
     */
    var xsDown: Boolean

    /**
     * If `true`, screens this size and up will be hidden.
     */
    var xsUp: Boolean
}
