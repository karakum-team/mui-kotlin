// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Slide")
@file:JsNonModule

package material

external interface SlideProps : react.RProps {
    /**
     * Perform the enter transition when it first mounts if `in` is also `true`.
     * Set this to `false` to disable this behavior.
     * @default true
     */
    var appear: Boolean

    /**
     * A single child content element.
     */
    var children: react.ReactElement

    /**
     * Direction the child node will enter from.
     * @default 'down'
     */
    var direction: dynamic /* 'left' | 'right' | 'up' | 'down' */

    /**
     * The transition timing function.
     * You may specify a single easing or a object containing enter and exit values.
     * @default {
     *   enter: easing.easeOut,
     *   exit: easing.sharp,
     * }
     */
    var easing: dynamic

    /**
     * If `true`, the component will transition in.
     */
    var `in`: dynamic

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
 * The Slide transition is used by the [Drawer](https://material-ui.com/components/drawers/) component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
 *
 * Demos:
 *
 * - [Dialogs](https://material-ui.com/components/dialogs/)
 * - [Transitions](https://material-ui.com/components/transitions/)
 *
 * API:
 *
 * - [Slide API](https://material-ui.com/api/slide/)
 * - inherits [Transition API](https://reactcommunity.org/react-transition-group/transition#Transition-props)
 */
@JsName("default")
external val Slide: react.FC<SlideProps>
