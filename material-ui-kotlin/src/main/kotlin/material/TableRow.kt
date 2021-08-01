// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableRow")
@file:JsNonModule

package material

external interface TableRowProps : react.RProps {
    /**
     * Should be valid <tr> children such as `TableCell`.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * If `true`, the table row will shade on hover.
     * @default false
     */
    var hover: Boolean

    /**
     * If `true`, the table row will have the selected shading.
     * @default false
     */
    var selected: Boolean

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>
}

/**
 * Will automatically set dynamic row height
 * based on the material table element parent (head, body, etc).
 *
 * Demos:
 *
 * - [Tables](https://material-ui.com/components/tables/)
 *
 * API:
 *
 * - [TableRow API](https://material-ui.com/api/table-row/)
 */
@JsName("default")
external val TableRow: react.FC<TableRowProps>
