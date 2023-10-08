package karakum.mui.adapters

// TODO: Remove when default function for `useAutocomplete` will be supported
fun String.adaptUseAutocomplete(): String {
    return replace(
        """export interface UseAutocompleteParameters<
  Value,
  Multiple extends boolean | undefined,
  DisableClearable extends boolean | undefined,
  FreeSolo extends boolean | undefined,
> extends UseAutocompleteProps<Value, Multiple, DisableClearable, FreeSolo> {}""",
        "",
    ).replace(
        "  getInputProps: () => React.InputHTMLAttributes<HTMLInputElement> & {\n" +
                "    ref: React.Ref<HTMLInputElement>;\n" +
                "  };",
        "  getInputProps: () => React.InputHTMLAttributes<HTMLInputElement>;"
    )
}
