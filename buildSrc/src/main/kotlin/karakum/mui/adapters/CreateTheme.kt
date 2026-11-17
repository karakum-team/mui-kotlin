package karakum.mui.adapters

// Adapts `@mui/system/createTheme/createTheme.d.ts`:
// In TS, `@mui/system` declares empty marker interfaces for module augmentation (Typography, Mixins,
// Shadows, Transitions, ZIndex), but types `palette` as `Record<string, any>` / `Record<string, any> & { mode: 'light' | 'dark' }`.
// In Kotlin, using `ReadonlyRecord` for `Palette` blocks `@JsPlainObject` (JSO) usage because `@JsPlainObject`
// interfaces can only inherit from other `@JsPlainObject` interfaces and cannot have methods.
// We declare `@JsPlainObject` marker interfaces `Palette` and `PaletteOptions` in `mui.system`,
// mirroring `Typography`, `Mixins`, `Transitions`, and `ZIndex`.
fun String.adaptCreateTheme(): String {
    if ("export interface ThemeOptions {" !in this || "export interface Theme extends" !in this)
        return this

    return replace(
        "export interface ThemeOptions",
        "export interface Palette {}\nexport interface PaletteOptions {}\nexport interface ThemeOptions",
    )
        .replace(
            Regex("""palette\?: Record<string, any>( \| undefined)?;"""),
            "palette?: PaletteOptions$1;",
        )
        .replace(
            Regex("""palette: Record<string, any> & \{\s*mode: 'light' \| 'dark';?\s*};"""),
            "palette: Palette;",
        )
}
