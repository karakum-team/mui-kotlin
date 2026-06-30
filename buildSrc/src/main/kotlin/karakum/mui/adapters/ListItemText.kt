package karakum.mui.adapters

// v7 parameterizes `ListItemTextProps<PrimaryTypographyComponent extends …, SecondaryTypographyComponent
// extends …>`. The generator doesn't propagate these params (the only members that reference them are
// `TypographyProps<…>`, which collapse to `TypographyProps`), so strip the param block to keep the
// Kotlin API param-free.
// NB: the `ListItemTextSlotsAndSlotProps<…>` alias/extends params are stripped generally by
// `stripSlotsAndSlotPropsTypeParams` in Converter.kt; here we only drop the params on `ListItemTextProps`
// itself (the generator doesn't propagate them).
fun String.adaptListItemText(): String = replace(
    "ListItemTextProps<PrimaryTypographyComponent extends React.ElementType = 'span', SecondaryTypographyComponent extends React.ElementType = 'p'>",
    "ListItemTextProps",
)
