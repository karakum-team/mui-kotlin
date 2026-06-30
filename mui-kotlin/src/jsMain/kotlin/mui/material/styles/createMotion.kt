// Automatically generated - do not modify!

@file:JsModule("@mui/material/styles/createMotion")

package mui.material.styles

external interface Motion {
    var reducedMotion: String /* 'never' | 'system' | 'always' */
}

external interface MotionOptions {
    var reducedMotion: String? /* 'never' | 'system' | 'always' */
}

@JsName("default")
external fun createMotion(
    inputMotion: MotionOptions? = definedExternally,
): Motion
