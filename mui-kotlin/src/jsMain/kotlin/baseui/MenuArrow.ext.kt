// Automatically generated - do not modify!

package baseui

import react.CSSProperties
import react.ReactElement
import react.dom.html.HTMLAttributes
import web.cssom.ClassName
import web.html.HTMLElement

/**
 * The state-dependent arm of `MenuArrowProps.className`, upstream `string | ((state:
 * MenuArrowState) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is declared on [BaseUiDivProps], shared by every part that renders
 * this tag, which cannot name the state type. Assign a [ClassName] directly when the class does not
 * depend on state.
 */
fun MenuArrowProps.className(block: (state: MenuArrowState) -> ClassName?) {
    className = block
}

/**
 * The state-dependent arm of `MenuArrowProps.style`, upstream `CSSProperties | ((state:
 * MenuArrowState) => CSSProperties | undefined)`. See [MenuArrowProps.className].
 */
fun MenuArrowProps.style(block: (state: MenuArrowState) -> CSSProperties?) {
    style = block
}

/**
 * The callback arm of `MenuArrowProps.render`, upstream `ReactElement | ((props: HTMLProps, state:
 * MenuArrowState) => ReactElement)`.
 *
 * `props` are the ones Base UI expects to be spread onto the element the callback returns; upstream
 * types them as its own `HTMLProps`, which is `HTMLAttributes<any> & { ref }`. Assign a
 * [ReactElement] directly to render a fixed element instead.
 *
 * Base UI does not merge them for you — `useRenderElement` calls `render(props, state)` and uses
 * the result as it is — so whatever the callback leaves out is lost, `ref` and the `data-*` state
 * attributes included. Only the non-callback arm gets merged.
 */
fun MenuArrowProps.render(
    block: (props: HTMLAttributes<HTMLElement>, state: MenuArrowState) -> ReactElement<*>
) {
    render = block
}
