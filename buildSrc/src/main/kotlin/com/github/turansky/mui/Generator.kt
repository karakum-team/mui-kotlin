package com.github.turansky.mui

import java.io.File

private const val GENERATOR_COMMENT = "Automatically generated - do not modify!"

private enum class Suppress {
    UNUSED_TYPEALIAS_PARAMETER,
    NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE,

    ;
}

private val DEFAULT_IMPORTS = """
import kotlinext.js.ReadonlyArray
""".trimIndent()

// language=Kotlin
private const val ALIASES = """
typealias Union = String
"""

// language=Kotlin
private val STUBS = """
external interface Theme

@Suppress("UNUSED_TYPEALIAS_PARAMETER")
typealias SxProps<T> = react.CSSProperties

external interface ResponsiveStyleValue<T: Any>

external interface TransitionProps: react.Props

external interface TableCellBaseProps: react.PropsWithChildren
external interface TablePaginationActionsProps: react.Props
""".trimIndent()

private val CORE_ALIASES = setOf(
    "NoSsr",
    "Portal",
)

private val EXCLUDED_TYPES = setOf(
    "Timeline",
    "YearPicker",
)

private enum class Package {
    material,
    lab,

    ;
}

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateMaterialDeclarations(typesDir.resolve("material"), sourceDir)
    generateLabDeclarations(typesDir.resolve("lab"), sourceDir)
}

private fun generateMaterialDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/material")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() }
        .filter { it.name != "StyledEngineProvider" }
        .map {
            val fileName = "${it.name}.d.ts"
            if (it.name !in CORE_ALIASES) {
                it
            } else {
                it.parentFile.parentFile
                    .resolve("core")
                    .resolve(it.name)
            }.resolve(fileName)
        }
        .forEach { generate(it, targetDir, Package.material) }

    targetDir.resolve("Aliases.kt")
        .writeText(fileContent(body = ALIASES, pkg = Package.material))

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = STUBS, pkg = Package.material))
}

private fun generateLabDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/lab")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { !it.name.startsWith("Adapter") }
        .filter { it.name !in EXCLUDED_TYPES }
        .filter { it.name.isComponentName() }
        .filter { !it.resolve("${it.name}.d.ts").readText().startsWith("export { default } from ") }
        .onEach {
            when (it.name) {
                "TreeItem" -> {
                    val file = it.resolve("${it.name}Content.d.ts")
                    generate(file, targetDir, Package.lab)
                }
            }
        }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir, Package.lab) }
}

private fun String.isComponentName(): Boolean {
    if ("_" in this)
        return false

    val char = get(0)
    return char == char.toUpperCase() && char != char.toLowerCase()
}

private fun moduleDeclaration(
    pkg: Package,
    componentName: String,
): String =
    "@file:JsModule(\"@mui/${pkg.name}/$componentName\")\n@file:JsNonModule"


private fun generate(
    definitionFile: File,
    targetDir: File,
    pkg: Package,
) {
    val componentName = definitionFile.name.substringBefore(".")
    val (body, extensions) = convertDefinitions(definitionFile)

    val annotations = moduleDeclaration(pkg, componentName)
    targetDir.resolve("$componentName.kt")
        .writeText(fileContent(annotations, body, pkg))

    if (extensions.isNotEmpty()) {
        targetDir.resolve("$componentName.ext.kt")
            .writeText(fileContent(body = extensions, pkg = pkg))
    }

    val classesName = componentName + "Classes"
    val classesFile = definitionFile.parentFile.resolve(classesName.decapitalize() + ".d.ts")
    if (classesFile.exists()) {
        val classes = convertClasses(classesName, classesFile)
        targetDir.resolve("$componentName.classes.kt")
            .writeText(fileContent(body = classes, pkg = pkg))
    }
}

private fun fileContent(
    annotations: String = "",
    body: String,
    pkg: Package,
) =
    sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        // "package mui.${pkg.name}",
        "package mui.material",
        DEFAULT_IMPORTS,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")
