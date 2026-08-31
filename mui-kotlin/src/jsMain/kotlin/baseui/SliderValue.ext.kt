// Automatically generated - do not modify!

package baseui

import web.dom.Element
import web.cssom.ClassName
import web.html.HTMLElement
import react.CSSProperties
import react.ReactElement
import react.dom.html.HTMLAttributes

/**
 * The state-dependent arm of `SliderValueProps.className`, upstream
 * `string | ((state: SliderValueState) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is inherited through [BaseUiOutputProps] from a parent shared by every part that
 * renders this tag, which cannot name one part's state type. Assign a [ClassName] directly
 * when the class does not depend on state.
 */
fun SliderValueProps.className(
    block: (state: SliderValueState) -> ClassName?,
) {
    className = block
}

/**
 * The state-dependent arm of `SliderValueProps.style`, upstream
 * `CSSProperties | ((state: SliderValueState) => CSSProperties | undefined)`. See [SliderValueProps.className].
 */
fun SliderValueProps.style(
    block: (state: SliderValueState) -> CSSProperties?,
) {
    style = block
}

/**
 * The callback arm of `SliderValueProps.render`, upstream
 * `ReactElement | ((props: HTMLProps, state: SliderValueState) => ReactElement)`.
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
fun SliderValueProps.render(
    block: (props: HTMLAttributes<HTMLElement>, state: SliderValueState) -> ReactElement<*>,
) {
    render = block
}
