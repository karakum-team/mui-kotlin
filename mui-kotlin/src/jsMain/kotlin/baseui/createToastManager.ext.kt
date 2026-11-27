// Automatically generated - do not modify!

package baseui

import seskar.js.JsValue

sealed /* union */
external interface CreateToastManagerAction {
    companion object {
        @JsValue("add")
        val add: CreateToastManagerAction

        @JsValue("close")
        val close: CreateToastManagerAction

        @JsValue("update")
        val update: CreateToastManagerAction

        @JsValue("promise")
        val promise: CreateToastManagerAction
    }
}
