// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.PropsWithChildren
import react.ReactNode

external interface ToastProviderProps : PropsWithChildren {
    override var children: ReactNode?

    /**
     * The default amount of time (in ms) before a toast is auto dismissed.
     * A value of `0` will prevent the toast from being dismissed automatically.
     * @default 5000
     */
    var timeout: Number?

    /**
     * The maximum number of toasts that can be displayed at once.
     * When the limit is exceeded, the oldest toasts are marked as `limited` (via the `data-limited`
     * attribute) rather than removed, so they can be hidden or animated out.
     * @default 3
     */
    var limit: Number?

    /**
     * A global manager for toasts to use outside of a React component.
     */
    var toastManager: Any? /* ToastManager */
}

external interface ToastProviderState
