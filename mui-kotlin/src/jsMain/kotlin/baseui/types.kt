// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface ChangeEventCustomProperties {
    var direction: Direction?
}

external interface IncrementValueParameters {
    var direction: Direction

    var event: Any? /* Event | React.SyntheticEvent */

    var reason: DirectionalChangeReason

    var currentValue: Number?
}

external interface EventWithOptionalKeyState {
    var altKey: Boolean?

    var shiftKey: Boolean?
}
