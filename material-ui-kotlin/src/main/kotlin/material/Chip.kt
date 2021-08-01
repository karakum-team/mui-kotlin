// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Chip")
@file:JsNonModule

package material

external interface ChipProps : react.RProps {
    /**
     * The Avatar element to display.
     */
    var avatar: react.ReactElement

    /**
     * This prop isn't supported.
     * Use the `component` prop if you need to change the children structure.
     */
    var children: dynamic

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * If `true`, the chip will appear clickable, and will raise when pressed,
     * even if the onClick prop is not defined.
     * If `false`, the chip will not appear clickable, even if onClick prop is defined.
     * This can be used, for example,
     * along with the component prop to indicate an anchor Chip is clickable.
     * Note: this controls the UI and does not affect the onClick event.
     */
    var clickable: Boolean

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'default'
     */
    var color: dynamic

    /**
     * Override the default delete icon element. Shown only if `onDelete` is set.
     */
    var deleteIcon: react.ReactElement

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean

    /**
     * Icon element.
     */
    var icon: react.ReactElement

    /**
     * The content of the component.
     */
    var label: react.ReactNode

    /**
     * Callback fired when the delete icon is clicked.
     * If set, the delete icon will be shown.
     */
    var onDelete: dynamic

    /**
     * The size of the component.
     * @default 'medium'
     */
    var size: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The variant to use.
     * @default 'filled'
     */
    var variant: dynamic
}

/**
 * Chips represent complex entities in small blocks, such as a contact.
 *
 * Demos:
 *
 * - [Chips](https://material-ui.com/components/chips/)
 *
 * API:
 *
 * - [Chip API](https://material-ui.com/api/chip/)
 */
@JsName("default")
external val Chip: react.FC<ChipProps>
