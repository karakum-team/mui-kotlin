// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface AppBarProps : react.RProps {
    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic

    /**
     * The positioning type. The behavior of the different options is described
     * [in the MDN web docs](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Positioning).
     * Note: `sticky` is not universally supported and will fall back to `static` when unavailable.
     */
    var position: dynamic /* 'fixed' | 'absolute' | 'sticky' | 'static' | 'relative' */
}

/**
 *
 * Demos:
 *
 * - [App Bar](https://material-ui.com/components/app-bar/)
 *
 * API:
 *
 * - [AppBar API](https://material-ui.com/api/app-bar/)
 * - inherits [Paper API](https://material-ui.com/api/paper/)
 */
@JsName("default")
external val AppBar: react.FC<AppBarProps>
