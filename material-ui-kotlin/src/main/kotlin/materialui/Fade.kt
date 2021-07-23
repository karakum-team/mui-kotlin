// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Fade")
@file:JsNonModule

package materialui

external interface FadeProps : react.RProps {
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
 * The Fade transition is used by the [Modal](https://material-ui.com/components/modal/) component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
 * Demos:
 *
 * - [Transitions](https://material-ui.com/components/transitions/)
 *
 * API:
 *
 * - [Fade API](https://material-ui.com/api/fade/)
 * - inherits [Transition API](https://reactcommunity.org/react-transition-group/transition#Transition-props)
 */
@JsName("default")
external val Fade: react.FC<FadeProps>
