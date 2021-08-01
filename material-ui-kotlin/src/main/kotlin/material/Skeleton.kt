// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Skeleton")
@file:JsNonModule

package material

external interface SkeletonProps : react.RProps {
    /**
     * The animation.
     * If `false` the animation effect is disabled.
     * @default 'pulse'
     */
    var animation: dynamic /* 'pulse' | 'wave' | false */

    /**
     * Optional children to infer width and height from.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * Height of the skeleton.
     * Useful when you don't want to adapt the skeleton to a text element but for instance a card.
     */
    var height: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The type of content that will be rendered.
     * @default 'text'
     */
    var variant: dynamic

    /**
     * Width of the skeleton.
     * Useful when the skeleton is inside an inline element with no width of its own.
     */
    var width: dynamic
}

/**
 *
 * Demos:
 *
 * - [Skeleton](https://material-ui.com/components/skeleton/)
 *
 * API:
 *
 * - [Skeleton API](https://material-ui.com/api/skeleton/)
 */
@JsName("default")
external val Skeleton: react.FC<SkeletonProps>
