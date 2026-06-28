package karakum.mui.adapters

fun String.adaptAccordion(): String = this
    .replace(
        oldValue = "export interface AccordionOwnerState extends AccordionProps {}",
        newValue = "",
    )
    // v7: `AccordionOwnProps` redefines `classes` (AccordionClasses), and `AccordionProps` also pulls in
    // `PaperProps` (with PaperClasses) through `ExtendPaperTypeMap`. As standalone parents these two
    // `classes` declarations clash irreconcilably ("property types do not match"). Make AccordionOwnProps
    // extend PaperProps — exactly the AppBar shape — so AccordionClasses *hides* the inherited PaperClasses
    // in a single chain, which the `VIRTUAL_MEMBER_HIDDEN` suppress (Accordion ∈ OVERRIDE_FIX_REQUIRED) covers.
    .replace(
        oldValue = "export interface AccordionOwnProps {",
        newValue = "export interface AccordionOwnProps extends PaperProps {",
    )
