// Automatically generated - do not modify!

@file:Suppress(
"VIRTUAL_MEMBER_HIDDEN",
"VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId
import web.dom.Element

external interface MenuRadioItemProps: 
NonNativeButtonProps,
BaseUiDivProps {
/**
 * Value of the radio item.
 * This is the value that will be set in the MenuRadioGroup when the item is selected.
 */
var value: Any

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

external interface MenuRadioItemState {
/**
 * Whether the radio item should ignore user interaction.
 */
var disabled: Boolean

/**
 * Whether the radio item is currently highlighted.
 */
var highlighted: Boolean

/**
 * Whether the radio item is currently selected.
 */
var checked: Boolean
}
