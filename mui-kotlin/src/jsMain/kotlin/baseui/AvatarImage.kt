// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface AvatarImageProps :
    BaseUiImgProps {
    /**
     * Callback fired when the loading status changes.
     */
    var onLoadingStatusChange: ((status: ImageLoadingStatus) -> Unit)?

    /**
     * Whether the image element stays mounted and loads in place instead of being preloaded.
     * Supports `loading="lazy"` and optimized image components such as `next/image`.
     * @default false
     */
    var keepMounted: Boolean?
}

external interface AvatarImageState : AvatarRootState {
    /**
     * The transition status of the component.
     */
    var transitionStatus: TransitionStatus?
}
