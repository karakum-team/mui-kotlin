// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface NumberFieldScrubAreaProps :
    BaseUiSpanProps {
    /**
     * Cursor movement direction in the scrub area.
     * @default 'horizontal'
     */
    var direction: NumberFieldScrubAreaDirection?

    /**
     * Determines how many pixels the cursor must move before the value changes.
     * A higher value will make scrubbing less sensitive.
     * @default 2
     */
    var pixelSensitivity: Number?

    /**
     * If specified, determines the distance that the cursor may move from the center
     * of the scrub area before it will loop back around.
     */
    var teleportDistance: Number?
}

external interface NumberFieldScrubAreaState : NumberFieldRootState
