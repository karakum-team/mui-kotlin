package karakum.mui.adapters

fun String.adaptUseMenu(): String {
    return replace(
        """
export interface MenuInternalState extends ListState<string> {
}
""",
        "",
    )
}
