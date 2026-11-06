package playground.samples.treeview

import js.array.ReadonlyArray
import js.objects.unsafeJso
import mui.types.Value
import mui.types.single
import muix.tree.view.RichTreeView
import muix.tree.view.RichTreeViewProps
import muix.tree.view.SimpleTreeView
import muix.tree.view.TreeItem
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.ReactNode
import react.useState

private external interface SampleTreeItem {
    var id: String
    var label: String
}

private val sampleTreeItems: ReadonlyArray<SampleTreeItem> = arrayOf(
    unsafeJso<SampleTreeItem> {
        id = "item-1"
        label = "Item 1"
    },
    unsafeJso<SampleTreeItem> {
        id = "item-2"
        label = "Item 2"
    },
)

val RichTreeViewSelectionSample: Sample = Sample(
    id = "rich-tree-view-selection",
    library = Libraries.TreeView,
    title = "RichTreeView selection",
    kind = Kind.Check,
    covers = listOf(
        "RichTreeView.items/selectedItems (TreeViewSelectionValue / Value)",
        "RichTreeView.onSelectedItemsChange (single)",
        "RichTreeView.onItemClick",
        "SimpleTreeView.multiSelect",
        "TreeItem.itemId/label",
    ),
    fileName = "RichTreeViewSelectionSample.kt",
    refs = listOf(
        Ref("10dc9f7c", "https://github.com/karakum-team/mui-kotlin/commit/10dc9f7c"),
    ),
    notes = "Only running this proves that the plain `items` objects render as rows, and that the selection and " +
            "click callbacks deliver real item ids: clicking \"Item 2\" moves `selected` and sets `lastClick=item-2`.",
    content = FC<Props> {
        var selectedItem by useState<String?>("item-1")
        var lastClick by useState("-")

        StatusLine {
            id = "selection-status"
            fields = listOf(
                "selected" to selectedItem,
                "lastClick" to lastClick,
            )
        }

        // 1. Verify RichTreeView with items and selection handlers
        RichTreeView<RichTreeViewProps<SampleTreeItem>> {
            items = sampleTreeItems
            selectedItems = Value(selectedItem)
            onSelectedItemsChange = { _, itemIds ->
                selectedItem = itemIds?.single()
            }
            onItemClick = { _, itemId ->
                lastClick = itemId
            }
        }

        // 2. Verify SimpleTreeView with expandedItems and selection
        SimpleTreeView {
            multiSelect = false
            TreeItem {
                itemId = "simple-1"
                label = ReactNode("Simple Item 1")
            }
        }
    },
)
