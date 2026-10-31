package playground.samples.treeview

import csstype.PropertiesBuilder
import js.objects.unsafeJso
import mui.material.Button
import muix.tree.view.*
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
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
    covers = listOf("RichTreeView loading", "TreeItemLoader", "richTreeViewClasses"),
    fileName = "RichTreeViewLoadingSample.kt",
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

        RichTreeView {
            className = ClassName("loading-tree")
            items = loadingTreeItems
            loading = isLoading
            slots = unsafeJso<RichTreeViewSlots> {
                itemLoader = LoadingRow
                if (customLoading) loading = CustomLoading
            }
            slotProps = unsafeJso<RichTreeViewSlotProps> {
                itemLoader = unsafeJso<TreeItemLoaderProps> {
                    className = ClassName("loading-row")
                }
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
