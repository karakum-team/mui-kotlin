// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ImageList")
@file:JsNonModule

package material

external interface ImageListProps : react.RProps {
    /**
     * Cell height in `px`.
     * Set to `'auto'` to let the children determine the height.
     * @deprecated Use rowHeight instead.
     */
    var cellHeight: dynamic

    /**
     * Items that will be in the image list.
     */
    var children: react.ReactNode

    /**
     * Number of columns.
     */
    var cols: Number

    /**
     * The gap between items in `px`.
     */
    var gap: Number

    /**
     * The height of one row in `px`.
     */
    var rowHeight: dynamic

    /**
     * The spacing between items in `px`.
     * @deprecated Use gap instead.
     */
    var spacing: Number
}

/**
 *
 * Demos:
 *
 * - [Image List](https://material-ui.com/components/image-list/)
 *
 * API:
 *
 * - [ImageList API](https://material-ui.com/api/image-list/)
 */
@JsName("default")
external val ImageList: react.FC<ImageListProps>
