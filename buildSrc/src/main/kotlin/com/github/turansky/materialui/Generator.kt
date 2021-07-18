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

    println("TARGET DIR: $targetDir")
}
