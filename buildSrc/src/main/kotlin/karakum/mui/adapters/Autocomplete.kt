package karakum.mui.adapters

fun String.adaptAutocomplete(): String =
// v7 `renderValue` references two conditional helper types parameterized by `Multiple`/`FreeSolo`
// (`AutocompleteRenderValue<…>`, `AutocompleteRenderValueGetItemProps<Multiple>`). The generated
// `AutocompleteProps` keeps only the `<Value>` type parameter, so those references are unresolved
// and the conditional types themselves aren't emitted. Collapse them to `any` — same treatment
// the generator already applies to `getTagProps` in `renderTags`.
// (`onKeyDown`'s ` & { defaultMuiPrevented }` intersection is stripped by `dropInlineIntersections`,
// and `React.KeyboardEvent<HTMLDivElement>` is mapped in `FunctionType.kt`.)
// The generated `AutocompleteProps` keeps only `<Value>`. v7 added `extends boolean | undefined`
// bounds to the other type parameters, which breaks parent extraction (`findParentType` would grab a
// bound instead of the parent list, dropping the `UseAutocompleteProps`/`StandardProps` inheritance —
    // and with it `options`, `value`, `onChange`, …). Strip the bounds so the parent list is recovered.
    replace(
        "<Value, Multiple extends boolean | undefined, DisableClearable extends boolean | undefined, FreeSolo extends boolean | undefined, ChipComponent extends React.ElementType = ChipTypeMap['defaultComponent']> extends UseAutocompleteProps",
        "<Value, Multiple, DisableClearable, FreeSolo> extends UseAutocompleteProps",
    )
        .replace("AutocompleteRenderValue<Value, Multiple, FreeSolo>", "any")
        .replace("AutocompleteRenderValueGetItemProps<Multiple>", "any")
