// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableFooter")
@file:JsNonModule

package material

external interface TableFooterProps : react.Props {
    /**
     * The content of the component, normally `TableRow`.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableFooterClasses

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
 * - [TableFooter API](https://material-ui.com/api/table-footer/)
 */
@JsName("default")
external val TableFooter: react.FC<TableFooterProps>
