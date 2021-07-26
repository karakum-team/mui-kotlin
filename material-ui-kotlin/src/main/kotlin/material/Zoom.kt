// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Zoom")
@file:JsNonModule

package material

external interface ZoomProps : react.RProps {
    /**
     * Perform the enter transition when it first mounts if `in` is also `true`.
     * Set this to `false` to disable this behavior.
     * @default true
     */
    var appear: Boolean

    /**
     * A single child content element.
     */
    var children: dynamic

    /**
     * The transition timing function.
     * You may specify a single easing or a object containing enter and exit values.
     */
    var easing: dynamic

    /**
     * If `true`, the component will transition in.
     */
    var `in`: Boolean

    var ref: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     * @default {
     *   enter: duration.enteringScreen,
     *   exit: duration.leavingScreen,
     * }
     */
    var timeout: dynamic
}

/**
 * The Zoom transition can be used for the floating variant of the
 * [Button](https://material-ui.com/components/buttons/#floating-action-buttons) component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
 *
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
