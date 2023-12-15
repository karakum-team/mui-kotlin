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
    @JsValue('small')
    object small : Size, BaseSize, NormalSize

    @JsValue('medium')
    object medium : Size, BaseSize

    @JsValue('normal')
    object normal : NormalSize

    @JsValue('large')
    object large : Size
}

sealed external interface BaseSize
sealed external interface NormalSize
