// Automatically generated - do not modify!

package baseui

import seskar.js.JsValue

sealed external interface Side {
    companion object {
        @JsValue("top")
        val top: Side

        @JsValue("bottom")
        val bottom: Side

        @JsValue("left")
        val left: Side

        @JsValue("right")
        val right: Side

        @JsValue("inline-end")
        val inlineEnd: Side

        @JsValue("inline-start")
        val inlineStart: Side
    }
}

sealed external interface Align {
    companion object {
        @JsValue("start") val start: Align
        @JsValue("center") val center: Align
        @JsValue("end") val end: Align
    }
}
