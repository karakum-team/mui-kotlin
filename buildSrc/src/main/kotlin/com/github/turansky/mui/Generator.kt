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
private val SYSTEM_ALIASES = """
typealias Union = String
""".trimIndent()

// language=Kotlin
private val SYSTEM_STUBS = """
external interface Theme

@Suppress("UNUSED_TYPEALIAS_PARAMETER")
typealias SxProps<T> = react.CSSProperties

external interface ResponsiveStyleValue<T: Any>

external interface StandardProps: react.PropsWithClassName{
    var style: react.CSSProperties?
}
""".trimIndent()


// language=Kotlin
private val MATERIAL_STUBS = """
external interface TableCellBaseProps: react.PropsWithChildren
external interface TablePaginationActionsProps: react.Props
""".trimIndent()

// language=Kotlin
private val TRANSITIONS_STUBS = """
external interface TransitionProps: react.Props
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
    materialTransitions,
    lab,
    system,

    ;

    val pkg: String
        get() = name.replace(Regex("""[A-Z]""")) { "." + it.value.toLowerCase() }

    val path: String
        get() = name.replace(Regex("""[A-Z]""")) { "/" + it.value.toLowerCase() }
}

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateSystemDeclarations(sourceDir)
    generateMaterialDeclarations(typesDir.resolve("material"), sourceDir)
    generateTransitionsDeclarations(sourceDir)
    generateLabDeclarations(typesDir.resolve("lab"), sourceDir)
}

private fun generateSystemDeclarations(
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/system")
        .also { it.mkdirs() }

    targetDir.resolve("Aliases.kt")
        .writeText(fileContent(body = SYSTEM_ALIASES, pkg = Package.system))

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = SYSTEM_STUBS, pkg = Package.system))
}


private fun generateMaterialDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/material")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() || it.name == "internal" }
        .filter { it.name != "StyledEngineProvider" }
        .map {
            val fileName = when (it.name) {
                "internal" -> "SwitchBase.d.ts"
                else -> "${it.name}.d.ts"
            }
            if (it.name !in CORE_ALIASES) {
                it
            } else {
                it.parentFile.parentFile
                    .resolve("core")
                    .resolve(it.name)
            }.resolve(fileName)
        }
        .forEach { generate(it, targetDir, Package.material) }

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = MATERIAL_STUBS, pkg = Package.material))
}


private fun generateTransitionsDeclarations(
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/material/transitions")
        .also { it.mkdirs() }

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = TRANSITIONS_STUBS, pkg = Package.materialTransitions))
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

                "CalendarPicker" -> {
                    val file = it.resolve("shared.d.ts")
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

    var annotations = moduleDeclaration(pkg, componentName)
    if (componentName in OVERRIDE_FIX_REQUIRED)
        annotations += "\n\n@file:Suppress(\n\"VIRTUAL_MEMBER_HIDDEN\",\n)"

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
        "package mui.${pkg.pkg}",
        DEFAULT_IMPORTS,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")
