package com.github.turansky.mui

import java.io.File

internal data class IconConversionResult(
    val name: String,
    val body: String,
)

internal fun convertIcons(
    definitionFile: File,
): Sequence<IconConversionResult> {
    val content = definitionFile.readText()
    println(content)
    return emptySequence()
}
