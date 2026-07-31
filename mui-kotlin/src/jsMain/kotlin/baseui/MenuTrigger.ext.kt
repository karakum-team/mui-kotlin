// Automatically generated - do not modify!

package baseui

import react.CSSProperties
import react.ReactElement
import react.dom.html.HTMLAttributes
import web.cssom.ClassName
import web.html.HTMLElement

/**
 * The state-dependent arm of `MenuTriggerProps.className`, upstream `string | ((state:
 * MenuTriggerState) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is inherited through [BaseUiButtonProps] from a parent shared by
 * every part that renders this tag, which cannot name one part's state type. Assign a [ClassName]
 * directly when the class does not depend on state.
 */
fun MenuTriggerProps.className(block: (state: MenuTriggerState) -> ClassName?) {
    className = block
}

/**
 * The state-dependent arm of `MenuTriggerProps.style`, upstream `CSSProperties | ((state:
 * MenuTriggerState) => CSSProperties | undefined)`. See [MenuTriggerProps.className].
 */
fun MenuTriggerProps.style(block: (state: MenuTriggerState) -> CSSProperties?) {
    style = block
}

/**
 * The callback arm of `MenuTriggerProps.render`, upstream `ReactElement | ((props: HTMLProps,
 * state: MenuTriggerState) => ReactElement)`.
 *
 * `props` are the ones Base UI expects on the element the callback returns; upstream types them as
 * its own `HTMLProps`, which is `HTMLAttributes<any> & { ref }`. Assign a [ReactElement] directly
 * to render a fixed element instead.
 *
 * Applying them is the callback's job — `useRenderElement` calls `render(props, state)` and takes
 * the result as it is, merging nothing, so a callback that ignores `props` drops `ref` and the
 * `data-*` state attributes with them. `+props` inside the element builder does it (`Object.assign`
 * underneath):
 *
 *     render { props, _ -> hr.create { +props } }
 *
 * That copies `children` as well, so a builder using it must not also add children of its own: the
 * wrappers' `jsx` reports "Both `children` source options used" and keeps the builder's, dropping
 * the ones that came in through `props`.
 */
fun MenuTriggerProps.render(
    block: (props: HTMLAttributes<HTMLElement>, state: MenuTriggerState) -> ReactElement<*>
) {
    render = block
}
