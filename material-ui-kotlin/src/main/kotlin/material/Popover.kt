// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Popover")
@file:JsNonModule

package material

external interface PopoverProps : react.RProps {
    /**
     * A ref for imperative actions.
     * It currently only supports updatePosition() action.
     */
    var action: react.Ref<dynamic>

    /**
     * An HTML element, or a function that returns one.
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
     * @default {
     *   vertical: 'top',
     *   horizontal: 'left',
     * }
     */
    var anchorOrigin: PopoverOrigin

    /**
     * This is the position that may be used to set the position of the popover.
     * The coordinates are relative to the application's client area.
     */
    var anchorPosition: PopoverPosition

    /**
     * This determines which anchor prop to refer to when setting
     * the position of the popover.
     * @default 'anchorEl'
     */
    var anchorReference: PopoverReference

    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: PopoverClasses

    /**
     * An HTML element, component instance, or function that returns either.
     * The `container` will passed to the Modal component.
     *
     * By default, it uses the body of the anchorEl's top-level document object,
     * so it's simply `document.body` most of the time.
     */
    var container: dynamic /* ModalProps['container'] */

    /**
     * The elevation of the popover.
     * @default 8
     */
    var elevation: Number

    /**
     * Specifies how close to the edge of the window the popover can appear.
     * @default 16
     */
    var marginThreshold: Number

    var onClose: dynamic /* ModalProps['onClose'] */

    /**
     * If `true`, the component is shown.
     */
    var open: Boolean

    /**
     * Props applied to the [`Paper`](/api/paper/) element.
     * @default {}
     */
    var PaperProps: PaperProps

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * This is the point on the popover which
     * will attach to the anchor's origin.
     *
     * Options:
     * vertical: [top, center, bottom, x(px)];
     * horizontal: [left, center, right, x(px)].
     * @default {
     *   vertical: 'top',
     *   horizontal: 'left',
     * }
     */
    var transformOrigin: PopoverOrigin

    /**
     * The component used for the transition.
     * [Follow this guide](/components/transitions/#transitioncomponent-prop) to learn more about the requirements for this component.
     * @default Grow
     */
    var TransitionComponent: dynamic

    /**
     * Set to 'auto' to automatically calculate transition time based on height.
     * @default 'auto'
     */
    var transitionDuration: dynamic

    /**
     * Props applied to the transition element.
     * By default, the element is based on this [`Transition`](http://reactcommunity.org/react-transition-group/transition) component.
     * @default {}
     */
    var TransitionProps: TransitionProps
}

external interface PopoverOrigin {
    var vertical: Union /* 'top' | 'center' | 'bottom' | number */

    var horizontal: Union /* 'left' | 'center' | 'right' | number */
}

external interface PopoverPosition {
    var top: Number

    var left: Number
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
