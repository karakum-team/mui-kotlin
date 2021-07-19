// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface AccordionProps : react.RProps {
    /**
     * The content of the accordion.
     */
    var children: dynamic

    /**
     * If `true`, expands the accordion by default.
     */
    var defaultExpanded: Boolean

    /**
     * If `true`, the accordion will be displayed in a disabled state.
     */
    var disabled: Boolean

    /**
     * If `true`, expands the accordion, otherwise collapse it.
     * Setting this prop enables control over the accordion.
     */
    var expanded: Boolean

    /**
     * Callback fired when the expand/collapse state is changed.
     *
     * @param {object} event The event source of the callback.
     * @param {boolean} expanded The `expanded` state of the accordion.
     */
    var onChange: dynamic

    /**
     * The component used for the collapse effect.
     * [Follow this guide](/components/transitions/#transitioncomponent-prop) to learn more about the requirements for this component.
     */
    var TransitionComponent: dynamic

    /**
     * Props applied to the [`Transition`](http://reactcommunity.org/react-transition-group/transition#Transition-props) element.
     */
    var TransitionProps: dynamic
}
