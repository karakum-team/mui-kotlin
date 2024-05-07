package karakum.mui

import java.io.File

internal data class IconConversionResult(
    val name: String,
    val body: String,
)

internal fun convertIcons(
    definitionFile: File,
): Sequence<IconConversionResult> {
    val body = definitionFile.readText()
        .removeSuffix("\n")
        .splitToSequence("\nexport const ")
        .drop(1)
        .map { it.removeSuffix(": SvgIconComponent;") }
        .map { name -> "external val $name: SvgIconComponent" }
        .joinToString("\n")

    return sequenceOf(
        IconConversionResult(
            name = "Icons",
            body = body,
        ),
        IconConversionResult(
            name = "SvgIconComponent",
            body = "typealias SvgIconComponent = react.FC<mui.material.SvgIconProps>",
        )
    )
}
