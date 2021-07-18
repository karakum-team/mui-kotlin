package com.github.turansky.materialui

import java.io.File

private const val GENERATOR_COMMENT = "Automatically generated - do not modify!"

// language=Kotlin
private const val MODULE_DECLARATION = "@file:JsModule(\"@material-ui/core\")\n@file:JsNonModule"

private enum class Suppress {
    UNUSED_TYPEALIAS_PARAMETER,
    NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE,

    ;
}

// language=Kotlin
private const val PACKAGE = """package materialui"""

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("materialui")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir) }
}

private fun String.isComponentName(): Boolean {
    if ("_" in this)
        return false

    val char = get(0)
    return char == char.toUpperCase() && char != char.toLowerCase()
}

private fun generate(
    definitionFile: File,
    targetDir: File,
) {
    val name = definitionFile.name.substringBefore(".") + ".kt"
    val body = convertDefinitions(definitionFile)

    val annotations = MODULE_DECLARATION

    val text = sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        PACKAGE,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")

    targetDir.resolve(name)
        .writeText(text)
}
