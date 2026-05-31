// Automatically generated - do not modify!

@file:JsModule("@mui/material/Radio")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
)

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface RadioProps :
    mui.system.StandardProps,
    SwitchBaseProps,
    mui.system.PropsWithSx {
    /**
     * The icon to display when the component is checked.
     * @default <RadioButtonIcon checked />
     */
    var checkedIcon: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: RadioClasses?

    /**
     * The color of the component.
     * It supports both default and custom theme colors, which can be added as shown in the
     * [palette customization guide](https://mui.com/material-ui/customization/palette/#custom-colors).
     * @default 'primary'
     */
    var color: RadioColor?

    /**
     * If `true`, the component is disabled.
     */
    var disabled: Boolean?

    /**
     * The icon to display when the component is unchecked.
     * @default <RadioButtonIcon />
     */
    var icon: react.ReactNode?

    /**
     * The size of the component.
     * `small` is equivalent to the dense radio styling.
     * @default 'medium'
     */
    var size: BaseSize?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

external interface RadioPropsSizeOverrides

external interface RadioPropsColorOverrides

external interface RadioRootSlotPropsOverrides

external interface RadioInputSlotPropsOverrides

external interface RadioSlots {
    /**
     * The component that renders the root slot.
     * @default SwitchBase
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the input slot.
     * @default SwitchBase's input
     */
    var input: react.ElementType<*>
}

external interface RadioSlotProps : react.Props {
    /** TS: SlotProps< React.ElementType<SwitchBaseProps>, RadioRootSlotPropsOverrides, RadioOwnerState > */
    var root: Any?

    /** TS: SlotProps<'input', RadioInputSlotPropsOverrides, RadioOwnerState> */
    var input: Any?
}

external interface RadioSlotsAndSlotProps : react.Props {
    var slots: RadioSlots?

    var slotProps: RadioSlotProps?
}

external interface RadioOwnerState

/**
 *
 * Demos:
 *
 * - [Radio Group](https://v6.mui.com/material-ui/react-radio-button/)
 *
 * API:
 *
 * - [Radio API](https://v6.mui.com/material-ui/api/radio/)
 * - inherits [ButtonBase API](https://v6.mui.com/material-ui/api/button-base/)
 */
@JsName("default")
external val Radio: react.FC<RadioProps>
