package karakum.mui.adapters.treeview

fun String.adaptTreeView(): String = this
    .replace(
        oldValue = """export interface TreeViewProps<Multiple extends boolean | undefined> extends SimpleTreeViewProps<Multiple> {
}
export interface TreeViewSlots extends SimpleTreeViewSlots {
}
export interface TreeViewSlotProps extends SimpleTreeViewSlotProps {
}
export type SingleSelectTreeViewProps = SimpleTreeViewProps<false>;
export type MultiSelectTreeViewProps = SimpleTreeViewProps<true>;
""",
        newValue = """export interface TreeViewProps<Multiple extends boolean | undefined> extends SimpleTreeViewProps<Multiple> {
}""",
    )
