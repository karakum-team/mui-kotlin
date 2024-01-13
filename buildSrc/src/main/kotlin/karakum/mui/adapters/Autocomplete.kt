package karakum.mui.adapters

fun String.adaptAutocomplete(): String {
    return replace(
        """  onKeyDown?: (
    event: React.KeyboardEvent<HTMLDivElement> & { defaultMuiPrevented?: boolean },
  ) => void;
""", ""
    )
}
