package playground.samples.treeview

import kotlinx.coroutines.delay
import mui.material.Button
import muix.tree.view.richTreeViewClasses
import muix.tree.view.SetItemSelectionParameters
import muix.tree.view.useRichTreeViewApiRef
import playground.samples.treeview.consumer.TreeNode
import playground.samples.treeview.consumer.TreeView
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.small
import react.dom.html.ReactHTML.span
import react.useEffect
import react.useState
import web.dom.ElementId
import web.dom.document
import web.html.HTMLElement

private const val ROOT_ID = "consumer-tree-view-root"

// Fleet → Aircraft A (A-1, A-2) / Aircraft B (disabled) / Archive (unselectable; 2024, 2025).
private val FLEET: List<TreeNode> = listOf(
    TreeNode(
        key = "fleet",
        label = "Fleet",
        children = listOf(
            TreeNode(
                key = "aircraft-a",
                label = "Aircraft A",
                children = listOf(
                    TreeNode(key = "a-1", label = "A-1"),
                    TreeNode(key = "a-2", label = "A-2"),
                ),
            ),
            TreeNode(key = "aircraft-b", label = "Aircraft B", disabled = true),
            TreeNode(
                key = "archive",
                label = "Archive",
                selectable = false,
                children = listOf(
                    TreeNode(key = "archive-2024", label = "2024"),
                    TreeNode(key = "archive-2025", label = "2025"),
                ),
            ),
        ),
    ),
)

private const val NODE_COUNT = 8
private val ALL_EXPANDED = setOf("fleet", "aircraft-a", "archive")
private val AIRCRAFT_A_FAMILY = setOf("aircraft-a", "a-1", "a-2")

// The API scenario: select Aircraft A (step 1), wait for the cascade to reach `value` (step 2), deselect A-1 and
// wait for `withoutIncompleteParents` to drop the parent (step 3).
private enum class Phase { Start, Cascading, Pruning, Done }

// 1:1 port of a downstream application's tree view wrapper over `RichTreeView` (`samples/treeview/consumer/`),
// workarounds included. Each later commit fixes one generator gap and deletes the matching workaround from the
// port, so this sample is where every fix is proven.
val ConsumerTreeViewSample: Sample = Sample(
    id = "consumer-tree-view",
    library = Libraries.TreeView,
    title = "Consumer-style TreeView",
    kind = Kind.Check,
    covers = listOf(
        "RichTreeView.items/getItemId/getItemLabel/getItemChildren",
        "RichTreeView.isItemDisabled/isItemSelectionDisabled",
        "RichTreeView.multiSelect/checkboxSelection/selectionPropagation",
        "RichTreeView.expansionTrigger/itemChildrenIndentation/defaultExpandedItems",
        "RichTreeView.selectedItems/onSelectedItemsChange",
        "RichTreeView.apiRef / useRichTreeViewApiRef: RichTreeViewPublicAPI.setItemSelection/getItemSelection/focusItem",
        "RichTreeViewSlots.expandIcon/collapseIcon",
        "RichTreeViewSlotProps.item (ownerState -> TreeItemProps)",
        "TreeItemProps.onKeyDown/slots/slotProps",
        "TreeItemSlots.checkbox, TreeItemSlotProps.content/label/checkbox",
        "richTreeViewClasses.root/itemContent, treeItemClasses.content/label/iconContainer/checkbox",
        "defaultMuiPrevented, --TreeView-itemChildrenIndentation/--TreeView-itemDepth, data-* state attributes",
    ),
    fileName = "ConsumerTreeViewSample.kt",
    refs = listOf(
        Ref(
            "MUI_V9_TODO.md — Tree View behavioural props",
            "https://github.com/karakum-team/mui-kotlin/blob/main/MUI_V9_TODO.md#tree-view-the-behavioural-props-are-still-missing-found-during-the-98--912-bump",
        ),
    ),
    notes = "Port of a downstream application's tree view wrapper over RichTreeView, workarounds included " +
            "(samples/treeview/consumer/RichTreeView.ext.kt mirrors the original wrapper's extension file; its " +
            "one asDynamic() outside MissedWrappers.kt is the application-specific data-unselectable attribute). " +
            "Only running this proves that the hand-written store parameters " +
            "(selectionPropagation, items, item callbacks) reach MUI, that the custom checkbox / icon / label slots " +
            "render, that MUI sets the --TreeView-* custom properties and data-* attributes the styles rely on, and " +
            "that the public API drives selection: selecting Aircraft A cascades to A-1 and A-2, and deselecting A-1 " +
            "drops the now-incomplete parent from value.",
    content = FC<Props> {
        val (value, setValue) = useState(emptySet<String>())
        var changes by useState(0)
        val apiRef = useRichTreeViewApiRef<TreeNode>()
        val (phase, setPhase) = useState(Phase.Start)
        val (cascaded, setCascaded) = useState<Boolean?>(null)
        val (pruned, setPruned) = useState<Boolean?>(null)
        var domChecks by useState<List<Check>?>(null)

        // Step 1 runs after the first commit, when the tree's DOM and `apiRef.current` exist. A step the tree
        // never answers is reported as a failure instead of a check list that never mounts.
        useEffect(phase) {
            when (phase) {
                Phase.Start -> {
                    val api = apiRef.current
                    domChecks = domChecks(apiPresent = api != null)
                    if (api == null) {
                        setCascaded(false)
                        setPruned(false)
                        setPhase(Phase.Done)
                    } else {
                        api.setItemSelection(
                            SetItemSelectionParameters(
                                itemId = "aircraft-a",
                                keepExistingSelection = true,
                                shouldBeSelected = true,
                            ),
                        )
                        setPhase(Phase.Cascading)
                    }
                }

                Phase.Cascading, Phase.Pruning -> {
                    delay(1500)
                    setCascaded { it ?: false }
                    setPruned { it ?: false }
                    setPhase(Phase.Done)
                }

                Phase.Done -> Unit
            }
        }

        // Steps 2 and 3 react to the controlled value flowing back through `onChange`.
        useEffect(value, phase) {
            when (phase) {
                Phase.Cascading -> if (value.containsAll(AIRCRAFT_A_FAMILY)) {
                    setCascaded(true)
                    apiRef.current?.setItemSelection(
                        SetItemSelectionParameters(
                            itemId = "a-1",
                            keepExistingSelection = true,
                            shouldBeSelected = false,
                        ),
                    )
                    setPhase(Phase.Pruning)
                }

                Phase.Pruning -> if ("a-1" !in value) {
                    setPruned(value == setOf("a-2"))
                    setPhase(Phase.Done)
                }

                else -> Unit
            }
        }

        Button {
            onClick = {
                setValue(emptySet())
                setCascaded(null)
                setPruned(null)
                setPhase(Phase.Start)
            }
            +"Run the API scenario again"
        }

        StatusLine {
            id = "consumer-tree-view-status"
            fields = listOf(
                "selected" to value.sorted().joinToString(","),
                "changes" to changes,
                "phase" to phase.name,
            )
        }

        div {
            id = ElementId(ROOT_ID)

            TreeView {
                nodes = FLEET
                this.value = value
                onChange = {
                    setValue(it)
                    changes += 1
                }
                defaultExpandedKeys = ALL_EXPANDED
                cascadeSelection = true
                renderNode = { node ->
                    span.create {
                        +node.label
                        if (!node.selectable) {
                            small { +" (unselectable)" }
                        }
                    }
                }
                this.apiRef = apiRef
            }
        }

        val checks = domChecks
        if (phase == Phase.Done && checks != null) {
            CheckList {
                id = "consumer-tree-view-checks"
                this.checks = checks + listOf(
                    Check("API: selecting Aircraft A cascades to A-1 and A-2", cascaded == true),
                    Check("API: deselecting A-1 drops the incomplete parent from value", pruned == true),
                )
            }
        }
    },
)

private fun domChecks(apiPresent: Boolean): List<Check> {
    val root = document.getElementById(ElementId(ROOT_ID))
    val tree = root?.querySelector("ul[role=tree]")?.unsafeCast<HTMLElement>()
    val items = root?.querySelectorAll("li[role=treeitem]")
    val itemCount = items?.length ?: 0
    val unselectable = root?.querySelector(".MuiTreeItem-content[$UNSELECTABLE_ATTRIBUTE]")?.textContent.orEmpty()
    val disabled = root?.querySelector(".MuiTreeItem-content[data-disabled]")?.textContent.orEmpty()
    val indentation = tree?.style?.getPropertyValue("--TreeView-itemChildrenIndentation").orEmpty()
    val depths = (0 until itemCount).map { index ->
        items!![index].unsafeCast<HTMLElement>().style.getPropertyValue("--TreeView-itemDepth")
    }
    val contentsByClassKey = root?.querySelectorAll(".${richTreeViewClasses.itemContent}")?.length ?: 0

    return listOf(
        Check("$NODE_COUNT tree items rendered", itemCount == NODE_COUNT),
        Check("apiRef.current is set", apiPresent),
        Check("[data-unselectable] marks the Archive content", "Archive" in unselectable),
        Check("[data-disabled] marks the Aircraft B content", "Aircraft B" in disabled),
        Check("root style sets --TreeView-itemChildrenIndentation to 16px", indentation == "16px"),
        Check("item roots carry --TreeView-itemDepth 0, 1 and 2", depths.toSet() == setOf("0", "1", "2")),
        Check(
            "richTreeViewClasses.itemContent (MuiRichTreeView-itemContent) marks every item content",
            richTreeViewClasses.itemContent.toString() == "MuiRichTreeView-itemContent" && contentsByClassKey == NODE_COUNT,
        ),
    )
}

private const val UNSELECTABLE_ATTRIBUTE = "data-unselectable"
