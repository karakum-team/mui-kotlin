// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.PropsWithChildren
import react.ReactNode

external interface MenuRootProps : PropsWithChildren {
    /**
     * Whether the menu is initially open.
     *
     * To render a controlled menu, use the `open` prop instead.
     * @default false
     */
    var defaultOpen: Boolean?

    /**
     * Whether to loop keyboard focus back to the first item
     * when the end of the list is reached while using the arrow keys.
     * @default true
     */
    var loopFocus: Boolean?

    /**
     * Whether moving the pointer over items should highlight them.
     * Disabling this prop allows CSS `:hover` to be differentiated from the `:focus` (`data-highlighted`) state.
     * @default true
     */
    var highlightItemOnHover: Boolean?

    /**
     * Determines if the menu enters a modal state when open.
     * - `true`: user interaction is limited to the menu: document page scroll is locked and pointer interactions on outside elements are disabled.
     * - `false`: user interaction with the rest of the document is allowed.
     *
     * On touch devices, a `true` modal blocks outside taps but leaves the page scrollable unless the popup spans nearly the full viewport width, matching native iOS behavior.
     *
     * Nested menus ignore this prop, and menus opened by hover are never modal.
     * @default true
     */
    var modal: Boolean?

    /**
     * Event handler called when the menu is opened or closed.
     */
    var onOpenChange: ((open: Boolean, eventDetails: MenuRootChangeEventDetails) -> Unit)?

    /**
     * Event handler called after any animations complete when the menu is opened or closed.
     */
    var onOpenChangeComplete: ((open: Boolean) -> Unit)?

    /**
     * Callback fired when an item is highlighted or unhighlighted.
     * Receives the highlighted item element (or `undefined` if no item is highlighted) and details
     * containing the reason for the change, the event, and the item's text label.
     * The `reason` can be:
     * - `'keyboard'`: the highlight changed due to keyboard navigation.
     * - `'pointer'`: the highlight changed due to pointer hovering. The event may be a `MouseEvent`
     *   rather than a `PointerEvent`.
     * - `'none'`: the highlight changed for another reason, such as automatic highlighting while
     *   filtering, the item list changing, or the popup opening or closing.
     */
    var onItemHighlighted: Any? /* (highlightedItem: HTMLElement | undefined, eventDetails: MenuRootHighlightEventDetails) => void */

    /**
     * Whether the menu is currently open.
     */
    var open: Boolean?

    /**
     * The visual orientation of the menu.
     * Controls whether roving focus uses up/down or left/right arrow keys.
     * @default 'vertical'
     */
    var orientation: MenuRootOrientation?

    /**
     * Whether the component should ignore user interaction.
     * @default false
     */
    var disabled: Boolean?

    /**
     * When in a submenu, determines whether pressing the Escape key
     * closes the entire menu, or only the current child menu.
     * @default false
     */
    var closeParentOnEsc: Boolean?

    /**
     * A ref to imperative actions.
     * - `unmount`: Ends the closing phase of the menu after an externally controlled closing animation finishes.
     *   Call `preventUnmountOnClose()` in `onOpenChange` first, otherwise the menu completes closing on its own.
     *   Whether it leaves the DOM is decided by `keepMounted` on the portal.
     * - `close`: Closes the menu imperatively when called.
     */
    var actionsRef: Any? /* React.RefObject<MenuRootActions | null> */

    /**
     * ID of the trigger that the menu is associated with.
     * This is useful in conjunction with the `open` prop to create a controlled menu.
     * There's no need to specify this prop when the menu is uncontrolled (that is, when the `open` prop is not set).
     */
    var triggerId: String?

    /**
     * ID of the trigger that the menu is associated with.
     * This is useful in conjunction with the `defaultOpen` prop to create an initially open menu.
     */
    var defaultTriggerId: String?

    /**
     * A handle to associate the menu with a trigger.
     * If specified, allows external triggers to control the menu's open state.
     */
    var handle: Any? /* MenuHandle<Payload> */

    /**
     * The content of the menu.
     * This can be a regular React node or a render function that receives the `payload` of the active trigger.
     */
    override var children: ReactNode? /* React.ReactNode | PayloadChildRenderFunction<Payload> */
}

external interface MenuRootState

external interface MenuRootActions {
    fun unmount()
    fun close()
}

external interface MenuRootChangeEventDetails : BaseUIChangeEventDetails {
    /** Prevents the popup from unmounting until the `unmount` action is called. */
    var preventUnmountOnClose: () -> Unit
}

external interface MenuRootHighlightEventDetails : BaseUIHighlightEventDetails
