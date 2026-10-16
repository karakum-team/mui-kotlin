import js.objects.unsafeJso
import mui.material.Button
import mui.material.Chip
import mui.material.styles.CssVarsPalette
import mui.material.styles.FocusVisible
import mui.material.styles.ThemeOptions
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.h3
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
    unsafeJso<ThemeOptions> {
        focusVisible = true
    }
)

private val customRingTheme = createTheme(
    unsafeJso<ThemeOptions> {
        focusVisible = unsafeJso<FocusVisible> {
            outlineColor = NamedColor.magenta
            outlineWidth = 4.px
            outlineOffset = 6.px
        }
    }
)

// The generated ThemeOptions stub does not expose the CSS variables opt-in yet.
private external interface CssVariablesThemeOptions : ThemeOptions {
    var cssVariables: Boolean
}

private val cssVariablesTheme = createTheme(
    unsafeJso<CssVariablesThemeOptions> {
        cssVariables = true
    }
)

// Theme.palette is currently Any?, but its CSS variables fields use the generated interfaces.
private val cssVariablesPalette = cssVariablesTheme.palette.unsafeCast<CssVarsPalette>()

val Theming = FC<Props> {
    h3 { +"theme.focusVisible (MUI 9.4.0)" }
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

    h3 { +"CSS variables palette" }
    p {
        id = ElementId("css-vars-alert-error-color")
        +"Alert.errorColor: ${cssVariablesPalette.Alert.errorColor}"
    }
}
