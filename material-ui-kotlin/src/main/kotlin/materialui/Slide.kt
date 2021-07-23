// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Slide")
@file:JsNonModule

package materialui

external interface SlideProps : react.RProps {
    /**
     * A single child content element.
     */
    var children: dynamic

    /**
     * Direction the child node will enter from.
     */
    var direction: dynamic /* 'left' | 'right' | 'up' | 'down' */

    /**
     * If `true`, show the component; triggers the enter or exit animation.
     */
    var `in`: dynamic

    var ref: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     */
    var timeout: dynamic
}

/**
 * The Slide transition is used by the [Drawer](https://material-ui.com/components/drawers/) component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
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
