// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ListSubheader")
@file:JsNonModule

package material

external interface ListSubheaderProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The color of the component. It supports those theme colors that make sense for this component.
     * @default 'default'
     */
    var color: dynamic /* 'default' | 'primary' | 'inherit' */

    /**
     * If `true`, the List Subheader will not have gutters.
     * @default false
     */
    var disableGutters: Boolean

    /**
     * If `true`, the List Subheader will not stick to the top during scroll.
     * @default false
     */
    var disableSticky: Boolean

    /**
     * If `true`, the List Subheader is indented.
     * @default false
     */
    var inset: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic
}

/**
 *
 * Demos:
 *
 * - [Lists](https://material-ui.com/components/lists/)
 *
 * API:
 *
 * - [ListSubheader API](https://material-ui.com/api/list-subheader/)
 */
@JsName("default")
external val ListSubheader: react.FC<ListSubheaderProps>
