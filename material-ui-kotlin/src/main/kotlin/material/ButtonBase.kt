// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ButtonBase")
@file:JsNonModule

package material

external interface ButtonBaseProps : react.RProps {
    /**
     * A ref for imperative actions.
     * It currently only supports `focusVisible()` action.
     */
    var action: dynamic

    /**
     * @ignore
     *
     * Use that prop to pass a ref to the native button component.
     * @deprecated Use `ref` instead.
     */
    var buttonRef: dynamic

    /**
     * If `true`, the ripples will be centered.
     * They won't start at the cursor interaction position.
     */
    var centerRipple: Boolean

    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * If `true`, the base button will be disabled.
     */
    var disabled: Boolean

    /**
     * If `true`, the ripple effect will be disabled.
     *
     * ⚠️ Without a ripple there is no styling for :focus-visible by default. Be sure
     * to highlight the element by applying separate styles with the `focusVisibleClassName`.
     */
    var disableRipple: Boolean

    /**
     * If `true`, the touch ripple effect will be disabled.
     */
    var disableTouchRipple: Boolean

    /**
     * If `true`, the base button will have a keyboard focus ripple.
     */
    var focusRipple: Boolean

    /**
     * This prop can help identify which element has keyboard focus.
     * The class name will be applied when the element gains the focus through keyboard interaction.
     * It's a polyfill for the [CSS :focus-visible selector](https://drafts.csswg.org/selectors-4/#the-focus-visible-pseudo).
     * The rationale for using this feature [is explained here](https://github.com/WICG/focus-visible/blob/master/explainer.md).
     * A [polyfill can be used](https://github.com/WICG/focus-visible) to apply a `focus-visible` class to other components
     * if needed.
     */
    var focusVisibleClassName: String

    /**
     * Callback fired when the component is focused with a keyboard.
     * We trigger a `onFocus` callback too.
     */
    var onFocusVisible: dynamic

    // @types/react is stricter
    var tabIndex: dynamic

    /**
     * Props applied to the `TouchRipple` element.
     */
    var TouchRippleProps: dynamic
}

/**
 * `ButtonBase` contains as few styles as possible.
 * It aims to be a simple building block for creating a button.
 * It contains a load of style reset and some focus/ripple logic.
 * Demos:
 *
 * - [Buttons](https://material-ui.com/components/buttons/)
 *
 * API:
 *
 * - [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val ButtonBase: react.FC<ButtonBaseProps>
