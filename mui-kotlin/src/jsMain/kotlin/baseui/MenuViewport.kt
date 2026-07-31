// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import mui.system.Union
import react.PropsWithChildren
import react.ReactNode

external interface MenuViewportProps : BaseUiDivProps, PropsWithChildren {
    /** The content to render inside the transition container. */
    override var children: ReactNode?
}

external interface MenuViewportState {
    /** The activation direction of the transitioned content. */
    var activationDirection: String

    /** Whether the viewport is currently transitioning between contents. */
    var transitioning: Boolean

    /** Present if animations should be instant. */
    var instant: Union /* 'dismiss' | 'click' | 'group' | 'trigger-change' */
}
