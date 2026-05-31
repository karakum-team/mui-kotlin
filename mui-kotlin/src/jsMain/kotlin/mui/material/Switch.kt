// Automatically generated - do not modify!

@file:JsModule("@mui/material/Switch")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
)

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface SwitchProps :
    mui.system.StandardProps,
    SwitchBaseProps,
    mui.system.PropsWithSx {
    /**
     * The icon to display when the component is checked.
     */
    var checkedIcon: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: SwitchClasses?

    /**
     * The color of the component.
     * It supports both default and custom theme colors, which can be added as shown in the
     * [palette customization guide](https://mui.com/material-ui/customization/palette/#custom-colors).
     * @default 'primary'
     */
    var color: SwitchColor?

    /**
     * If `true`, the component is disabled.
     */
    var disabled: Boolean?

    /**
     * The icon to display when the component is unchecked.
     */
    var icon: react.ReactNode?

    /**
     * The size of the component.
     * `small` is equivalent to the dense switch styling.
     * @default 'medium'
     */
    var size: BaseSize?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?

    /**
     * The value of the component. The DOM API casts this to a string.
     * The browser uses "on" as the default value.
     */
    var value: Any?
}

external interface SwitchPropsSizeOverrides

external interface SwitchPropsColorOverrides

external interface SwitchRootSlotPropsOverrides

external interface SwitchTrackSlotPropsOverrides

external interface SwitchThumbSlotPropsOverrides

external interface SwitchSwitchBaseSlotPropsOverrides

external interface SwitchInputSlotPropsOverrides

external interface SwitchSlots {
    /**
     * The component that renders the root slot.
     * @default 'span'
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the track slot.
     * @default 'span'
     */
    var track: react.ElementType<*>

    /**
     * The component that renders the thumb slot.
     * @default 'span'
     */
    var thumb: react.ElementType<*>

    /**
     * The component that renders the switchBase slot.
     * @default SwitchBase
     */
    var switchBase: react.ElementType<*>

    /**
     * The component that renders the switchBase's input slot.
     * @default SwitchBaseInput
     */
    var input: react.ElementType<*>
}

external interface SwitchSlotProps : react.Props {
    /** TS: SlotProps<'span', SwitchRootSlotPropsOverrides, SwitchOwnerState> */
    var root: react.dom.html.HTMLAttributes<web.html.HTMLSpanElement>?

    /** TS: SlotProps<'span', SwitchTrackSlotPropsOverrides, SwitchOwnerState> */
    var track: react.dom.html.HTMLAttributes<web.html.HTMLSpanElement>?

    /** TS: SlotProps<'span', SwitchThumbSlotPropsOverrides, SwitchOwnerState> */
    var thumb: react.dom.html.HTMLAttributes<web.html.HTMLSpanElement>?

    /** TS: SlotProps< React.ElementType<SwitchBaseProps>, SwitchSwitchBaseSlotPropsOverrides, SwitchOwnerState > */
    var switchBase: SwitchBaseProps?

    /** TS: SlotProps<'input', SwitchInputSlotPropsOverrides, SwitchOwnerState> */
    var input: react.dom.html.InputHTMLAttributes<web.html.HTMLInputElement>?
}

external interface SwitchSlotsAndSlotProps : react.Props {
    var slots: SwitchSlots?

    var slotProps: SwitchSlotProps?
}

external interface SwitchOwnerState

/**
 *
 * Demos:
 *
 * - [Switch](https://v6.mui.com/material-ui/react-switch/)
 * - [Transfer List](https://v6.mui.com/material-ui/react-transfer-list/)
 *
 * API:
 *
 * - [Switch API](https://v6.mui.com/material-ui/api/switch/)
 * - inherits [IconButton API](https://v6.mui.com/material-ui/api/icon-button/)
 */
@JsName("default")
external val Switch: react.FC<SwitchProps>
