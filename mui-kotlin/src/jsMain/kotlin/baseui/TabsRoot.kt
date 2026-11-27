// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

external interface TabsRootProps :
    BaseUiDivProps {
    /**
     * The value of the currently active `Tab`. Use when the component is controlled.
     * When the value is `null`, no Tab will be active.
     */
    var value: Any?

    /**
     * The default value. Use when the component is not controlled.
     * When the value is `null`, no Tab will be active.
     * @default 0
     */
    var defaultValue: Any?

    /**
     * The component orientation (layout flow direction).
     * @default 'horizontal'
     */
    var orientation: Orientation?

    /**
     * Callback invoked when new value is being set.
     *
     * The event `reason` is `'none'` for user-initiated changes, such as a click
     * or keyboard navigation; `'initial'` for the first automatic selection or
     * fallback in uncontrolled roots when `defaultValue` is omitted or
     * `undefined`, including when the implicit initial value is disabled or
     * missing; `'disabled'` for automatic fallback when the selected tab becomes
     * disabled in uncontrolled roots; or `'missing'` for automatic fallback when
     * the selected tab is removed, or when an explicit `defaultValue` never
     * matches a mounted tab in uncontrolled roots.
     *
     * For automatic changes, the selected value can be `null` when no enabled Tab
     * is available as a fallback.
     *
     * Automatic changes cannot be canceled; calling `eventDetails.cancel()` for
     * `'initial'`, `'disabled'`, or `'missing'` has no effect.
     */
    var onValueChange: ((value: Any?, eventDetails: TabsRootChangeEventDetails) -> Unit)?
}

external interface TabsRootState {
    /**
     * The component orientation.
     */
    var orientation: Orientation

    /**
     * The direction used for tab activation.
     */
    var tabActivationDirection: TabsTabActivationDirection
}

external interface TabsRootChangeEventDetails : BaseUIChangeEventDetails
