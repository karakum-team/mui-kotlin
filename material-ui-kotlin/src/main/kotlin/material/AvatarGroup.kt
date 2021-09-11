// Automatically generated - do not modify!

@file:JsModule("@mui/material/AvatarGroup")
@file:JsNonModule

package material

external interface AvatarGroupProps : react.PropsWithChildren {
    /**
     * The avatars to stack.
     */
    override var children: Array<out react.ReactNode>?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: AvatarGroupClasses

    /**
     * Max avatars to show before +x.
     * @default 5
     */
    var max: Number

    /**
     * Spacing between avatars.
     * @default 'medium'
     */
    var spacing: Union /* 'small' | 'medium' | number */

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * The variant to use.
     * @default 'circular'
     */
    var variant: Union /* 'circular' | 'rounded' | 'square', AvatarGroupPropsVariantOverrides */
}

/**
 *
 * Demos:
 *
 * - [Avatars](https://material-ui.com/components/avatars/)
 *
 * API:
 *
 * - [AvatarGroup API](https://material-ui.com/api/avatar-group/)
 */
@JsName("default")
external val AvatarGroup: react.FC<AvatarGroupProps>
