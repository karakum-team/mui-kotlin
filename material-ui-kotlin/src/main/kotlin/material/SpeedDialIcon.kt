// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/SpeedDialIcon")
@file:JsNonModule

package material

external interface SpeedDialIconProps : react.RProps {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The icon to display.
     */
    var icon: react.ReactNode

    /**
     * The icon to display in the SpeedDial Floating Action Button when the SpeedDial is open.
     */
    var openIcon: react.ReactNode

    /**
     * @ignore
     * If `true`, the component is shown.
     */
    var open: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Speed Dial](https://material-ui.com/components/speed-dial/)
 *
 * API:
 *
 * - [SpeedDialIcon API](https://material-ui.com/api/speed-dial-icon/)
 */
@JsName("default")
external val SpeedDialIcon: react.FC<SpeedDialIconProps>
