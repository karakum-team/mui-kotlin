package karakum.mui.adapters

fun String.adaptAccordion(): String = this
    .replace(
        oldValue = "export interface AccordionOwnerState extends AccordionProps {}",
        newValue = "",
    )
