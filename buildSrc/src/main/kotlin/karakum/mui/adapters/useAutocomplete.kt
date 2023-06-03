package karakum.mui.adapters

fun String.adaptUseAutocomplete(): String {
    return replace(
        """export interface UseAutocompleteParameters<
  T,
  Multiple extends boolean | undefined,
  DisableClearable extends boolean | undefined,
  FreeSolo extends boolean | undefined,
> extends UseAutocompleteProps<T, Multiple, DisableClearable, FreeSolo> {}""",
        "",
    )
}
