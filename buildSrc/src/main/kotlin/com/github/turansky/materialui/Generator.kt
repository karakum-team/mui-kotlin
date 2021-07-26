package com.github.turansky.materialui

import java.io.File

private const val GENERATOR_COMMENT = "Automatically generated - do not modify!"

private enum class Suppress {
    UNUSED_TYPEALIAS_PARAMETER,
    NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE,

    ;
}

// language=Kotlin
private const val PACKAGE = """package material"""

private val ALIASES = setOf(
    "Portal",
    "StyledEngineProvider",
)

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("material")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() }
        .filter { it.name !in ALIASES }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir) }
}

private fun String.isComponentName(): Boolean {
    if ("_" in this)
        return false

    val char = get(0)
    return char == char.toUpperCase() && char != char.toLowerCase()
}

private fun moduleDeclaration(componentName: String): String =
    "@file:JsModule(\"@material-ui/core/$componentName\")\n@file:JsNonModule"


private fun generate(
    definitionFile: File,
    targetDir: File,
) {
    val componentName = definitionFile.name.substringBefore(".")
    val fileName = "$componentName.kt"
    val (body, extensions) = convertDefinitions(definitionFile)

    val annotations = moduleDeclaration(componentName)

    val text = sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        PACKAGE,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")

    targetDir.resolve(fileName)
        .writeText(text)

    if (extensions.isEmpty())
        return

    val extensionsText = sequenceOf(
        "// $GENERATOR_COMMENT",
        PACKAGE,
        extensions,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")

    targetDir.resolve(fileName.replace(".kt", ".ext.kt"))
        .writeText(extensionsText)
}
