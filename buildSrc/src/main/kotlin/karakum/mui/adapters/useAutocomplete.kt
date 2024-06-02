package karakum.mui.adapters

// TODO: Remove when default function for `useAutocomplete` will be supported
fun String.adaptUseAutocomplete(): String {
    return replace(
        oldValue = """export interface UseAutocompleteParameters<
  Value,
  Multiple extends boolean | undefined,
  DisableClearable extends boolean | undefined,
  FreeSolo extends boolean | undefined,
> extends UseAutocompleteProps<Value, Multiple, DisableClearable, FreeSolo> {}""",
        newValue = "",
    )
        .replace(
            oldValue = "  getInputProps: () => React.InputHTMLAttributes<HTMLInputElement> & {\n" +
                    "    ref: React.Ref<HTMLInputElement>;\n" +
                    "  };",
            newValue = "  getInputProps: () => React.InputHTMLAttributes<HTMLInputElement>;"
        )
        .replace(
            oldValue = "React.HTMLAttributes<HTMLLIElement> & { key: any }",
            newValue = "React.HTMLAttributes<HTMLLIElement>",
        )
}
