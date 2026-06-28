package karakum.mui.adapters

// Fixes `CssVarsTheme` from `createThemeFoundation.d.ts` so the member converter can process its body.
// Patterns that would produce invalid Kotlin if left unchanged:
//   • generateSpacing return type  () => SystemTheme['spacing']  — toFunctionType() bypasses kotlinType(),
//     so the indexed-access type would pass through raw; replace only the function-return occurrence.
//     Standalone members (spacing / breakpoints / direction) are handled by kotlinType() line 642.
//   • Rest parameter  getCssVar: (field: ThemeCssVar, ...vars: ThemeCssVar[]) => string
//   • ThemeCssVar / SupportedColorScheme — TS type aliases not generated as Kotlin types; only the
//     usage positions (`: ThemeCssVar`) are replaced, not the type declaration itself.
//   • Inline record type  generateStyleSheets: () => Array<Record<string, any>>  (handled in FunctionType.kt)
fun String.adaptCreateThemeFoundation(): String =
    // generateSpacing returns an indexed-access type via toFunctionType(); replace only that occurrence.
    replace("=> SystemTheme['spacing']", "=> Any /* SystemTheme['spacing'] */")
        .replace(", ...vars: ThemeCssVar[])", ")")
        // Only replace usage sites (`: ThemeCssVar`), not the type declaration `type ThemeCssVar = …`.
        .replace(": ThemeCssVar", ": String /* ThemeCssVar */")
        .replace("(colorScheme: SupportedColorScheme)", "(colorScheme: String /* SupportedColorScheme */)")
        // SxProps is from @mui/system but not imported in this generated file; collapse to Any.
        .replace("SxProps<CssVarsTheme>", "Any /* SxProps<CssVarsTheme> */")
