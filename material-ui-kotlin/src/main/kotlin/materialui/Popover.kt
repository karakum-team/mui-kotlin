// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface PopoverProps : react.RProps {
    /**
     * A ref for imperative actions.
     * It currently only supports updatePosition() action.
     */
    var action: dynamic

    /**
     * A HTML element, or a function that returns it.
     * It's used to set the position of the popover.
     */
    var anchorEl: dynamic

    /**
     * This is the point on the anchor where the popover's
     * `anchorEl` will attach to. This is not used when the
     * anchorReference is 'anchorPosition'.
     *
     * Options:
     * vertical: [top, center, bottom];
     * horizontal: [left, center, right].
     */
    var anchorOrigin: dynamic

    /**
     * This is the position that may be used
     * to set the position of the popover.
     * The coordinates are relative to
     * the application's client area.
     */
    var anchorPosition: dynamic

    /**
     * This determines which anchor prop to refer to to set
     * the position of the popover.
     */
    var anchorReference: dynamic

    /**
     * The content of the component.
     */
    var children: dynamic

    /**
     * A HTML element, component instance, or function that returns either.
     * The `container` will passed to the Modal component.
     *
     * By default, it uses the body of the anchorEl's top-level document object,
     * so it's simply `document.body` most of the time.
     */
    var container: dynamic

    /**
     * The elevation of the popover.
     */
    var elevation: Number

    /**
     * This function is called in order to retrieve the content anchor element.
     * It's the opposite of the `anchorEl` prop.
     * The content anchor element should be an element inside the popover.
     * It's used to correctly scroll and set the position of the popover.
     * The positioning strategy tries to make the content anchor element just above the
     * anchor element.
     */
    var getContentAnchorEl: dynamic

    /**
     * Specifies how close to the edge of the window the popover can appear.
     */
    var marginThreshold: Number

    var onClose: dynamic

    /**
     * Callback fired before the component is entering.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEnter: dynamic

    /**
     * Callback fired when the component has entered.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEntered: dynamic

    /**
     * Callback fired when the component is entering.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onEntering: dynamic

    /**
     * Callback fired before the component is exiting.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExit: dynamic

    /**
     * Callback fired when the component has exited.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExited: dynamic

    /**
     * Callback fired when the component is exiting.
     * @deprecated Use the `TransitionProps` prop instead.
     */
    var onExiting: dynamic

    /**
     * If `true`, the popover is visible.
     */
    var open: Boolean

    /**
     * Props applied to the [`Paper`](/api/paper/) element.
     */
    var PaperProps: dynamic

    /**
     * This is the point on the popover which
     * will attach to the anchor's origin.
     *
     * Options:
     * vertical: [top, center, bottom, x(px)];
     * horizontal: [left, center, right, x(px)].
     */
    var transformOrigin: dynamic

    /**
     * The component used for the transition.
     * [Follow this guide](/components/transitions/#transitioncomponent-prop) to learn more about the requirements for this component.
     */
    var TransitionComponent: dynamic

    /**
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
 * - [Menus](https://material-ui.com/components/menus/)
 * - [Popover](https://material-ui.com/components/popover/)
 *
 * API:
 *
 * - [Popover API](https://material-ui.com/api/popover/)
 * - inherits [Modal API](https://material-ui.com/api/modal/)
 */
@JsName("default")
external val Popover: react.FC<PopoverProps>
