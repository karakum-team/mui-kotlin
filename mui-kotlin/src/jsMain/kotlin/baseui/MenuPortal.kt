// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface MenuPortalProps :
    BaseUiDivProps {
    /**
     * Whether to keep the portal mounted in the DOM while the popup is hidden.
     * @default false
     */
    var keepMounted: Boolean?

    /**
     * A parent element to render the portal element into.
     */
    var container: Any? /* HTMLElement | ShadowRoot | React.RefObject<HTMLElement | ShadowRoot | null> */
}

external interface MenuPortalState
