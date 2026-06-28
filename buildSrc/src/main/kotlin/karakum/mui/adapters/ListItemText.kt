package karakum.mui.adapters

// v7 parameterizes `ListItemTextProps<PrimaryTypographyComponent extends …, SecondaryTypographyComponent
// extends …>`. The `extends` bounds inside the type parameters make `findParentType` grab a bound instead
// of the real parent list, dropping `StandardProps`/`HTMLAttributes`/`ListItemTextSlotsAndSlotProps`.
// The generator doesn't propagate these params anyway (the only members that reference them are
// `TypographyProps<…>`, which collapse to `TypographyProps`), so strip the param block. (Targeted fix;
// the general depth-aware `findParentType` is deferred to the mui-x bump — see MUI_V7_TODO.md.)
fun String.adaptListItemText(): String = replace(
    "ListItemTextProps<PrimaryTypographyComponent extends React.ElementType = 'span', SecondaryTypographyComponent extends React.ElementType = 'p'> extends",
    "ListItemTextProps extends",
)
