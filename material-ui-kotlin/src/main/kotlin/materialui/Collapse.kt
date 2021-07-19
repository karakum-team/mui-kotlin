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
    var disableStrictModeCompat: Boolean

    /**
     * If `true`, the component will transition in.
     */
    var `in`: Boolean

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     *
     * Set to 'auto' to automatically calculate transition time based on height.
     */
    var timeout: dynamic
}

/**
 * The Collapse transition is used by the
 * [Vertical Stepper](https://material-ui.com/components/steppers/#vertical-stepper) StepContent component.
 * It uses [react-transition-group](https://github.com/reactjs/react-transition-group) internally.
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
