package karakum.mui.adapters

// `createThemeNoVars.d.ts` declares the material `ThemeOptions` / `BaseTheme` / `Theme`, but gates their CSS
// variables parts behind conditional types keyed on the augmentable `CssThemeVariables` interface:
//   type CssVarsOptions = CssThemeVariables extends { enabled: true } ? ColorSystemOptions : {};
//   palette: Palette & (CssThemeVariables extends { enabled: true } ? CssVarsPalette : {});
//   type CssVarsProperties = CssThemeVariables extends { enabled: true } ? Pick<CssVarsTheme, …> : Partial<Pick<CssVarsTheme, 'vars'>>;
// Kotlin has no module augmentation, so collapse each to its default (not enabled) branch, which is what
// `createTheme` returns unless the app augments the interface.
//
// The public `ThemeOptions` (`createTheme.d.ts`) additionally mixes in `CssVarsThemeOptions`'s
// `cssVariables` / `colorSchemes` / `defaultColorScheme`. It reuses the same name, so it cannot be generated
// next to this one; its three CSS variables entry points are appended here instead.
fun String.adaptCreateThemeNoVars(): String {
    if ("export interface BaseTheme extends SystemTheme {" !in this)
        return this

    // The augmentation hook itself (and its JSDoc, whose `{ enabled: true }` example breaks member parsing).
    return replace(Regex("""/\*\*(?:(?!\*/).)*?\*/\s*export interface CssThemeVariables \{}\n""", RegexOption.DOT_MATCHES_ALL), "")
        .replace(Regex("""\ntype CssVars(?:Options|Properties) = CssThemeVariables extends \{\s*enabled: true;\s*} \? [^;]+;"""), "")
        .replace(Regex("""\s*// shut off automatic exporting[^\n]*\nexport \{\};"""), "")
        .replace(
            "export interface ThemeOptions extends Omit<SystemThemeOptions, 'zIndex'>, CssVarsOptions {",
            """
            export interface ThemeOptions extends SystemThemeOptions {
              /**
               * Generate CSS variables for the theme (`theme.vars`), optionally with their configuration.
               */
              cssVariables?: boolean | undefined;
              /**
               * The color schemes the theme supports, e.g. `{ light: true, dark: true }`.
               */
              colorSchemes?: Partial<Record<DefaultColorScheme, boolean | ColorSystemOptions>> | undefined;
              /**
               * The color scheme used when none is selected.
               */
              defaultColorScheme?: DefaultColorScheme | undefined;
            """.trimIndent(),
        )
        .replace(
            Regex("""palette: Palette & \(CssThemeVariables extends \{\s*enabled: true;\s*} \? CssVarsPalette : \{}\);"""),
            "palette: Palette;",
        )
        // `Shadows` is a 25-tuple (`['none', string, …]`) — `shadows.d.ts` is not generated; an array is what
        // Kotlin can say. `ZIndexOptions = Partial<ZIndex>`, likewise not generated.
        .replace("shadows?: Shadows | undefined;", "shadows?: string[] | undefined;")
        .replace("shadows: Shadows;", "shadows: string[];")
        .replace("zIndex?: ZIndexOptions | undefined;", "zIndex?: ZIndex | undefined;")
        // `false` only says "not opted in"; reading it as an absent ring keeps the member typed.
        .replace("focusVisible?: FocusVisible | false | undefined;", "focusVisible?: FocusVisible | undefined;")
        // Keep the object form; the `(palette) => options` callback form needs an `unsafeCast`.
        .replace(
            "typography?: TypographyVariantsOptions | ((palette: Palette) => TypographyVariantsOptions) | undefined;",
            "typography?: TypographyVariantsOptions | undefined;",
        )
        .replace(
            "export interface Theme extends BaseTheme, CssVarsProperties {",
            "export interface Theme extends BaseTheme {\n  vars?: ThemeVars | undefined;",
        )
}
