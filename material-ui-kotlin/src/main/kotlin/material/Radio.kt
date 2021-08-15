// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Radio")
@file:JsNonModule

package material

external interface RadioProps : react.Props {
    /**
     * The icon to display when the component is checked.
     * @default <RadioButtonIcon checked />
     */
    var checkedIcon: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: RadioClasses

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'primary'
     */
    var color: Union /* 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' | 'default', RadioPropsColorOverrides */

    /**
     * If `true`, the component is disabled.
     */
    var disabled: Boolean

    /**
     * The icon to display when the component is unchecked.
     * @default <RadioButtonIcon />
     */
    var icon: react.ReactNode

    /**
     * The size of the component.
     * `small` is equivalent to the dense radio styling.
     * @default 'medium'
     */
    var size: Union /* 'small' | 'medium', RadioPropsSizeOverrides */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Radio Buttons](https://material-ui.com/components/radio-buttons/)
 *
 * API:
 *
 * - [Radio API](https://material-ui.com/api/radio/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val Radio: react.FC<RadioProps>
