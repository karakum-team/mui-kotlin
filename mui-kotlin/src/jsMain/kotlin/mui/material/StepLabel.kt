// Automatically generated - do not modify!

@file:JsModule("@mui/material/StepLabel")

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface StepLabelProps :
    mui.system.StandardProps,
    react.dom.html.HTMLAttributes<web.html.HTMLDivElement>,
    react.PropsWithChildren,
    mui.system.PropsWithSx {
    /**
     * In most cases will simply be a string containing a title for the label.
     */
    override var children: react.ReactNode?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: StepLabelClasses?

    /**
     * The props used for each slot inside.
     * @default {}
     * @deprecated use the `slotProps` prop instead. This prop will be removed in v7. See [Migrating from deprecated APIs](https://mui.com/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var componentsProps: ComponentsProps?

    interface ComponentsProps {
        var label: react.Props? /* React.HTMLProps<HTMLSpanElement> */
    }

    /**
     * If `true`, the step is marked as failed.
     * @default false
     */
    var error: Boolean?

    /**
     * Override the default label of the step icon.
     */
    var icon: react.ReactNode?

    /**
     * The optional node to display.
     */
    var optional: react.ReactNode?

    /**
     * The component to render in place of the [`StepIcon`](https://mui.com/material-ui/api/step-icon/).
     * @deprecated Use `slots.stepIcon` instead. This prop will be removed in v7. See [Migrating from deprecated APIs](/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var StepIconComponent: react.ElementType<StepIconProps>?

    /**
     * Props applied to the [`StepIcon`](https://mui.com/material-ui/api/step-icon/) element.
     * @deprecated Use `slotProps.stepIcon` instead. This prop will be removed in v7. See [Migrating from deprecated APIs](/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var StepIconProps: StepIconProps?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?
}

external interface StepLabelSlots {
    /**
     * The component that renders the root.
     * @default span
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the label.
     * @default span
     */
    var label: react.ElementType<*>

    /**
     * The component to render in place of the [`StepIcon`](https://mui.com/material-ui/api/step-icon/).
     */
    var stepIcon: react.ElementType<*>
}

external interface StepLabelOwnerState

/**
 *
 * Demos:
 *
 * - [Stepper](https://v6.mui.com/material-ui/react-stepper/)
 *
 * API:
 *
 * - [StepLabel API](https://v6.mui.com/material-ui/api/step-label/)
 */
@JsName("default")
external val StepLabel: react.FC<StepLabelProps>
