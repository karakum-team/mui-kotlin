package playground.samples.treeview

import csstype.PropertiesBuilder
import js.objects.unsafeJso
import mui.material.Button
import muix.tree.view.*
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.useState
import web.cssom.ClassName
import web.cssom.NamedColor
import web.cssom.px

private external interface LoadingTreeItem {
    var id: String
    var label: String
}

private val loadingTreeItems = arrayOf(
    unsafeJso<LoadingTreeItem> {
        id = "loaded-file"
        label = "Loaded file"
    },
)

// Consume the generated ownerState type and forward the actual slot props to the public component.
private val LoadingRow = FC<TreeItemLoaderProps> { props ->
    TreeItemLoader {
        +props
        props.ownerState?.let { state ->
            title = "Row ${state.index.toInt() + 1} of ${state.itemsCount.toInt()}, depth ${state.itemDepth.toInt()}"
        }
    }
}

private val CustomLoading = FC<Props> {
    TreeItemLoader {
        +"Loading project files…"
    }
}

// MUI X 9.13+: covers loading, both loading slots, the public TreeItemLoader export and class keys.
val RichTreeViewLoadingSample: Sample = Sample(
    id = "rich-tree-view-loading",
    library = Libraries.TreeView,
    title = "RichTreeView loading",
    kind = Kind.Check,
    covers = listOf(
        "RichTreeView.loading",
        "RichTreeViewSlots.loading/itemLoader",
        "RichTreeViewSlotProps.itemLoader (SlotComponentProps)",
        "TreeItemLoader",
        "TreeItemLoaderProps.ownerState (TreeItemLoaderOwnerState)",
        "richTreeViewClasses.itemLoader",
        "treeItemLoaderClasses.label",
    ),
    fileName = "RichTreeViewLoadingSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — MUI X 9.12 → 9.14",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#mui-x-912--914-update-2026-10-01",
        ),
        Ref("48b0e810", "https://github.com/karakum-team/mui-kotlin/commit/48b0e810"),
        Ref("10dc9f7c", "https://github.com/karakum-team/mui-kotlin/commit/10dc9f7c"),
    ),
    notes = "Only running this proves that the public `TreeItemLoader` export resolves and that both loading " +
            "slots are honoured: \"Finish loading\" swaps the loader rows for the tree, \"Use custom loading " +
            "slot\" replaces them with \"Loading project files…\", and each item-loader row carries a title built " +
            "from its typed `ownerState`.",
    content = FC<Props> {
        var isLoading by useState(true)
        var customLoading by useState(false)

        Button {
            onClick = { isLoading = !isLoading }
            +(if (isLoading) "Finish loading" else "Reload tree")
        }
        Button {
            onClick = { customLoading = !customLoading }
            +(if (customLoading) "Use item loader slot" else "Use custom loading slot")
        }

        StatusLine {
            id = "rich-tree-view-loading-status"
            fields = listOf(
                "loading" to isLoading,
                "customSlot" to customLoading,
            )
        }

        RichTreeView<RichTreeViewProps<LoadingTreeItem>> {
            className = ClassName("loading-tree")
            items = loadingTreeItems
            loading = isLoading
            slots = if (customLoading) {
                RichTreeViewSlots(itemLoader = LoadingRow, loading = CustomLoading)
            } else {
                RichTreeViewSlots(itemLoader = LoadingRow)
            }
            slotProps = unsafeJso<RichTreeViewSlotProps> {
                itemLoader = SlotComponentProps(
                    unsafeJso<TreeItemLoaderProps> {
                        className = ClassName("loading-row")
                    },
                )
            }
            sx = unsafeJso<PropertiesBuilder> {
                maxWidth = 420.px
                richTreeViewClasses.itemLoader {
                    minHeight = 32.px
                }
                treeItemLoaderClasses.label {
                    backgroundColor = NamedColor.lavender
                }
            }
        }
    },
)
