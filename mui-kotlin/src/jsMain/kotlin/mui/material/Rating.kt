// Automatically generated - do not modify!

@file:JsModule("@mui/material/Rating")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface RatingProps :
    RatingOwnProps,
    react.dom.html.HTMLAttributes<web.html.HTMLSpanElement>,
    mui.types.PropsWithComponent

external interface IconContainerProps :
    react.dom.html.HTMLAttributes<web.html.HTMLSpanElement> {
    var value: Number
}

external interface RatingPropsSizeOverrides

external interface RatingRootSlotPropsOverrides

external interface RatingLabelSlotPropsOverrides

external interface RatingIconSlotPropsOverrides

external interface RatingDecimalSlotPropsOverrides

external interface RatingSlots {
    /**
     * The component used for the root slot.
     * @default 'span'
     */
    var root: react.ElementType<*>

    /**
     * The component used for the label slot.
     * @default 'label'
     */
    var label: react.ElementType<*>

    /**
     * The component used for the icon slot.
     * @default 'span'
     */
    var icon: react.ElementType<*>

    /**
     * The component used fo r the decimal slot.
     * @default 'span'
     */
    var decimal: react.ElementType<*>
}

external interface RatingSlotProps : react.Props {
    /** TS: SlotProps<'span', RatingRootSlotPropsOverrides, RatingOwnerState> */
    var root: Any?

    /** TS: SlotProps<'label', RatingLabelSlotPropsOverrides, RatingOwnerState> */
    var label: Any?

    /** TS: SlotProps<'span', RatingIconSlotPropsOverrides, RatingOwnerState> */
    var icon: Any?

    /** TS: SlotProps<'span', RatingDecimalSlotPropsOverrides, RatingOwnerState> */
    var decimal: Any?
}

external interface RatingSlotsAndSlotProps : react.Props {
    var slots: RatingSlots?

    var slotProps: RatingSlotProps?
}

external interface RatingOwnProps :
    RatingSlotsAndSlotProps,
    mui.system.PropsWithSx {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: RatingClasses?

    /**
     * The default value. Use when the component is not controlled.
     * @default null
     */
    var defaultValue: Any? /* Number */

    /**
     * If `true`, the component is disabled.
     * @default false
     */
    var disabled: Boolean?

    /**
     * The icon to display when empty.
     * @default <StarBorder fontSize="inherit" />
     */
    var emptyIcon: react.ReactNode?

    /**
     * The label read when the rating input is empty.
     * @default 'Empty'
     */
    var emptyLabelText: react.ReactNode?

    /**
     * Accepts a function which returns a string value that provides a user-friendly name for the current value of the rating.
     * This is important for screen reader users.
     *
     * For localization purposes, you can use the provided [translations](https://mui.com/material-ui/guides/localization/).
     * @param {number} value The rating label's value to format.
     * @returns {string}
     * @default function defaultLabelText(value) {
     *   return `${value || '0'} Star${value !== 1 ? 's' : ''}`;
     * }
     */
    var getLabelText: ((value: Number) -> String)?

    /**
     * If `true`, only the selected icon will be highlighted.
     * @default false
     */
    var highlightSelectedOnly: Boolean?

    /**
     * The icon to display.
     * @default <Star fontSize="inherit" />
     */
    var icon: react.ReactNode?

    /**
     * The component containing the icon.
     * @deprecated Use `slotProps.icon.component` instead. This prop will be removed in v7. See [Migrating from deprecated APIs](/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     * @default function IconContainer(props) {
     *   const { value, ...other } = props;
     *   return <span {...other} />;
     * }
     */
    var IconContainerComponent: react.ElementType<IconContainerProps>?

    /**
     * Maximum rating.
     * @default 5
     */
    var max: Number?

    /**
     * The name attribute of the radio `input` elements.
     * This input `name` should be unique within the page.
     * Being unique within a form is insufficient since the `name` is used to generate IDs.
     */
    var name: String?

    /**
     * Callback fired when the value changes.
     * @param {React.SyntheticEvent} event The event source of the callback.
     * @param {number|null} value The new value.
     */
    var onChange: ((event: react.dom.events.SyntheticEvent<*, *>, value: Number?) -> Unit)?

    /**
     * Callback function that is fired when the hover state changes.
     * @param {React.SyntheticEvent} event The event source of the callback.
     * @param {number} value The new value.
     */
    var onChangeActive: ((event: react.dom.events.SyntheticEvent<*, *>, value: Number) -> Unit)?

    /**
     * The minimum increment value change allowed.
     * @default 1
     */
    var precision: Number?

    /**
     * Removes all hover effects and pointer events.
     * @default false
     */
    var readOnly: Boolean?

    /**
     * The size of the component.
     * @default 'medium'
     */
    var size: Size?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?

    /**
     * The rating value.
     */
    var value: Number?
}

external interface RatingOwnerState

/**
 *
 * Demos:
 *
 * - [Rating](https://v6.mui.com/material-ui/react-rating/)
 *
 * API:
 *
 * - [Rating API](https://v6.mui.com/material-ui/api/rating/)
 */
@JsName("default")
external val Rating: react.FC<RatingProps>
