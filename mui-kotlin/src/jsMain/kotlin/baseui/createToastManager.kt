// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import js.promise.Promise
import react.Props

external interface ToastManager<Data> {
    @JsName(" subscribe")
    var subscribe: (listener: (data: ToastManagerEvent) -> Unit) -> () -> Unit

    var add: (options: ToastManagerAddOptions<Props>) -> String

    var close: (id: String?) -> Unit

    var update: ToastManagerUpdate<Data>

    var promise: (promiseValue: Promise<Props>, options: ToastManagerPromiseOptions<Props, Props>) -> Promise<*>
}

external interface ToastManagerEvent {
    var action: createToastManagerAction

    var options: Any
}
