package playground.samples.system

import js.objects.unsafeJso
import mui.system.Breakpoint.Companion.md
import mui.system.createBreakpoints
import mui.system.createTheme
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import react.FC
import react.Props
import react.dom.html.ReactHTML.p
import web.dom.ElementId

private const val MEDIUM_UP = "@media (min-width:900px)"

private val factoryBreakpoints = createBreakpoints(unsafeJso {})
private val factoryTheme = createTheme()

val SystemFactoriesSample: Sample = Sample(
    id = "runtime-factories",
    library = Libraries.System,
    title = "System factories",
    kind = Kind.ImportSmoke,
    covers = listOf("createBreakpoints(...).up", "createTheme().breakpoints.up"),
    fileName = "SystemFactoriesSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — Runtime imports",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#runtime-imports--resolved-from-npm-exports-during-generation",
        ),
        Ref("a3919d02", "https://github.com/karakum-team/mui-kotlin/commit/a3919d02"),
    ),
    notes = "Only running this proves that `createBreakpoints` binds to `@mui/system/createBreakpoints#" +
            "unstable_createBreakpoints` and `createTheme` to `@mui/system/createTheme#default`: both factories " +
            "must return working breakpoints whose `up(md)` is the default 900px media query.",
    content = FC<Props> {
        val breakpointsUp = factoryBreakpoints.up(md)
        val themeUp = factoryTheme.breakpoints.up(md)

        CheckList {
            id = "runtime-factories-checks"
            checks = listOf(
                Check("createBreakpoints({}).up(md) is $MEDIUM_UP", breakpointsUp == MEDIUM_UP),
                Check("createTheme().breakpoints.up(md) is $MEDIUM_UP", themeUp == MEDIUM_UP),
            )
        }

        p {
            id = ElementId("runtime-factories")
            +"Breakpoints: $breakpointsUp; system: $themeUp"
        }
    },
)
