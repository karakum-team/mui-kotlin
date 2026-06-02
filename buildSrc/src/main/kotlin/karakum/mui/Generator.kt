package karakum.mui

import java.io.File

private const val GENERATOR_COMMENT = "Automatically generated - do not modify!"

private val DEFAULT_IMPORTS = listOf(
    "Modifier" to "popper.core.Modifier",

    "Promise" to "js.promise.Promise",
    "SlideDirection" to "mui.material.SlideDirection",

    "ReadonlyArray" to "js.array.ReadonlyArray",
    "Record<" to "js.objects.Record",
    "recordOf(" to "js.objects.recordOf",
    "unsafeCast(" to "js.reflect.unsafeCast",
    "Tuple2" to "js.array.Tuple2",
    "Void" to "js.core.Void",

    "ElementId" to "web.dom.ElementId",
    "Element" to "web.dom.Element",
    "InputType" to "web.html.InputType",
    "ButtonType" to "web.html.ButtonType",
    "Hidden?" to "web.html.Hidden",

    " ClassName" to "web.cssom.ClassName",
    "Event" to "web.events.Event",
    "HTMLElement" to "web.html.HTMLElement",

    "BoxProps" to "mui.system.BoxProps",
    "InitColorSchemeScriptProps" to "mui.system.InitColorSchemeScriptProps",
    "SystemProps" to "mui.system.SystemProps",
    "UseMediaQueryOptions" to "mui.system.UseMediaQueryOptions",
    "Breakpoints" to "mui.system.Breakpoints",

    "JsVirtual" to "seskar.js.JsVirtual",
    "JsValue" to "seskar.js.JsValue",
)

// language=kotlin
private val TYPES_PROPS_WITH_COMPONENT = """
external interface PropsWithComponent : react.Props {
    var component: react.ElementType<*>?
}
""".trimIndent()

// language=kotlin
private val SYSTEM_ALIASES = """
typealias Union = String
""".trimIndent()

// language=kotlin
private val SYSTEM_SX_PROPS = """
@Suppress("UNUSED_TYPEALIAS_PARAMETER")
typealias SxProps<T> = react.CSSProperties
""".trimIndent()

// language=kotlin
private val SYSTEM_PROPS_WITH_SX = """
import react.Props    
    
external interface PropsWithSx : Props {
    var sx: SxProps<Theme>?
}
""".trimIndent()

// language=kotlin
private val SYSTEM_SX = """
import csstype.PropertiesBuilder
import js.objects.unsafeJso

inline fun PropsWithSx.sx(
    crossinline block: PropertiesBuilder.() -> Unit,
) {
    sx = unsafeJso(block)
}
""".trimIndent()

// language=kotlin
private val SYSTEM_RESPONSIVE_STYLE_VALUE = """
external interface ResponsiveStyleValue<T : Any>

inline fun <T : Any> responsive(
    value: T,
): ResponsiveStyleValue<T> =
    unsafeCast(value)

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
fun <T : Any, R> responsive(
    vararg values: Pair<Breakpoint, T>,
): R where R : T,
           R : ResponsiveStyleValue<T> =
    unsafeCast(recordOf(pairs = values))
""".trimIndent()

// language=kotlin
private val SYSTEM_STANDARD_PROPS = """
external interface StandardProps:
    react.PropsWithStyle,
    react.PropsWithClassName
""".trimIndent()

// language=kotlin
private val SYSTEM_SPACING_STUB = """
external interface Spacing
""".trimIndent()

// language=kotlin
private val SYSTEM_SYSTEM_PROPS_STUB = """
@Suppress("UNUSED_TYPEALIAS_PARAMETER")
typealias SystemProps<T> = react.Props
""".trimIndent()

private val SYSTEM_BREAKPOINT = convertUnion("Breakpoint = 'xs' | 'sm' | 'md' | 'lg' | 'xl'")!!

// language=kotlin
private val SYSTEM_SHAPE = """
external interface Shape {
    var borderRadius: web.cssom.BorderRadius
}

typealias ShapeOptions = Shape
""".trimIndent()

private val MATERIAL_PALETTE_MODE = convertUnion("PaletteMode = 'light' | 'dark'")!!
private val BASE_ORIENTATION = convertUnion("Orientation = 'horizontal' | 'vertical'")!!

// language=kotlin
private val MATERIAL_ORIENTATION = """
typealias Orientation = mui.base.Orientation
""".trimIndent()

// language=kotlin
private val STYLE_TRANSITION_CREATE_OPTIONS = """
external interface TransitionCreateOptions {
    var duration: Number?
    var easing: String?
    var delay: Number?    
}
""".trimIndent()

// language=kotlin
private val MATERIAL_SIZE = """
sealed external interface Size {
    companion object {
        @JsValue("small")
        val small: small

        @JsValue("medium")
        val medium: medium

        @JsValue("normal")
        val normal: normal

        @JsValue("large")
        val large: large
    }

    sealed interface small : Size, BaseSize, NormalSize
    sealed interface medium : Size, BaseSize
    sealed interface normal : NormalSize
    sealed interface large : Size
}

sealed external interface BaseSize
sealed external interface NormalSize
""".trimIndent()

// language=kotlin
private val TRANSITIONS_STUBS = """
external interface TransitionProps: react.Props
""".trimIndent()

// language=kotlin
private val PICKERS_STUBS = """
typealias PickerSelectionState = String

typealias PickerOnChangeFn<TDate> = (
    date: TDate?, 
    selectionState: PickerSelectionState?,
) -> Unit
""".trimIndent()

private val CALENDAR_PICKER_VIEW = convertUnion("CalendarPickerView = 'year' | 'day' | 'month'")!!
private val CLOCK_PICKER_VIEW = convertUnion("ClockPickerView = 'hours' | 'minutes' | 'seconds'")!!

private val HAS_PROP_TYPES_IN_SEPARATE_FILES = setOf(
    "NoSsr",
    "Portal",
    "TextareaAutosize",
)

private val EXCLUDED_TYPES = setOf(
    "CalendarPicker",
    "CalendarPickerSkeleton",
    "ClockPicker",
    "DatePicker",
    "DateTimePicker",
    "DesktopDatePicker",
    "DesktopDateTimePicker",
    "DesktopTimePicker",
    "LocalizationProvider",
    "MobileDatePicker",
    "MobileDateTimePicker",
    "MobileTimePicker",
    "MonthPicker",
    "PickersDay",
    "StaticDatePicker",
    "StaticDateTimePicker",
    "StaticTimePicker",
    "TimePicker",
    "YearPicker",

    "DateRangePicker",
    "DateRangePickerDay",
    "DesktopDateRangePicker",
    "MobileDateRangePicker",
    "StaticDateRangePicker",

    // TODO: fix
    "Timeline"
)

private enum class Package(
    id: String? = null,
    pkg: String? = null,
) {
    types,
    base,
    material,
    materialStyles("material/styles"),
    materialTransitions,
    iconsMaterial("icons-material"),
    system,
    pickers("x-date-pickers", "muix.pickers"),
    treeView("x-tree-view", "muix.tree.view"),
    lab,

    dateioCore("", "dateio.core"),

    ;

    val id = id ?: name

    val pkg: String = pkg ?: ("mui." + name.replace(Regex("""[A-Z]""")) {
        "." + it.value.lowercase()
    })
}

fun generateKotlinDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    generateTypesDeclarations(sourceDir)
    generateBaseDeclarations(typesDir.resolve("base"), sourceDir)
    generateSystemDeclarations(typesDir.resolve("system"), sourceDir)
    generateMaterialDeclarations(typesDir.resolve("material"), sourceDir)
    generateIconsMaterialDeclarations(typesDir.resolve("icons-material"), sourceDir)
    generateStylesDeclarations(typesDir.resolve("material/styles"), sourceDir)
    generateTransitionsDeclarations(sourceDir)
    generateLabDeclarations(typesDir.resolve("lab"), sourceDir)
    generateTreeViewDeclarations(typesDir.resolve("x-tree-view"), sourceDir)
    generatePickersDeclarations(typesDir.resolve("x-date-pickers"), sourceDir)
    generateDeteioDeclarations(typesDir.resolve("../@date-io/core"), sourceDir)
}

private fun generateTypesDeclarations(
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/types")
        .also { it.mkdirs() }

    targetDir.resolve("PropsWithComponent.kt")
        .writeText(fileContent(body = TYPES_PROPS_WITH_COMPONENT, pkg = Package.types))
}

private fun generateBaseDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/base")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() || it.name.isHookName() }
        .filter { it.name != "ClassNameGenerator" }
        .map {
            var name = it.name
            if (name == "AutocompleteUnstyled")
                name = "useAutocomplete"

            it.resolve("$name.d.ts")
        }
        .flatMap { component ->
            val dir = component.parentFile

            val additionalFiles = if (dir.name == "TablePagination")
                dir.existed("common.types.d.ts")
            else
                emptySequence()

            val files = dir.existed(
                "${dir.name}Props.d.ts",
                "${dir.name}.types.d.ts",
                "use${dir.name}Props.d.ts",
                "use${dir.name}.types.d.ts",
                "Use${dir.name}Props.d.ts",
            ) + additionalFiles

            // TODO: Temporary skipping these hooks because there are problems in default function generation
            val ignoredHooksDefaultFiles = setOf(
                "useList",
                "useDropdown",
                "useCompoundItem",
                "useCompoundParent",
                "useCompound",
            )

            when (dir.name) {
                in ignoredHooksDefaultFiles -> files
                "Transitions" -> dir.existed(
                    "CssAnimation.d.ts",
                    "CssTransition.d.ts",
                )

                "useTransition" -> dir.existed(
                    // TODO: Fix incorrect files processing and uncomment
                    // "TransitionContext.d.ts",
                    // "useTransitionStateManager.d.ts",
                    // "useTransitionTrigger.d.ts",
                )

                else -> files + component
            }
        }
        .forEach { generate(it, targetDir, Package.base) }

    sequenceOf(
        "Orientation" to BASE_ORIENTATION,
    ).forEach { (name, body) ->
        targetDir.resolve("$name.kt")
            .writeText(fileContent("", body, Package.base))
    }
}

private fun generateSystemDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/system")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory }
        ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() || it.name.isHookName() }
        .filter { it.name !in setOf("useThemeProps", "RtlProvider", "DefaultPropsProvider", "useThemeWithoutDefault") }
        .map { it.resolve("${it.name}.d.ts") }
        .flatMap { component ->
            val dir = component.parentFile

            dir.existed(
                dir.name + "Props.d.ts",
            ) + component
        }
        .forEach { generate(it, targetDir, Package.system) }

    typesDir.resolve("createTheme")
        .listFiles { file -> file.name.startsWith("create") && file.name.endsWith(".d.ts") }!!
        .filter { it.nameWithoutExtension.removeSuffix(".d") != "createSpacing" }
        .forEach { generate(it, targetDir, Package.system) }

    // MUI v6 moved Breakpoints / BreakpointsOptions out of createTheme.d.ts into createBreakpoints/.
    typesDir.resolve("createBreakpoints")
        .takeIf { it.isDirectory }
        ?.listFiles { file -> file.name.startsWith("create") && file.name.endsWith(".d.ts") }
        ?.forEach { generate(it, targetDir, Package.system) }

    sequenceOf(
        "Breakpoint" to SYSTEM_BREAKPOINT,
        "shape" to SYSTEM_SHAPE,

        "Aliases" to SYSTEM_ALIASES,

        "SxProps" to SYSTEM_SX_PROPS,
        "PropsWithSx" to SYSTEM_PROPS_WITH_SX,
        "sx" to SYSTEM_SX,

        "ResponsiveStyleValue" to SYSTEM_RESPONSIVE_STYLE_VALUE,
        "StandardProps" to SYSTEM_STANDARD_PROPS,
        "Spacing" to SYSTEM_SPACING_STUB,
        "SystemProps" to SYSTEM_SYSTEM_PROPS_STUB,
    ).forEach { (name, body) ->
        targetDir.resolve("$name.kt")
            .writeText(fileContent(body = body, pkg = Package.system))
    }
}

private fun generateMaterialDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("mui/material")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() || it.name == "internal" || it.name.isHookName() }
        .filter {
            it.name !in setOf(
                "useTouchRipple",
                "useAutocomplete",
                "DefaultPropsProvider",
                "useLazyRipple"
            )
        }
        .filter { it.name != "StyledEngineProvider" }
        // OverridableComponent is a TS type-helper utility (call signatures + conditional types
        // that don't translate). Keep it skipped — components that use it are still generated
        // via the OverridableComponent-detection heuristic in Converter.kt.
        .filter { it.name != "OverridableComponent" }
        // v6 ships Pigment-CSS variants alongside default Emotion-based components.
        // They duplicate HiddenProps/GridBaseProps in the same Kotlin package; skip them.
        .filter { !it.name.startsWith("Pigment") }
        .onEach {
            when (it.name) {
                "ButtonBase" -> {
                    val file = it.resolve("TouchRipple.d.ts")
                    generate(file, targetDir, Package.material, true)
                }

                "TablePagination" -> {
                    val file = it.resolve("${it.name}Actions.d.ts")
                    generate(file, targetDir, Package.material, true)
                }

                in HAS_PROP_TYPES_IN_SEPARATE_FILES -> {
                    val file = it.resolve("${it.name}.types.d.ts")
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

    sequenceOf(
        MUI to MUI_BODY,
        "PaletteMode" to MATERIAL_PALETTE_MODE,
        "Size" to MATERIAL_SIZE,
        "Orientation" to MATERIAL_ORIENTATION,
        "LinkBaseProps" to MATERIAL_LINK_BASE_PROPS_STUB,
        "TablePaginationBaseProps" to MATERIAL_TABLE_PAGINATION_BASE_PROPS_STUB,
    ).forEach { (name, body) ->
        targetDir.resolve("$name.kt")
            .writeText(fileContent(body = body, pkg = Package.material))
    }
}

// language=kotlin
private val MATERIAL_LINK_BASE_PROPS_STUB = """
external interface LinkBaseProps : react.Props
""".trimIndent()

// language=kotlin
private val MATERIAL_TABLE_PAGINATION_BASE_PROPS_STUB = """
external interface TablePaginationBaseProps : react.Props
""".trimIndent()

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

        if (name in setOf(
                "createThemeNoVars",
                "createThemeWithVars",
                "createTheme",
                "createColorScheme",
                "ThemeProvider",
                "ThemeProviderNoVars",
                "ThemeProviderWithVars"
            )
        )
            return false

        if (name.startsWith("create"))
            return true

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

    // MUI v6 split `Theme` and `ThemeOptions` definitions across createThemeNoVars/
    // createThemeWithVars/createTheme with complex TS conditional types we skip.
    // Emit minimal stubs so downstream references resolve.
    targetDir.resolve("Theme.kt")
        .writeText(
            fileContent(
                body = """
                    external interface Theme : mui.system.Theme

                    typealias ThemeOptions = mui.system.ThemeOptions
                """.trimIndent(),
                pkg = Package.materialStyles,
            )
        )

    // ThemeProvider.d.ts uses TS conditional types (`extends X ? {...} : {}`) that confuse
    // the generator. Emit a minimal external val + props stub here.
    targetDir.resolve("ThemeProvider.kt")
        .writeText(
            fileContent(
                annotations = "@file:JsModule(\"@mui/material/styles/ThemeProvider\")",
                body = """
                    external interface ThemeProviderProps : react.PropsWithChildren {
                        override var children: react.ReactNode?
                        var theme: Any? /* Partial<Theme> | ((outerTheme: Theme) => Theme) */
                    }

                    @JsName("default")
                    external val ThemeProvider: react.FC<ThemeProviderProps>
                """.trimIndent(),
                pkg = Package.materialStyles,
            )
        )
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
            moduleDeclaration(Package.iconsMaterial, null, null)
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

    val directories = typesDir.listFiles { file -> file.isDirectory }
        ?: return

    directories.asSequence()
        .filter { !it.name.startsWith("Adapter") }
        .filter { it.name !in EXCLUDED_TYPES }
        .filter { it.name.isComponentName() }
        .filter { !it.resolve("${it.name}.d.ts").readText().startsWith("export { default } from ") }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir, Package.lab) }
}

private fun generateTreeViewDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("muix/tree/view")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory }
        ?: return

    directories.asSequence()
        .filter { it.name.isComponentName() || it.name.isHookName() }
        .filter { !it.resolve("${it.name}.d.ts").readText().startsWith("export { default } from ") }
        .filter { it.name !in setOf("TreeItem2", "TreeItem2Icon", "TreeItem2Provider", "useTreeItem2") }
        .onEach {
            when (it.name) {
                "TreeItem" -> {
                    val contentFile = it.resolve("${it.name}Content.d.ts")
                    generate(contentFile, targetDir, Package.treeView)

                    val typesFile = it.resolve("${it.name}.types.d.ts")
                    generate(typesFile, targetDir, Package.treeView)
                }

                "TreeView", "SimpleTreeView", "RichTreeView", "TreeItem2LabelInput", "TreeItem2DragAndDropOverlay" -> {
                    val typesFile = it.resolve("${it.name}.types.d.ts")
                    generate(typesFile, targetDir, Package.treeView)
                }
            }
        }
        .filter { it.name != "TreeItem2DragAndDropOverlay" }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir, Package.treeView) }
}

private fun generatePickersDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("muix/pickers")
        .also { it.mkdirs() }

    val directories = typesDir.listFiles { file -> file.isDirectory } ?: return

    directories.asSequence()
        .filter { !it.name.startsWith("Adapter") }
        .filter { it.name.isComponentName() }
        .filter {
            it.name !in setOf(
                "DateTimeField",
                "TimeField",
                "PickersTextField",
                "PickersShortcuts",
                "PickersLayout"
            )
        }
        .forEach {
            val file = it.resolve("${it.name}.d.ts")
            generate(file, targetDir, Package.pickers)

            if (it.name in setOf(
                    "DateCalendar",
                    "DatePicker",
                    "DateTimeField",
                    "DateTimePicker",
                    "DesktopDatePicker",
                    "DesktopDateTimePicker",
                    "DesktopTimePicker",
                    "DigitalClock",
                    "MobileDatePicker",
                    "MobileDateTimePicker",
                    "MobileTimePicker",
                    "MonthCalendar",
                    "MultiSectionDigitalClock",
                    "PickersCalendarHeader",
                    "PickersLayout",
                    "PickersSectionList",
                    "PickersTextField",
                    "TimeClock",
                    "TimeField",
                    "TimePicker",
                    "YearCalendar"
                )
            ) {
                val typesFile = it.resolve("${it.name}.types.d.ts")
                generate(typesFile, targetDir, Package.pickers)
            }
        }

    sequenceOf(
        "Stubs" to PICKERS_STUBS,
        "CalendarPickerView" to CALENDAR_PICKER_VIEW,
        "ClockPickerView" to CLOCK_PICKER_VIEW,
        DATE_ADAPTER to DATE_ADAPTER_BODY,
    ).forEach { (name, body) ->
        targetDir.resolve("$name.kt")
            .writeText(fileContent(body = body, pkg = Package.pickers))
    }

    DATE_ADAPTERS.forEach { (name, body) ->
        val annotations = moduleDeclaration(
            pkg = Package.pickers,
            subpackage = null,
            componentName = name
        )

        val content = fileContent(
            annotations = annotations,
            body = body,
            pkg = Package.pickers,
        )

        targetDir.resolve("$name.kt")
            .writeText(content)
    }
}

private fun generateDeteioDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    val targetDir = sourceDir.resolve("dateio/core")
        .also { it.mkdirs() }

    generate(
        definitionFile = typesDir.resolve("IUtils.d.ts"),
        targetDir = targetDir,
        pkg = Package.dateioCore,
    )
}

private fun String.isComponentName(): Boolean {
    if ("_" in this)
        return false

    val char = get(0)
    @Suppress("DEPRECATION")
    return char == char.uppercaseChar() && char != char.lowercaseChar()
}

private fun String.isHookName(): Boolean {
    return startsWith("use")
}

private fun moduleDeclaration(
    pkg: Package,
    subpackage: String?,
    componentName: String?,
): String {
    val moduleName = sequenceOf(
        "@mui",
        pkg.id,
        subpackage,
        componentName,
    ).filterNotNull()
        .joinToString("/")

    return "@file:JsModule(\"$moduleName\")"
}


private fun generate(
    definitionFile: File,
    targetDir: File,
    pkg: Package,
    fullPath: Boolean = false,
) {
    // MUI v6 sometimes ships only `<Component>/index.d.ts` (e.g. useMediaQuery, OverridableComponent).
    // Fall back to it when the standard `<Component>.d.ts` is missing.
    val actualFile = if (!definitionFile.exists()) {
        val indexFallback = definitionFile.parentFile?.resolve("index.d.ts")
        if (indexFallback != null && indexFallback.exists()) indexFallback else definitionFile
    } else definitionFile

    val componentName = when {
        actualFile.name == "shared.d.ts" -> "CalendarPickerView"
        actualFile.name == "index.d.ts" -> actualFile.parentFile.name
        else -> actualFile.name.removeSuffix(".d.ts")
    }
    val (body, extensions) = convertDefinitions(actualFile)

    val subpackage = when {
        pkg == Package.materialStyles
            -> null

        fullPath || componentName == "SwitchBase" || componentName == "useAutocomplete" || componentName == "useSwitch" || componentName.startsWith(
            "create"
        )
            -> definitionFile.parentFile.name

        else -> null
    }

    val annotations = mutableListOf<String>()
    if ("external val " in body || "external fun " in body)
        annotations += moduleDeclaration(pkg, subpackage, componentName)

    if (componentName == "TextField")
        annotations += "@file:Suppress(\n" +
                "\"VIRTUAL_MEMBER_HIDDEN\",\n" +
                "\"NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE\",\n" +
                ")"

    // Combine into a single @file:Suppress(...) — Kotlin disallows repeating @file:Suppress.
    run {
        val suppressKeys = mutableListOf<String>()
        if (componentName in OVERRIDE_FIX_REQUIRED) suppressKeys += "VIRTUAL_MEMBER_HIDDEN"
        if (componentName in VAR_TYPE_MISMATCH_ON_OVERRIDE_FIX_REQUIRED) suppressKeys += "VAR_TYPE_MISMATCH_ON_OVERRIDE"
        if (suppressKeys.isNotEmpty() && componentName != "TextField") {
            annotations += "@file:Suppress(\n" + suppressKeys.joinToString(",\n") { "\"$it\"" } + ",\n)"
        }
    }

    if (componentName != "CalendarPickerView" && componentName != "createTypography") {
        val finalBody = when {
            componentName == "createTransitions" -> body + "\n\n" + STYLE_TRANSITION_CREATE_OPTIONS
            else -> body
        }

        targetDir.resolve("$componentName.kt")
            .writeText(fileContent(annotations.joinToString("\n\n"), finalBody, pkg))
    }

    // MUI v6 Tooltip uses `placement?: PopperProps['placement']` — no standalone enum to
    // emit. Provide a typealias to popper's Placement for ergonomic Kotlin usage.
    // Rating: typed accessor for `defaultValue` (widened to Any? to satisfy Kotlin diamond).
    val extensionsBody = when (componentName) {
        "Tooltip" -> sequenceOf(
            extensions.takeIf { it.isNotEmpty() },
            "typealias TooltipPlacement = popper.core.Placement",
        ).filterNotNull().joinToString("\n\n")

        "Rating" -> sequenceOf(
            extensions.takeIf { it.isNotEmpty() },
            """
                inline var RatingProps.defaultValueAsNumber: Number?
                    get() = js.reflect.unsafeCast(defaultValue)
                    set(value) { defaultValue = value }
            """.trimIndent(),
        ).filterNotNull().joinToString("\n\n")

        // StepIcon: SvgIconOwnProps stripped from extends (HTMLAttributes T-param diamond).
        // Provide typed accessor so consumers can opt in to SvgIcon API.
        "StepIcon" -> sequenceOf(
            extensions.takeIf { it.isNotEmpty() },
            """
                inline fun StepIconProps.asSvgIconOwnProps(): SvgIconOwnProps =
                    js.reflect.unsafeCast(this)
            """.trimIndent(),
        ).filterNotNull().joinToString("\n\n")

        else -> extensions
    }

    if (extensionsBody.isNotEmpty() && componentName != "Stepper") {
        val fileName = "$componentName.ext"

        val finalBody = when (componentName) {
            "createTypography" -> extensionsBody.replace("Variant", "TypographyVariant")
            else -> extensionsBody
        }

        targetDir.resolve("$fileName.kt")
            .writeText(fileContent(body = finalBody, pkg = pkg))
    }

    if (componentName == "RadioGroup")
        return

    val classesFileName = "${componentName}Classes".replaceFirstChar(Char::lowercase)
    val classesFile = definitionFile.parentFile.resolve("$classesFileName.d.ts")

    if (classesFile.exists()) {
        val classes = convertClasses(classesFileName.replaceFirstChar(Char::uppercase), classesFile)
        val annotation = moduleDeclaration(pkg, subpackage, componentName)
            .takeIf { "external val" in classes }
            ?: ""

        targetDir.resolve("$componentName.classes.kt")
            .writeText(fileContent(annotations = annotation, body = classes, pkg = pkg))
    }
}

private fun fileContent(
    annotations: String = "",
    body: String,
    pkg: Package,
): String {
    val (resolvedBody, addedImports) = resolveImportedFqns(body, pkg)
    val defaultImports = DEFAULT_IMPORTS
        .filter { it.first in resolvedBody }
        .map { it.second }
        .plus(systemImports(resolvedBody, pkg))
        .plus(addedImports)
        .distinct()
        .map { "import $it" }
        .joinToString("\n")

    return sequenceOf(
        "// $GENERATOR_COMMENT",
        annotations,
        "package ${pkg.pkg}",
        defaultImports,
        resolvedBody,
    ).filter { it.isNotEmpty() }
        .joinToString("\n\n")
        .removeSuffix("\n") + "\n"
}

// Cross-package types that should appear as short names in body and be imported at the top.
// FQN is replaced with the short name (last segment after `.`); if the FQN's package matches
// the file's package, the prefix is just stripped (no import). Otherwise an `import FQN` is added.
// Negative lookahead `(?![A-Za-z0-9_])` ensures `react.dom.events.MouseEvent` doesn't accidentally
// match inside `react.dom.events.MouseEventHandler`.
private val IMPORTED_FQNS = listOf(
    // mui.base
    "mui.base.BadgeOwnProps",
    "mui.base.ClickAwayListenerProps",
    "mui.base.Orientation",
    "mui.base.PopperOwnProps",
    "mui.base.PopperProps",
    "mui.base.UseAutocompleteProps",

    // mui.material.transitions
    "mui.material.transitions.TransitionProps",

    // mui.types
    "mui.types.PropsWithComponent",

    // mui.system
    "mui.system.ContainerClasses",
    "mui.system.PropsWithSx",
    "mui.system.ResponsiveStyleValue",
    "mui.system.StackClasses",
    "mui.system.StandardProps",
    "mui.system.Union",

    // popper.core
    "popper.core.Instance",
    "popper.core.Modifier",
    "popper.core.Options",
    "popper.core.Placement",

    // react (top-level)
    "react.CSSProperties",
    "react.ComponentType",
    "react.ElementType",
    "react.FC",
    "react.Key",
    "react.Props",
    "react.PropsWithChildren",
    "react.PropsWithClassName",
    "react.PropsWithStyle",
    "react.ReactElement",
    "react.ReactNode",
    "react.Ref",
    "react.RefCallback",
    "react.RefObject",
    "react.StateSetter",

    // react.dom.aria
    "react.dom.aria.AriaRole",

    // react.dom.events (order doesn't matter due to negative-lookahead in replace)
    "react.dom.events.ChangeEvent",
    "react.dom.events.ChangeEventHandler",
    "react.dom.events.EventHandler",
    "react.dom.events.FocusEventHandler",
    "react.dom.events.KeyboardEventHandler",
    "react.dom.events.MouseEvent",
    "react.dom.events.MouseEventHandler",
    "react.dom.events.ReactEventHandler",
    "react.dom.events.SyntheticEvent",

    // react.dom.html
    "react.dom.html.AnchorHTMLAttributes",
    "react.dom.html.ButtonHTMLAttributes",
    "react.dom.html.FieldsetHTMLAttributes",
    "react.dom.html.FormHTMLAttributes",
    "react.dom.html.HTMLAttributes",
    "react.dom.html.ImgHTMLAttributes",
    "react.dom.html.InputHTMLAttributes",
    "react.dom.html.LabelHTMLAttributes",
    "react.dom.html.LiHTMLAttributes",
    "react.dom.html.TableHTMLAttributes",
    "react.dom.html.TdAlign",
    "react.dom.html.TdHTMLAttributes",
    "react.dom.html.TextareaHTMLAttributes",

    // react.dom.svg
    "react.dom.svg.SVGAttributes",

    // js
    "js.array.ReadonlyArray",
    "js.array.Tuple",
    "js.objects.Record",

    // seskar
    "seskar.js.JsValue",

    // csstype
    "csstype.PropertiesBuilder",

    // web
    "web.cssom.BorderRadius",
    "web.cssom.ClassName",
    "web.cssom.Color",
    "web.cssom.MediaQueryList",
    "web.cssom.Transition",
    "web.dom.Element",
    "web.dom.ElementId",
    "web.dom.Node",
    "web.dom.TagName",
    "web.events.Event",
    "web.events.EventTarget",
    "web.html.ButtonType",
    "web.html.HTMLAnchorElement",
    "web.html.HTMLButtonElement",
    "web.html.HTMLDivElement",
    "web.html.HTMLElement",
    "web.html.HTMLFieldSetElement",
    "web.html.HTMLFormElement",
    "web.html.HTMLHRElement",
    "web.html.HTMLImageElement",
    "web.html.HTMLInputElement",
    "web.html.HTMLLIElement",
    "web.html.HTMLLabelElement",
    "web.html.HTMLParagraphElement",
    "web.html.HTMLSpanElement",
    "web.html.HTMLTableCellElement",
    "web.html.HTMLTableElement",
    "web.html.HTMLTableRowElement",
    "web.html.HTMLTableSectionElement",
    "web.html.HTMLTextAreaElement",
    "web.html.HTMLUListElement",
    "web.html.Hidden",
    "web.html.InputType",
    "web.svg.SVGSVGElement",
    "web.uievents.UIEvent",
    "web.window.Window",
)

private fun resolveImportedFqns(
    body: String,
    pkg: Package,
): Pair<String, List<String>> {
    var rewritten = body
    val imports = mutableListOf<String>()
    for (fqn in IMPORTED_FQNS) {
        val shortName = fqn.substringAfterLast(".")
        val fqnPkg = fqn.substringBeforeLast(".")
        val pattern = Regex(Regex.escape(fqn) + "(?![A-Za-z0-9_])")
        // Skip lines that are `import ...` statements — those are intentional inline imports in
        // hand-written stub templates (e.g. SYSTEM_PROPS_WITH_SX, SYSTEM_SX). Rewriting their
        // FQN would produce invalid `import ShortName` lines.
        val anyNonImportMatch = rewritten.lineSequence().any { line ->
            !line.trimStart().startsWith("import ") && pattern.containsMatchIn(line)
        }
        if (!anyNonImportMatch) continue
        // Skip when the file declares a local type with the same short name — replacement would
        // shadow the local declaration with the imported one (e.g. createPalette.kt declares
        // `external interface Color`, which intentionally hides `web.cssom.Color`).
        val localDeclRegex = Regex(
            """(?:(?:sealed )?external (?:interface|class)|typealias)\s+""" + Regex.escape(shortName) + """(?![A-Za-z0-9_])"""
        )
        if (localDeclRegex.containsMatchIn(rewritten)) continue
        rewritten = rewritten.lineSequence().joinToString("\n") { line ->
            if (line.trimStart().startsWith("import ")) line
            else pattern.replace(line, shortName)
        }
        if (fqnPkg != pkg.pkg) {
            imports.add(fqn)
        }
    }
    return rewritten to imports
}

private fun systemImports(
    body: String,
    pkg: Package,
): Sequence<String> =
    if ("SxProps<Theme>" in body && pkg != Package.system) {
        sequenceOf(
            "mui.material.styles.Theme",
            "mui.system.SxProps",
        )
    } else emptySequence()
