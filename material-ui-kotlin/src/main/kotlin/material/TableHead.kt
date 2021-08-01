// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableHead")
@file:JsNonModule

package material

external interface TableHeadProps : react.RProps {
    /**
     * The content of the component, normally `TableRow`.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 *
 * Demos:
 *
 * - [Tables](https://material-ui.com/components/tables/)
 *
 * API:
 *
 * - [TableHead API](https://material-ui.com/api/table-head/)
 */
@JsName("default")
external val TableHead: react.FC<TableHeadProps>
