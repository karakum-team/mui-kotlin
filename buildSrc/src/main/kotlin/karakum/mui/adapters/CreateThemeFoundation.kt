package karakum.mui.adapters

// STOPGAP (v7). `createThemeFoundation.d.ts` declares:
//   export interface CssVarsTheme extends ColorSystem { colorSchemes; …; getCssVar: (…, ...vars) => …; … }
// The member converter fails to split this particular body (a mix of indexed-access types like
// `SystemTheme['spacing']`, no-arg arrows `() => ThemeVars`, and a rest param `...vars`), so the raw
// TS body leaks into the generated Kotlin and produces a syntax error (unclosed comment / missing brace).
//
// Until the splitter is fixed, empty the interface body — `external interface CssVarsTheme : ColorSystem`
// still compiles and keeps the type and its parent for references elsewhere. The individual members are
// lost (acceptable: upstream kotlin-wrappers is still on 6.5, no v7 reference exists). See MUI_V7_TODO.md.
fun String.adaptCreateThemeFoundation(): String {
    val header = "export interface CssVarsTheme extends ColorSystem {"
    val start = indexOf(header)
    if (start < 0) return this

    // Brace-balanced scan from the opening `{` to its match. The only braces inside the body live in a
    // JSDoc example (`{ foo: { bar } }`) and are themselves balanced, so the scan lands on the real close.
    val open = start + header.length - 1
    var depth = 0
    var end = -1
    var i = open
    while (i < length) {
        when (this[i]) {
            '{' -> depth++
            '}' -> if (--depth == 0) {
                end = i; break
            }
        }
        i++
    }
    if (end < 0) return this

    return substring(0, open) + "{\n}" + substring(end + 1)
}
