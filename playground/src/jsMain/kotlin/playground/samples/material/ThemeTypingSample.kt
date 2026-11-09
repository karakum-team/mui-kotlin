package playground.samples.material

import js.objects.Record
import js.objects.get
import js.objects.recordOf
import js.objects.unsafeJso
import mui.material.ButtonProps
import mui.material.PaletteMode
import mui.material.styles.Duration
import mui.material.styles.FontStyleOptions
import mui.material.styles.MixinsOptions
import mui.material.styles.Palette
import mui.material.styles.PaletteOptions
import mui.material.styles.ReducedMotionMode
import mui.material.styles.ShapeOptions
import mui.material.styles.SimplePaletteColorOptions
import mui.material.styles.ThemeOptions
import mui.material.styles.TransitionsOptions
import mui.material.styles.TypographyUtils
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

private val customThemeOptions = ThemeOptions(
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
    mixins = MixinsOptions(
        toolbar = unsafeJso<mui.material.styles.CSSProperties> {
            minHeight = 64.px
        },
    ),
    transitions = TransitionsOptions(
        duration = unsafeJso<Duration> {
            standard = 450
        },
    ),
    zIndex = ZIndex(
        mobileStepper = defaultTheme.zIndex.mobileStepper,
        speedDial = defaultTheme.zIndex.speedDial,
        appBar = 2000,
        drawer = defaultTheme.zIndex.drawer,
        modal = defaultTheme.zIndex.modal,
        snackbar = defaultTheme.zIndex.snackbar,
        tooltip = defaultTheme.zIndex.tooltip,
        fab = defaultTheme.zIndex.fab,
    ),
    components = recordOf(
        "MuiButton" to recordOf(
            "defaultProps" to unsafeJso<ButtonProps> {
                disabled = true
            },
        ),
    ),
)

private val customTheme = createTheme(customThemeOptions)

// Every Material option must be assignable to its System counterpart without casts or suppressions.
private val systemOptions = mui.system.ThemeOptions(
    mixins = customThemeOptions.mixins,
    transitions = customThemeOptions.transitions,
    zIndex = customThemeOptions.zIndex,
    components = customThemeOptions.components,
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

// An explicitly Double-typed callback must be accepted by the generated factory, not widened to Number.
private val typographyUtils = TypographyUtils(
    pxToRem = { px: Double -> defaultTheme.typography.pxToRem(px) },
)

private val cssVariablesTheme = createTheme(ThemeOptions(cssVariables = true))

val ThemeTypingSample: Sample = Sample(
    id = "theme-typing",
    library = Libraries.Material,
    title = "Theme typing",
    kind = Kind.Check,
    covers = listOf(
        "Theme.palette: Palette (mode, primary.main)",
        "Theme.typography: TypographyVariants (fontFamily, h1, pxToRem)",
        "TypographyUtils.pxToRem: (Double) -> String, including fractional pixels",
        "FontStyle / FontStyleOptions fontSize / htmlFontSize: Double",
        "Theme.shadows / shape / mixins.toolbar / zIndex / transitions / motion",
        "Theme.alpha / lighten / darken",
        "Theme.vars: ThemeVars",
        "ThemeOptions.palette / typography / shape / mixins / transitions / zIndex / components / cssVariables",
        "Material Mixins / Transitions / ZIndex and their options are assignable to System markers",
        "Theme.components / ThemeOptions.components: Record<String, *> shared with System",
        "createTypography(palette, options)",
        "createTypography(palette, callback)",
        "ThemeOptions / FontStyleOptions / TypographyVariantsOptions / MixinsOptions / TransitionsOptions @JsPlainObject factories",
    ),
    fileName = "ThemeTypingSample.kt",
    notes = "Only running this proves that the members converted from `createThemeNoVars.d.ts` read the " +
            "runtime theme under the names and shapes MUI builds: palette and typography values, the 25 shadows, " +
            "the color helpers, the CSS variables `vars`, that `@JsPlainObject` options reach `createTheme`, " +
            "that both `createTypography` overloads preserve partial options and invoke the callback with the palette, " +
            "and that Material's theme fields remain usable through System types without casts.",
    content = FC<Props> {
        val theme = defaultTheme
        val fontSize: Double = fractionalTypography.fontSize
        val htmlFontSize: Double = fractionalTypography.htmlFontSize
        val systemTheme: mui.system.Theme = customTheme
        val systemMixins: mui.system.Mixins = customTheme.mixins
        val systemTransitions: mui.system.Transitions = customTheme.transitions
        val systemZIndex: mui.system.ZIndex = customTheme.zIndex
        val components: Record<String, *>? = customTheme.components

        CheckList {
            id = "theme-typing-checks"
            checks = listOf(
                Check("palette.mode is light", theme.palette.mode == PaletteMode.light),
                Check("palette.primary.main is #1976d2", theme.palette.primary.main == "#1976d2"),
                Check("typography.fontFamily mentions Roboto", "${theme.typography.fontFamily}".contains("Roboto")),
                Check("typography.h1.fontSize is 6rem", "${theme.typography.h1.fontSize}" == "6rem"),
                Check("typography.pxToRem(16) is 1rem", theme.typography.pxToRem(16.0) == "1rem"),
                Check("typography.pxToRem(16.5) preserves fractional pixels", theme.typography.pxToRem(16.5) == "1.03125rem"),
                Check("TypographyUtils factory accepts a Double callback", typographyUtils.pxToRem(16.5) == "1.03125rem"),
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
                Check("ThemeOptions.mixins: toolbar.minHeight is 64px", "${customTheme.mixins.toolbar.minHeight}" == "64px"),
                Check("ThemeOptions.transitions: duration.standard is 450", customTheme.transitions.duration.standard == 450),
                Check("System ThemeOptions accepts Material MixinsOptions", systemOptions.mixins === customThemeOptions.mixins),
                Check("System ThemeOptions accepts Material TransitionsOptions", systemOptions.transitions === customThemeOptions.transitions),
                Check("System ThemeOptions accepts Material ZIndex", systemOptions.zIndex === customThemeOptions.zIndex),
                Check("System ThemeOptions accepts Material components", systemOptions.components === customThemeOptions.components),
                Check("Material Mixins is usable as System Mixins", systemTheme.mixins === systemMixins),
                Check("Material Transitions is usable as System Transitions", systemTheme.transitions === systemTransitions),
                Check("Material ZIndex is usable as System ZIndex", systemTheme.zIndex === systemZIndex),
                Check(
                    "Material components is a System-compatible record containing MuiButton",
                    components?.get("MuiButton") != undefined && systemTheme.components === components,
                ),
                Check("createTypography(fontSize = 28).pxToRem(16) is 2rem", largeTypography.pxToRem(16.0) == "2rem"),
                Check("createTypography callback receives the palette", callbackPalette === theme.palette),
                Check("createTypography callback: pxToRem(16) is 2rem", callbackTypography.pxToRem(16.0) == "2rem"),
                Check("createTypography callback: h1.fontSize is 4rem", "${callbackTypography.h1.fontSize}" == "4rem"),
                Check("fractional font sizes are preserved", fontSize == 17.5 && htmlFontSize == 12.5),
                Check("fractional font sizes: pxToRem(16) is 1.6rem", fractionalTypography.pxToRem(16.0) == "1.6rem"),
                Check("FontStyleOptions factory preserves fontSize", fontStyleOptions.fontSize == 20.0),
                Check("FontStyleOptions factory leaves fontFamily absent", fontStyleOptions.fontFamily == undefined),
                Check("TypographyVariantsOptions factory allows empty options", emptyTypographyOptions.fontSize == undefined),
            )
        }
    },
)
