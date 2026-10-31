package playground.samples.treeview.consumer

import js.objects.unsafeJso
import muix.tree.view.RichTreeViewItemsOwnerState
import muix.tree.view.RichTreeViewSlotProps
import muix.tree.view.RichTreeViewSlots
import muix.tree.view.SlotComponentProps
import muix.tree.view.TreeItemSlots
import muix.tree.view.TreeViewExpansionTrigger
import muix.tree.view.TreeViewSelectionPropagation
import muix.tree.view.TreeViewSelectionValue
import muix.tree.view.asMultiple
import react.FC
import react.PropsWithClassName
import react.ReactNode
import react.RefObject
import react.dom.events.KeyboardEventHandler
import react.dom.events.SyntheticEvent
import react.dom.html.HTMLAttributes
import react.useMemo
import react.useRef
import web.dom.Element
import web.dom.Node
import web.html.HTMLLIElement
import web.html.HTMLSpanElement
import muix.tree.view.RichTreeView as MuiRichTreeView

// Mirrors a downstream application's `TreeView` wrapper over `RichTreeView`. Deviations from the original: the
// application's theme (`SurfaceStyles`, `spacing`) is replaced by the local `Spacing.kt` / `TreeView.styles.kt`,
// `PropsWithChangeableValue` is a local copy, and `TreeViewProps.apiRef` is added so the sample can drive the
// public API from its checks (which makes both declarations `internal`: the hand-written `RichTreeViewApi` is).

// W6: `items` is `ReadonlyArray<*>` and every item callback receives `Any`.
private val GET_ITEM_CHILDREN = { item: Any ->
    item.unsafeCast<TreeNode>().children.toTypedArray()
}

private val ITEM_KEY_DOWN: KeyboardEventHandler<HTMLLIElement> = { e ->
    if (e.isFromInteractiveElement()) {
        e.defaultMuiPrevented = true
    }
}

private val IS_ITEM_DISABLED = { item: Any ->
    item.unsafeCast<TreeNode>().disabled
}

private val IS_ITEM_SELECTION_DISABLED = { item: Any ->
    !item.unsafeCast<TreeNode>().selectable
}

private val CASCADE_SELECTION_PROPAGATION = TreeViewSelectionPropagation(
    parents = false,
    descendants = true,
)

private val INDEPENDENT_SELECTION_PROPAGATION = TreeViewSelectionPropagation(
    parents = false,
    descendants = false,
)

private val SLOTS = RichTreeViewSlots(
    expandIcon = ExpandIcon,
    collapseIcon = CollapseIcon,
)

private val ITEM_SLOTS = TreeItemSlots(
    checkbox = TreeItemCheckbox,
)

internal external interface TreeViewProps :
    PropsWithChangeableValue<Set<String>> {
    var nodes: List<TreeNode>
    var defaultExpandedKeys: Set<TreeNodeKey>?
    var cascadeSelection: Boolean?
    var renderNode: ((TreeNode) -> ReactNode)?

    // Not in the original wrapper: lets the sample reach the public API the component keeps for its label clicks.
    var apiRef: RefObject<RichTreeViewApi>?
}

internal val TreeView: FC<TreeViewProps> = FC { props ->
    val nodes = props.nodes
    val value = props.value
    val defaultExpandedKeys = props.defaultExpandedKeys
    val cascade = props.cascadeSelection ?: true
    val renderNode = props.renderNode

    val tree = useMemo(nodes) { nodes.toTypedArray() }
    val selected = useMemo(value) { value.toTypedArray() }
    val defaultExpanded = useMemo(defaultExpandedKeys) { defaultExpandedKeys?.toTypedArray() }
    val nodeByKey = useMemo(nodes) { nodes.associateByKey() }

    val ownApi = useRef<RichTreeViewApi>()
    val treeViewApi = props.apiRef ?: ownApi

    val itemSlotProps = useMemo(treeViewApi, cascade, renderNode, nodeByKey) {
        unsafeJso<RichTreeViewSlotProps> {
            item = SlotComponentProps { ownerState ->
                val node = nodeByKey.getValue(ownerState.itemId)
                val nodeContent = renderNode?.invoke(node)

                TreeItemProps(
                    onKeyDown = ITEM_KEY_DOWN,
                    slots = ITEM_SLOTS,
                    slotProps = TreeItemSlotProps(
                        content = contentSlotProps(node),
                        label = labelSlotProps(treeViewApi, ownerState, nodeContent, node.selectable),
                        checkbox = checkboxSlotProps(node, cascade),
                    ),
                )
            }
        }
    }

    MuiRichTreeView {
        className = treeViewStyle()
        apiRef = treeViewApi

        multiSelect = true
        checkboxSelection = true
        selectionPropagation = if (cascade) CASCADE_SELECTION_PROPAGATION else INDEPENDENT_SELECTION_PROPAGATION
        expansionTrigger = TreeViewExpansionTrigger.iconContainer
        itemChildrenIndentation = 2.spacing

        slots = SLOTS
        slotProps = itemSlotProps

        items = tree
        getItemId = { it.unsafeCast<TreeNode>().key }
        getItemLabel = { it.unsafeCast<TreeNode>().label }
        getItemChildren = GET_ITEM_CHILDREN
        isItemDisabled = IS_ITEM_DISABLED
        isItemSelectionDisabled = IS_ITEM_SELECTION_DISABLED

        if (defaultExpanded != null) {
            defaultExpandedItems = defaultExpanded
        }

        selectedItems = TreeViewSelectionValue(selected)
        onSelectedItemsChange = { _, keys ->
            val selectedKeys = keys.asMultiple().toSet()
            props.onChange?.invoke(
                if (cascade) selectedKeys.withoutIncompleteParents(nodes) else selectedKeys,
            )
        }
    }
}

private fun Set<TreeNodeKey>.withoutIncompleteParents(
    nodes: List<TreeNode>,
): Set<TreeNodeKey> =
    toMutableSet().apply {
        fun removeIncompleteParent(node: TreeNode) {
            node.children.forEach(::removeIncompleteParent)
            if (node.children.any { it.key !in this }) remove(node.key)
        }

        nodes.forEach(::removeIncompleteParent)
    }

private const val INTERACTIVE_ELEMENTS: String = "button, a, input, select, textarea, [role=button]"

private fun SyntheticEvent<*, *>.isFromInteractiveElement(): Boolean {
    val interactive = target.unsafeCast<Element>().closest(INTERACTIVE_ELEMENTS)
        ?: return false

    return interactive != currentTarget && currentTarget.unsafeCast<Node>().contains(interactive)
}

private fun List<TreeNode>.associateByKey(): Map<TreeNodeKey, TreeNode> =
    buildMap {
        fun addNode(node: TreeNode) {
            put(node.key, node)
            node.children.forEach(::addNode)
        }

        forEach(::addNode)
    }

private fun contentSlotProps(
    node: TreeNode,
): PropsWithClassName =
    unsafeJso {
        when {
            !node.selectable -> {
                this.dataUnselectable = true
            }
            !node.disabled -> {
                this.className = nestedOnHover
            }
        }
    }

private fun checkboxSlotProps(
    node: TreeNode,
    cascade: Boolean,
): TreeItemCheckboxProps =
    unsafeJso {
        this.cascade = cascade
        this.selectable = node.selectable
    }

private fun labelSlotProps(
    apiRef: RefObject<RichTreeViewApi>,
    ownerState: RichTreeViewItemsOwnerState,
    content: ReactNode?,
    selectable: Boolean,
): HTMLAttributes<HTMLSpanElement> =
    unsafeJso {
        if (content != null) {
            children = content
        }

        onClick = fn@{ e ->
            val api = apiRef.current
                ?: return@fn

            e.stopPropagation()
            if (!selectable || e.isFromInteractiveElement()) return@fn

            api.focusItem(e, ownerState.itemId)
            api.setItemSelection(
                ItemSelectionParameters(
                    itemId = ownerState.itemId,
                    event = e,
                    keepExistingSelection = true,
                    shouldBeSelected = api.getItemSelection(ownerState.itemId) != "selected",
                ),
            )
        }
    }
