package playground.samples.system

import mui.system.Direction
import mui.system.Theme
import mui.system.ThemeOptions
import mui.system.ThemeProvider
import mui.system.createTheme
import mui.system.useThemeWithoutDefault
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.FC
import react.Props

private val fallbackTheme = createTheme()

private val providedTheme = createTheme(
    ThemeOptions(
        direction = Direction.rtl,
    ),
)

private external interface ThemeProbeProps : Props {
    var id: String
    var insideProvider: Boolean
}

private val ThemeProbe = FC<ThemeProbeProps> { props ->
    val withoutDefault = useThemeWithoutDefault<Theme?>()
    val withDefault = useThemeWithoutDefault(fallbackTheme)

    CheckList {
        id = props.id
        checks = if (props.insideProvider) {
            listOf(
                Check("useThemeWithoutDefault() returns the provided rtl theme", withoutDefault?.direction == Direction.rtl),
                Check("useThemeWithoutDefault(fallback) ignores the fallback", withDefault.direction == Direction.rtl),
            )
        } else {
            listOf(
                Check("useThemeWithoutDefault() is null without a provider", withoutDefault == null),
                Check("useThemeWithoutDefault(fallback) returns the fallback", withDefault === fallbackTheme),
            )
        }
    }
}

val ThemeWithoutDefaultSample: Sample = Sample(
    id = "theme-without-default",
    library = Libraries.System,
    title = "useThemeWithoutDefault",
    kind = Kind.Check,
    covers = listOf(
        "useThemeWithoutDefault<T>()",
        "useThemeWithoutDefault(defaultTheme)",
        "ThemeProvider.theme",
        "ThemeOptions.direction",
    ),
    fileName = "ThemeWithoutDefaultSample.kt",
    notes = "Only running this proves that `useThemeWithoutDefault` binds to " +
            "`@mui/system/useThemeWithoutDefault#default` and reads the context theme: without a provider it " +
            "returns `null` or the given fallback, inside `mui.system.ThemeProvider` it returns the provided theme.",
    content = FC<Props> {
        ThemeProbe {
            id = "theme-without-default-outside-checks"
            insideProvider = false
        }

        ThemeProvider {
            theme = providedTheme

            ThemeProbe {
                id = "theme-without-default-inside-checks"
                insideProvider = true
            }
        }
    },
)
