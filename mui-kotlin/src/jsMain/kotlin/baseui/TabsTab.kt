// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import web.dom.ElementId

external interface TabsTabProps :
    NativeButtonProps,
    BaseUiButtonProps {
    /**
     * The value of the Tab.
     */
    var value: Any?

    /**
     * Whether the Tab is disabled.
     *
     * If a first Tab on a `<Tabs.List>` is disabled, it won't initially be selected.
     * Instead, the next enabled Tab will be selected.
     * However, it does not work like this during server-side rendering, as it is not known
     * during pre-rendering which Tabs are disabled.
     * To work around it, ensure that `defaultValue` or `value` on `<Tabs.Root>` is set to an enabled Tab's value.
     */
    var disabled: Boolean?
}

external interface TabsTabPosition {
    var left: Number

    var right: Number

    var top: Number

    var bottom: Number
}

external interface TabsTabSize {
    var width: Number

    var height: Number
}

external interface TabsTabMetadata {
    var disabled: Boolean

    var id: ElementId?

    var value: Any?
}

external interface TabsTabState {
    /**
     * Whether the component should ignore user interaction.
     */
    var disabled: Boolean

    /**
     * Whether the component is active.
     */
    var active: Boolean

    /**
     * The component orientation.
     */
    var orientation: Orientation

    /**
     * The direction used for tab activation.
     */
    var tabActivationDirection: TabsTabActivationDirection
}
