// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface CollapseProps : react.RProps {
    /**
     * The content node to be collapsed.
     */
    var children: dynamic

    /**
     * The height of the container when collapsed.
     * @deprecated The prop was renamed to support the addition of horizontal orientation, use `collapsedSize` instead.
     */
    var collapsedHeight: dynamic

    /**
     * The height of the container when collapsed.
     */
    var collapsedSize: dynamic

    /**
     * The component used for the root node.
     * Either a string to use a HTML element or a component.
     */
    var component: dynamic

    /**
     * Enable this prop if you encounter 'Function components cannot be given refs',
     * use `unstable_createStrictModeTheme`,
     * and can't forward the ref in the passed `Component`.
     */
    var disableStrictModeCompat: dynamic

    /**
     * If `true`, the component will transition in.
     */
    var `in`: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     *
     * Set to 'auto' to automatically calculate transition time based on height.
     */
    var timeout: dynamic
}
