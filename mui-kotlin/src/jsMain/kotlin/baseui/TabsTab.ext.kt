// Automatically generated - do not modify!

package baseui

import react.CSSProperties
import react.ReactElement
import react.dom.html.HTMLAttributes
import seskar.js.JsValue
import web.cssom.ClassName
import web.html.HTMLElement

sealed /* union */
external interface TabsTabActivationDirection {
    companion object {
        @JsValue("left")
        val left: TabsTabActivationDirection

        @JsValue("right")
        val right: TabsTabActivationDirection

        @JsValue("up")
        val up: TabsTabActivationDirection

        @JsValue("down")
        val down: TabsTabActivationDirection

        @JsValue("none")
        val none: TabsTabActivationDirection
    }
}

/**
 * The state-dependent arm of `TabsTabProps.className`, upstream
 * `string | ((state: TabsTabState) => string | undefined)`.
 *
 * The prop itself is `Any?`: it is inherited through [BaseUiButtonProps] from a parent shared by every part that
 * renders this tag, which cannot name one part's state type. Assign a [ClassName] directly
 * when the class does not depend on state.
 */
fun TabsTabProps.className(
    block: (state: TabsTabState) -> ClassName?,
) {
    className = block
}

/**
 * The state-dependent arm of `TabsTabProps.style`, upstream
 * `CSSProperties | ((state: TabsTabState) => CSSProperties | undefined)`. See [TabsTabProps.className].
 */
fun TabsTabProps.style(
    block: (state: TabsTabState) -> CSSProperties?,
) {
    style = block
}

/**
 * The callback arm of `TabsTabProps.render`, upstream
 * `ReactElement | ((props: HTMLProps, state: TabsTabState) => ReactElement)`.
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
fun TabsTabProps.render(
    block: (props: HTMLAttributes<HTMLElement>, state: TabsTabState) -> ReactElement<*>,
) {
    render = block
}
