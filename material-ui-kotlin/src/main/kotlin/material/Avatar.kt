// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Avatar")
@file:JsNonModule

package material

external interface AvatarProps : react.RProps {
    /**
     * Used in combination with `src` or `srcSet` to
     * provide an alt attribute for the rendered `img` element.
     */
    var alt: String

    /**
     * Used to render icon or text elements inside the Avatar if `src` is not set.
     * This can be an element, or just a string.
     */
    var children: react.ReactNode

    /**
     * Attributes applied to the `img` element if the component is used to display an image.
     * It can be used to listen for the loading error event.
     */
    var imgProps: dynamic

    /**
     * The `sizes` attribute for the `img` element.
     */
    var sizes: String

    /**
     * The `src` attribute for the `img` element.
     */
    var src: String

    /**
     * The `srcSet` attribute for the `img` element.
     * Use this attribute for responsive image display.
     */
    var srcSet: String

    /**
     * The shape of the avatar.
     */
    var variant: dynamic /* 'circle' | 'circular' | 'rounded' | 'square' */
}

/**
 *
 * Demos:
 *
 * - [Avatars](https://material-ui.com/components/avatars/)
 *
 * API:
 *
 * - [Avatar API](https://material-ui.com/api/avatar/)
 */
@JsName("default")
external val Avatar: react.FC<AvatarProps>
