// Automatically generated - do not modify!

package muix.tree.view

import web.dom.Element
import web.cssom.ClassName
import mui.material.styles.Theme
import mui.system.SxProps
import mui.system.PropsWithSx
import react.ElementType
import react.Props
import react.PropsWithChildren
import react.PropsWithClassName
import react.ReactNode
import react.Ref

external interface SimpleTreeViewProps :
    PropsWithChildren,
    PropsWithClassName,
    PropsWithSx {
    /**
     * The content of the component.
     */
    override var children: ReactNode?

    /**
     * Overridable component slots.
     */
    var slots: SimpleTreeViewSlots?

    /**
     * The props used for each component slot.
     */
    var slotProps: SimpleTreeViewSlotProps?

    override var className: ClassName?

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: SimpleTreeViewClasses?

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    override var sx: SxProps<Theme>?

    /**
     * The ref object that allows Tree View manipulation. Can be instantiated with `useSimpleTreeViewApiRef()`.
     */
    var apiRef: Ref<*>?
}

external interface SimpleTreeViewSlots : TreeViewSlots {
    /**
     * Element rendered at the root.
     * @default SimpleTreeViewRoot
     */
    var root: ElementType<*>?
}

external interface SimpleTreeViewSlotProps :
    TreeViewSlotProps {
    var root: Props?
}
