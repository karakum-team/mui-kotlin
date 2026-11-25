// Automatically generated - do not modify!

package baseui

import seskar.js.JsValue

sealed /* union */
external interface MenuFilterProviderChangeEventReason {
    companion object {
        @JsValue("inputChange")
        val inputChange: MenuFilterProviderChangeEventReason

        @JsValue("inputClear")
        val inputClear: MenuFilterProviderChangeEventReason

        @JsValue("clearPress")
        val clearPress: MenuFilterProviderChangeEventReason

        @JsValue("popupClose")
        val popupClose: MenuFilterProviderChangeEventReason
    }
}
