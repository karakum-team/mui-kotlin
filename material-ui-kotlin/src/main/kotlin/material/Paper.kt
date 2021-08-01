// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Paper")
@file:JsNonModule

package material

external interface PaperProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * Shadow depth, corresponds to `dp` in the spec.
     * It accepts values between 0 and 24 inclusive.
     * @default 1
     */
    var elevation: Number

    /**
     * If `true`, rounded corners are disabled.
     * @default false
     */
    var square: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The variant to use.
     * @default 'elevation'
     */
    var variant: Union /* 'elevation' | 'outlined', PaperPropsVariantOverrides */
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
