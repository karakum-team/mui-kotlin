// Automatically generated - do not modify!

@file:JsModule("@mui/lab/TimelineDot")
@file:JsNonModule

package mui.material

import kotlinext.js.ReadonlyArray

external interface TimelineDotProps : react.PropsWithChildren {
    /**
     * The content of the component.
     */
    override var children: ReadonlyArray<react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TimelineDotClasses

    /**
     * The dot can have a different colors.
     * @default 'grey'
     */
    var color: Union /* 'inherit' | 'grey' | 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The dot can appear filled or outlined.
     * @default 'filled'
     */
    var variant: Union /* 'filled' | 'outlined', TimelineDotPropsVariantOverrides */
}

/**
 *
 * Demos:
 *
 * - [Timeline](https://mui.com/components/timeline/)
 *
 * API:
 *
 * - [TimelineDot API](https://mui.com/api/timeline-dot/)
 */
@JsName("default")
external val TimelineDot: react.FC<TimelineDotProps>
