// Automatically generated - do not modify!

package baseui

import react.CSSProperties
import react.ReactElement
import react.dom.html.HTMLAttributes
import web.cssom.ClassName
import web.html.HTMLElement

/**
 * The state-dependent arm of `MenuViewportProps.className`, upstream `string | ((state:
 * MenuViewportState) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is declared on [BaseUiDivProps], shared by every part that renders
 * this tag, which cannot name the state type. Assign a [ClassName] directly when the class does not
 * depend on state.
 */
fun MenuViewportProps.className(block: (state: MenuViewportState) -> ClassName?) {
    className = block
}

/**
 * The state-dependent arm of `MenuViewportProps.style`, upstream `CSSProperties | ((state:
 * MenuViewportState) => CSSProperties | undefined)`. See [MenuViewportProps.className].
 */
fun MenuViewportProps.style(block: (state: MenuViewportState) -> CSSProperties?) {
    style = block
}

/**
 * The callback arm of `MenuViewportProps.render`, upstream `ReactElement | ((props: HTMLProps,
 * state: MenuViewportState) => ReactElement)`.
 *
 * `props` are the ones Base UI expects to be spread onto the element the callback returns; upstream
 * types them as its own `HTMLProps`, which is `HTMLAttributes<any> & { ref }`. Assign a
 * [ReactElement] directly to render a fixed element instead.
 *
 * Base UI does not merge them for you — `useRenderElement` calls `render(props, state)` and uses
 * the result as it is — so whatever the callback leaves out is lost, `ref` and the `data-*` state
 * attributes included. Only the non-callback arm gets merged.
 */
fun MenuViewportProps.render(
    block: (props: HTMLAttributes<HTMLElement>, state: MenuViewportState) -> ReactElement<*>
) {
    render = block
}
