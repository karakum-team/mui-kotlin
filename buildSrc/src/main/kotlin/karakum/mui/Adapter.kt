package karakum.mui

import karakum.mui.adapters.*
import karakum.mui.adapters.datepickers.adaptLocalizationProvider
import karakum.mui.adapters.treeview.adaptRichTreeView
import karakum.mui.adapters.treeview.adaptTreeItem
import karakum.mui.adapters.treeview.adaptTreeView

fun String.adaptRawContent(): String = this
    // MUI v6 occasionally puts `; // line comment` on the same line as a property's terminating
    // semicolon (e.g. Typography.color). The generator splits members by `;\n` and gets confused
    // when the `;` is not immediately followed by `\n`. Strip the line comment first.
    .replace(Regex(""";\s*//[^\n]*\n"""), ";\n")
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
    .adaptLocalizationProvider()
