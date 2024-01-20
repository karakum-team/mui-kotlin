package karakum.mui

import karakum.mui.adapters.*

fun String.adaptRawContent(): String =
    adaptComponentsAndSlots()
        .adaptInput()
        .adaptOption()
        .adaptSelect()
        .adaptFormControl()
        .adaptModal()
        .adaptAccordion()
        .adaptAutocomplete()
        .adaptUseAutocomplete()
        .adaptBreadcrumbs()
        .adaptUseMenu()
        .adaptUseSlider()
