// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface ZoomProps : react.RProps {
    /**
     * A single child content element.
     */
    var children: dynamic

    /**
     * Enable this prop if you encounter 'Function components cannot be given refs',
     * use `unstable_createStrictModeTheme`,
     * and can't forward the ref in the child component.
     */
    var disableStrictModeCompat: Boolean

    /**
     * If `true`, the component will transition in.
     */
    var `in`: Boolean

    var ref: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     */
    var timeout: dynamic
}
