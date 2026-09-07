// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import mui.system.Union

external interface ToastRootProps :
    BaseUiDivProps {
    /**
     * The toast to render.
     */
    var toast: Any? /* ToastRootToastObject<any> */

    /**
     * Direction(s) in which the toast can be swiped to dismiss.
     * @default ['down', 'right']
     */
    var swipeDirection: Union? /* 'up' | 'down' | 'left' | 'right' | ('up' | 'down' | 'left' | 'right')[] */
}

external interface ToastRootState {
    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?

    /**
     * Whether the toasts in the viewport are expanded.
     */
    var expanded: Boolean

    /**
     * Whether the toast was limited because the toast limit was exceeded.
     */
    var limited: Boolean

    /**
     * The type of the toast.
     */
    var type: String

    /**
     * Whether the toast is being swiped.
     */
    var swiping: Boolean

    /**
     * The direction the toast is being swiped.
     */
    var swipeDirection: Union /* 'up' | 'down' | 'left' | 'right' */
}
