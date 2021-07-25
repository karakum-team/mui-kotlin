// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Badge")
@file:JsNonModule

package material

external interface BadgeProps : react.RProps {
    /**
     * The anchor of the badge.
     */
    var anchorOrigin: dynamic

    /**
     * Wrapped shape the badge should overlap.
     */
    var overlap: dynamic /* 'rectangle' | 'circle' | 'rectangular' | 'circular' */

    /**
     * The content rendered within the badge.
     */
    var badgeContent: react.ReactNode

    /**
     * The badge will be added relative to this node.
     */
    var children: react.ReactNode

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     */
    var color: dynamic /* 'primary' | 'secondary' | 'default' | 'error' */

    /**
     * If `true`, the badge will be invisible.
     */
    var invisible: Boolean

    /**
     * Max count to show.
     */
    var max: Number

    /**
     * Controls whether the badge is hidden when `badgeContent` is zero.
     */
    var showZero: Boolean

    /**
     * The variant to use.
     */
    var variant: dynamic /* 'standard' | 'dot' */
}

/**
 *
 * Demos:
 *
 * - [Avatars](https://material-ui.com/components/avatars/)
 * - [Badges](https://material-ui.com/components/badges/)
 *
 * API:
 *
 * - [Badge API](https://material-ui.com/api/badge/)
 */
@JsName("default")
external val Badge: react.FC<BadgeProps>
