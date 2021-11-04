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
""".trimIndent()

// language=Kotlin
private val TRANSITIONS_STUBS = """
external interface TransitionProps: react.Props
""".trimIndent()

// language=Kotlin
private val LAB_STUBS = """
typealias PickerSelectionState = String

typealias PickerOnChangeFn<TDate> = (
    date: TDate?, 
    selectionState: PickerSelectionState?,
) -> Unit
""".trimIndent()

private val CORE_TYPES = setOf(
    "ClickAwayListener",
    "NoSsr",
    "Popper",
    "Portal",
    "TextareaAutosize",
)

private val EXCLUDED_TYPES = setOf(
    "Timeline",
    "YearPicker",
)

private enum class Package {
    core,
    material,
    materialTransitions,
    lab,
    system,

    ;

    val pkg: String
        get() = name.replace(Regex("""[A-Z]""")) { "." + it.value.toLowerCase() }
}

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateCoreDeclarations(typesDir.resolve("core"), sourceDir)
    generateSystemDeclarations(typesDir.resolve("system"), sourceDir)
    generateMaterialDeclarations(typesDir.resolve("material"), sourceDir)
    generateTransitionsDeclarations(sourceDir)
    generateLabDeclarations(typesDir.resolve("lab"), sourceDir)
}

private fun generateCoreDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/core")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name in CORE_TYPES }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir, Package.core) }
}

private fun generateSystemDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/system")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir, Package.system) }

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
        .filter { it.name !in CORE_TYPES }
        .filter { it.name != "StyledEngineProvider" }
        .onEach {
            when (it.name) {
                "TablePagination" -> {
                    val file = it.resolve("${it.name}Actions.d.ts")
                    generate(file, targetDir, Package.material, true)
                }
            }
        }
        .map {
            val fileName = when (it.name) {
                "internal" -> "SwitchBase.d.ts"
                else -> "${it.name}.d.ts"
            }
            it.resolve(fileName)
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

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = LAB_STUBS, pkg = Package.lab))
}

private fun String.isComponentName(): Boolean {
    if ("_" in this)
        return false

    val char = get(0)
    return char == char.toUpperCase() && char != char.toLowerCase()
}

private fun moduleDeclaration(
    pkg: Package,
    subpackage: String?,
    componentName: String,
): String {
    val moduleName = sequenceOf(
        "@mui",
        pkg.name,
        subpackage,
        componentName,
    ).filterNotNull()
        .joinToString("/")

    return "@file:JsModule(\"$moduleName\")\n@file:JsNonModule"
}


private fun generate(
    definitionFile: File,
    targetDir: File,
    pkg: Package,
    fullPath: Boolean = false,
) {
    val componentName = when (definitionFile.name) {
        "shared.d.ts" -> "CalendarPickerView"
        else -> definitionFile.name.substringBefore(".")
    }
    val (body, extensions) = convertDefinitions(definitionFile)

    val subpackage = if (fullPath || componentName == "SwitchBase") {
        definitionFile.parentFile.name
    } else null

    var annotations = moduleDeclaration(pkg, subpackage, componentName)

    if (componentName in OVERRIDE_FIX_REQUIRED)
        annotations += "\n\n@file:Suppress(\n\"VIRTUAL_MEMBER_HIDDEN\",\n)"

    if (componentName != "CalendarPickerView") {
        targetDir.resolve("$componentName.kt")
            .writeText(fileContent(annotations, body, pkg))
    }

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
