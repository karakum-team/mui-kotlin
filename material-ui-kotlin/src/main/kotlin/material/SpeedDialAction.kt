// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/SpeedDialAction")
@file:JsNonModule

package material

external interface SpeedDialActionProps : react.RProps {
    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * Props applied to the [`Fab`](/api/fab/) component.
     * @default {}
     */
    var FabProps: dynamic

    /**
     * Adds a transition delay, to allow a series of SpeedDialActions to be animated.
     * @default 0
     */
    var delay: Number

    /**
     * The icon to display in the SpeedDial Fab.
     */
    var icon: react.ReactNode

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic

    /**
     * `classes` prop applied to the [`Tooltip`](/api/tooltip/) element.
     */
    var TooltipClasses: dynamic

    /**
     * Placement of the tooltip.
     * @default 'left'
     */
    var tooltipPlacement: dynamic

    /**
     * Label to display in the tooltip.
     */
    var tooltipTitle: react.ReactNode

    /**
     * Make the tooltip always visible when the SpeedDial is open.
     * @default false
     */
    var tooltipOpen: Boolean
}

/**
 *
 * Demos:
 *
 * - [Speed Dial](https://material-ui.com/components/speed-dial/)
 *
 * API:
 *
 * - [SpeedDialAction API](https://material-ui.com/api/speed-dial-action/)
 * - inherits [Tooltip API](https://material-ui.com/api/tooltip/)
 */
@JsName("default")
external val SpeedDialAction: react.FC<SpeedDialActionProps>
