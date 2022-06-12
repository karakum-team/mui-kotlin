// Automatically generated - do not modify!

package mui.base

import csstype.ClassName

external interface SwitchUnstyledProps : react.PropsWithClassName {
    /**
     * Class name applied to the root element.
     */
    override var className: ClassName?

    /**
     * The component used for the Root slot.
     * Either a string to use a HTML element or a component.
     * This is equivalent to `components.Root`. If both are provided, the `component` is used.
     */
    var component: react.ElementType<*>?

    /**
     * The components used for each slot inside the Switch.
     * Either a string to use a HTML element or a component.
     * @default {}
     */
    var components: Components?

    interface Components {
        var Root: react.ElementType<*>?
        var Thumb: react.ElementType<*>?
        var Input: react.ElementType<*>?
        var Track: react.ElementType<*>? /* React.ElementType | null */
    }

    /**
     * The props used for each slot inside the Switch.
     * @default {}
     */
    var componentsProps: ComponentsProps?

    interface ComponentsProps {
        var root: react.Props? /* SlotComponentProps<'span', SwitchUnstyledComponentsPropsOverrides, SwitchUnstyledOwnerState> */
        var thumb: react.Props? /* SlotComponentProps<'span', SwitchUnstyledComponentsPropsOverrides, SwitchUnstyledOwnerState> */
        var input: react.Props? /* SlotComponentProps<'input', SwitchUnstyledComponentsPropsOverrides, SwitchUnstyledOwnerState> */
        var track: react.Props? /* SlotComponentProps<'span', SwitchUnstyledComponentsPropsOverrides, SwitchUnstyledOwnerState> */
    }
}
