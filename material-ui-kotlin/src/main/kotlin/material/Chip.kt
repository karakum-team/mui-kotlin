// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Chip")
@file:JsNonModule

package material

external interface ChipProps : react.RProps {
    /**
     * Avatar element.
     */
    var avatar: dynamic

    /**
     * This prop isn't supported.
     * Use the `component` prop if you need to change the children structure.
     */
    var children: dynamic

    /**
     * If `true`, the chip will appear clickable, and will raise when pressed,
     * even if the onClick prop is not defined.
     * If false, the chip will not be clickable, even if onClick prop is defined.
     * This can be used, for example,
     * along with the component prop to indicate an anchor Chip is clickable.
     */
    var clickable: Boolean

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic

    /**
     * Override the default delete icon element. Shown only if `onDelete` is set.
     */
    var deleteIcon: dynamic

    /**
     * If `true`, the chip should be displayed in a disabled state.
     */
    var disabled: Boolean

    /**
     * Icon element.
     */
    var icon: dynamic

    /**
     * The content of the label.
     */
    var label: react.ReactNode

    /**
     * Callback function fired when the delete icon is clicked.
     * If set, the delete icon will be shown.
     */
    var onDelete: dynamic

    /**
     * The size of the chip.
     */
    var size: dynamic /* 'small' | 'medium' */

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'default' | 'outlined' */
}

/**
 * Chips represent complex entities in small blocks, such as a contact.
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
