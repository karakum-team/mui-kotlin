// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import mui.system.Union
import react.PropsWithChildren
import react.ReactNode
import web.dom.ElementId

external interface MenuPopupProps :
    BaseUiDivProps,
    PropsWithChildren {
    override var children: ReactNode?

    /**
     * @ignore
     */
    var id: ElementId?

    /**
     * Determines the element to focus when the menu is closed.
     *
     * - `false`: Do not move focus.
     * - `true`: Move focus based on the default behavior (trigger or previously focused element).
     * - `RefObject`: Move focus to the ref element.
     * - `function`: Called with the interaction type (`mouse`, `touch`, `pen`, or `keyboard`).
     *   Return an element to focus, `true` to use the default behavior, or `false`/`undefined` to do nothing.
     */
    var finalFocus: Any? /* boolean | React.RefObject<HTMLElement | null> | ((closeType: InteractionType) => boolean | HTMLElement | null | void) */
}

external interface MenuPopupState {
    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?

    /**
     * The side of the anchor the component is placed on.
     */
    var side: Side

    /**
     * The alignment of the component relative to the anchor.
     */
    var align: Align

    /**
     * Whether the menu is currently open.
     */
    var open: Boolean

    /**
     * Whether the component is nested.
     */
    var nested: Boolean

    /**
     * Whether transitions should be skipped.
     */
    var instant: Union /* 'dismiss' | 'click' | 'group' | 'trigger-change' */
}
