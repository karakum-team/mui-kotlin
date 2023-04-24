package karakum.mui

internal fun String.toFunctionType(): String? {
    if (!startsWith("("))
        return null

    if (startsWith("(state: {"))
        return null

    return replace(" => ", "->")
        .replace("{\n    matches: boolean;\n}", DYNAMIC)
        .replace("MouseEvent | TouchEvent", "web.uievents.UIEvent")
        .replace("React.MouseEvent | React.KeyboardEvent | React.FocusEvent", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent | Event", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<any> | Event", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<{}>", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<any>", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.ChangeEvent<unknown>", "react.dom.events.ChangeEvent<*>")
        .replace("React.ChangeEvent<HTMLInputElement>", "react.dom.events.ChangeEvent<web.html.HTMLInputElement>")
        .replace("React.MouseEvent<HTMLButtonElement>", "react.dom.events.MouseEvent<web.html.HTMLButtonElement, *>")
        .replace("React.FocusEvent<HTMLButtonElement>", "react.dom.events.FocusEvent<web.html.HTMLButtonElement>")
        .replace(
            "React.KeyboardEvent<HTMLButtonElement>",
            "react.dom.events.KeyboardEvent<web.html.HTMLButtonElement>"
        )
        .replace("React.MouseEvent<HTMLElement>", "react.dom.events.MouseEvent<web.html.HTMLElement, *>")
        .replace("React.HTMLAttributes<HTMLLIElement>", "react.dom.html.HTMLAttributes<web.html.HTMLLIElement>")
        .replace(
            "React.InputHTMLAttributes<HTMLInputElement>['value']",
            "Any /* string | ReadonlyArray<string> | number */"
        )
        .replace(
            "?: React.HTMLAttributes<HTMLInputElement>",
            ": react.dom.html.HTMLAttributes<web.html.HTMLInputElement>?"
        )
        .replace("React.ReactNode", "react.ReactNode")
        .replace("React.RefObject", "react.RefObject")
        .replace(" | null", "?")
        .replace("AutocompleteValue<T, Multiple, DisableClearable, FreeSolo>", DYNAMIC)
        .replace("details?: AutocompleteChangeDetails<T>", "details: AutocompleteChangeDetails<T>?")
        .replace("AutocompleteRenderGetTagProps", "Function<*> /* AutocompleteRenderGetTagProps */")
        .replace(
            "T | AutocompleteFreeSoloValueMapping<FreeSolo>",
            "T /* or AutocompleteFreeSoloValueMapping<FreeSolo> */"
        )
        .replace(
            "AutocompleteOwnerState<T, Multiple, DisableClearable, FreeSolo, ChipComponent>",
            "AutocompleteProps<T> /* AutocompleteOwnerState<T> */"
        )
        .replace(
            ": 'page' | 'first' | 'last' | 'next' | 'previous'",
            ": mui.system.Union /* 'page' | 'first' | 'last' | 'next' | 'previous' */"
        )
        .replace(
            ": 'first' | 'last' | 'next' | 'previous'",
            ": mui.system.Union /* 'first' | 'last' | 'next' | 'previous' */"
        )
        .replace("ClockView", "mui.system.Union /* ClockView */")
        .replace("MuiPickersAdapter<TDate>", "$DYNAMIC /* MuiPickersAdapter<TDate> */")
        .replace("CSSObject", "$DYNAMIC /* CSSObject from `@mui/styled-engine` */")
        .replace("void | Promise<void>", "Promise<Void>?")
        .replace("Breakpoint | number", "Breakpoint")
        .replace("SelectOption<TValue>[]", "ReadonlyArray<SelectOption<TValue>>")
        .replace("TValue[]", "ReadonlyArray<TValue>")
        .replace("T[]", "ReadonlyArray<T>")
        .replace("string[]", "ReadonlyArray<String>")
        .replace("number | string | boolean", DYNAMIC)
        .replace("number | string", DYNAMIC)
        .replace("number | number[]", DYNAMIC)
        .replace("string", "String")
        .replace("boolean", "Boolean")
        .replace("number", "Number")
        .replace("void", "Unit")
        .replace(": any", ": $DYNAMIC")
}
