package karakum.mui

internal fun findParentType(
    content: String,
): String? {
    if (" extends " !in content)
        return null

    val parentSource = content
        .substringAfter(" extends ")
        .substringBefore(" {\n")
        .substringBefore(" {}")
        .substringAfter("\n> extends ")

    if ("<TDate>" in parentSource)
        return null

    if ("<TInputDate, TDate>" in parentSource)
        return null

    if (parentSource.startsWith("StandardProps<"))
        return parseStandardProps(parentSource)

    if (parentSource.startsWith("Omit<")) {
        // For v6 `Omit<StandardProps<...>[, '...']>` we need to strip the outer comma at depth 0,
        // not at the first encountered comma (which may be inside a nested generic).
        val innerStart = "Omit<".length
        var depth = 0
        var topComma = -1
        for (i in innerStart until parentSource.length) {
            when (parentSource[i]) {
                '<' -> depth++
                '>' -> depth--
                ',' -> if (depth == 0) {
                    topComma = i; break
                }
            }
        }
        val rawInner = if (topComma >= 0) {
            parentSource.substring(innerStart, topComma).trim()
        } else {
            parentSource.removeSurrounding("Omit<", ">").trim()
        }
        val result = rawInner.toTypeParameter()

        return when {
            result == "SystemThemeOptions" -> "mui.system.ThemeOptions"
            result == "ExtendMui<ButtonBaseProps>" -> "mui.material.ButtonBaseProps"
            result.startsWith("StandardProps<") -> parseStandardProps(result)
            else -> result
        }
    }

    if (parentSource.startsWith("UseAutocompleteProps<")) {
        val (first, second) = parentSource.split(",\n    ")
        return sequenceOf(
            "mui.base." + first.replace(", Multiple, DisableClearable, FreeSolo", ""),
            parseStandardProps(second),
        ).joinToString(",", "\n")
    }

    if (parentSource.startsWith("UsePaginationProps")) {
        val (first, second) = parentSource.split(",\n    ")
        return sequenceOf(
            first,
            parseStandardProps(second),
        ).joinToString(",", "\n")
    }

    if (parentSource.startsWith("TypographyProps<")) {
        val (first, _) = parentSource.split("<")
        return first
    }

    return when (parentSource) {
        "ButtonBaseProps",
        "ListItemBaseProps",
        "ListItemButtonBaseProps",
        "BaseTextFieldProps",
        "ListProps",
        "RichTreeViewPropsBase",
        "UseInputProps",
        "UseSwitchProps",
        "UsePaginationItem",

        "TypographyProps",
        "TabsProps",

        "BaseTheme",

        "OptionOwnProps<OptionValue>",
        "SelectOwnProps<OptionValue>",
            -> parentSource

        "SystemTheme",
            -> "mui.system.Theme"

        "ButtonOwnProps",
        "PopperProps",
            -> "mui.base.$parentSource"

        "HTMLDivProps",
            -> "react.dom.html.HTMLAttributes<web.html.HTMLDivElement>"

        "TransitionProps",
        "React.HTMLAttributes<HTMLElement>",
        "React.HTMLAttributes<HTMLDivElement>",
        "React.HTMLAttributes<HTMLUListElement>",
        "React.HTMLAttributes<HTMLSpanElement>",
        "React.HTMLAttributes<HTMLInputElement | HTMLTextAreaElement>",
            -> parentSource.toTypeParameter()

        else -> {
            // Fallback: accept simple identifiers (possibly with single-level generic).
            // Also handles multi-parent extends lists (e.g. `DrawerProps, SwipeableDrawerSlotsAndSlotProps`).
            // All-or-nothing: if any parent looks suspicious, return null and let the caller
            // default to react.Props — better to lose some inheritance than emit unresolved refs.
            val parents = parentSource.depthAwareSplit(',')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            when {
                parents.isEmpty() -> null
                parents.all { it.isAcceptableParent() } -> when (parents.size) {
                    1 -> parents[0]
                    else -> parents.joinToString(",\n", "\n")
                }

                else -> null
            }
        }
    }
}

private val IDENTIFIER_RE = Regex("""[A-Za-z_][\w]*(?:<[A-Za-z_][\w]*(?:\s*,\s*[A-Za-z_][\w]*)*>)?""")
private val TS_UTILITY_PREFIXES = setOf(
    "Partial", "Pick", "Omit", "Record", "Required", "Readonly",
    "DistributiveOmit", "DistributivePick", "Exclude", "Extract",
)

// Internal MUI/utility types referenced by `extends` but not exported / not Kotlin-side.
// Dropping them from the parent list loses some inheritance, but the alternative is unresolved
// references at compile time. See MUI_V6_TODO.md for follow-up.
private val INTERNAL_REJECTED_PARENTS = setOf(
    "LinkBaseProps",
    "BasePopperProps",
    "TablePaginationBaseProps",
    "DialogActionsProps",
    "PickersArrowSwitcherSlots",
    "CssContainerQueries",
    "NormalCssProperties",
    "StyledComponentProps",
    "RichTreeViewPluginSlots",
    "RichTreeViewPluginSlotProps",
    "SimpleTreeViewPluginSlots",
    "SimpleTreeViewPluginSlotProps",
)

private fun String.isAcceptableParent(): Boolean {
    val prefix = substringBefore("<")
    if (prefix in TS_UTILITY_PREFIXES) return false
    if (prefix in INTERNAL_REJECTED_PARENTS) return false
    return matches(IDENTIFIER_RE)
}

private fun String.depthAwareSplit(delim: Char): List<String> {
    val parts = mutableListOf<String>()
    var depth = 0
    var start = 0
    for (i in indices) {
        when (this[i]) {
            '<', '{', '(' -> depth++
            '>', '}', ')' -> depth--
            delim -> if (depth == 0) {
                parts += substring(start, i)
                start = i + 1
            }
        }
    }
    if (start <= length) parts += substring(start)
    return parts
}

private fun parseStandardProps(
    source: String,
): String {
    // Source may be `StandardProps<X>` or `StandardProps<X, 'keys'>` or
    // `StandardProps<X>, AdditionalParent[, ...]` (MUI v6 commonly adds SlotsAndSlotProps as
    // an extra parent after StandardProps). Walk depth-aware: extract first generic arg as the
    // inner Kotlin parent, then collect remaining top-level parents after the closing `>`.
    val ltOpen = source.indexOf('<')
    if (ltOpen < 0) return "\nmui.system.StandardProps"

    var depth = 1
    var i = ltOpen + 1
    while (i < source.length && depth > 0) {
        when (source[i]) {
            '<' -> depth++
            '>' -> depth--
        }
        if (depth == 0) break
        i++
    }
    if (depth != 0) return "\nmui.system.StandardProps"

    val inner = source.substring(ltOpen + 1, i)
    val rest = source.substring(i + 1).trimStart(',', ' ', '\n').trimEnd()

    val firstArg = run {
        var bracketDepth = 0
        var commaIdx = -1
        for (j in inner.indices) {
            when (inner[j]) {
                '<', '{', '(' -> bracketDepth++
                '>', '}', ')' -> bracketDepth--
                ',' -> if (bracketDepth == 0) {
                    commaIdx = j; break
                }
            }
        }
        (if (commaIdx >= 0) inner.substring(0, commaIdx) else inner).trim()
    }

    val parents = mutableListOf("mui.system.StandardProps", firstArg.toTypeParameter())
    if (rest.isNotEmpty()) {
        var dd = 0
        var start = 0
        for (j in rest.indices) {
            when (rest[j]) {
                '<', '{', '(' -> dd++
                '>', '}', ')' -> dd--
                ',' -> if (dd == 0) {
                    val token = rest.substring(start, j).trim()
                    if (token.isNotEmpty()) parents += token
                    start = j + 1
                }
            }
        }
        if (start < rest.length) {
            val tail = rest.substring(start).trim()
            if (tail.isNotEmpty()) parents += tail
        }
    }

    return parents.joinToString(",\n", "\n")
}
