package karakum.mui.adapters

// `createTypography.d.ts` builds the theme's typography types on mapped `Record`s Kotlin cannot extend:
//   export interface TypographyVariants extends Record<TypographyVariant, TypographyStyle>, FontStyle, TypographyUtils {}
//   export interface TypographyVariantsOptions extends Partial<Record<TypographyVariant, TypographyStyleOptions> & FontStyleOptions> {}
// Spell the `Record` out as one member per `TypographyVariant` literal (read from the same file, so a new
// variant flows in). `TypographyStyle` / `TypographyStyleOptions` are a type alias of and an empty extension
// of the material `CSSProperties`, which the generator would emit as nothing / a parentless marker. That
// interface only adds `@font-face` and an index signature to csstype's properties, so the members use
// `React.CSSProperties` (`react.CSSProperties`) — the type Kotlin styling code already speaks.
fun String.adaptCreateTypography(): String {
    val variantsHeader =
        "export interface TypographyVariants extends Record<TypographyVariant, TypographyStyle>, FontStyle, TypographyUtils {}"
    if (variantsHeader !in this)
        return this

    val variants = Regex("""export type TypographyVariant = ([^;]+);""")
        .find(this)!!
        .groupValues[1]
        .split("|")
        .map { it.trim().removeSurrounding("'") }

    fun members(type: String, optional: Boolean): String =
        variants.joinToString("") { "  $it${if (optional) "?" else ""}: $type;\n" }

    return replace(
        variantsHeader,
        "export interface TypographyVariants extends FontStyle, TypographyUtils {\n" +
                members("React.CSSProperties", optional = false) + "}",
    )
        .replace(
            "export interface TypographyVariantsOptions extends Partial<Record<TypographyVariant, TypographyStyleOptions> & FontStyleOptions> {}",
            "export interface TypographyVariantsOptions extends FontStyleOptions {\n" +
                    members("React.CSSProperties", optional = true) + "}",
        )
        .replace(Regex("""\nexport type TypographyStyle = CSSProperties;"""), "")
        .replace(Regex("""\nexport interface TypographyStyleOptions extends TypographyStyle \{}"""), "")
}
