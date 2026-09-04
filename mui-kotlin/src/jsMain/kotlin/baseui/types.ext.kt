// Automatically generated - do not modify!

package baseui

import seskar.js.JsValue

sealed external interface Direction {
    companion object {
        @JsValue("-1")
        val sMinus1: Direction

        @JsValue("1")
        val s1: Direction
    }
}

sealed external interface DirectionalChangeReason {
    companion object {
        @JsValue("increment-press")
        val incrementPress: DirectionalChangeReason

        @JsValue("decrement-press")
        val decrementPress: DirectionalChangeReason

        @JsValue("wheel")
        val wheel: DirectionalChangeReason

        @JsValue("scrub")
        val scrub: DirectionalChangeReason

        @JsValue("keyboard")
        val keyboard: DirectionalChangeReason
    }
}
