package playground.samples.material

import js.objects.unsafeJso
import mui.material.Button
import mui.material.Chip
import mui.material.styles.CssVarsPalette
import mui.material.styles.FocusVisible
import mui.material.styles.ThemeOptions
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.p
import web.cssom.NamedColor
import web.cssom.px
import web.dom.ElementId

// Live check of `theme.focusVisible`, the one public API @mui/material 9.4.0 added, and the only
// usage reference for `mui.material.styles.createTheme` / `ThemeProvider` in the repo.
//
// What driving this in Chrome established — none of it visible to `compileKotlinJs`:
//
//  - `createTheme` bound to the `@mui/material/styles` barrel resolves at runtime. The deep path
//    the ThemeProvider stub used before (`@mui/material/styles/ThemeProvider`) does NOT: it is
//    absent from the package's `exports` map and Vite fails the dependency scan on it. That is
//    what forced both stubs onto the barrel.
//  - `focusVisible = true` reaches MATERIAL's `createTheme` and comes back resolved:
//    `outline: rgb(25,118,210) solid 2px; outline-offset: 2px` — i.e. `palette.primary.main`,
//    the curated default. `mui.system.createTheme` knows nothing of the option and would have
//    left a raw boolean on the theme.
//  - The object arm binds too: `outlineColor`/`outlineWidth`/`outlineOffset` set through
//    `FocusVisible` land as `rgb(255,0,255) solid 4px` at `6px` offset, so `web.cssom` values
//    serialize into the theme unchanged.
//  - The Button OUTSIDE any provider still gets the `Mui-focusVisible` state class on keyboard
//    focus but computes `outline-style: none` — proof the ring comes from the theme option and
//    not from a component default.
//  - The Chip inside the provider picks up the same ring, confirming the option is theme-wide
//    rather than Button-specific.
//
// Focus is moved with real Tab / Shift+Tab presses; `element.focus()` alone does not satisfy
// `:focus-visible` and would make the rings look absent.

private val defaultRingTheme = createTheme(
    ThemeOptions(
        focusVisible = true,
    ),
)

private val customRingTheme = createTheme(
    ThemeOptions(
        focusVisible = unsafeJso<FocusVisible> {
            outlineColor = NamedColor.magenta
            outlineWidth = 4.px
            outlineOffset = 6.px
        },
    ),
)

private val cssVariablesTheme = createTheme(
    ThemeOptions(
        cssVariables = true,
    ),
)

// `Theme.palette` is the plain `Palette`: its CSS variables fields (`CssVarsPalette`) exist only when
// `cssVariables` is on, which Kotlin's types cannot follow.
private val cssVariablesPalette = cssVariablesTheme.palette.unsafeCast<CssVarsPalette>()

val ThemeFocusVisibleSample: Sample = Sample(
    id = "theme-focus-visible",
    library = Libraries.Material,
    title = "Theme focusVisible",
    kind = Kind.Check,
    covers = listOf(
        "styles.createTheme",
        "styles.ThemeProvider",
        "ThemeOptions.focusVisible (Boolean)",
        "FocusVisible.outlineColor/outlineWidth/outlineOffset",
        "CssVarsPalette.Alert.errorColor",
        "Button",
        "Chip.clickable",
    ),
    fileName = "ThemeFocusVisibleSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — material theme",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#the-material-theme-is-converted-from-createthemenovarsdts",
        ),
        Ref("6ac39c87", "https://github.com/karakum-team/mui-kotlin/commit/6ac39c87"),
        Ref("f305a69c", "https://github.com/karakum-team/mui-kotlin/commit/f305a69c"),
    ),
    notes = "Only running this proves that `createTheme`/`ThemeProvider` bound to the `@mui/material/styles` " +
            "barrel resolve, and that `focusVisible` reaches material's `createTheme`: Tab to each button — no ring " +
            "outside a provider, the `palette.primary.main` 2px ring for `true` (also on the chip), and a magenta " +
            "4px ring at 6px offset for the object form. Focus with real Tab presses; `element.focus()` does not " +
            "satisfy `:focus-visible`.",
    content = FC<Props> {
        p { +"Tab through the three buttons: no ring, default ring, magenta ring." }

        Button {
            id = ElementId("focus-ring-off")
            +"no theme"
        }

        ThemeProvider {
            theme = defaultRingTheme

            Button {
                id = ElementId("focus-ring-default")
                +"focusVisible = true"
            }

            Chip {
                id = ElementId("focus-ring-default-chip")
                label = ReactNode("chip")
                clickable = true
            }
        }

        ThemeProvider {
            theme = customRingTheme

            Button {
                id = ElementId("focus-ring-custom")
                +"focusVisible = { … }"
            }
        }

        p { +"CSS variables palette" }
        StatusLine {
            id = "css-vars-alert-error-color"
            fields = listOf("Alert.errorColor" to cssVariablesPalette.Alert.errorColor)
        }
    },
)
