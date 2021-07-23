// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Paper")
@file:JsNonModule

package materialui

external interface PaperProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * The component used for the root node.
     * Either a string to use a HTML element or a component.
     */
    var component: dynamic

    /**
     * Shadow depth, corresponds to `dp` in the spec.
     * It accepts values between 0 and 24 inclusive.
     */
    var elevation: Number

    /**
     * If `true`, rounded corners are disabled.
     */
    var square: Boolean

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'elevation' | 'outlined' */
}

/**
 *
 * Demos:
 *
 * - [Cards](https://material-ui.com/components/cards/)
 * - [Paper](https://material-ui.com/components/paper/)
 *
 * API:
 *
 * - [Paper API](https://material-ui.com/api/paper/)
 */
@JsName("default")
external val Paper: react.FC<PaperProps>
