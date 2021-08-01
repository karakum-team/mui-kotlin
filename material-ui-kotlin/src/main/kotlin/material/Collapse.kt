// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Collapse")
@file:JsNonModule

package material

external interface CollapseProps : react.RProps {
    /**
     * The content node to be collapsed.
     */
    var children: react.ReactNode

    var className: String

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The width (horizontal) or height (vertical) of the container when collapsed.
     * @default '0px'
     */
    var collapsedSize: dynamic

    /**
     * The component used for the root node.
     * Either a string to use a HTML element or a component.
     */
    var component: dynamic

    /**
     * The transition timing function.
     * You may specify a single easing or a object containing enter and exit values.
     */
    var easing: dynamic

    /**
     * If `true`, the component will transition in.
     */
    var `in`: Boolean

    /**
     * The transition orientation.
     * @default 'vertical'
     */
    var orientation: Union /* 'horizontal' | 'vertical' */

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     *
     * Set to 'auto' to automatically calculate transition time based on height.
     * @default duration.standard
     */
    var timeout: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 * The Collapse transition is used by the
 * [Vertical Stepper](https://material-ui.com/components/steppers/#vertical-stepper) StepContent component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
 *
 * Demos:
 *
 * - [Cards](https://material-ui.com/components/cards/)
 * - [Lists](https://material-ui.com/components/lists/)
 * - [Transitions](https://material-ui.com/components/transitions/)
 *
 * API:
 *
 * - [Collapse API](https://material-ui.com/api/collapse/)
 * - inherits [Transition API](https://reactcommunity.org/react-transition-group/transition#Transition-props)
 */

@JsName("default")
external val Collapse: react.FC<CollapseProps>
