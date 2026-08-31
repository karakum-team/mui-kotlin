package karakum.mui

// Replace every balanced inline object literal `{ … }` with `Any`. v7 inlines config/return
// objects directly into callback signatures (e.g. `(state: { … }) => ReactNode`,
// `(query) => { matches: boolean }`, `options?: Partial<{ … }>`); Kotlin function types can't
// express anonymous object types, so collapse them to `Any`.
private fun String.collapseInlineObjects(): String {
    if ('{' !in this) return this

    val sb = StringBuilder()
    var i = 0
    while (i < length) {
        if (this[i] == '{') {
            var depth = 0
            var j = i
            while (j < length) {
                when (this[j]) {
                    '{' -> depth++
                    '}' -> if (--depth == 0) break
                }
                j++
            }
            if (j < length) {
                sb.append("Any")
                i = j + 1
                continue
            }
            sb.append(this, i, length)
            break
        }
        sb.append(this[i])
        i++
    }
    return sb.toString()
}

internal fun String.toFunctionType(
    // See [kotlinType]. Applied before the replacement chain below, which is a curated list of MUI
    // shapes and would otherwise let a name the target generates through untouched — bare, and so
    // without the `?` the map exists to carry.
    knownTypes: Map<String, String> = emptyMap(),
): String? {
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
        return "$argsClean$returnClean".toFunctionType(knownTypes)
    }

    if (!startsWith("("))
        return null

    // A real function type must have an arrow. Parenthesized non-functions (e.g. `(A & B)`,
    // `(A | B)`) also start with `(` — let them fall through to the `Any? /* … */` fallback
    // instead of being mangled by the string replacements below.
    if (" => " !in this)
        return null

    if (startsWith("(state: {"))
        return null

    val converted = knownTypes.entries
        .fold(replace(" => ", "->")) { acc, (name, kotlinType) ->
            // Not preceded by `.`, so a qualified `Foo.TransitionStatus` is left alone.
            Regex("(?<![A-Za-z0-9_.])" + Regex.escape(name) + "(?![A-Za-z0-9_])")
                .replace(acc) { kotlinType }
        }
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
        .replace(
            "React.KeyboardEvent<HTMLDivElement>",
            "react.dom.events.KeyboardEvent<web.html.HTMLDivElement>"
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
            "AutocompleteOwnerState<Value>"
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
        // `string | string[]` (e.g. createTransitions `create`'s `props` arg) — a single value or a
        // list. Collapse to `ReadonlyArray<String>` (as v6 did) BEFORE the generic `string[]` rule,
        // otherwise it becomes `String | ReadonlyArray<String>` and gets widened to `Any` at line ~208.
        .replace("string | string[]", "ReadonlyArray<String>")
        .replace("string[]", "ReadonlyArray<String>")
        .replace("number | string | boolean", "Any /* String or Number or Boolean */")
        .replace("number | string", "Any /* number | string */")
        .replace("string | number", "Any /* string | number */ ")
        .replace("number | number[]", "ReadonlyArray<Number>")
        .replace("string | undefined", "String?")
        .replace("SelectOption<Value> | undefined", "SelectOption<Value>?")
        // MUI-X v9 `PickerValidDate` / `PickerValue` are kept as NAMED types (PICKERS_STUBS); generic
        // params (`TSectionValue` / `TView`) are preserved on their declaring interfaces — no widening here.
        .replace("string", "String")
        .replace("Record<String, any>", "Record<String, *>")
        .replace("boolean", "Boolean")
        .replace("number", "Number")
        .replace("void", "Unit")
        .replace("object", "Any")
        .replace(": any", ": Any")
        // Collapse any inline object literals left after the specific `<{}>` / `{ matches }`
        // replaces above (e.g. `(state: { … })`, `options?: Partial<{ … }>`), then tidy the
        // resulting `Partial<Any>` / optional-param / union-param forms into valid Kotlin.
        .collapseInlineObjects()
        .replace("Partial<Any>", "Any")
        .replace(": String | ReadonlyArray<String>", ": Any /* String | ReadonlyArray<String> */")
        .replace("?: Any", ": Any?")

    if (converted.isKotlinFunctionType())
        return converted

    // The whole callback is widened, not just the offending parameter — coarse, but it keeps the
    // TypeScript readable at the call site. Logged because it is the one widening on this path that
    // nothing else reports: every other Base UI loss prints from `BaseUi.kt` or `generate`.
    println("Function type left untranslated, widened: $this")

    return "Any? /* $this */"
}

/**
 * Whether the conversion above actually landed on Kotlin, rather than leaving TypeScript in place.
 *
 * The replacements are a curated list built against the shapes the MUI packages use; a shape not on it
 * passes through untouched, and the result is emitted as if it were Kotlin. Three such shapes reach
 * here from Base UI, and each is a parse error — the generated file stops being Kotlin at all, which is
 * how they were found: ktfmt, which formatted the tree at the time, refused to parse the file. Do not
 * read that as a standing safety net. IntelliJ IDEA's formatter replaced ktfmt and does not fail on a
 * region it cannot parse — it leaves it alone and exits 0 — so a generated non-Kotlin construct now
 * surfaces one task later, at `compileKotlinJs`, with a less pointed message. The check below is what
 * keeps it from getting that far:
 *
 *     value: Value extends Number ? Number : Value        // conditional type (Base UI `slider`)
 *     ->ReadonlyArray<String>? | Promise<…>               // union in return position (Base UI `field`)
 *     value: unknown                                      // TS-only keyword
 *
 * The caller cannot be relied on to recover: `kotlinType` does fall through to an `Any?`-with-marker
 * catch-all, but only past a dozen further handlers, one of which returns its argument verbatim for any
 * type ending in `Props` / `Actions` / `Size` / … — so a rejected function type could come back out as
 * raw TypeScript. The marker is therefore produced here.
 *
 * Marker spans are excluded before the check: the replacements above deliberately record the TypeScript
 * they replaced, and that text is not code. (No inline example — Kotlin nests block comments.)
 */
private fun String.isKotlinFunctionType(): Boolean {
    val code = replace(COMMENT_SPAN, "")

    return '|' !in code &&
            !code.contains(" extends ") &&
            !NON_KOTLIN_KEYWORD.containsMatchIn(code)
}

private val COMMENT_SPAN = Regex("""/\*.*?\*/""", RegexOption.DOT_MATCHES_ALL)

// Both keywords do occur in the packages' `.d.ts`, but in the conditional-type machinery of type
// aliases rather than in member types, so neither has reached a converted function type so far. Listed
// together so the check stays closed over the TS-only type keywords rather than over the one that
// happens to occur today.
private val NON_KOTLIN_KEYWORD = Regex("""\b(?:unknown|never)\b""")
