import js.objects.unsafeJso
import mui.base.CssAnimation
import mui.base.CssTransition
import mui.base.UseOptionParameters
import mui.base.UseOptionReturnValue
import mui.base.UseSelectParameters
import mui.base.UseSelectReturnValue
import mui.base.useBadge
import mui.base.useButton
import mui.base.useInput
import mui.base.useMenu
import mui.base.useMenuButton
import mui.base.useMenuItem
import mui.base.useOption
import mui.base.useSelect
import mui.base.useSlider
import mui.base.useSnackbar
import mui.base.useSwitch
import mui.base.useTab
import mui.base.useTabPanel
import mui.base.useTabsList
import mui.material.ClickAwayListener
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import mui.material.styles.createMixins
import mui.material.styles.createStyles
import mui.material.styles.createTheme
import mui.material.styles.useTheme
import mui.material.touchRippleClasses
import mui.system.Breakpoint.Companion.md
import mui.system.createBreakpoints
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.p
import react.useState
import web.cssom.ClassName
import web.cssom.Position
import web.cssom.integer
import web.dom.ElementId
import mui.system.createTheme as createSystemTheme

// These references must reach JS: loading every repaired hook catches missing exports even when
// invoking the hook would require a menu/listbox/tabs provider. useBadge is also called below.
private val optionHook: (UseOptionParameters<String>) -> UseOptionReturnValue = ::useOption
private val selectHook: (UseSelectParameters<String, Boolean>) -> UseSelectReturnValue<String, Boolean> = ::useSelect
private val hookImports = listOf(
    ::useBadge, ::useButton, ::useInput, ::useMenu, ::useMenuButton, ::useMenuItem,
    optionHook, selectHook, ::useSlider, ::useSnackbar, ::useSwitch, ::useTab, ::useTabPanel, ::useTabsList,
)

private val transitionFixture = unsafeJso<TransitionFixture> {
    requestedEnter = true
    registerTransition = { {} }
    onExited = {}
}

private val importTheme = createTheme(unsafeJso { focusVisible = true })
private val importBreakpoints = createBreakpoints(unsafeJso {})
private val importMixins = createMixins(importBreakpoints, unsafeJso {})
private val importSystemTheme = createSystemTheme()

private val ThemeProbe = FC<Props> {
    val theme = useTheme<Theme>()
    val badge = useBadge(unsafeJso { badgeContent = ReactNode("7") })
    p {
        id = ElementId("runtime-theme")
        +"Theme from provider: ${theme.focusVisible != null}; badge: "
        +badge.displayValue
    }
}

val RuntimeImports = FC<Props> {
    var outsideClicks by useState(0)
    div {
        // Keep controls above SliderStylization's existing viewport-sized layer.
        style = unsafeJso {
            position = Position.relative
            zIndex = integer(2)
        }
        h3 { +"Runtime imports" }
        p {
            id = ElementId("runtime-hooks")
            +"Base hooks loaded: ${hookImports.count { jsTypeOf(it) == "function" }}"
        }
        p {
            id = ElementId("runtime-factories")
            +"Breakpoints: ${importBreakpoints.up(md)}; system: ${importSystemTheme.breakpoints.up(md)}; "
            +"mixins: ${jsTypeOf(importMixins.toolbar)}"
        }
        p {
            id = ElementId("runtime-classes")
            +touchRippleClasses.root.toString()
        }
        ThemeProvider {
            theme = importTheme
            ThemeProbe()
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
        p {
            id = ElementId("runtime-clicks")
            +"Outside clicks: $outsideClicks"
        }
        button {
            id = ElementId("runtime-create-styles")
            // Upstream deprecates this API and logs one warning; reaching it must not fail to import.
            onClick = { createStyles(unsafeJso<react.CSSProperties> {}) }
            +"Call legacy createStyles"
        }
        transitionFixtureContext.Provider {
            value = transitionFixture
            CssAnimation {
                enterClassName = ClassName("runtime-animation-enter")
                +"CssAnimation mounted"
            }
            CssTransition {
                enterClassName = ClassName("runtime-transition-enter")
                +"CssTransition mounted"
            }
        }
    }
}
