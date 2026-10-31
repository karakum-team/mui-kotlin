package karakum.mui.adapters.treeview

// MUI-X v9 `RichTreeViewSlotProps<R, Multiple>` is generic in source, but the wrapper widens its generics away and
// references it param-less (`slotProps?: RichTreeViewSlotProps`), see the matching declaration-side replacement in
// `convertDefinitions`.
fun String.adaptRichTreeView(): String = this
    .replace(
        oldValue = ": RichTreeViewSlotProps<R, Multiple>",
        newValue = ": RichTreeViewSlotProps",
    )
