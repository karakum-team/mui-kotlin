package karakum.mui.adapters.treeview

fun String.adaptRichTreeView(): String = this
    .replace(
        oldValue = " | React.JSXElementConstructor<TreeItem2Props>",
        newValue = "",
    )
    .replace(
        oldValue = ": RichTreeViewSlotProps<R, Multiple>",
        newValue = ": RichTreeViewSlotProps",
    )
