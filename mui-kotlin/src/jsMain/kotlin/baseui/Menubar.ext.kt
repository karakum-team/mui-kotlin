// Automatically generated - do not modify!

package baseui

import react.CSSProperties
import react.ReactElement
import react.dom.html.HTMLAttributes
import web.cssom.ClassName
import web.html.HTMLElement

/**
 * The state-dependent arm of `MenubarProps.className`, upstream
 * `string | ((state: MenubarState) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is inherited through [BaseUiDivProps] from a parent shared by every part that
 * renders this tag, which cannot name one part's state type. Assign a [ClassName] directly
 * when the class does not depend on state.
 */
fun MenubarProps.className(
    block: (state: MenubarState) -> ClassName?,
) {
    className = block
}

/**
 * The state-dependent arm of `MenubarProps.style`, upstream
 * `CSSProperties | ((state: MenubarState) => CSSProperties | undefined)`. See [MenubarProps.className].
 */
fun MenubarProps.style(
    block: (state: MenubarState) -> CSSProperties?,
) {
    style = block
}

/**
 * The callback arm of `MenubarProps.render`, upstream
 * `ReactElement | ((props: HTMLProps, state: MenubarState) => ReactElement)`.
 *
 * `props` are the ones Base UI expects on the element the callback returns; upstream types them as its
 * own `HTMLProps`, which is `HTMLAttributes<any> & { ref }`. Assign a [ReactElement] directly to
 * render a fixed element instead.
 *
 * Applying them is the callback's job — `useRenderElement` calls `render(props, state)` and takes the
 * result as it is, merging nothing, so a callback that ignores `props` drops `ref` and the `data-*`
 * state attributes with them. `+props` inside the element builder does it (`Object.assign` underneath):
 *
 *     render { props, _ -> hr.create { +props } }
 *
 * That copies `children` as well, so a builder using it must not also add children of its own: the
 * wrappers' `jsx` reports "Both `children` source options used" and keeps the builder's, dropping the
 * ones that came in through `props`.
 */
fun MenubarProps.render(
    block: (props: HTMLAttributes<HTMLElement>, state: MenubarState) -> ReactElement<*>,
) {
    render = block
}
