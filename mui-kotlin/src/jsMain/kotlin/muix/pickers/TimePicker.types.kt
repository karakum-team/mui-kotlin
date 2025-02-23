// Automatically generated - do not modify!

package muix.pickers

external interface TimePickerProps<TDate, TEnableAccessibleFieldDOMStructure> : react.Props {
    /**
     * CSS media query when `Mobile` mode will be changed to `Desktop`.
     * @default '@media (pointer: fine)'
     * @example '@media (min-width: 720px)' or theme.breakpoints.up("sm")
     */
    var desktopModeMediaQuery: String?

    /**
     * Overridable component slots.
     * @default {}
     */
    var slots: TimePickerSlots<TDate>?

    /**
     * The props used for each component slot.
     * @default {}
     */
    var slotProps: TimePickerSlotProps<TDate, TEnableAccessibleFieldDOMStructure>?
}

external interface TimePickerSlots<TDate>

external interface TimePickerSlotProps<TDate, TEnableAccessibleFieldDOMStructure> : react.Props
