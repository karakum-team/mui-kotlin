// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Tooltip")
@file:JsNonModule

package material

external interface TooltipProps : react.RProps {
    /**
     * If `true`, adds an arrow to the tooltip.
     */
    var arrow: Boolean

    /**
     * Tooltip reference element.
     */
    var children: dynamic

    /**
     * Do not respond to focus events.
     */
    var disableFocusListener: Boolean

    /**
     * Do not respond to hover events.
     */
    var disableHoverListener: Boolean

    /**
     * Do not respond to long press touch events.
     */
    var disableTouchListener: Boolean

    /**
     * The number of milliseconds to wait before showing the tooltip.
     * This prop won't impact the enter touch delay (`enterTouchDelay`).
     */
    var enterDelay: Number

    /**
     * The number of milliseconds to wait before showing the tooltip when one was already recently opened.
     */
    var enterNextDelay: Number

    /**
     * The number of milliseconds a user must touch the element before showing the tooltip.
     */
    var enterTouchDelay: Number

    /**
     * This prop is used to help implement the accessibility logic.
     * If you don't provide this prop. It falls back to a randomly generated id.
     */
    var id: String

    /**
     * Makes a tooltip interactive, i.e. will not close when the user
     * hovers over the tooltip before the `leaveDelay` is expired.
     */
    var interactive: Boolean

    /**
     * The number of milliseconds to wait before hiding the tooltip.
     * This prop won't impact the leave touch delay (`leaveTouchDelay`).
     */
    var leaveDelay: Number

    /**
     * The number of milliseconds after the user stops touching an element before hiding the tooltip.
     */
    var leaveTouchDelay: Number

    /**
     * Callback fired when the component requests to be closed.
     *
     * @param {object} event The event source of the callback.
     */
    var onClose: dynamic

    /**
     * Callback fired when the component requests to be open.
     *
     * @param {object} event The event source of the callback.
     */
    var onOpen: dynamic

    /**
     * If `true`, the tooltip is shown.
     */
    var open: Boolean

    /**
     * Tooltip placement.
     */
    var placement: dynamic

    /**
     * The component used for the popper.
     */
    var PopperComponent: dynamic

    /**
     * Props applied to the [`Popper`](/api/popper/) element.
     */
    var PopperProps: dynamic

    /**
     * Tooltip title. Zero-length titles string are never displayed.
     */
    var title: dynamic

    /**
     * The component used for the transition.
     * [Follow this guide](/components/transitions/#transitioncomponent-prop) to learn more about the requirements for this component.
     */
    var TransitionComponent: dynamic

    /**
     * Props applied to the [`Transition`](http://reactcommunity.org/react-transition-group/transition#Transition-props) element.
     */
    var TransitionProps: dynamic
}

/**
 *
 * Demos:
 *
 * - [Tooltips](https://material-ui.com/components/tooltips/)
 *
 * API:
 *
 * - [Tooltip API](https://material-ui.com/api/tooltip/)
 */
@JsName("default")
external val Tooltip: react.FC<TooltipProps>
