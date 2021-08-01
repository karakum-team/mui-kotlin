// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Switch")
@file:JsNonModule

package material

external interface SwitchProps : react.RProps {
    /**
     * The icon to display when the component is checked.
     */
    var checkedIcon: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'primary'
     */
    var color: dynamic

    /**
     * If `true`, the component is disabled.
     */
    var disabled: Boolean

    /**
     * The icon to display when the component is unchecked.
     */
    var icon: react.ReactNode

    /**
     * The size of the component.
     * `small` is equivalent to the dense switch styling.
     * @default 'medium'
     */
    var size: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The value of the component. The DOM API casts this to a string.
     * The browser uses "on" as the default value.
     */
    var value: dynamic
}

/**
 *
 * Demos:
 *
 * - [Switches](https://material-ui.com/components/switches/)
 * - [Transfer List](https://material-ui.com/components/transfer-list/)
 *
 * API:
 *
 * - [Switch API](https://material-ui.com/api/switch/)
 * - inherits [IconButton API](https://material-ui.com/api/icon-button/)
 */
@JsName("default")
external val Switch: react.FC<SwitchProps>
