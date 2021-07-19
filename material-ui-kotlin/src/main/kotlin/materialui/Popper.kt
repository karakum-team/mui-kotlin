// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface PopperProps : react.RProps {
    var ref: dynamic

    /**
     * A HTML element, [referenceObject](https://popper.js.org/docs/v1/#referenceObject),
     * or a function that returns either.
     * It's used to set the position of the popper.
     * The return value will passed as the reference object of the Popper instance.
     */
    var anchorEl: dynamic

    /**
     * Popper render function or node.
     */
    var children: dynamic

    /**
     * A HTML element, component instance, or function that returns either.
     * The `container` will have the portal children appended to it.
     *
     * By default, it uses the body of the top-level document object,
     * so it's simply `document.body` most of the time.
     */
    var container: dynamic

    /**
     * Disable the portal behavior.
     * The children stay within it's parent DOM hierarchy.
     */
    var disablePortal: dynamic

    /**
     * Always keep the children in the DOM.
     * This prop can be useful in SEO situation or
     * when you want to maximize the responsiveness of the Popper.
     */
    var keepMounted: Boolean

    /**
     * Popper.js is based on a "plugin-like" architecture,
     * most of its features are fully encapsulated "modifiers".
     *
     * A modifier is a function that is called each time Popper.js needs to
     * compute the position of the popper.
     * For this reason, modifiers should be very performant to avoid bottlenecks.
     * To learn how to create a modifier, [read the modifiers documentation](https://popper.js.org/docs/v1/#modifiers).
     */
    var modifiers: Any

    /**
     * If `true`, the popper is visible.
     */
    var open: Boolean

    /**
     * Popper placement.
     */
    var placement: dynamic

    /**
     * Options provided to the [`popper.js`](https://popper.js.org/docs/v1/) instance.
     */
    var popperOptions: Any

    /**
     * A ref that points to the used popper instance.
     */
    var popperRef: dynamic

    /**
     * Help supporting a react-transition-group/Transition component.
     */
    var transition: Boolean
}

/**
 * Poppers rely on the 3rd party library [Popper.js](https://popper.js.org/docs/v1/) for positioning.
 * Demos:
 *
 * - [Autocomplete](https://material-ui.com/components/autocomplete/)
 * - [Menus](https://material-ui.com/components/menus/)
 * - [Popper](https://material-ui.com/components/popper/)
 *
 * API:
 *
 * - [Popper API](https://material-ui.com/api/popper/)
 */
@JsName("default")
external val Popper: react.FC<PopperProps>
