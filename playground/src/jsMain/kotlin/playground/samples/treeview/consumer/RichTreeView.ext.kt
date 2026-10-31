package playground.samples.treeview.consumer

import js.objects.unsafeJso
import kotlinx.js.JsPlainObject
import muix.tree.view.TreeItemProps
import muix.tree.view.TreeItemSlotProps
import muix.tree.view.TreeItemSlots
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

// `TreeItemProps` and `TreeItemSlotProps` extend `react.Props`, which the js-plain-objects plugin does not accept
// as a supertype, so these two factories stay hand-written for good.
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
