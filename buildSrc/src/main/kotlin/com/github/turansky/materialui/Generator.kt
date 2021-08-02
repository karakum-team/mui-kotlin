package com.github.turansky.materialui

import java.io.File

private const val GENERATOR_COMMENT = "Automatically generated - do not modify!"

private enum class Suppress {
    UNUSED_TYPEALIAS_PARAMETER,
    NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE,

    ;
}

// language=Kotlin
private const val PACKAGE = "package material"

// language=Kotlin
private const val ALIASES = """
typealias Union = String
"""

// language=Kotlin
private const val STUBS = """
external interface Theme

external interface SxProps<T: Any>

external interface TransitionProps: react.RProps

external interface TableCellBaseProps: react.RProps
external interface TablePaginationActionsProps: react.RProps
"""

private val UNSTYLED_ALIASES = setOf(
    "NoSsr",
    "Portal",
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
        .filter { it.name != "StyledEngineProvider" }
        .map {
            val fileName = "${it.name}.d.ts"
            if (it.name !in UNSTYLED_ALIASES) {
                it
            } else {
                it.parentFile.parentFile
                    .resolve("unstyled")
                    .resolve(it.name)
            }.resolve(fileName)
        }
        .forEach { generate(it, targetDir) }

    targetDir.resolve("Aliases.kt")
        .writeText(fileContent(body = ALIASES))

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = STUBS))
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
    val (body, extensions) = convertDefinitions(definitionFile)

    val annotations = moduleDeclaration(componentName)
    targetDir.resolve("$componentName.kt")
        .writeText(fileContent(annotations, body))

    if (extensions.isNotEmpty()) {
        targetDir.resolve("$componentName.ext.kt")
            .writeText(fileContent(body = extensions))
    }

    val classesName = componentName + "Classes"
    val classesFile = definitionFile.parentFile.resolve(classesName.decapitalize() + ".d.ts")
    if (classesFile.exists()) {
        val classes = convertClasses(classesName, classesFile)
        targetDir.resolve("$componentName.classes.kt")
            .writeText(fileContent(body = classes))
    }
}

private fun fileContent(
    annotations: String = "",
    body: String,
) =
    sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        PACKAGE,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")
