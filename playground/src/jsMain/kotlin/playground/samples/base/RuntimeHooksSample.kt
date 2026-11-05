package playground.samples.base

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
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.p
import web.cssom.ClassName
import web.dom.ElementId

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

val RuntimeHooksSample: Sample = Sample(
    id = "runtime-hooks",
    library = Libraries.Base,
    title = "Runtime hooks",
    kind = Kind.ImportSmoke,
    covers = listOf(
        "useBadge, useButton, useInput, useMenu, useMenuButton, useMenuItem, useOption, useSelect, " +
                "useSlider, useSnackbar, useSwitch, useTab, useTabPanel, useTabsList (14 hooks)",
        "useBadge(...).displayValue",
        "CssAnimation.enterClassName",
        "CssTransition.enterClassName",
    ),
    fileName = "RuntimeHooksSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — Runtime imports",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#runtime-imports--resolved-from-npm-exports-during-generation",
        ),
        Ref("a3919d02", "https://github.com/karakum-team/mui-kotlin/commit/a3919d02"),
    ),
    notes = "Only running this proves that the 14 legacy hooks bind to named (not default) exports of their " +
            "entrypoints, and that `CssAnimation`/`CssTransition` resolve from `@mui/base/Transitions`: a wrong " +
            "binding is `undefined` at load, which kotlinc cannot see. `TransitionFixture.kt` supplies the " +
            "`TransitionContext` the ungenerated Popup would normally provide.",
    content = FC<Props> {
        val loadedHooks = hookImports.count { jsTypeOf(it) == "function" }
        val badge = useBadge(unsafeJso { badgeContent = ReactNode("7") })

        CheckList {
            id = "runtime-hooks-checks"
            checks = listOf(
                Check("14/14 hook references are functions", loadedHooks == 14),
                Check("useBadge displayValue is 7", badge.displayValue == ReactNode("7")),
            )
        }

        p {
            id = ElementId("runtime-hooks")
            +"Base hooks loaded: $loadedHooks"
        }

        StatusLine {
            id = "runtime-hooks-badge"
            fields = listOf("displayValue" to badge.displayValue)
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
    },
)
