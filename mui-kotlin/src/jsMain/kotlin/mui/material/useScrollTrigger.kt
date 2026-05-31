// Automatically generated - do not modify!

@file:JsModule("@mui/material/useScrollTrigger")

package mui.material

import web.events.EventTarget

external interface UseScrollTriggerOptions {
    var disableHysteresis: Boolean?

    var target: EventTarget /* Node? or web.window.Window? */?

    var threshold: Number?
}

@JsName("default")
external fun useScrollTrigger(
    options: UseScrollTriggerOptions? = definedExternally,
): Boolean
