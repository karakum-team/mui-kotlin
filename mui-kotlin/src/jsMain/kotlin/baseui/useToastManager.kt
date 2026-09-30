// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import js.array.ReadonlyArray
import js.promise.Promise
import mui.system.Union
import react.Props
import react.ReactNode
import web.dom.ElementId

external interface ToastObject<Data> {
    /**
     * The unique identifier for the toast.
     */
    var id: ElementId?

    /**
     * The ref for the toast.
     */

    /**
     * The title of the toast.
     */
    var title: ReactNode?

    /**
     * The type of the toast. Used to conditionally style the toast,
     * including conditionally rendering elements based on the type.
     */
    var type: String?

    /**
     * The description of the toast.
     */
    var description: ReactNode?

    /**
     * The amount of time (in ms) before the toast is auto dismissed.
     * A value of `0` will prevent the toast from being dismissed automatically.
     * @default 5000
     */
    var timeout: Number?

    /**
     * The priority of the toast.
     * - `low` - The toast will be announced politely.
     * - `high` - The toast will be announced urgently.
     * @default 'low'
     */
    var priority: Union? /* 'low' | 'high' */

    /**
     * The transition status of the toast.
     */
    var transitionStatus: Union? /* 'starting' | 'ending' */

    /**
     * A counter that increments whenever the toast is updated or upserted.
     */
    var updateKey: Number?

    /**
     * Determines if the toast was limited because the toast limit was exceeded.
     */
    var limited: Boolean?

    /**
     * The height of the toast.
     */
    var height: Number?

    /**
     * Callback function to be called when the toast is closed.
     */
    var onClose: (() -> Unit)?

    /**
     * Callback function to be called when the toast is removed from the list after any animations are complete when closed.
     */
    var onRemove: (() -> Unit)?

    /**
     * The props for the action button.
     */
    var actionProps: Props? /* React.ComponentPropsWithoutRef<'button'> */

    /**
     * The props forwarded to the toast positioner element when rendering anchored toasts.
     */
    var positionerProps: ToastManagerPositionerProps?

    /**
     * Custom data for the toast.
     */
    var data: Any?
}

external interface ToastManagerPositionerProps :
    ToastPositionerProps {
    /**
     * An element to position the toast against.
     */
    var anchor: Any? /* Element */
}

external interface UseToastManagerReturnValue<Data> {
    var toasts: ReadonlyArray<ToastObject<Data>>

    var add: (options: ToastManagerAddOptions<Props>) -> String

    var close: (toastId: String?) -> Unit

    var update: ToastManagerUpdate<Data>

    var promise: (promise: Promise<Props>, options: ToastManagerPromiseOptions<Props, Props>) -> Promise<*>
}

external interface ToastManagerAddOptions<Data> : ToastObject<Data> {
    /**
     * The unique identifier for the toast. Adding a toast with an existing ID
     * updates it in place and refreshes its auto-dismiss timer.
     */
    var id: ElementId?
}

external interface ToastManagerUpdateOptions<Data>

external interface ToastManagerPromiseOptions<Value, Data> {
    var loading: Any? /* string | ToastManagerUpdateOptions<Data> */

    var success: Any? /* string | ToastManagerUpdateOptions<Data> | ((result: Value) => string | ToastManagerUpdateOptions<Data>) */

    var error: Any? /* string | ToastManagerUpdateOptions<Data> | ((error: any) => string | ToastManagerUpdateOptions<Data>) */
}
