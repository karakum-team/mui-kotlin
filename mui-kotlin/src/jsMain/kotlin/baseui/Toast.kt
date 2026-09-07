// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/toast")

package baseui

import react.FC

/**
 * `export * as Toast` from `@base-ui/react/toast` — the module's only value export.
 *
 * The package's `exports` map has no wildcard entry, so a part's own subpath is not importable: this
 * object is the only way to reach a part at runtime.
 */
external object Toast {
    val Action: FC<ToastActionProps>
    val Arrow: FC<ToastArrowProps>
    val Close: FC<ToastCloseProps>
    val Content: FC<ToastContentProps>
    val Description: FC<ToastDescriptionProps>
    val Portal: FC<ToastPortalProps>
    val Positioner: FC<ToastPositionerProps>
    val Provider: FC<ToastProviderProps>
    val Root: FC<ToastRootProps>
    val Title: FC<ToastTitleProps>
    val Viewport: FC<ToastViewportProps>

    fun <Data : Any> createToastManager(): ToastManager<Data>
    fun <Data : Any> useToastManager(): UseToastManagerReturnValue<Data>
}
