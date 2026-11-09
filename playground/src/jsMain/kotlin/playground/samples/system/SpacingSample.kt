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
import web.cssom.rem
import web.dom.ElementId
import mui.material.styles.ThemeOptions as MaterialThemeOptions
import mui.material.styles.createTheme as createMaterialTheme

private val defaultSpacing = createTheme().spacing

private val unitSpacing = createTheme(
    ThemeOptions(
        spacing = SpacingOptions(4.0),
    ),
).spacing

private val lengthSpacing = createSpacing(SpacingOptions(8.px))

private val fractionalUnitSpacing = createTheme(
    ThemeOptions(
        spacing = SpacingOptions(4.5),
    ),
).spacing

private val remUnitSpacing = createSpacing(SpacingOptions(0.5.rem))

private val functionSpacing = createSpacing(SpacingOptions { abs -> (abs * 3).px })

private val arraySpacing = createSpacing(SpacingOptions(arrayOf(0.0, 4.0, 6.5, 16.0)))

private val lengthArraySpacing = createSpacing(SpacingOptions(arrayOf(0.px, 4.px, 6.5.px, 1.rem)))

private val reusedSpacing = createSpacing(SpacingOptions(defaultSpacing))

private val materialSpacing = createMaterialTheme(
    MaterialThemeOptions(
        spacing = SpacingOptions(4.0),
    ),
).spacing

private val fractionalMaterialSpacing = createMaterialTheme(
    MaterialThemeOptions(
        spacing = SpacingOptions(4.5),
    ),
).spacing

val SpacingSample: Sample = Sample(
    id = "spacing",
    library = Libraries.System,
    title = "Spacing",
    kind = Kind.Check,
    covers = listOf(
        "Spacing.invoke() / (value) / (topBottom, rightLeft) / (top, rightLeft, bottom) / (top, right, bottom, left)",
        "createSpacing(SpacingOptions)",
        "SpacingArgument = Double",
        "SpacingOptions(Double / Length / Spacing)",
        "SpacingOptions((Double) -> Length)",
        "SpacingOptions(ReadonlyArray<Double> / ReadonlyArray<Length>)",
        "ThemeOptions.spacing",
        "styles.createTheme(...).spacing",
    ),
    fileName = "SpacingSample.kt",
    notes = "Numeric factors and pixel amounts use Double; CSS units use typed Length values such as 8.px or 0.5.rem. " +
            "Only scale indices must be integral. Running proves fractional factors are multiplied, numeric " +
            "scale results gain px, and callbacks and typed CSS lengths retain their units.",
    content = FC<Props> {
        CheckList {
            id = "spacing-checks"
            checks = listOf(
                Check("spacing() is 8px", "${defaultSpacing()}" == "8px"),
                Check("spacing(2.0) is 16px", "${defaultSpacing(2.0)}" == "16px"),
                Check("spacing(1.0, 2.0) is 8px 16px", "${defaultSpacing(1.0, 2.0)}" == "8px 16px"),
                Check("spacing(1.0, 2.0, 3.0) is 8px 16px 24px", "${defaultSpacing(1.0, 2.0, 3.0)}" == "8px 16px 24px"),
                Check(
                    "spacing(1.0, 2.0, 3.0, 4.0) is 8px 16px 24px 32px",
                    "${defaultSpacing(1.0, 2.0, 3.0, 4.0)}" == "8px 16px 24px 32px",
                ),
                Check("ThemeOptions.spacing = SpacingOptions(4.0): spacing(2.0) is 8px", "${unitSpacing(2.0)}" == "8px"),
                Check("spacing(0.5) is 4px", "${defaultSpacing(0.5)}" == "4px"),
                Check("spacing(0.5, -1.5) is 4px -12px", "${defaultSpacing(0.5, -1.5)}" == "4px -12px"),
                Check(
                    "spacing(0.5, 1.5, 2.5) is 4px 12px 20px",
                    "${defaultSpacing(0.5, 1.5, 2.5)}" == "4px 12px 20px",
                ),
                Check(
                    "spacing(0.5, 1.5, 2.5, 3.5) is 4px 12px 20px 28px",
                    "${defaultSpacing(0.5, 1.5, 2.5, 3.5)}" == "4px 12px 20px 28px",
                ),
                Check(
                    "fractional System theme unit and negative factor",
                    "${fractionalUnitSpacing(-0.5)}" == "-2.25px",
                ),
                Check(
                    "SpacingOptions(8.px): spacing(2.0) is calc(2 * 8px)",
                    "${lengthSpacing(2.0)}" == "calc(2 * 8px)",
                ),
                Check(
                    "CSS length unit accepts fractional factors",
                    "${lengthSpacing(0.5)}" == "calc(0.5 * 8px)",
                ),
                Check(
                    "typed rem unit accepts fractional factors",
                    "${remUnitSpacing(1.5)}" == "calc(1.5 * 0.5rem)",
                ),
                Check("SpacingOptions { abs * 3 px }: spacing(2.0) is 6px", "${functionSpacing(2.0)}" == "6px"),
                Check("CSS length callback receives a fractional number", "${functionSpacing(0.5)}" == "1.5px"),
                Check("CSS length callback preserves a negative factor", "${functionSpacing(-1.5)}" == "-4.5px"),
                Check("CSS length callback uses the default factor", "${functionSpacing()}" == "3px"),
                Check("numeric scale uses an integral index", "${arraySpacing(3.0)}" == "16px"),
                Check("scale entries may be fractional", "${arraySpacing(2.0)}" == "6.5px"),
                Check("negative scale index negates a fractional entry", "${arraySpacing(-2.0)}" == "-6.5px"),
                Check("CSS length scale preserves fractional pixels", "${lengthArraySpacing(2.0)}" == "6.5px"),
                Check("CSS length scale preserves rem units", "${lengthArraySpacing(3.0)}" == "1rem"),
                Check("negative scale index negates a CSS entry", "${lengthArraySpacing(-3.0)}" == "-1rem"),
                Check("existing Spacing can be reused without wrapping the function", reusedSpacing === defaultSpacing),
                Check(
                    "material createTheme(spacing = 4.0): spacing(2.0) is 8px",
                    "${materialSpacing(2.0)}" == "8px",
                ),
                Check("fractional Material theme unit and factor", "${fractionalMaterialSpacing(0.5)}" == "2.25px"),
            )
        }

        div {
            id = ElementId("spacing-box")
            style = unsafeJso<CSSProperties> {
                padding = defaultSpacing(1.0, 2.0)
            }
            +"padding = spacing(1.0, 2.0)"
        }
    },
)
