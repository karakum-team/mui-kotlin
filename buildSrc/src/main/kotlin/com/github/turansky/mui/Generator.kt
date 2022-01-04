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
@Suppress("UNUSED_TYPEALIAS_PARAMETER")
typealias SxProps<T> = react.CSSProperties

external interface ResponsiveStyleValue<T: Any>

external interface StandardProps: 
    react.PropsWithStyle,
    react.PropsWithClassName
""".trimIndent()

private val SYSTEM_BREAKPOINT = convertUnion("Breakpoint = 'xs' | 'sm' | 'md' | 'lg' | 'xl'")!!

// language=Kotlin
private val SYSTEM_SHAPE = """
external interface Shape {
    var borderRadius: csstype.BorderRadius
}

typealias ShapeOptions = Shape
""".trimIndent()

// language=Kotlin
private val MATERIAL_STUBS = """
import org.w3c.dom.HTMLTableCellElement
import react.dom.html.HTMLAttributes

typealias TableCellBaseProps = HTMLAttributes<HTMLTableCellElement>
""".trimIndent()

// language=Kotlin
private val MATERIAL_SIZE = """
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
// language=JavaScript
@JsName("(/*union*/{small: 'small', medium: 'medium', large: 'large'}/*union*/)")
sealed external interface Size {
    object small : Size, BaseSize
    object medium : Size, BaseSize
    object large : Size
}

sealed external interface BaseSize
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

private val BASE_TYPES = setOf(
    "ClickAwayListener",
    "NoSsr",
    "Portal",
    "TextareaAutosize",
)

private val EXCLUDED_TYPES = setOf(
    "Timeline",
    "YearPicker",
)

private enum class Package(
    id: String? = null,
) {
    base,
    material,
    materialStyles("material/styles"),
    materialTransitions,
    iconsMaterial("icons-material"),
    lab,
    system,

    ;

    val id = id ?: name

    val pkg: String
        get() = name.replace(Regex("""[A-Z]""")) { "." + it.value.toLowerCase() }
}

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateBaseDeclarations(typesDir.resolve("base"), sourceDir)
    generateSystemDeclarations(typesDir.resolve("system"), sourceDir)
    generateMaterialDeclarations(typesDir.resolve("material"), sourceDir)
    generateStylesDeclarations(typesDir.resolve("material/styles"), sourceDir)
    generateTransitionsDeclarations(sourceDir)
    generateLabDeclarations(typesDir.resolve("lab"), sourceDir)
}

fun generateKotlinIconsDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateIconsMaterialDeclarations(typesDir.resolve("icons-material"), sourceDir)
}

private val TEMP_BASE_EXCLUDED = setOf(
    "ButtonUnstyled",
    "FormControlUnstyled",
    "ModalUnstyled",
    "PopperUnstyled",
    "SliderUnstyled",
    "TabUnstyled",
    "TabsUnstyled",
)

private fun generateBaseDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/base")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() }
        .filter { it.name !in TEMP_BASE_EXCLUDED }
        .map {
            var name = it.name
            if (name == "AutocompleteUnstyled")
                name = "useAutocomplete"

            it.resolve("$name.d.ts")
        }
        .flatMap { component ->
            val dir = component.parentFile
            sequenceOf(
                dir.resolve(dir.name + "Props.d.ts"),
                dir.resolve("Use" + dir.name.removeSuffix("Unstyled") + "Props.d.ts"),
            ).filter { it.exists() }
                .plus(component)
        }
        .forEach { generate(it, targetDir, Package.base) }
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

    targetDir.resolve("Breakpoint.kt")
        .writeText(fileContent(body = SYSTEM_BREAKPOINT, pkg = Package.system))

    targetDir.resolve("shape.kt")
        .writeText(fileContent(body = SYSTEM_SHAPE, pkg = Package.system))

    typesDir.resolve("createTheme")
        .listFiles { file -> file.name.startsWith("create") && file.name.endsWith(".d.ts") }!!
        .forEach { generate(it, targetDir, Package.system) }

    generate(typesDir.resolve("useTheme.d.ts"), targetDir, Package.system)

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
        .filter { it.name.isComponentName() || it.name == "internal" || it.name == "useMediaQuery" || it.name == "usePagination" }
        .filter { it.name !in BASE_TYPES }
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

    targetDir.resolve("Size.kt")
        .writeText(fileContent(body = MATERIAL_SIZE, pkg = Package.material))

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = MATERIAL_STUBS, pkg = Package.material))
}

private fun generateStylesDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/material/styles")
        .also { it.mkdirs() }

    fun isStyleDefinition(fileName: String): Boolean {
        val name = fileName.removeSuffix(".d.ts")
            .takeIf { it != fileName }
            ?: return false

        if (name.startsWith("create"))
            return name != "createTypography"

        return when (name) {
            "ThemeProvider",
            "useTheme",
            "zIndex",
            -> true

            else -> false
        }
    }

    typesDir.listFiles { file -> isStyleDefinition(file.name) }!!
        .forEach { generate(it, targetDir, Package.materialStyles) }
}


private fun generateTransitionsDeclarations(
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/material/transitions")
        .also { it.mkdirs() }

    targetDir.resolve("Stubs.kt")
        .writeText(fileContent(body = TRANSITIONS_STUBS, pkg = Package.materialTransitions))
}

private fun generateIconsMaterialDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/icons/material")
        .also { it.mkdirs() }

    for ((name, body) in convertIcons(typesDir.resolve("index.d.ts"))) {
        val annotations = if (name != "SvgIconComponent") {
            moduleDeclaration(Package.iconsMaterial, null, name)
        } else ""

        targetDir.resolve("$name.kt")
            .writeText(fileContent(annotations, body, Package.iconsMaterial))
    }
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
        pkg.id,
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

    val subpackage = when {
        pkg == Package.materialStyles
        -> null

        fullPath || componentName == "SwitchBase" || componentName == "useAutocomplete" || componentName.startsWith("create")
        -> definitionFile.parentFile.name

        else -> null
    }

    var annotations = moduleDeclaration(pkg, subpackage, componentName)

    if (componentName == "TextField")
        annotations += "\n\n@file:Suppress(\n" +
                "\"VIRTUAL_MEMBER_HIDDEN\",\n" +
                "\"NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE\",\n" +
                ")"

    if (componentName in OVERRIDE_FIX_REQUIRED)
        annotations += "\n\n@file:Suppress(\n\"VIRTUAL_MEMBER_HIDDEN\",\n)"

    if (componentName != "CalendarPickerView") {
        targetDir.resolve("$componentName.kt")
            .writeText(fileContent(annotations, body, pkg))
    }

    if (extensions.isNotEmpty()) {
        val fileName = when (componentName) {
            "Stepper" -> "Orientation"
            else -> "$componentName.ext"
        }

        targetDir.resolve("$fileName.kt")
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
): String {
    val defaultImports = if ("ReadonlyArray" in body) {
        DEFAULT_IMPORTS
    } else ""

    return sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        "package mui.${pkg.pkg}",
        defaultImports,
        body,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")
        .removeSuffix("\n") + "\n"
}
