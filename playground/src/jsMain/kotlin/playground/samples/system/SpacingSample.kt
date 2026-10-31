package playground.samples.system

import js.objects.unsafeJso
import mui.system.SpacingOptions
import mui.system.ThemeOptions
import mui.system.createSpacing
import mui.system.createTheme
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.CSSProperties
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import web.cssom.px
import web.dom.ElementId
import mui.material.styles.ThemeOptions as MaterialThemeOptions
import mui.material.styles.createTheme as createMaterialTheme

private val defaultSpacing = createTheme().spacing

private val unitSpacing = createTheme(
    unsafeJso<ThemeOptions> {
        spacing = SpacingOptions(4)
    }
).spacing

private val lengthSpacing = createSpacing(SpacingOptions(8.px))

private val functionSpacing = createSpacing(SpacingOptions { abs: Int -> (abs * 3).px })

private val arraySpacing = createSpacing(SpacingOptions(arrayOf(0, 4, 8, 16)))

private val materialSpacing = createMaterialTheme(
    unsafeJso<MaterialThemeOptions> {
        spacing = SpacingOptions(4)
    }
).spacing

val SpacingSample: Sample = Sample(
    id = "spacing",
    library = Libraries.System,
    title = "Spacing",
    kind = Kind.Check,
    covers = listOf(
        "Spacing.invoke() / (value) / (topBottom, rightLeft) / (top, rightLeft, bottom) / (top, right, bottom, left)",
        "createSpacing(SpacingOptions)",
        "SpacingOptions(Int / Length / (abs: Int) -> Length / ReadonlyArray<Int>)",
        "ThemeOptions.spacing",
        "styles.createTheme(...).spacing",
    ),
    fileName = "SpacingSample.kt",
    notes = "Only running this proves that `Spacing` is MUI's callable spacing function and that every " +
            "`SpacingOptions` factory reaches `createSpacing` in the shape it expects: the default 8px grid, a 4px unit, " +
            "a CSS length (`calc(n * 8px)`), a unit converter and a scale array.",
    content = FC<Props> {
        CheckList {
            id = "spacing-checks"
            checks = listOf(
                Check("spacing() is 8px", "${defaultSpacing()}" == "8px"),
                Check("spacing(2) is 16px", "${defaultSpacing(2)}" == "16px"),
                Check("spacing(1, 2) is 8px 16px", "${defaultSpacing(1, 2)}" == "8px 16px"),
                Check("spacing(1, 2, 3) is 8px 16px 24px", "${defaultSpacing(1, 2, 3)}" == "8px 16px 24px"),
                Check(
                    "spacing(1, 2, 3, 4) is 8px 16px 24px 32px",
                    "${defaultSpacing(1, 2, 3, 4)}" == "8px 16px 24px 32px",
                ),
                Check("ThemeOptions.spacing = SpacingOptions(4): spacing(2) is 8px", "${unitSpacing(2)}" == "8px"),
                Check(
                    "SpacingOptions(8.px): spacing(2) is calc(2 * 8px)",
                    "${lengthSpacing(2)}" == "calc(2 * 8px)",
                ),
                Check("SpacingOptions { abs * 3 px }: spacing(2) is 6px", "${functionSpacing(2)}" == "6px"),
                Check("SpacingOptions([0, 4, 8, 16]): spacing(3) is 16px", "${arraySpacing(3)}" == "16px"),
                Check(
                    "material createTheme(spacing = 4): spacing(2) is 8px",
                    "${materialSpacing(2)}" == "8px",
                ),
            )
        }

        div {
            id = ElementId("spacing-box")
            style = unsafeJso<CSSProperties> {
                padding = defaultSpacing(1, 2)
            }
            +"padding = spacing(1, 2)"
        }
    },
)
