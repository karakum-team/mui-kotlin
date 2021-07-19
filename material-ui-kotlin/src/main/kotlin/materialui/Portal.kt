// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface PortalProps : react.RProps {
    /**
     * The children to render into the `container`.
     */
    var children: dynamic

    /**
     * A HTML element, component instance, or function that returns either.
     * The `container` will have the portal children appended to it.
     *
     * By default, it uses the body of the top-level document object,
     * so it's simply `document.body` most of the time.
     */
    var container: dynamic

    /**
     * Disable the portal behavior.
     * The children stay within it's parent DOM hierarchy.
     */
    var disablePortal: Boolean

    /**
     * Callback fired once the children has been mounted into the `container`.
     *
     * This prop will be removed in v5, the ref can be used instead.
     * @deprecated Use the ref instead.
     */
    var onRendered: dynamic
}
