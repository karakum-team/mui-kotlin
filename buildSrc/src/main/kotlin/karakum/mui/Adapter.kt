package karakum.mui

import karakum.mui.adapters.*
import karakum.mui.adapters.datepickers.adaptLocalizationProvider
import karakum.mui.adapters.treeview.adaptRichTreeView
import karakum.mui.adapters.treeview.adaptTreeItem
import karakum.mui.adapters.treeview.adaptTreeView

fun String.adaptRawContent(): String = this
    // MUI-X v9 `internals/models/validation.d.ts` declares `FutureAndPastValidationProps` WITHOUT `export`
    // (the generator skips non-exported interfaces), yet Base{Date,Time}ValidationProps extend it for
    // `disablePast`/`disableFuture`. Export it so those fields survive in the generated parents.
    .replace("\ninterface FutureAndPastValidationProps {", "\nexport interface FutureAndPastValidationProps {")
    // MUI-X v9 `PickerDayProps extends ExportedPickerDayProps, Omit<ButtonBaseProps, …8 members…>`.
    // PickerDay re-declares those 8 handlers with an extra `day` argument (e.g.
    // `onKeyDown?: (event, day) => void`), so they CANNOT override ButtonBase's `DOMAttributes` handlers
    // (different arity) — meaning PickerDayProps can't extend ButtonBaseProps in Kotlin's invariant model.
    // The `Omit` exists precisely because of those incompatible refinements; drop the whole parent and let
    // PickerDay keep its own handlers as plain members. (It loses the non-refined ButtonBase props.)
    .replace(
        ", Omit<ButtonBaseProps, 'classes' | 'onFocus' | 'onBlur' | 'onKeyDown' | 'onMouseDown' | 'onClick' | 'onMouseEnter' | 'LinkComponent'>",
        "",
    )
    // MUI v6 occasionally puts `; // line comment` on the same line as a property's terminating
    // semicolon (e.g. Typography.color). The generator splits members by `;\n` and gets confused
    // when the `;` is not immediately followed by `\n`. Strip the line comment first.
    .replace(Regex(""";\s*//[^\n]*\n"""), ";\n")
    // Must precede `dropInlineIntersections`: it rewrites the `type InputOwnProps = (…) & Omit<…> & { … }`
    // alias into `interface InputOwnProps extends … { … }`. If the generic intersection drop ran first it
    // would strip the trailing ` & { … }` the rewrite keys on, leaving InputOwnProps unconvertible.
    .adaptInput()
    .adaptListItemText()
    .adaptBivarianceHack()
    // Must precede `dropInlineIntersections`: rewrites the `type TouchRippleProps = StandardProps<…> & { … }`
    // alias into a proper interface so its members survive (the intersection drop would otherwise erase them).
    .adaptTouchRipple()
    // v7 intersects several (often deprecated) props with an inline object literal, e.g.
    //   imgProps?: (React.ImgHTMLAttributes<…> & {\n    sx?: SxProps<Theme> | undefined;\n  }) | undefined;
    // Kotlin has no structural intersection with an anonymous object, and the inner `;\n` also
    // breaks member splitting (`convertMembers` splits on `;\n`). Drop the ` & { … }` part entirely
    // — the base type (callback signature, props bag, or type alias) then converts cleanly.
    .dropInlineIntersections()
    // v7 also gives some props an inline object-literal *value* type, e.g.
    //   slotProps?: {\n  /** … */\n  collapsedIcon?: …;\n} | undefined;   (Breadcrumbs, StepLabel, …)
    // Those inner `;\n` + doc-comments break member/comment splitting. Replace the object value
    // with `any` so the member converts to `Any?`.
    .dropMemberValueObjects()
    .adaptClasses()
    .adaptOption()
    .adaptSelect()
    .adaptFormControl()
    .adaptModal()
    .adaptAlert()
    .adaptAccordion()
    .adaptAutocomplete()
    .adaptUseAutocomplete()
    .adaptSlider()
    .adaptBreadcrumbs()
    .adaptUseMenu()
    .adaptUseSlider()
    .adaptInitColorSchemeScript()
    .adaptCreateThemeFoundation()
    .adaptRichTreeView()
    .adaptTreeItem()
    .adaptTreeView()
    .adaptLocalizationProvider()

// Drop every ` & { … }` inline object literal (balanced braces) from type positions. Targets
// intersections only (interface/extends bodies use ` {`, not ` & {`), so the base type on the
// left of the intersection is preserved and converts normally.
private fun String.dropInlineIntersections(): String {
    val marker = " & {"
    if (marker !in this) return this

    // `*TypeMap` declarations carry the component's props inline as `props: AdditionalProps & { … }`
    // or `props: AdditionalProps & XxxBaseProps & { … }` (v7 @mui/system Container/Stack/Grid). That
    // inline object IS the props body `findMapProps` extracts (and the `& XxxBaseProps` is the parent
    // it keys on) — dropping it would leave the props interface empty. Preserve any ` & {` whose line
    // is a TypeMap `props:` line (the scan still continues inside, so member-level intersections within
    // the body are dropped normally).
    fun isProtected(at: Int): Boolean {
        val lineStart = lastIndexOf('\n', at) + 1
        val linePrefix = substring(lineStart, at)
        return "props: AdditionalProps" in linePrefix || "props: P" in linePrefix
    }

    val sb = StringBuilder()
    var pos = 0
    var idx = indexOf(marker, pos)
    while (idx >= 0) {
        if (isProtected(idx)) {
            sb.append(this, pos, idx + marker.length)
            pos = idx + marker.length
            idx = indexOf(marker, pos)
            continue
        }

        sb.append(this, pos, idx)

        // Walk from the opening brace to its matching close brace and skip that whole span.
        var depth = 0
        var end = -1
        var i = idx + marker.length - 1 // index of '{'
        while (i < length) {
            when (this[i]) {
                '{' -> depth++
                '}' -> if (--depth == 0) {
                    end = i; break
                }
            }
            i++
        }

        if (end < 0) {
            // Unbalanced — leave the rest untouched.
            sb.append(this, idx, length)
            return sb.toString()
        }

        pos = end + 1
        idx = indexOf(marker, pos)
    }
    sb.append(this, pos, length)
    return sb.toString()
}

// Member names whose inline-object value the generator converts into a dedicated nested interface
// (KotlinType.kt: `Classes`/`Components`/`ComponentsProps`/`Slots`/`SlotProps`). They must NOT be
// collapsed to `any` here — leave their `{ … }` intact so KotlinType can build the named interface.
private val STRUCTURED_MEMBER_NAMES = setOf(
    "classes", "components", "componentsProps", "slots", "slotProps",
)

// Replace member-value inline object literals (`name?: { … }`) with `any`. Comment-aware: `/* … */`
// regions are passed through untouched and skipped while brace-matching, so `: {` and `{}` inside
// JSDoc (e.g. `@default {}`, `{ foo: { bar } }`) don't trigger false matches or unbalance the scan.
// Interface/extends bodies use ` {` (not `: {`) and are unaffected. Members in STRUCTURED_MEMBER_NAMES
// are skipped so their inline objects survive into KotlinType's nested-interface handlers.
private fun String.dropMemberValueObjects(): String {
    if (": {" !in this) return this

    // The identifier (minus a trailing `?`) ending right before the `:` at index `colon`.
    fun memberNameBefore(colon: Int): String {
        var j = colon - 1
        if (j >= 0 && this[j] == '?') j--
        val end = j + 1
        while (j >= 0 && (this[j].isLetterOrDigit() || this[j] == '_')) j--
        return substring(j + 1, end)
    }

    val sb = StringBuilder()
    val n = length
    var i = 0
    while (i < n) {
        if (this[i] == '/' && i + 1 < n && this[i + 1] == '*') {
            val close = indexOf("*/", i + 2)
            val end = if (close < 0) n else close + 2
            sb.append(this, i, end)
            i = end
            continue
        }

        // A mapped/index-signature object (`{ [key in X]: Y }`, `{ [k: string]: V }`) is semantically
        // a Record, not an arbitrary props bag — keep it so kotlinType's STANDARD_TYPE_MAP / Props
        // handlers convert it (e.g. createBreakpoints `values` → `Record<Breakpoint, Number>`).
        fun isMappedType(brace: Int): Boolean {
            var k = brace + 1
            while (k < n && this[k].isWhitespace()) k++
            return k < n && this[k] == '['
        }

        if (this[i] == ':' && i + 2 < n && this[i + 1] == ' ' && this[i + 2] == '{' &&
            memberNameBefore(i) !in STRUCTURED_MEMBER_NAMES &&
            !isMappedType(i + 2)
        ) {
            var depth = 0
            var j = i + 2
            var closeBrace = -1
            while (j < n) {
                if (this[j] == '/' && j + 1 < n && this[j + 1] == '*') {
                    val c = indexOf("*/", j + 2)
                    j = if (c < 0) n else c + 2
                    continue
                }
                when (this[j]) {
                    '{' -> depth++
                    '}' -> if (--depth == 0) {
                        closeBrace = j; break
                    }
                }
                j++
            }
            if (closeBrace >= 0) {
                sb.append(": any")
                i = closeBrace + 1
                continue
            }
        }

        sb.append(this[i])
        i++
    }
    return sb.toString()
}
