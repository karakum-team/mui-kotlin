@file:JsModule("@mui/base/useTransition")

import react.Context

// Test scaffolding for the legacy transition components, whose surrounding Popup is not generated.
// The repaired CssAnimation/CssTransition themselves are imported from the generated bindings.
@JsName("TransitionContext")
internal external val transitionFixtureContext: Context<TransitionFixture>

internal external interface TransitionFixture {
    var requestedEnter: Boolean
    var registerTransition: () -> (() -> Unit)
    var onExited: () -> Unit
}
