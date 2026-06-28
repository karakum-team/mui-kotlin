package karakum.mui.adapters

fun String.adaptAutocomplete(): String =
// v7 `renderValue` references two conditional helper types parameterized by `Multiple`/`FreeSolo`
// (`AutocompleteRenderValue<…>`, `AutocompleteRenderValueGetItemProps<Multiple>`). The generated
// `AutocompleteProps` keeps only the `<Value>` type parameter, so those references are unresolved
// and the conditional types themselves aren't emitted. Collapse them to `any` — same treatment
// the generator already applies to `getTagProps` in `renderTags`.
    replace("AutocompleteRenderValue<Value, Multiple, FreeSolo>", "any")
        .replace("AutocompleteRenderValueGetItemProps<Multiple>", "any")
