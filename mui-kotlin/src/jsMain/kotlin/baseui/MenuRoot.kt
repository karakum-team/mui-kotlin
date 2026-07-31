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
     *
     * @default false
     */
    var defaultOpen: Boolean?

    /**
     * Whether to loop keyboard focus back to the first item when the end of the list is reached
     * while using the arrow keys.
     *
     * @default true
     */
    var loopFocus: Boolean?

    /**
     * Whether moving the pointer over items should highlight them. Disabling this prop allows CSS
     * `:hover` to be differentiated from the `:focus` (`data-highlighted`) state.
     *
     * @default true
     */
    var highlightItemOnHover: Boolean?

    /**
     * Determines if the menu enters a modal state when open.
     * - `true`: user interaction is limited to the menu: document page scroll is locked and pointer
     *   interactions on outside elements are disabled.
     * - `false`: user interaction with the rest of the document is allowed.
     *
     * @default true
     */
    var modal: Boolean?

    /** Event handler called when the menu is opened or closed. */
    var onOpenChange: ((open: Boolean, eventDetails: MenuRootChangeEventDetails) -> Unit)?

    /** Event handler called after any animations complete when the menu is closed. */
    var onOpenChangeComplete: ((open: Boolean) -> Unit)?

    /** Whether the menu is currently open. */
    var open: Boolean?

    /**
     * The visual orientation of the menu. Controls whether roving focus uses up/down or left/right
     * arrow keys.
     *
     * @default 'vertical'
     */
    var orientation: Any? /* MenuRootOrientation */

    /**
     * Whether the component should ignore user interaction.
     *
     * @default false
     */
    var disabled: Boolean?

    /**
     * When in a submenu, determines whether pressing the Escape key closes the entire menu, or only
     * the current child menu.
     *
     * @default false
     */
    var closeParentOnEsc: Boolean?

    /**
     * A ref to imperative actions.
     * - `unmount`: Manually unmounts the menu. Call this after any externally controlled closing
     *   animation finishes.
     * - `close`: When specified, the menu can be closed imperatively.
     */
    var actionsRef: Any? /* React.RefObject<MenuRootActions | null> */

    /**
     * ID of the trigger that the popover is associated with. This is useful in conjunction with the
     * `open` prop to create a controlled popover. There's no need to specify this prop when the
     * popover is uncontrolled (that is, when the `open` prop is not set).
     */
    var triggerId: String?

    /**
     * ID of the trigger that the popover is associated with. This is useful in conjunction with the
     * `defaultOpen` prop to create an initially open popover.
     */
    var defaultTriggerId: String?

    /**
     * A handle to associate the menu with a trigger. If specified, allows external triggers to
     * control the menu's open state.
     */
    var handle: Any? /* MenuHandle<Payload> */

    /**
     * The content of the popover. This can be a regular React node or a render function that
     * receives the `payload` of the active trigger.
     */
    override var children: ReactNode? /* React.ReactNode | PayloadChildRenderFunction<Payload> */
}

external interface MenuRootState

external interface MenuRootActions {
    fun unmount()

    fun close()
}

external interface MenuRootChangeEventDetails : BaseUIChangeEventDetails {
    var preventUnmountOnClose: () -> Unit
}
