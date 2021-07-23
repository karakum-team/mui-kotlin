// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ExpansionPanel")
@file:JsNonModule

package materialui

external interface ExpansionPanelProps : react.RProps {
    /**
     * The content of the expansion panel.
     */
    var children: dynamic

    /**
     * If `true`, expands the panel by default.
     */
    var defaultExpanded: Boolean

    /**
     * If `true`, the panel will be displayed in a disabled state.
     */
    var disabled: Boolean

    /**
     * If `true`, expands the panel, otherwise collapse it.
     * Setting this prop enables control over the panel.
     */
    var expanded: Boolean

    /**
     * Callback fired when the expand/collapse state is changed.
     *
     * @param {object} event The event source of the callback.
     * @param {boolean} expanded The `expanded` state of the panel.
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

/**
 * ⚠️ The ExpansionPanel component was renamed to Accordion to use a more common naming convention.
 *
 * You should use `import { Accordion } from '@material-ui/core'`
 * or `import Accordion from '@material-ui/core/Accordion'`.
 * API:
 *
 * - [ExpansionPanel API](https://material-ui.com/api/expansion-panel/)
 * - inherits [Paper API](https://material-ui.com/api/paper/)
 */
@JsName("default")
external val ExpansionPanel: react.FC<ExpansionPanelProps>
