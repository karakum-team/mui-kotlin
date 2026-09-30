import csstype.PropertiesBuilder
import js.objects.unsafeJso
import mui.material.Button
import mui.material.Typography
import muix.tree.view.RichTreeView
import muix.tree.view.RichTreeViewProps
import muix.tree.view.RichTreeViewSlotProps
import muix.tree.view.RichTreeViewSlots
import muix.tree.view.TreeItemLoader
import muix.tree.view.TreeItemLoaderProps
import muix.tree.view.richTreeViewClasses
import muix.tree.view.treeItemLoaderClasses
import react.FC
import react.Props
import react.useState
import web.cssom.ClassName
import web.cssom.NamedColor
import web.cssom.px

// `items` still lives behind the unconverted UseTreeViewStoreParameters<TStore> upstream type.
// Keep that existing gap explicit: these sample-only shapes let us exercise the new, generated
// loading API without claiming the library now exposes the rest of the store parameters.
private external interface LoadingTreeItem {
    var id: String
    var label: String
}

private external interface LoadingTreeProps : RichTreeViewProps {
    var items: Array<LoadingTreeItem>
}

private val LoadingTree = RichTreeView.unsafeCast<FC<LoadingTreeProps>>()

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
val TreeViewLoading = FC<Props> {
    var isLoading by useState(true)
    var customLoading by useState(false)

    Typography {
        +"RichTreeView loading — MUI X 9.14.0"
    }

    Button {
        onClick = { isLoading = !isLoading }
        +(if (isLoading) "Finish loading" else "Reload tree")
    }
    Button {
        onClick = { customLoading = !customLoading }
        +(if (customLoading) "Use item loader slot" else "Use custom loading slot")
    }

    LoadingTree {
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
}
