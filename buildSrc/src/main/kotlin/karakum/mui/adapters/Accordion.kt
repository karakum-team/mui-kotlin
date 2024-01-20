package karakum.mui.adapters

fun String.adaptAccordion(): String =
    replace(
        oldValue = "export interface AccordionOwnerState extends AccordionProps {}",
        newValue = "",
    )
