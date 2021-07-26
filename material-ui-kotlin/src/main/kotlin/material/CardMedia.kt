// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/CardMedia")
@file:JsNonModule

package material

external interface CardMediaProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: dynamic

    /**
     * Image to be displayed as a background image.
     * Either `image` or `src` prop must be specified.
     * Note that caller must specify height otherwise the image will not be visible.
     */
    var image: String

    /**
     * An alias for `image` property.
     * Available only with media components.
     * Media components: `video`, `audio`, `picture`, `iframe`, `img`.
     */
    var src: String

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic
}

/**
 *
 * Demos:
 *
 * - [Cards](https://material-ui.com/components/cards/)
 *
 * API:
 *
 * - [CardMedia API](https://material-ui.com/api/card-media/)
 */
@JsName("default")
external val CardMedia: react.FC<CardMediaProps>
