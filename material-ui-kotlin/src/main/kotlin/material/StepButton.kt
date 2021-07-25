// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/StepButton")
@file:JsNonModule

package material

external interface StepButtonProps : react.RProps {
    var active: Boolean

    var alternativeLabel: Boolean

    var completed: Boolean

    var disabled: Boolean

    var icon: react.ReactNode

    var last: Boolean

    var optional: react.ReactNode

    var orientation: dynamic
}

/**
 *
 * Demos:
 *
 * - [Steppers](https://material-ui.com/components/steppers/)
 *
 * API:
 *
 * - [StepButton API](https://material-ui.com/api/step-button/)
 * - inherits [ButtonBase API](https://material-ui.com/api/button-base/)
 */
@JsName("default")
external val StepButton: react.FC<StepButtonProps>
