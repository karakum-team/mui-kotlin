package karakum.mui

import karakum.mui.adapters.*

fun String.adaptRawContent(): String = this
    .adaptComponentsAndSlots()
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
