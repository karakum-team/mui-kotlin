package karakum.mui

internal fun String.toFunctionType(): String? {
    // TS generic function `<T extends X = Y>(args) => ResultType<T>` — Kotlin function types
    // don't support generic params, so drop the leading `<...>` declaration. The param name
    // remains in arg/return positions: in arg → Any (lost info), in return generics → `*`
    // (star projection).
    if (startsWith("<")) {
        var depth = 0
        var end = -1
        for (i in indices) {
            when (this[i]) {
                '<' -> depth++
                '>' -> {
                    depth--; if (depth == 0) {
                        end = i; break
                    }
                }
            }
        }
        if (end < 0 || end + 1 >= length) return null
        val genericDecl = substring(1, end)
        val rest = substring(end + 1).trimStart()
        if (!rest.startsWith("(")) return null
        val arrowIdx = rest.indexOf(" => ")
        if (arrowIdx < 0) return null
        val paramNames = genericDecl.split(",")
            .map { it.trim().substringBefore(" ").substringBefore("=").trim() }
            .filter { it.isNotEmpty() }
        val argsPart = rest.substring(0, arrowIdx)
        val returnPart = rest.substring(arrowIdx)
        // Generic param (e.g. `ExternalProps extends Record<string, any> = {}`) typically stands
        // for a React props bag (slot props). Substitute with `react.Props` in both arg and return
        // positions so consumers can pass through their own props objects. The IMPORTED_FQNS
        // post-processing shortens `react.Props` → `Props` and adds the `import` at top of file.
        val argsClean = paramNames.fold(argsPart) { acc, p ->
            acc.replace(Regex("(?<![A-Za-z0-9_])" + Regex.escape(p) + "(?![A-Za-z0-9_])"), "react.Props")
        }
        val returnClean = paramNames.fold(returnPart) { acc, p ->
            acc.replace(Regex("(?<![A-Za-z0-9_])" + Regex.escape(p) + "(?![A-Za-z0-9_])"), "*")
        }
            // Use*SlotProps / Use*InputProps are TS type aliases not generated as Kotlin types —
            // collapse to `react.Props` so the function type stays a props-style return.
            .replace(Regex("""Use\w+Props<\*>"""), "react.Props")
        return "$argsClean$returnClean".toFunctionType()
    }

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
        .replace("React.ChangeEvent<unknown>", "react.dom.events.ChangeEvent<*, *>")
        .replace("React.ChangeEvent<HTMLInputElement>", "react.dom.events.ChangeEvent<web.html.HTMLInputElement, *>")
        .replace("React.MouseEvent<HTMLButtonElement>", "react.dom.events.MouseEvent<web.html.HTMLButtonElement, *>")
        .replace("React.FocusEvent<HTMLButtonElement>", "react.dom.events.FocusEvent<web.html.HTMLButtonElement>")
        .replace(
            "React.ReactElement<any, string | React.JSXElementConstructor<any>>[] | null | undefined",
            "react.ReactElement<*>?",
        )
        .replace(
            "React.KeyboardEvent<HTMLButtonElement>",
            "react.dom.events.KeyboardEvent<web.html.HTMLButtonElement>"
        )
        .replace("React.MouseEvent<HTMLElement>", "react.dom.events.MouseEvent<web.html.HTMLElement, *>")
        .replace("React.HTMLAttributes<HTMLLIElement>", "react.dom.html.HTMLAttributes<web.html.HTMLLIElement>")
        .replace("React.HTMLAttributes<HTMLUListElement>", "react.dom.html.HTMLAttributes<web.html.HTMLUListElement>")
        .replace("React.HTMLAttributes<HTMLDivElement>", "react.dom.html.HTMLAttributes<web.html.HTMLDivElement>")
        .replace(
            "React.InputHTMLAttributes<HTMLInputElement>",
            "react.dom.html.InputHTMLAttributes<web.html.HTMLInputElement>"
        )
        .replace(
            "Omit<React.HTMLAttributes<HTMLLabelElement>, 'color'>",
            "react.dom.html.InputHTMLAttributes<web.html.HTMLLabelElement>"
        )
        .replace("React.HTMLAttributes<HTMLButtonElement>", "react.dom.html.HTMLAttributes<web.html.HTMLButtonElement>")
        .replace(
            "React.InputHTMLAttributes<HTMLInputElement>['value']",
            "Any /* string | ReadonlyArray<string> | number */"
        )
        .replace(
            "?: React.HTMLAttributes<HTMLInputElement>",
            ": react.dom.html.HTMLAttributes<web.html.HTMLInputElement>?"
        )
        .replace(": Event", ": Event")
        .replace("?: any", ": Any?")
        .replace("?: react.Props", ": react.Props?")
        .replace("?: string", ": String?")
        .replace("?: boolean", ": Boolean?")
        .replace("React.ReactNode", "react.ReactNode")
        .replace("React.RefObject", "react.RefObject")
        .replace("HTMLElement | null", "HTMLElement")
        .replace(" | null | undefined", "?")
        .replace(" | null", "?")
        .replace("AutocompleteValue<Value, Multiple, DisableClearable, FreeSolo>", "Any")
        .replace("details?: AutocompleteChangeDetails<Value>", "details: AutocompleteChangeDetails<Value>?")
        .replace("AutocompleteRenderGetTagProps", "Function<*> /* AutocompleteRenderGetTagProps */")
        .replace(
            "Value | AutocompleteFreeSoloValueMapping<FreeSolo>",
            "Value /* or AutocompleteFreeSoloValueMapping<FreeSolo> */"
        )
        .replace(
            "AutocompleteOwnerState<Value, Multiple, DisableClearable, FreeSolo, ChipComponent>",
            "AutocompleteProps<Value> /* AutocompleteOwnerState<Value> */"
        )
        .replace("UseSwitchInputSlotProps", "Any /* UseSwitchInputSlotProps */")
        .replace(
            ": 'page' | 'first' | 'last' | 'next' | 'previous'",
            ": mui.system.Union /* 'page' | 'first' | 'last' | 'next' | 'previous' */"
        )
        .replace(
            ": 'first' | 'last' | 'next' | 'previous'",
            ": mui.system.Union /* 'first' | 'last' | 'next' | 'previous' */"
        )
        .replace(
            ": DateView",
            ": mui.system.Union /* 'year' | 'month' | 'day' */"
        )
        .replace(": ListAction<string>", ": Any /* ListAction<string> */")
        .replace(": CustomAction | ListAction<ItemValue>", ": Any /* CustomAction | ListAction<ItemValue> */")
        .replace(": ListAction<Value> | SelectAction<Value>", ": Any /* ListAction<Value> | SelectAction<Value> */")
        .replace(": ListAction<string | number>", ": Any /* ListAction<string | number> */")
        .replace("ClockView", "mui.system.Union /* ClockView */")
        .replace("UsePaginationItem['type']", "mui.system.Union /* UsePaginationItem['type'] */")
        .replace("MuiPickersAdapter<TDate>", "Any /* MuiPickersAdapter<TDate> */")
        .replace("CSSObject", "Any /* CSSObject from `@mui/styled-engine` */")
        .replace("void | Promise<void>", "Promise<Void>?")
        .replace("Breakpoint | number", "Breakpoint")
        .replace("SelectOption<TValue>[]", "ReadonlyArray<SelectOption<TValue>>")
        .replace("TValue[]", "ReadonlyArray<TValue>")
        .replace("TOption[]", "ReadonlyArray<TOption>")
        .replace("ItemValue[]", "ReadonlyArray<ItemValue>")
        .replace("OptionValue[]", "ReadonlyArray<OptionValue>")
        .replace("Value[]", "ReadonlyArray<Value>")
        .replace("string[]", "ReadonlyArray<String>")
        .replace("number | string | boolean", "Any /* String or Number or Boolean */")
        .replace("number | string", "Any /* number | string */")
        .replace("string | number", "Any /* string | number */ ")
        .replace("number | number[]", "ReadonlyArray<Number>")
        .replace("string | undefined", "String?")
        .replace("SelectOption<Value> | undefined", "SelectOption<Value>?")
        .replace("string", "String")
        .replace("boolean", "Boolean")
        .replace("number", "Number")
        .replace("void", "Unit")
        .replace("object", "Any")
        .replace(": any", ": Any")
}
