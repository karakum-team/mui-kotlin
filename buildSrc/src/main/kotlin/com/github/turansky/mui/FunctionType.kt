package com.github.turansky.mui

internal fun String.toFunctionType(): String? {
    if (!startsWith("("))
        return null

    if (startsWith("(state: {"))
        return null

    return replace(" => ", "->")
        .replace("{\n    matches: boolean;\n}", DYNAMIC)
        .replace("MouseEvent | TouchEvent", "org.w3c.dom.events.UIEvent")
        .replace("React.SyntheticEvent | Event", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<any> | Event", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<{}>", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent<any>", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.SyntheticEvent", "react.dom.events.SyntheticEvent<*, *>")
        .replace("React.ChangeEvent<unknown>", "react.dom.events.ChangeEvent<*>")
        .replace("React.ChangeEvent<HTMLInputElement>", "react.dom.events.ChangeEvent<org.w3c.dom.HTMLInputElement>")
        .replace("React.MouseEvent<HTMLButtonElement>", "react.dom.events.MouseEvent<org.w3c.dom.HTMLButtonElement, *>")
        .replace("React.MouseEvent<HTMLElement>", "react.dom.events.MouseEvent<org.w3c.dom.HTMLElement, *>")
        .replace("React.HTMLAttributes<HTMLLIElement>", "react.dom.html.HTMLAttributes<org.w3c.dom.HTMLLIElement>")
        .replace("React.ReactNode", "react.ReactNode")
        .replace(" | null", "?")
        .replace("AutocompleteValue<T, Multiple, DisableClearable, FreeSolo>", DYNAMIC)
        .replace("details?: AutocompleteChangeDetails<T>", "details: AutocompleteChangeDetails<T>?")
        .replace("AutocompleteRenderGetTagProps", "Function<*> /* AutocompleteRenderGetTagProps */")
        .replace(": 'page' | 'first' | 'last' | 'next' | 'previous'", ": mui.system.Union /* 'page' | 'first' | 'last' | 'next' | 'previous' */")
        .replace(": 'first' | 'last' | 'next' | 'previous'", ": mui.system.Union /* 'first' | 'last' | 'next' | 'previous' */")
        .replace("ClockView", "mui.system.Union /* ClockView */")
        .replace("MuiPickersAdapter<TDate>", "$DYNAMIC /* MuiPickersAdapter<TDate> */")
        .replace("void | Promise<void>", "kotlin.js.Promise<Nothing?>?")
        .replace("Breakpoint | number", "Breakpoint")
        .replace("T[]", "ReadonlyArray<T>")
        .replace("string[]", "ReadonlyArray<String>")
        .replace("number | string", DYNAMIC)
        .replace("string", "String")
        .replace("boolean", "Boolean")
        .replace("number", "Number")
        .replace("void", "Unit")
        .replace(": any", ": $DYNAMIC")
}
