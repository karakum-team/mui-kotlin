// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import js.array.ReadonlyArray

external interface AccordionRootProps :
    BaseUiDivProps {
    /**
     * The controlled value of the item(s) that should be expanded.
     *
     * To render an uncontrolled accordion, use the `defaultValue` prop instead.
     */
    var value: Any? /* ReadonlyArray<Any> */

    /**
     * The uncontrolled value of the item(s) that should be initially expanded.
     *
     * To render a controlled accordion, use the `value` prop instead.
     */
    var defaultValue: Any? /* ReadonlyArray<Any> */

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * Allows the browser's built-in page search to find and expand the panel contents.
     *
     * Overrides the `keepMounted` prop and uses `hidden="until-found"`
     * to hide the element without removing it from the DOM.
     * @default false
     */
    var hiddenUntilFound: Boolean?

    /**
     * Whether to keep the element in the DOM while the panel is closed.
     * This prop is ignored when `hiddenUntilFound` is used.
     * @default false
     */
    var keepMounted: Boolean?

    /**
     * Event handler called when an accordion item is expanded or collapsed.
     * Provides the new value as an argument.
     */
    var onValueChange: ((value: ReadonlyArray<Any>, eventDetails: AccordionRootChangeEventDetails) -> Unit)?

    /**
     * Whether multiple items can be open at the same time.
     * @default false
     */
    var multiple: Boolean?
}

external interface AccordionRootState {
    /**
     * The current value.
     */
    var value: Any? /* ReadonlyArray<Any> */

    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean
}

external interface AccordionRootChangeEventDetails : BaseUIChangeEventDetails
