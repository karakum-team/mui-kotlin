package com.github.turansky.mui

import java.io.File

internal data class IconConversionResult(
    val name: String,
    val body: String,
)

internal fun convertIcons(
    definitionFile: File,
): Sequence<IconConversionResult> =
    definitionFile.readText()
        .removeSuffix("\n")
        .splitToSequence("\nexport const ")
        .drop(1)
        .map { it.removeSuffix(": SvgIconComponent;") }
        .map { name ->
            IconConversionResult(
                name = name,
                body = """
                    @JsName("default")
                    external val $name: SvgIconComponent
                """.trimIndent(),
            )
        }
        .plus(
            IconConversionResult(
                name = "SvgIconComponent",
                body = "typealias SvgIconComponent = react.FC<mui.material.SvgIconProps>",
            )
        )
