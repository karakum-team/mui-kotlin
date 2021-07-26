// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Backdrop")
@file:JsNonModule

package material

external interface BackdropProps : react.RProps {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * If `true`, the component is shown.
     */
    var open: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic

    /**
     * The duration for the transition, in milliseconds.
     * You may specify a single timeout for all transitions, or individually with an object.
     */
    var transitionDuration: dynamic
}

/**
 *
 * Demos:
 *
 * - [Backdrop](https://material-ui.com/components/backdrop/)
 *
 * API:
 *
 * - [Backdrop API](https://material-ui.com/api/backdrop/)
 * - inherits [Fade API](https://material-ui.com/api/fade/)
 */

@JsName("default")
external val Backdrop: react.FC<BackdropProps>
