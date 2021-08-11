// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/TableContainer")
@file:JsNonModule

package material

external interface TableContainerProps : react.PropsWithChildren {
    /**
     * The content of the component, normally `Table`.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: TableContainerClasses

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
 * - [TableContainer API](https://material-ui.com/api/table-container/)
 */
@JsName("default")
external val TableContainer: react.FC<TableContainerProps>
