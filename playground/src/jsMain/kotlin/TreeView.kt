import js.objects.unsafeJso
import mui.icons.material.ChevronRight
import mui.icons.material.ExpandMore
import mui.material.Typography
import mui.system.sx
import muix.tree.view.SimpleTreeView
import muix.tree.view.SimpleTreeViewSlots
import muix.tree.view.TreeItem
import muix.tree.view.TreeItemProps
import muix.tree.view.treeItemClasses
import react.ChildrenBuilder
import react.FC
import react.Props
import react.ReactNode
import web.cssom.FontWeight
import web.cssom.px

// Live check of the generated `muix.tree.view` declarations, and the only usage reference for them in
// the repository. Every prop below is set through a *typed* member, so a declaration that stops being
// generated breaks the build here rather than failing silently at some consumer's call site.
//
// Three things this can establish that `:mui-kotlin:compileKotlinJs` cannot:
//  - `itemId` and `label` reach the component at all. Neither is declared on `TreeItemProps`; both are
//    inherited from `UseTreeItemParameters`, generated type-only out of `useTreeItem/useTreeItem.types.d.ts`.
//    Only a tree that actually renders its labels proves that inheritance lands on the right JS props.
//  - `disabled` suppresses selection. MUI X 9.10.1 changed selection propagation to exclude disabled
//    items, and it is the one behavioural change in the 9.8 → 9.12 range with a visible effect.
//  - `disableSelection` is a *different* prop from `disabled`: "Archive" stays enabled — it expands,
//    takes focus and hovers — but never becomes selected.
//
// `slots` carries the expand/collapse icons through `ElementType<*>`, and `sx` targets the generated
// `treeItemClasses` keys, so a renamed class key shows up as unstyled output rather than as an error.
val TreeView = FC<Props> {
    Typography {
        +"SimpleTreeView — muix.tree.view"
    }

    SimpleTreeView {
        slots = unsafeJso<SimpleTreeViewSlots> {
            expandIcon = ChevronRight
            collapseIcon = ExpandMore
        }

        sx {
            maxWidth = 420.px

            treeItemClasses.label {
                fontSize = 14.px
            }

            treeItemClasses.content {
                borderRadius = 6.px
            }
        }

        node("workspace", "Workspace") {
            node("src", "src") {
                leaf("app", "App.kt")
                leaf("tree", "TreeView.kt")
            }

            node("docs", "docs") {
                leaf("readme", "README.md")
            }

            // 9.10.1: disabled items are excluded from selection propagation.
            leaf("reports", "Reports (disabled)") {
                disabled = true
            }

            // Enabled and expandable, but never selected — a different prop from `disabled`.
            node("archive", "Archive (disableSelection)", { disableSelection = true }) {
                leaf("archive-2025", "2025.zip")
            }
        }
    }
}

private fun ChildrenBuilder.node(
    id: String,
    text: String,
    configure: TreeItemProps.() -> Unit = {},
    children: ChildrenBuilder.() -> Unit,
) {
    TreeItem {
        itemId = id
        label = ReactNode(text)

        // Scoped to this item's OWN content row. A bare `treeItemClasses.label` selector here would
        // cascade into every descendant item's label too, because nested Tree Items render inside this
        // item's subtree — which would make leaves look like they had opted into bold.
        sx {
            "& > .${treeItemClasses.content} .${treeItemClasses.label}" {
                fontWeight = FontWeight.bold
            }
        }

        configure()
        children()
    }
}

private fun ChildrenBuilder.leaf(
    id: String,
    text: String,
    configure: TreeItemProps.() -> Unit = {},
) {
    TreeItem {
        itemId = id
        label = ReactNode(text)
        configure()
    }
}
