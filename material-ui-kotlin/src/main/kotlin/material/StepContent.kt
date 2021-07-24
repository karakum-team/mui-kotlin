// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/StepContent")
@file:JsNonModule

package material

external interface StepContentProps : react.RProps {
    /**
     * Step content.
     */
    var children: react.ReactNode

    /**
     * The component used for the transition.
     * [Follow this guide](/components/transitions/#transitioncomponent-prop) to learn more about the requirements for this component.
     */
    var TransitionComponent: dynamic

    /**
     * Adjust the duration of the content expand transition.
     * Passed as a prop to the transition component.
     *
     * Set to 'auto' to automatically calculate transition time based on height.
     */
    var transitionDuration: dynamic

    /**
     * Props applied to the [`Transition`](http://reactcommunity.org/react-transition-group/transition#Transition-props) element.
     */
    var TransitionProps: dynamic
}

/**
 *
 * Demos:
 *
 * - [Steppers](https://material-ui.com/components/steppers/)
 *
 * API:
 *
 * - [StepContent API](https://material-ui.com/api/step-content/)
 */
@JsName("default")
external val StepContent: react.FC<StepContentProps>
