// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface ImageListItemBarProps : react.RProps {
    /**
     * An IconButton element to be used as secondary action target
     * (primary action target is the item itself).
     */
    var actionIcon: react.ReactNode

    /**
     * Position of secondary action IconButton.
     */
    var actionPosition: dynamic /* 'left' | 'right' */

    /**
     * Position of the title bar.
     */
    var position: dynamic /* 'top' | 'bottom' */

    /**
     * String or element serving as subtitle (support text).
     */
    var subtitle: react.ReactNode

    /**
     * Title to be displayed on item.
     */
    var title: react.ReactNode

    /**
     * Position of the title bar.
     * @deprecated Use position instead.
     */
    var titlePosition: dynamic /* 'top' | 'bottom' */
}

/**
 *
 * Demos:
 *
 * - [Image List](https://material-ui.com/components/image-list/)
 *
 * API:
 *
 * - [ImageListItemBar API](https://material-ui.com/api/image-list-item-bar/)
 */
@JsName("default")
external val ImageListItemBar: react.FC<ImageListItemBarProps>
