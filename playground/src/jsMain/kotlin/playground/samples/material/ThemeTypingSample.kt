package playground.samples.material

import js.objects.unsafeJso
import mui.material.PaletteMode
import mui.material.styles.FontStyleOptions
import mui.material.styles.Palette
import mui.material.styles.PaletteOptions
import mui.material.styles.ReducedMotionMode
import mui.material.styles.ShapeOptions
import mui.material.styles.SimplePaletteColorOptions
import mui.material.styles.ThemeOptions
import mui.material.styles.TypographyVariantsOptions
import mui.material.styles.ZIndex
import mui.material.styles.createTheme
import mui.material.styles.createTypography
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.CSSProperties
import react.FC
import react.Props
import web.cssom.px
import web.cssom.rem

private val defaultTheme = createTheme()

private val customTheme = createTheme(
    ThemeOptions(
        palette = unsafeJso<PaletteOptions> {
            mode = PaletteMode.dark
            primary = unsafeJso<SimplePaletteColorOptions> {
                main = "#ff0000"
            }
        },
        typography = TypographyVariantsOptions(
            h1 = unsafeJso<CSSProperties> {
                fontSize = 3.rem
            },
        ),
        shape = unsafeJso<ShapeOptions> {
            borderRadius = 8.px
        },
        zIndex = unsafeJso<ZIndex> {
            appBar = 2000
        },
    ),
)

// `fontSize` scales `pxToRem`: 28 is twice the 14px default.
private val largeTypography = createTypography(
    defaultTheme.palette,
    TypographyVariantsOptions(fontSize = 28.0),
)

private var callbackPalette: Palette? = null
private val callbackTypography = createTypography(defaultTheme.palette) { palette ->
    callbackPalette = palette
    TypographyVariantsOptions(
        fontSize = 28.0,
        h1 = unsafeJso<CSSProperties> {
            fontSize = 4.rem
        },
    )
}

private val fractionalTypography = createTypography(
    defaultTheme.palette,
    TypographyVariantsOptions(fontSize = 17.5, htmlFontSize = 12.5),
)

private val fontStyleOptions = FontStyleOptions(fontSize = 20.0)
private val emptyTypographyOptions = TypographyVariantsOptions()

private val cssVariablesTheme = createTheme(ThemeOptions(cssVariables = true))

val ThemeTypingSample: Sample = Sample(
    id = "theme-typing",
    library = Libraries.Material,
    title = "Theme typing",
    kind = Kind.Check,
    covers = listOf(
        "Theme.palette: Palette (mode, primary.main)",
        "Theme.typography: TypographyVariants (fontFamily, h1, pxToRem)",
        "FontStyle / FontStyleOptions fontSize / htmlFontSize: Double",
        "Theme.shadows / shape / mixins.toolbar / zIndex / transitions / motion",
        "Theme.alpha / lighten / darken",
        "Theme.vars: ThemeVars",
        "ThemeOptions.palette / typography / shape / zIndex / cssVariables",
        "createTypography(palette, options)",
        "createTypography(palette, callback)",
        "ThemeOptions / FontStyleOptions / TypographyVariantsOptions @JsPlainObject factories",
    ),
    fileName = "ThemeTypingSample.kt",
    notes = "Only running this proves that the members converted from `createThemeNoVars.d.ts` read the " +
            "runtime theme under the names and shapes MUI builds: palette and typography values, the 25 shadows, " +
            "the color helpers, the CSS variables `vars`, that `@JsPlainObject` options reach `createTheme`, " +
            "and that both `createTypography` overloads preserve partial options and invoke the callback with the palette.",
    content = FC<Props> {
        val theme = defaultTheme
        val fontSize: Double = fractionalTypography.fontSize
        val htmlFontSize: Double = fractionalTypography.htmlFontSize

        CheckList {
            id = "theme-typing-checks"
            checks = listOf(
                Check("palette.mode is light", theme.palette.mode == PaletteMode.light),
                Check("palette.primary.main is #1976d2", theme.palette.primary.main == "#1976d2"),
                Check("typography.fontFamily mentions Roboto", "${theme.typography.fontFamily}".contains("Roboto")),
                Check("typography.h1.fontSize is 6rem", "${theme.typography.h1.fontSize}" == "6rem"),
                Check("typography.pxToRem(16) is 1rem", theme.typography.pxToRem(16) == "1rem"),
                Check("shadows has 25 entries, [0] is none", theme.shadows.size == 25 && theme.shadows[0] == "none"),
                Check("shape.borderRadius is 4", "${theme.shape.borderRadius}" == "4"),
                Check("mixins.toolbar.minHeight is 56", "${theme.mixins.toolbar.minHeight}" == "56"),
                Check("zIndex.appBar is 1100", theme.zIndex.appBar == 1100),
                Check("transitions.duration.standard is 300", theme.transitions.duration.standard == 300),
                Check("motion.reducedMotion is never", theme.motion.reducedMotion == ReducedMotionMode.never),
                Check("alpha(#000000, 0.5) is rgba(0, 0, 0, 0.5)", theme.alpha("#000000", 0.5) == "rgba(0, 0, 0, 0.5)"),
                Check("lighten(#000000, 0.5) is rgb(127, 127, 127)", theme.lighten("#000000", 0.5) == "rgb(127, 127, 127)"),
                Check("darken(#ffffff, 0.5) is rgb(127, 127, 127)", theme.darken("#ffffff", 0.5) == "rgb(127, 127, 127)"),
                Check("vars is absent without cssVariables", theme.vars == undefined),
                Check(
                    "cssVariables = true: vars.shape.borderRadius is a CSS variable",
                    "${cssVariablesTheme.vars?.shape?.borderRadius}".startsWith("var(--mui-shape-borderRadius"),
                ),
                Check("ThemeOptions.palette: mode is dark", customTheme.palette.mode == PaletteMode.dark),
                Check("ThemeOptions.palette: primary.main is #ff0000", customTheme.palette.primary.main == "#ff0000"),
                Check("ThemeOptions.typography: h1.fontSize is 3rem", "${customTheme.typography.h1.fontSize}" == "3rem"),
                Check("ThemeOptions.shape: borderRadius is 8px", "${customTheme.shape.borderRadius}" == "8px"),
                Check("ThemeOptions.zIndex: appBar is 2000", customTheme.zIndex.appBar == 2000),
                Check("createTypography(fontSize = 28).pxToRem(16) is 2rem", largeTypography.pxToRem(16) == "2rem"),
                Check("createTypography callback receives the palette", callbackPalette === theme.palette),
                Check("createTypography callback: pxToRem(16) is 2rem", callbackTypography.pxToRem(16) == "2rem"),
                Check("createTypography callback: h1.fontSize is 4rem", "${callbackTypography.h1.fontSize}" == "4rem"),
                Check("fractional font sizes are preserved", fontSize == 17.5 && htmlFontSize == 12.5),
                Check("fractional font sizes: pxToRem(16) is 1.6rem", fractionalTypography.pxToRem(16) == "1.6rem"),
                Check("FontStyleOptions factory preserves fontSize", fontStyleOptions.fontSize == 20.0),
                Check("FontStyleOptions factory leaves fontFamily absent", fontStyleOptions.fontFamily == undefined),
                Check("TypographyVariantsOptions factory allows empty options", emptyTypographyOptions.fontSize == undefined),
            )
        }
    },
)
