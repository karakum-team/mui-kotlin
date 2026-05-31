// Automatically generated - do not modify!

@file:JsModule("@mui/material/MobileStepper")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
)

package mui.material

import mui.material.styles.Theme
import mui.system.SxProps

external interface MobileStepperProps :
    mui.system.StandardProps,
    PaperProps,
    MobileStepperSlotsAndSlotProps,
    mui.system.PropsWithSx {
    /**
     * Set the active step (zero based index).
     * Defines which dot is highlighted when the variant is 'dots'.
     * @default 0
     */
    var activeStep: Number?

    /**
     * A back button element. For instance, it can be a `Button` or an `IconButton`.
     */
    var backButton: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: MobileStepperClasses?

    /**
     * Props applied to the `LinearProgress` element.
     * @deprecated Use `slotProps.progress` instead. This prop will be removed in v7. See [Migrating from deprecated APIs](/material-ui/migration/migrating-from-deprecated-apis/) for more details.
     */
    var LinearProgressProps: LinearProgressProps?

    /**
     * A next button element. For instance, it can be a `Button` or an `IconButton`.
     */
    var nextButton: react.ReactNode

    /**
     * Set the positioning type.
     * @default 'bottom'
     */
    var position: MobileStepperPosition?

    /**
     * The total steps.
     */
    var steps: Number

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?

    /**
     * The variant to use.
     * @default 'dots'
     */
    var variant: MobileStepperVariant?
}

external interface MobileStepperSlots {
    /**
     * The component that renders the root slot.
     * @default Paper
     */
    var root: react.ElementType<*>

    /**
     * The component that renders the progress slot.
     * @default LinearProgress
     */
    var progress: react.ElementType<*>

    /**
     * The component that renders the dots slot.
     * @default 'div'
     */
    var dots: react.ElementType<*>

    /**
     * The component that renders the dot slot.
     * @default 'div'
     */
    var dot: react.ElementType<*>
}

external interface MobileStepperRootSlotPropsOverrides

external interface MobileStepperProgressSlotPropsOverrides

external interface MobileStepperDotsSlotPropsOverrides

external interface MobileStepperDotSlotPropsOverrides

external interface MobileStepperSlotProps : react.Props {
    /** TS: SlotProps< React.ElementType<PaperProps>, MobileStepperRootSlotPropsOverrides, MobileStepperOwnerState > */
    var root: PaperProps?

    /** TS: SlotProps< React.ElementType<LinearProgressProps>, MobileStepperProgressSlotPropsOverrides, MobileStepperOwnerState > */
    var progress: LinearProgressProps?

    /** TS: SlotProps<'div', MobileStepperDotsSlotPropsOverrides, MobileStepperOwnerState> */
    var dots: react.dom.html.HTMLAttributes<web.html.HTMLDivElement>?

    /** TS: SlotProps<'div', MobileStepperDotSlotPropsOverrides, MobileStepperOwnerState> */
    var dot: react.dom.html.HTMLAttributes<web.html.HTMLDivElement>?
}

external interface MobileStepperSlotsAndSlotProps : react.Props {
    var slots: MobileStepperSlots?

    var slotProps: MobileStepperSlotProps?
}

external interface MobileStepperOwnerState

/**
 *
 * Demos:
 *
 * - [Stepper](https://v6.mui.com/material-ui/react-stepper/)
 *
 * API:
 *
 * - [MobileStepper API](https://v6.mui.com/material-ui/api/mobile-stepper/)
 * - inherits [Paper API](https://v6.mui.com/material-ui/api/paper/)
 */
@JsName("default")
external val MobileStepper: react.FC<MobileStepperProps>
