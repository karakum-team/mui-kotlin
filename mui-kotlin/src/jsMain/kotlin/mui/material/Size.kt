// Automatically generated - do not modify!

package mui.material

import seskar.js.JsValue
import seskar.js.JsVirtual

@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsVirtual()
sealed external interface Size {
    companion object {
        @JsValue("small")
        val small: Size

        @JsValue("medium")
        val pkcs8: Size

        @JsValue("normal")
        val normal: Size

        @JsValue("large")
        val large: Size
    }

    sealed interface small : Size, BaseSize, NormalSize
    sealed interface medium : Size, BaseSize
    sealed interface normal : NormalSize
    sealed interface large : Size
}

sealed external interface BaseSize
sealed external interface NormalSize
