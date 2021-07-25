// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ImageListItem")
@file:JsNonModule

package material

external interface ImageListItemProps : react.RProps {
    /**
     * While you can pass any node as children, the main use case is for an img.
     */
    var children: react.ReactNode

    /**
     * Width of the item in number of grid columns.
     */
    var cols: Number

    /**
     * Height of the item in number of grid rows.
     */
    var rows: Number
}

/**
 *
 * Demos:
 *
 * - [Image List](https://material-ui.com/components/image-list/)
 *
 * API:
 *
 * - [ImageListItem API](https://material-ui.com/api/image-list-item/)
 */
@JsName("default")
external val ImageListItem: react.FC<ImageListItemProps>
