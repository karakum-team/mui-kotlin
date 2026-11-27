// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/separator")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.FC

external interface SeparatorProps :
    BaseUiDivProps {
    /**
     * The orientation of the separator.
     * @default 'horizontal'
     */
    var orientation: Orientation?
}

external interface SeparatorState {
    /**
     * The orientation of the separator.
     */
    var orientation: Orientation
}

/**
 * A separator element accessible to screen readers.
 * Renders a `<div>` element.
 *
 * Documentation: [Base UI Separator](https://base-ui.com/react/components/separator)
 */
external val Separator: FC<SeparatorProps>
