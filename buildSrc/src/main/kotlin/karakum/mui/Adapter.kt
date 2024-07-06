package karakum.mui

import karakum.mui.adapters.*
import karakum.mui.adapters.treeview.adaptRichTreeView
import karakum.mui.adapters.treeview.adaptTreeItem
import karakum.mui.adapters.treeview.adaptTreeView

fun String.adaptRawContent(): String = this
    .adaptComponentsAndSlots()
    .adaptClasses()
    .adaptInput()
    .adaptOption()
    .adaptSelect()
    .adaptFormControl()
    .adaptModal()
    .adaptAlert()
    .adaptAccordion()
    .adaptAutocomplete()
    .adaptUseAutocomplete()
    .adaptBreadcrumbs()
    .adaptUseMenu()
    .adaptUseSlider()
    .adaptInitColorSchemeScript()
    .adaptRichTreeView()
    .adaptTreeItem()
    .adaptTreeView()
