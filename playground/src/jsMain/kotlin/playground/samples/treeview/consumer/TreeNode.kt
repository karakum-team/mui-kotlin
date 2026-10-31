package playground.samples.treeview.consumer

data class TreeNode(
    val key: TreeNodeKey,
    val label: String,
    val disabled: Boolean = false,
    val selectable: Boolean = true,
    val children: List<TreeNode> = emptyList(),
)
