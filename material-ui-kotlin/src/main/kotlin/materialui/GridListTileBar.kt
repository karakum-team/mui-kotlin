// Automatically generated - do not modify!

@file:JsModule("@material-ui/core")
@file:JsNonModule

package materialui

external interface GridListTileBarProps : react.RProps {
    /**
     * An IconButton element to be used as secondary action target
     * (primary action target is the tile itself).
     */
    var actionIcon: react.ReactNode

    /**
     * Position of secondary action IconButton.
     */
    var actionPosition: dynamic /* 'left' | 'right' */

    /**
     * String or element serving as subtitle (support text).
     */
    var subtitle: react.ReactNode

    /**
     * Title to be displayed on tile.
     */
    var title: react.ReactNode

    /**
     * Position of the title bar.
     */
    var titlePosition: dynamic /* 'top' | 'bottom' */
}

/**
 * ⚠️ The GridListTileBar component was renamed to ImageListTileBar to align with the current Material Design naming.
 *
 * You should use `import { ImageListTileBar } from '@material-ui/core'`
 * or `import ImageListTileBar from '@material-ui/core/ImageListTileBar'`.
 * API:
 *
 * - [GridListTileBar API](https://material-ui.com/api/grid-list-tile-bar/)
 */
@JsName("default")
external val GridListTileBar: react.FC<GridListTileBarProps>
