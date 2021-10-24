package com.github.turansky.mui

private val KNOWN_TYPES = setOf(
    "(value: T) => React.ReactNode",

    "(event: React.SyntheticEvent) => void",
    "(event: React.SyntheticEvent, checked: boolean) => void",
    "(event: React.SyntheticEvent, value: any) => void",

    "(event: React.SyntheticEvent<{}>, reason: CloseReason) => void",
    "(event: React.SyntheticEvent<{}>, reason: OpenReason) => void",

    "(event: React.ChangeEvent<HTMLInputElement>, value: string) => void",
    "(event: React.SyntheticEvent | Event) => void",
    "(event: React.MouseEvent<HTMLElement>, value: any) => void",
    "(value: number) => string",
    "(event: React.SyntheticEvent, value: number | null) => void",
    "(event: React.SyntheticEvent, value: number) => void",
    "(more: number) => React.ReactNode",
    "(event: React.SyntheticEvent, expanded: boolean) => void",
    "(event: React.SyntheticEvent<any>, reason: SnackbarCloseReason) => void",
    "(event: React.ChangeEvent<HTMLInputElement>, checked: boolean) => void",
    "(event: React.MouseEvent<HTMLButtonElement> | null, page: number) => void",
    "(event: React.SyntheticEvent, nodeIds: string[]) => void",
    "(event: React.SyntheticEvent, nodeIds: string) => void",
    "(event: React.SyntheticEvent, nodeId: string) => void",
    "(day: TDate, isFinish: PickerSelectionState) => void",
    "(date: TDate) => void | Promise<void>",
    "(view: CalendarPickerView) => void",
    "(day: TDate) => void",
    "(date: TDate) => void",
    "(day: TDate) => boolean",
    "(hours: string) => string",
    "(minutes: string) => string",
    "(seconds: string) => string",

    "(params: PaginationRenderItemParams) => React.ReactNode",
    "(params: AutocompleteRenderGroupParams) => React.ReactNode",
    "(params: AutocompleteRenderInputParams) => React.ReactNode",

    """
        (
          props: React.HTMLAttributes<HTMLLIElement>,
          option: T,
          state: AutocompleteRenderOptionState,
        ) => React.ReactNode
    """.trimIndent(),

    "(paginationInfo: LabelDisplayedRowsArgs) => React.ReactNode",
)

internal fun String.toFunctionType(): String? {
    if (this !in KNOWN_TYPES)
        return null

    return replace(" => ", "->")
        .replace("React.SyntheticEvent | Event", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<{}>", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<any>", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.ChangeEvent<HTMLInputElement>", "react.dom.events.ChangeEvent<org.w3c.dom.HTMLInputElement>")
        .replace("React.MouseEvent<HTMLButtonElement>", "react.dom.events.MouseEvent<org.w3c.dom.HTMLButtonElement, *>")
        .replace("React.MouseEvent<HTMLElement>", "react.dom.events.MouseEvent<org.w3c.dom.HTMLElement, *>")
        .replace("React.HTMLAttributes<HTMLLIElement>", "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLLIElement>")
        .replace("React.ReactNode", "react.ReactNode")
        .replace(" | null", "?")
        .replace("void | Promise<void>", "kotlin.js.Promise<Nothing?>?")
        .replace("string[]", "ReadonlyArray<String>")
        .replace("string", "String")
        .replace("boolean", "Boolean")
        .replace("number", "Number")
        .replace("void", "Unit")
        .replace(": any", ": $DYNAMIC")
}
