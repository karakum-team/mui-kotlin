// Automatically generated - do not modify!

package baseui

import web.dom.Element
import web.events.Event

/**
 * `internals/types.d.ts`. Marker parents carrying a single prop; declared twice upstream purely to
 * document opposite defaults (`nativeButton` defaults to `true` for [NativeButtonProps] and to
 * `false` for [NonNativeButtonProps]).
 */
external interface NativeButtonProps {
    var nativeButton: Boolean?
}

external interface NonNativeButtonProps {
    var nativeButton: Boolean?
}

/**
 * `internals/createBaseUIEventDetails.d.ts`. Upstream this is a conditional type over a mapped
 * reason-to-native-event table (`Reason extends string ? BaseUIChangeEventDetail<Reason, …> :
 * never`), which has no Kotlin equivalent — so the members are spelled out here instead.
 *
 * `reason` is left as `String`: the reason sets are per-component (`MenuRootChangeEventReason` and
 * friends are `typeof REASONS.x` unions over `internals/reason-parts.d.ts`), so narrowing it here
 * would be wrong for every other component.
 */
external interface BaseUIChangeEventDetails {
    var reason: String
    var event: Event
    var isCanceled: Boolean
    var isPropagationAllowed: Boolean
    var trigger: Element?

    fun cancel()

    fun allowPropagation()
}

external interface BaseUIGenericEventDetails {
    var reason: String
    var event: Event
}

/**
 * `utils/useAnchorPositioning.d.ts`. The real interface is ~60 anchor-positioning props (side,
 * align, offsets, collision handling) shared by every Positioner part. Generating it is deferred,
 * so Positioner props currently inherit nothing from it — see BASE_UI_TODO.md.
 */
external interface UseAnchorPositioningSharedParameters

/**
 * `floating-ui-react/components/FloatingPortal.d.ts`, where it is `interface Props<TState>`
 * declared inside the `FloatingPortal` namespace — a shape with no flat declaration to redirect to,
 * and one `floating-ui-react/` is not generated from at all. Written by hand so that
 * `MenuPortalProps`, which extends it, keeps a parent; see `resolveNamespaceStubs` in BaseUi.kt.
 *
 * Upstream it is `BaseUIComponentProps<'div', TState>` plus `container`, so extending
 * [BaseUiDivProps] reproduces the whole surface — `children` above all, without which a portal
 * cannot hold the popup it exists to move.
 */
external interface FloatingPortalProps : BaseUiDivProps {
    /**
     * A parent element to render the portal element into.
     *
     * `Any?` rather than the usual narrowing to the dominant arm: the union is `HTMLElement |
     * ShadowRoot | RefObject<HTMLElement | ShadowRoot | null> | null`, and `ShadowRoot` is not an
     * `Element` while the ref arm is not a node at all, so every candidate narrowing would be wrong
     * for two of the four.
     */
    var container:
        Any? /* HTMLElement | ShadowRoot | React.RefObject<HTMLElement | ShadowRoot | null> | null */
}
