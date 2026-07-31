// Automatically generated - do not modify!

@file:Suppress(
"VIRTUAL_MEMBER_HIDDEN",
"VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId
import web.dom.Element
import web.events.Event

external interface MenuCheckboxItemProps: 
NonNativeButtonProps,
BaseUiDivProps {
/**
 * Whether the checkbox item is currently ticked.
 *
 * To render an uncontrolled checkbox item, use the `defaultChecked` prop instead.
 */
var checked: Boolean?

/**
 * Whether the checkbox item is initially ticked.
 *
 * To render a controlled checkbox item, use the `checked` prop instead.
 * @default false
 */
var defaultChecked: Boolean?

/**
 * Event handler called when the checkbox item is ticked or unticked.
 */
var onCheckedChange: ((checked: Boolean, eventDetails: MenuCheckboxItemChangeEventDetails)->Unit)?

/**
 * The click handler for the menu item.
 */
var onClick: Any? /* BaseUiDivProps['onClick'] */

/**
 * Whether the component should ignore user interaction.
 * @default false
 */
var disabled: Boolean?

/**
 * Overrides the text label to use when the item is matched during keyboard text navigation.
 */
var label: String?

/**
 * @ignore
 */
var id: ElementId?

/**
 * Whether to close the menu when the item is clicked.
 * @default false
 */
var closeOnClick: Boolean?
}

external interface MenuCheckboxItemState {
/**
 * Whether the checkbox item should ignore user interaction.
 */
var disabled: Boolean

/**
 * Whether the checkbox item is currently highlighted.
 */
var highlighted: Boolean

/**
 * Whether the checkbox item is currently ticked.
 */
var checked: Boolean
}

external interface MenuCheckboxItemChangeEventDetails : BaseUIChangeEventDetails
