// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Zoom")
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

/**
 * The Zoom transition can be used for the floating variant of the
 * [Button](https://material-ui.com/components/buttons/#floating-action-buttons) component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
 * Demos:
 *
 * - [Transitions](https://material-ui.com/components/transitions/)
 *
 * API:
 *
 * - [Zoom API](https://material-ui.com/api/zoom/)
 * - inherits [Transition API](https://reactcommunity.org/react-transition-group/transition#Transition-props)
 */
@JsName("default")
external val Zoom: react.FC<ZoomProps>
