// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TabScrollButton")
@file:JsNonModule

package materialui

external interface TabScrollButtonProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Which direction should the button indicate?
     */
    var direction: dynamic /* 'left' | 'right' */

    /**
     * If `true`, the element will be disabled.
     */
    var disabled: Boolean

    /**
     * The tabs orientation (layout flow direction).
     */
    var orientation: dynamic /* 'horizontal' | 'vertical' */
}

/**
 *
 * Demos:
 *
 * - [Tabs](https://material-ui.com/components/tabs/)
 *
 * API:
 *
 * - [TabScrollButton API](https://material-ui.com/api/tab-scroll-button/)
 */
@JsName("default")
external val TabScrollButton: react.FC<TabScrollButtonProps>
