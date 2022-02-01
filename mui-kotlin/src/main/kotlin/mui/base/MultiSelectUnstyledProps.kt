// Automatically generated - do not modify!

package mui.base

external interface MultiSelectUnstyledProps : react.Props {
    /**
     * The default selected values. Use when the component is not controlled.
     * @default []
     */
    var defaultValue: dynamic

    /**
     * Callback fired when an option is selected.
     */
    var onChange: ((value: TValue[])->Unit)?

    /**
     * Function that customizes the rendering of the selected values.
     */
    var renderValue: ((option: SelectOption<TValue>[])->react.ReactNode)?

    /**
     * The selected values.
     * Set to an empty array to deselect all options.
     */
    var value: dynamic
}

external interface MultiSelectUnstyledOwnerState {
    var active: Boolean

    var disabled: Boolean

    var open: Boolean

    var focusVisible: Boolean
}
