// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Radio")
@file:JsNonModule

package materialui

external interface RadioProps : react.RProps {
    /**
     * The icon to display when the component is checked.
     */
    var checkedIcon: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' | 'default' */

    /**
     * If `true`, the radio will be disabled.
     */
    var disabled: Boolean

    /**
     * The icon to display when the component is unchecked.
     */
    var icon: react.ReactNode

    /**
     * The size of the radio.
     * `small` is equivalent to the dense radio styling.
     */
    var size: dynamic /* 'small' | 'medium' */
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
 * - inherits [IconButton API](https://material-ui.com/api/icon-button/)
 */
@JsName("default")
external val Radio: react.FC<RadioProps>
