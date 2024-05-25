package karakum.mui.adapters.treeview

fun String.adaptTreeItem(): String = this
    .replace(
        oldValue = "TreeViewItemId",
        newValue = "string",
    )
