// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Container")
@file:JsNonModule

package material

external interface ContainerProps : react.RProps {
    var children: dynamic

    /**
     * If `true`, the left and right padding is removed.
     */
    var disableGutters: Boolean

    /**
     * Set the max-width to match the min-width of the current breakpoint.
     * This is useful if you'd prefer to design for a fixed set of sizes
     * instead of trying to accommodate a fully fluid viewport.
     * It's fluid by default.
     */
    var fixed: Boolean

    /**
     * Determine the max-width of the container.
     * The container width grows with the size of the screen.
     * Set to `false` to disable `maxWidth`.
     */
    var maxWidth: dynamic /* 'xs' | 'sm' | 'md' | 'lg' | 'xl' | false */
}

/**
 *
 * Demos:
 *
 * - [Container](https://material-ui.com/components/container/)
 *
 * API:
 *
 * - [Container API](https://material-ui.com/api/container/)
 */
@JsName("default")
external val Container: react.FC<ContainerProps>
