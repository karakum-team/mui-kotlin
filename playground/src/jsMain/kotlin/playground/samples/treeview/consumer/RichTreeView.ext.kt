package playground.samples.treeview.consumer

import js.objects.unsafeJso
import kotlinx.js.JsPlainObject
import muix.tree.view.RichTreeViewProps
import muix.tree.view.RichTreeViewSlotProps
import muix.tree.view.RichTreeViewSlots
import muix.tree.view.TreeItemProps
import muix.tree.view.TreeItemSlotProps
import muix.tree.view.TreeItemSlots
import react.ElementType
import react.Props
import react.dom.events.KeyboardEvent
import react.dom.events.KeyboardEventHandler
import react.dom.events.SyntheticEvent
import web.html.HTMLLIElement

// Mirrors the extension file of a downstream application's tree view wrapper over `RichTreeView`. Every block
// below exists only to patch a hole in the generated `muix.tree.view` package; each block is deleted by the
// commit that fixes the corresponding generator gap, so the history of this file shows the consumer's
// workarounds disappearing one by one.
//
// `asDynamic()` is knowingly used here (`defaultMuiPrevented`, `dataUnselectable`) although playground/README.md
// confines it to `MissedWrappers.kt`: the point of this port is to reproduce the workarounds exactly as the
// original wrapper has them, until the generator makes them unnecessary.

// W1: the wrappers omit MUI's selection propagation parameter.
internal external interface RichTreeViewPropsExt : RichTreeViewProps {
    var selectionPropagation: SelectionPropagation
}

@JsPlainObject
internal external interface SelectionPropagation {
    val parents: Boolean
    val descendants: Boolean
}

// W2: `RichTreeViewSlotProps.item` is not generated, and the per-item `ownerState` resolver form is untyped.
internal external interface RichTreeViewSlotPropsExt : RichTreeViewSlotProps {
    var item: (TreeItemOwnerState) -> TreeItemProps
}

internal fun RichTreeViewSlotPropsExt(
    item: (TreeItemOwnerState) -> TreeItemProps,
): RichTreeViewSlotPropsExt =
    unsafeJso {
        this.item = item
    }

internal external interface TreeItemOwnerState {
    val itemId: String
}

// W3: `apiRef` is `Ref<*>?`, so the public API object has to be declared by hand.
internal external interface RichTreeViewApi {
    val getItemSelection: (String) -> String
    val setItemSelection: (ItemSelectionParameters) -> Unit
    val focusItem: (SyntheticEvent<*, *>?, String) -> Unit
}

@JsPlainObject
internal external interface ItemSelectionParameters {
    val itemId: String
    val event: SyntheticEvent<*, *>?
    val keepExistingSelection: Boolean
    val shouldBeSelected: Boolean
}

// W4: the `*Slots` interfaces carry no `@JsPlainObject` factory.
internal fun RichTreeViewSlots(
    expandIcon: ElementType<*>,
    collapseIcon: ElementType<*>,
): RichTreeViewSlots =
    unsafeJso {
        this.expandIcon = expandIcon
        this.collapseIcon = collapseIcon
    }

internal fun TreeItemProps(
    slots: TreeItemSlots?,
    slotProps: TreeItemSlotProps?,
    onKeyDown: KeyboardEventHandler<HTMLLIElement>?,
): TreeItemProps =
    unsafeJso {
        this.slots = slots
        this.slotProps = slotProps
        this.onKeyDown = onKeyDown
    }

internal fun TreeItemSlots(
    checkbox: ElementType<*>,
): TreeItemSlots =
    unsafeJso {
        this.checkbox = checkbox
    }

// W9: every slot prop is `Props?`, so the factory cannot say what each slot accepts.
internal fun TreeItemSlotProps(
    content: Props?,
    label: Props?,
    checkbox: Props?,
): TreeItemSlotProps =
    unsafeJso {
        this.content = content
        this.label = label
        this.checkbox = checkbox
    }

// W5: `TreeViewCancellableEvent.defaultMuiPrevented` is not exposed on the handler's event.
internal var KeyboardEvent<HTMLLIElement>.defaultMuiPrevented: Boolean
    get() = asDynamic().defaultMuiPrevented
    set(value) {
        asDynamic().defaultMuiPrevented = value
    }

// W12: application-specific attribute, stays.
internal const val UNSELECTABLE_ATTRIBUTE: String = "data-unselectable"

internal var Props.dataUnselectable: Boolean
    get() = asDynamic()[UNSELECTABLE_ATTRIBUTE]
    set(value) {
        asDynamic()[UNSELECTABLE_ATTRIBUTE] = value
    }
