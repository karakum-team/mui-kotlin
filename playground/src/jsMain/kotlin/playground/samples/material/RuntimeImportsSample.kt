package playground.samples.material

import js.objects.unsafeJso
import mui.material.ClickAwayListener
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import mui.material.styles.createMixins
import mui.material.styles.createStyles
import mui.material.styles.createTheme
import mui.material.styles.useTheme
import mui.material.touchRippleClasses
import mui.system.createBreakpoints
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.CSSProperties
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.p
import react.useState
import web.cssom.ClassName
import web.dom.ElementId

private val importTheme = createTheme(unsafeJso { focusVisible = true })

// `createBreakpoints` is only the argument `createMixins` needs; the system factories are covered by
// `system/SystemFactoriesSample.kt`.
private val importMixins = createMixins(createBreakpoints(unsafeJso {}), unsafeJso {})

private val ThemeProbe = FC<Props> {
    val theme = useTheme<Theme>()
    val themeFromProvider = theme.focusVisible != null

    CheckList {
        id = "runtime-imports-checks"
        checks = listOf(
            Check(
                "touchRippleClasses.root is MuiTouchRipple-root",
                touchRippleClasses.root == ClassName("MuiTouchRipple-root"),
            ),
            Check("createMixins(...).toolbar is an object", jsTypeOf(importMixins.toolbar) == "object"),
            Check("theme.focusVisible reaches the provider", themeFromProvider),
        )
    }

    p {
        id = ElementId("runtime-theme")
        +"Theme from provider: $themeFromProvider"
    }
}

val RuntimeImportsSample: Sample = Sample(
    id = "runtime-imports",
    library = Libraries.Material,
    title = "Runtime imports",
    kind = Kind.ImportSmoke,
    covers = listOf(
        "styles.createTheme",
        "styles.ThemeProvider",
        "styles.useTheme",
        "styles.createMixins",
        "styles.createStyles",
        "touchRippleClasses.root",
        "ClickAwayListener.onClickAway",
    ),
    fileName = "RuntimeImportsSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — Runtime imports",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#runtime-imports--resolved-from-npm-exports-during-generation",
        ),
        Ref("a3919d02", "https://github.com/karakum-team/mui-kotlin/commit/a3919d02"),
    ),
    notes = "Only running this proves that each repaired binding resolves to the export the generator chose: " +
            "`useTheme`/`createStyles` from the `@mui/material/styles` barrel, `createMixins` from " +
            "`private_createMixins`, `touchRippleClasses` from `@mui/material/ButtonBase`, `ClickAwayListener` " +
            "from its default export. Clicking \"Outside listener\" must raise `outsideClicks`, clicking " +
            "\"Inside listener\" must not; `createStyles` logs exactly one upstream deprecation warning.",
    content = FC<Props> {
        var outsideClicks by useState(0)

        ThemeProvider {
            theme = importTheme
            ThemeProbe()
        }

        p {
            id = ElementId("runtime-classes")
            +touchRippleClasses.root.toString()
        }

        ClickAwayListener {
            onClickAway = { outsideClicks++ }
            button {
                id = ElementId("runtime-inside")
                +"Inside listener"
            }
        }
        button {
            id = ElementId("runtime-outside")
            +"Outside listener"
        }
        StatusLine {
            id = "runtime-clicks"
            fields = listOf("outsideClicks" to outsideClicks)
        }

        button {
            id = ElementId("runtime-create-styles")
            // Upstream deprecates this API and logs one warning; reaching it must not fail to import.
            onClick = { createStyles(unsafeJso<CSSProperties> {}) }
            +"Call legacy createStyles"
        }
    },
)
