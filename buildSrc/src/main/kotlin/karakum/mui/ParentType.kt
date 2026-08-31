package karakum.mui

internal fun findParentType(
    content: String,
): String? {
    // Find " extends " or "\nextends " at angle-bracket / paren depth 0 so that type-parameter
    // bounds like `<T extends Bar>` don't shadow the real inheritance clause.
    // v6 keeps `extends` mid-line; v7 sometimes starts the clause at column 0 after a comment.
    val (extendsMarker, extendsAt) = run {
        var depth = 0
        for (i in content.indices) {
            when (content[i]) {
                '<', '(' -> depth++
                '>', ')' -> depth--
                ' ' -> if (depth == 0 && content.startsWith(" extends ", i))
                    return@run " extends " to i

                '\n' -> if (depth == 0 && content.startsWith("\nextends ", i))
                    return@run "\nextends " to i
            }
        }
        if (content.startsWith("extends ")) return@run "extends " to 0
        return null
    }

    val parentSource = content
        .substring(extendsAt + extendsMarker.length)
        .substringBefore(" {\n")
        .substringBefore(" {}")

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
            result == "ButtonBaseProps" -> "mui.material.ButtonBaseProps"
            result.startsWith("StandardProps<") -> parseStandardProps(result)
            else -> result
        }
    }

    if (parentSource.startsWith("UseAutocompleteProps<")) {
        // v7 emits the parents on a single line (`UseAutocompleteProps<…>, StandardProps<…>,
        // AutocompleteSlotsAndSlotProps<…>`) rather than the old `,\n    ` split. Use a depth-aware
        // split so commas inside the generics are ignored, keep only the two parents we can map
        // (UseAutocompleteProps collapsed to `<Value>`, and StandardProps), and drop the internal
        // AutocompleteSlotsAndSlotProps (its slots/slotProps are emitted via findAdditionalProps).
        val parts = parentSource.depthAwareSplit(',').map { it.trim() }
        val first = "mui.base." + parts.first().substringBefore("<") + "<Value>"
        val standard = parts.drop(1).firstOrNull { it.startsWith("StandardProps<") }
        return sequenceOf(first, standard?.let(::parseStandardProps))
            .filterNotNull()
            .joinToString(",", "\n")
    }

    if (parentSource.startsWith("UsePaginationProps")) {
        // v7 moved Pagination into `@mui/material` and emits the parents on a single line
        // (`UsePaginationProps, StandardProps<...>`) instead of the old `,\n    ` split.
        // Use a depth-aware split so the comma inside `StandardProps<...>` is ignored.
        val (first, second) = parentSource.depthAwareSplit(',').map { it.trim() }
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
            // Filter-and-keep: drop rejected parents (INTERNAL_REJECTED_PARENTS / utility prefixes) but
            // keep the rest. This lets interfaces with mixed lists (e.g. TreeViewSlots +
            // RichTreeViewItemsSlots) keep their valid parent while dropping the internal one.
            val parents = parentSource.depthAwareSplit(',')
                .map { it.trim() }
                .filter { it.isNotEmpty() && it.isAcceptableParent() }
            when (parents.size) {
                0 -> null
                1 -> parents[0]
                else -> parents.joinToString(",\n", "\n")
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
    // Material types: stubbed in Generator.kt::generateMaterialDeclarations and removed from this set
    // (LinkBaseProps, TablePaginationBaseProps) — see MATERIAL_*_STUB.
    "BasePopperProps",
    "DialogActionsProps",
    "PickersArrowSwitcherSlots",
    "PickersArrowSwitcherSlotProps",
    "CssContainerQueries",
    "NormalCssProperties",
    "StyledComponentProps",
    // MUI-X pickers: internal/cross-platform base types not suitable as Kotlin parents.
    "BaseDateTimePickerProps",
    "BaseTimePickerProps",
    "MobileOnlyPickerProps",
    // Responsive picker interfaces (DatePicker / DateTimePicker / TimePicker) extend both Desktop* and
    // Mobile* variants and re-declare slots/slotProps with narrower types. Kotlin can't override with
    // an incompatible type, so reject all Desktop*/Mobile* picker parents; the responsive interface
    // remains self-contained with its own members.
    "DesktopDatePickerProps",
    "MobileDatePickerProps",
    "DesktopDateTimePickerProps",
    "MobileDateTimePickerProps",
    "DesktopTimePickerProps",
    "MobileTimePickerProps",
    // DateTimePickerProps also extends ExportedYearCalendarProps which defines yearsPerRow;
    // DateTimePickerProps re-declares it with the same type but Kotlin still requires override.
    "ExportedYearCalendarProps",
    // MUI-X v9: internal validation / timezone / form / clock base prop interfaces that aren't exported
    // as standalone types (they live in internals), so they can't be Kotlin parents. The concrete props
    // interfaces remain self-contained with their own members.
    "BaseDateValidationProps",
    "YearValidationProps",
    "MonthValidationProps",
    "MonthValidationOptions",
    "DayValidationProps",
    "TimeValidationProps",
    "DateTimeValidationProps",
    "TimezoneProps",
    "FormProps",
    "BaseClockProps",
    "BaseDatePickerProps",
    "BaseDateTimePickerProps2",
    "DesktopOnlyPickerProps",
    "DigitalTimePickerProps",
    "ExportedPickersArrowSwitcherProps",
    "ExportedUseViewsOptions",
    "ExportedBaseClockProps",
    // v9 internal RichTreeViewItems component type — its `<TProps>`/Ref/slot-override shape doesn't
    // translate; kept rejected so RichTreeViewSlots just loses this one parent (keeps TreeViewSlots).
    "RichTreeViewItemsSlots",
    "PickerDayOwnerStateBase",
)

private fun String.isAcceptableParent(): Boolean {
    val prefix = substringBefore("<")
    if (prefix in TS_UTILITY_PREFIXES) return false
    if (prefix in INTERNAL_REJECTED_PARENTS) return false
    return matches(IDENTIFIER_RE)
}

/**
 * Splits on [delim] at bracket depth 0, so a delimiter inside `<…>`, `(…)` or `{…}` does not split.
 * Parts are returned untrimmed and possibly empty — callers differ on what they want done with those.
 */
internal fun String.depthAwareSplit(delim: Char): List<String> {
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
