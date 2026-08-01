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

// PickerValidDate:
//  MUI-X v9 opaque date model types (from @mui/x-date-pickers/models). `PickerValidDate` is a conditional
//  type (`keyof PickerValidDateLookup extends never ? any : …`) resolved by the configured date adapter via
//  module augmentation — `Any` is the faithful default. Kept as NAMED aliases so signatures read
//  `PickerValidDate` / `PickerValue`, not bare `Any`.

// PickerOwnerState:
//  Shared owner-state of every Picker subcomponent (from @mui/x-date-pickers/models). Restored as a real
//  interface so DigitalClockOwnerState / MonthButtonOwnerState / … keep extending it.
// language=kotlin
private val PICKERS_STUBS = """
typealias PickerSelectionState = String

typealias PickerOnChangeFn = (
    date: PickerValidDate?,
    selectionState: PickerSelectionState?,
) -> Unit

typealias PickerValidDate = Any

typealias PickerValue = Any?

typealias PickerVariant = String

typealias PickerOrientation = String

typealias TimeView = String /* 'hours' | 'minutes' | 'seconds' */

typealias DateView = String /* 'year' | 'month' | 'day' */

external interface PickerOwnerState {
    var isPickerValueEmpty: Boolean
    var isPickerOpen: Boolean
    var isPickerDisabled: Boolean
    var isPickerReadOnly: Boolean
    var pickerVariant: PickerVariant
    var pickerOrientation: PickerOrientation
}
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
    "Timeline",

    // MUI-X v9: lab no longer re-exports these tree-view components cleanly — the generated lab
    // re-export points at `muix.tree.view.TreeViewProps`, which v9 x-tree-view no longer provides
    // (see MUI_V9_TODO.md).
    "TreeView",
    "TreeItem",
)

private enum class Package(
    id: String? = null,
    pkg: String? = null,
    // npm scope the declarations are imported from. Defaults to `@mui`; Base UI lives in its own
    // scope (`@base-ui/react`), so this must not be hardcoded in `moduleDeclaration`.
    val scope: String = "@mui",
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

    dateioCore("", "dateio.core", scope = "@date-io"),

    // Base UI is a standalone library, not part of MUI, so it gets its own top-level package rather
    // than living under `mui.*`. `id` is empty because the npm subpath is the module name
    // (`@base-ui/react/menu`): `moduleDeclaration` is given that segment as its `subpackage` instead,
    // see `generateBaseUiDeclarations`.
    baseUi("", "baseui", scope = "@base-ui/react"),

    ;

    val id = id ?: name

    val pkg: String = pkg ?: ("mui." + name.replace(Regex("""[A-Z]""")) {
        "." + it.value.lowercase()
    })
}

fun generateKotlinDeclarations(
    nodeModulesDir: File,
    sourceDir: File,
) {
    val muiDir = nodeModulesDir.resolve("@mui")

    generateTypesDeclarations(sourceDir)
    generateBaseDeclarations(muiDir.resolve("base"), sourceDir)
    generateSystemDeclarations(muiDir.resolve("system"), sourceDir)
    generateMaterialDeclarations(muiDir.resolve("material"), sourceDir)
    generateIconsMaterialDeclarations(muiDir.resolve("icons-material"), sourceDir)
    generateStylesDeclarations(muiDir.resolve("material/styles"), sourceDir)
    generateTransitionsDeclarations(sourceDir)
    generateLabDeclarations(muiDir.resolve("lab"), sourceDir)
    generateTreeViewDeclarations(muiDir.resolve("x-tree-view"), sourceDir)
    generatePickersDeclarations(muiDir.resolve("x-date-pickers"), sourceDir)
    generateDeteioDeclarations(nodeModulesDir.resolve("@date-io/core"), sourceDir)
    generateBaseUiDeclarations(nodeModulesDir.resolve("@base-ui/react"), sourceDir)
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
        // ThemeProvider is hand-written below: v9 made it generic (`ThemeProviderProps<Theme>` +
        // `function ThemeProvider<T>(…)`), which the converter can't turn into a component `val` — it
        // emitted only the props interface, dropping `external val ThemeProvider`. Mirror the material
        // ThemeProvider stub instead (see generateStyleDeclarations).
        .filter {
            it.name !in setOf(
                "useThemeProps",
                "RtlProvider",
                "DefaultPropsProvider",
                "useThemeWithoutDefault",
                "ThemeProvider"
            )
        }
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

    // v9 `@mui/system/ThemeProvider` is still exported, but as a generic function
    // (`ThemeProviderProps<Theme = DefaultTheme>` + `function ThemeProvider<T>(…)`) which the converter
    // can't recognize as a component — it emitted only the props interface and dropped the `val`. Emit a
    // minimal stub (props + val), mirroring the material ThemeProvider stub in generateStyleDeclarations.
    targetDir.resolve("ThemeProvider.kt")
        .writeText(
            fileContent(
                annotations = "@file:JsModule(\"@mui/system/ThemeProvider\")",
                body = """
                    external interface ThemeProviderProps : react.PropsWithChildren {
                        /**
                         * Your component tree.
                         */
                        override var children: react.ReactNode?

                        /**
                         * The design system's unique id for getting the corresponded theme when there are multiple design systems.
                         */
                        var themeId: String?

                        /**
                         * A theme object. You can provide a function to extend the outer theme.
                         */
                        var theme: Any? /* Partial<Theme> | ((outerTheme: Theme) => Theme) */
                    }

                    @JsName("default")
                    external val ThemeProvider: react.FC<ThemeProviderProps>
                """.trimIndent(),
                pkg = Package.system,
            )
        )
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
                "useLazyRipple",
                "GridLegacy",
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
        .filter {
            it.name !in setOf(
                "TreeItem2", "TreeItem2Icon", "TreeItem2Provider", "useTreeItem2",
            )
        }
        .onEach {
            when (it.name) {
                "TreeItem" -> {
                    val contentFile = it.resolve("${it.name}Content.d.ts")
                    generate(contentFile, targetDir, Package.treeView)

                    val typesFile = it.resolve("${it.name}.types.d.ts")
                    generate(typesFile, targetDir, Package.treeView)
                }

                "TreeView", "SimpleTreeView", "RichTreeView", "TreeItemLabelInput", "TreeItem2DragAndDropOverlay" -> {
                    val typesFile = it.resolve("${it.name}.types.d.ts")
                    generate(typesFile, targetDir, Package.treeView)
                }
            }
        }
        .filter { it.name != "TreeItem2DragAndDropOverlay" }
        .map { it.resolve("${it.name}.d.ts") }
        .forEach { generate(it, targetDir, Package.treeView) }

    // MUI-X v9: type-only `internals/` slot bases that the public slot interfaces extend
    // (SimpleTreeViewSlots/RichTreeViewSlots → TreeViewSlots; TreeItemSlots → TreeItemIconSlots, …).
    // Generated with typesOnly to restore that inheritance without emitting their `declare const`
    // Context/Memo vals. See MUI_V9_TODO.md "5c".
    // NB: `internals/components/RichTreeViewItems.d.ts` (RichTreeViewItemsSlots) is NOT generated — the
    // file's internal `RichTreeViewItemsProps` drags in a `<TProps>` generic, a `Ref<…>` and slot
    // overrides that don't translate. RichTreeViewItemsSlots stays rejected; RichTreeViewSlots keeps its
    // TreeViewSlots inheritance but not RichTreeViewItemsSlots. See MUI_V9_TODO.md "5c".
    sequenceOf(
        "internals/TreeViewProvider/TreeViewStyleContext.d.ts",
        "TreeItemIcon/TreeItemIcon.types.d.ts",
    ).forEach { rel ->
        val file = typesDir.resolve(rel)
        if (file.exists()) generate(file, targetDir, Package.treeView, typesOnly = true)
    }
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
                    "PickerDay",
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

    // MUI-X v9: type-only source files living OUTSIDE the component directories that the picker props
    // aggregate via `extends` (DayCalendar slots + day props, date-validation props, view options).
    // Generating them restores the inheritance that would otherwise be dropped. See MUI_V9_TODO.md "5c".
    // NB: `internals/hooks/useViews.d.ts` (ExportedUseViewsOptions → views/openTo) is intentionally NOT
    // generated: the sibling `UseViewsOptions.onChange` has optional function-type params
    // (`selectionState?: …`) that Kotlin function types can't express. ExportedUseViewsOptions stays in
    // INTERNAL_REJECTED_PARENTS; DateCalendar loses only `views`/`openTo`/`onViewChange`. See MUI_V9_TODO.
    sequenceOf(
        "DateCalendar/DayCalendar.d.ts",
        "internals/models/validation.d.ts",
        "validation/validateDate.d.ts",
    ).forEach { rel ->
        val file = typesDir.resolve(rel)
        if (file.exists()) generate(file, targetDir, Package.pickers, typesOnly = true)
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

// Base UI modules covered so far, out of the 44 public ones. Kept as an explicit allow-list rather
// than scanning the `exports` map, so that what is generated stays reviewable as the target grows.
private val BASE_UI_MODULES = setOf(
    "menu",
    "slider",
)

/**
 * `.d.ts` generated for their declarations alone, without their module being in [BASE_UI_MODULES].
 *
 * A part can inherit from a declaration that lives in another module: `SliderRootState extends
 * FieldRootState`, and `FieldRootState` is declared in `field/`. Unlike the dotted namespace members
 * that [NAMESPACE_STUBS] stands in for, a bare identifier passes `isAcceptableParent`, so the parent is
 * *kept* and the generated Kotlin fails to compile against a name that was never emitted.
 *
 * Generating the file it comes from is preferable to a hand-written stub: the declaration stays the
 * upstream one, and when the module does join [BASE_UI_MODULES] the file is deduplicated by path in
 * [generateBaseUiDeclarations] rather than colliding with a stub of the same name. The file is
 * converted exactly as a part is, `.ext.kt` helpers included; what it does *not* get is the namespace
 * object, which is built per module and so needs the module itself to be listed.
 *
 * `FieldRootState` is inherited by 21 declarations across 12 modules (checkbox, switch, radio, select,
 * number-field, combobox, …) — 17 by that name and four through `FieldRoot.State` — so this entry pays
 * for itself well beyond `slider`.
 */
private val BASE_UI_EXTRA_FILES = setOf(
    "field/root/FieldRoot.d.ts",
)

private val BASE_UI_SIDE =
    convertUnion("Side = 'top' | 'bottom' | 'left' | 'right' | 'inline-end' | 'inline-start'")!!
private val BASE_UI_ALIGN = convertUnion("Align = 'start' | 'center' | 'end'")!!
private val BASE_UI_ORIENTATION = convertUnion("Orientation = 'horizontal' | 'vertical'")!!

// `TransitionStatus` is `'starting' | 'ending' | 'idle' | undefined`; the `undefined` arm is the
// optionality of the member, expressed in Kotlin by the `?` on its type.
private val BASE_UI_TRANSITION_STATUS =
    convertUnion("TransitionStatus = 'starting' | 'ending' | 'idle'")!!

// Types shared by every Base UI part that cannot be translated from their `.d.ts` and so are written
// by hand. Each one states why.
// language=kotlin
private val BASE_UI_STUBS = """
/**
 * `internals/types.d.ts`. Marker parents carrying a single prop; declared twice upstream purely to
 * document opposite defaults (`nativeButton` defaults to `true` for [NativeButtonProps] and to
 * `false` for [NonNativeButtonProps]).
 */
external interface NativeButtonProps {
    var nativeButton: Boolean?
}

external interface NonNativeButtonProps {
    var nativeButton: Boolean?
}

/**
 * `internals/createBaseUIEventDetails.d.ts`. Upstream this is a conditional type over a mapped
 * reason-to-native-event table (`Reason extends string ? BaseUIChangeEventDetail<Reason, …> : never`),
 * which has no Kotlin equivalent — so the members are spelled out here instead.
 *
 * `reason` is left as `String`: the reason sets are per-component (`MenuRootChangeEventReason` and
 * friends are `typeof REASONS.x` unions over `internals/reason-parts.d.ts`), so narrowing it here would
 * be wrong for every other component.
 */
external interface BaseUIChangeEventDetails {
    var reason: String
    var event: web.events.Event
    var isCanceled: Boolean
    var isPropagationAllowed: Boolean
    var trigger: web.dom.Element?

    fun cancel()
    fun allowPropagation()
}

external interface BaseUIGenericEventDetails {
    var reason: String
    var event: web.events.Event
}

/**
 * `utils/useAnchorPositioning.d.ts`. The real interface is ~60 anchor-positioning props (side, align,
 * offsets, collision handling) shared by every Positioner part. Generating it is deferred, so
 * Positioner props currently inherit nothing from it — see BASE_UI_TODO.md.
 */
external interface UseAnchorPositioningSharedParameters

/**
 * `floating-ui-react/components/FloatingPortal.d.ts`, where it is `interface Props<TState>` declared
 * inside the `FloatingPortal` namespace — a shape with no flat declaration to redirect to, and one
 * `floating-ui-react/` is not generated from at all. Written by hand so that `MenuPortalProps`, which
 * extends it, keeps a parent; see `resolveNamespaceStubs` in BaseUi.kt.
 *
 * Upstream it is `BaseUIComponentProps<'div', TState>` plus `container`, so extending
 * [BaseUiDivProps] reproduces the whole surface — `children` above all, without which a portal cannot
 * hold the popup it exists to move.
 */
external interface FloatingPortalProps : BaseUiDivProps {
    /**
     * A parent element to render the portal element into.
     *
     * `Any?` rather than the usual narrowing to the dominant arm: the union is
     * `HTMLElement | ShadowRoot | RefObject<HTMLElement | ShadowRoot | null> | null`, and `ShadowRoot`
     * is not an `Element` while the ref arm is not a node at all, so every candidate narrowing would be
     * wrong for two of the four.
     */
    var container: Any? /* HTMLElement | ShadowRoot | React.RefObject<HTMLElement | ShadowRoot | null> | null */
}
""".trimIndent()

// Kotlin counterpart of Base UI's `BaseUIComponentProps<ElementType, State>` — see
// `resolveComponentProps` in BaseUi.kt for why it cannot be converted from the `.d.ts`.
//
// One interface per intrinsic tag Base UI actually passes (17 in 1.6.0, `div` alone in 126 places).
// Each extends the same `react.dom.html.*` attributes the MUI target already maps tags to
// (IntrinsicType.kt) and adds the three props Base UI replaces them with:
//
//   className?: string | ((state: State) => string | undefined)
//   style?: React.CSSProperties | ((state: State) => React.CSSProperties | undefined)
//   render?: React.ReactElement | ((props: HTMLProps, state: State) => React.ReactElement)
//
// `className` and `style` widen the inherited members, so the file suppresses
// VAR_TYPE_MISMATCH_ON_OVERRIDE. They stay `Any?` because each is a value-or-callback union over the
// part's own state type, which this shared parent does not know; the generated `<Part>.ext.kt` helpers
// give the state-typed form. `render` is `Any?` for the same reason.
// language=kotlin
private val BASE_UI_ELEMENT_PROPS = sequenceOf(
    "a" to "AnchorHTMLAttributes<web.html.HTMLAnchorElement>",
    "button" to "ButtonHTMLAttributes<web.html.HTMLButtonElement>",
    "div" to "HTMLAttributes<web.html.HTMLDivElement>",
    "fieldset" to "FieldsetHTMLAttributes<web.html.HTMLFieldSetElement>",
    "form" to "FormHTMLAttributes<web.html.HTMLFormElement>",
    // h1–h6: only `h1`/`h2`/`h3` appear as a single literal tag; h4–h6 exist solely inside the
    // union-tag form (`BaseUIComponentProps<'h1' | … | 'h6', State>`), which resolves to the first arm.
    // They are declared anyway so the set is closed under that union.
    "h1" to "HTMLAttributes<web.html.HTMLElement>",
    "h2" to "HTMLAttributes<web.html.HTMLElement>",
    "h3" to "HTMLAttributes<web.html.HTMLElement>",
    "h4" to "HTMLAttributes<web.html.HTMLElement>",
    "h5" to "HTMLAttributes<web.html.HTMLElement>",
    "h6" to "HTMLAttributes<web.html.HTMLElement>",
    "img" to "ImgHTMLAttributes<web.html.HTMLImageElement>",
    "input" to "InputHTMLAttributes<web.html.HTMLInputElement>",
    "label" to "LabelHTMLAttributes<web.html.HTMLLabelElement>",
    "li" to "LiHTMLAttributes<web.html.HTMLLIElement>",
    "nav" to "HTMLAttributes<web.html.HTMLElement>",
    "output" to "HTMLAttributes<web.html.HTMLElement>",
    "p" to "HTMLAttributes<web.html.HTMLParagraphElement>",
    "span" to "HTMLAttributes<web.html.HTMLSpanElement>",
    "ul" to "HTMLAttributes<web.html.HTMLUListElement>",
).joinToString("\n\n") { (tag, attributes) ->
    val name = "BaseUi" + tag.replaceFirstChar(Char::uppercase) + "Props"

    """
    /** Base UI props for a rendered `<$tag>` element. */
    external interface $name : react.dom.html.$attributes {
        override var className: Any? /* string | ((state) => string | undefined) */
        override var style: Any? /* CSSProperties | ((state) => CSSProperties | undefined) */
        var render: Any? /* ReactElement | ((props: HTMLProps, state) => ReactElement) */
    }
    """.trimIndent()
}

private fun generateBaseUiDeclarations(
    typesDir: File,
    sourceDir: File,
) {
    if (!typesDir.exists()) {
        println("Skipping Base UI generation: ${typesDir.path} not found")
        return
    }

    // One flat `baseui` package, mirroring how `mui.material` keeps its whole surface flat. Base UI's
    // own declaration names are already module-prefixed (`MenuPopupProps`, `SelectPopupProps`), so the
    // only shared name is `Separator`, which really is one component reused by several modules.
    val targetDir = sourceDir.resolve("baseui")
        .also { it.mkdirs() }

    val modules = baseUiModules(typesDir, BASE_UI_MODULES)

    // Filtered for existence as a module's parts are: these paths are hand-written, so they are the
    // first thing an upstream move breaks, and `buildBaseUiAliases` reads every file before `generate`
    // gets its chance to report a missing one.
    val extraFiles = BASE_UI_EXTRA_FILES
        .map { typesDir.resolve(it).normalize() }
        .filter { file ->
            file.exists()
                .also { if (!it) println("Skipping Base UI extra file: ${file.path} not found") }
        }

    val files = (modules.flatMap { it.parts }.map { it.file } + extraFiles)
        // A part file can be referenced more than once: `store/MenuHandle.d.ts` backs both `Handle` and
        // `createHandle`, and shared parts (`../separator/Separator.d.ts`) are re-exported by several
        // modules. An entry of `BASE_UI_EXTRA_FILES` whose module has since been added is a duplicate of
        // that module's own part file, and drops out here.
        .distinctBy { it.absolutePath }

    // Built over the whole file set up front: namespace references cross files (`MenuSubmenuRoot.d.ts`
    // extends `MenuRoot.Props`), so a per-file map would leave those unresolved.
    val aliases = buildBaseUiAliases(files)

    val bodies = files.mapNotNull { file ->
        generate(
            definitionFile = file,
            targetDir = targetDir,
            pkg = Package.baseUi,
            typesOnly = true,
            preprocess = { adaptBaseUiContent(it, aliases) },
        )
    }

    val handWritten = listOf(
        "Side" to BASE_UI_SIDE,
        "Align" to BASE_UI_ALIGN,
        "Orientation" to BASE_UI_ORIENTATION,
        "TransitionStatus" to BASE_UI_TRANSITION_STATUS,
        "Stubs" to BASE_UI_STUBS,
    )

    handWritten.forEach { (name, body) ->
        targetDir.resolve("$name.kt")
            .writeText(fileContent(body = body, pkg = Package.baseUi))
    }

    targetDir.resolve("ElementProps.kt")
        .writeText(
            fileContent(
                annotations = "@file:Suppress(\"VAR_TYPE_MISMATCH_ON_OVERRIDE\")",
                body = BASE_UI_ELEMENT_PROPS,
                pkg = Package.baseUi,
            )
        )

    for (stub in unusedNamespaceStubs(bodies))
        println("Base UI: nothing referred to the $stub stub — has its upstream declaration changed?")

    // The namespace objects are what makes the types above renderable, and go last: a part is exposed
    // only if its props type is among the ones just written.
    val declaredTypes = baseUiDeclaredTypes(
        bodies + handWritten.map { (_, body) -> body } + BASE_UI_ELEMENT_PROPS
    )

    for (parent in unresolvedParents(bodies, declaredTypes))
        println("Base UI: '$parent' is named as a parent but not generated — add its file to BASE_UI_EXTRA_FILES?")

    modules.forEach { module ->
        // A flat module has no namespace object — its values are exported directly.
        val namespace = module.namespace ?: return@forEach
        val body = baseUiNamespaceObject(module, declaredTypes)
            ?: return@forEach

        targetDir.resolve("$namespace.kt")
            .writeText(
                fileContent(
                    // `Package.baseUi.id` is empty and `generate` derives no subpackage for it, so the
                    // module segment has to be passed here — otherwise the annotation would come out as
                    // `@file:JsModule("@base-ui/react")` and resolve to the package root.
                    annotations = moduleDeclaration(
                        pkg = Package.baseUi,
                        subpackage = module.id,
                        componentName = null,
                    ),
                    body = body,
                    pkg = Package.baseUi,
                )
            )
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
        pkg.scope,
        pkg.id,
        subpackage,
        componentName,
    ).filter { !it.isNullOrEmpty() }
        .joinToString("/")

    return "@file:JsModule(\"$moduleName\")"
}


/**
 * Converts one `.d.ts` into the `<Component>.kt` (plus `.ext.kt` / `.classes.kt`) files it yields.
 *
 * Returns the declaration body that was written, or `null` when nothing was — the source is missing, or
 * it converted to no declarations at all. Callers that need to know which types now exist use it;
 * everyone else ignores it.
 */
private fun generate(
    definitionFile: File,
    targetDir: File,
    pkg: Package,
    fullPath: Boolean = false,
    typesOnly: Boolean = false,
    preprocess: ((String) -> String)? = null,
): String? {
    // MUI v6 sometimes ships only `<Component>/index.d.ts` (e.g. useMediaQuery, OverridableComponent).
    // Fall back to it when the standard `<Component>.d.ts` is missing.
    val actualFile = if (!definitionFile.exists()) {
        val indexFallback = definitionFile.parentFile?.resolve("index.d.ts")
        if (indexFallback != null && indexFallback.exists()) indexFallback else definitionFile
    } else definitionFile

    // Upstream may drop a hook/component's `.d.ts` between versions while it's still listed by
    // the caller (e.g. a directory scan or a hardcoded name) — nothing to convert, so bail out
    // instead of writing a stub or crashing on the missing-file read below.
    if (!actualFile.exists()) {
        println("Skipping generation for ${definitionFile.path}: no declaration file found")
        return null
    }

    val componentName = when {
        actualFile.name == "shared.d.ts" -> "CalendarPickerView"
        actualFile.name == "index.d.ts" -> actualFile.parentFile.name
        else -> actualFile.name.removeSuffix(".d.ts")
    }
    val (body, extensions) = convertDefinitions(
        actualFile,
        typesOnly = typesOnly,
        preprocess = preprocess,
        keepEmptyBodyParents = pkg == Package.baseUi,
    )

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
        // Base UI parts routinely re-declare an inherited prop to give it a narrower type or their own
        // documentation (`MenuSubmenuRootProps` re-states `onOpenChange` and `closeParentOnEsc` from
        // `MenuRootProps`). Emitting a real `override` needs the parent's member types, which the
        // Override machinery only resolves for MUI shapes — suppressed package-wide for now, see
        // BASE_UI_TODO.md.
        //
        // VAR_TYPE_MISMATCH_ON_OVERRIDE for the same reason plus one of Base UI's own: every DOM event
        // handler inherited through `BaseUIComponentProps` is re-typed by the `WithBaseUIEvent<T>` mapped
        // type, which adds `preventBaseUIHandler` to the event. A part that re-declares such a handler
        // (`MenuItemProps.onClick`) therefore legitimately widens the plain DOM signature.
        if (pkg == Package.baseUi) {
            suppressKeys += "VIRTUAL_MEMBER_HIDDEN"
            suppressKeys += "VAR_TYPE_MISMATCH_ON_OVERRIDE"
        }
        if (componentName in VAR_TYPE_MISMATCH_ON_OVERRIDE_FIX_REQUIRED) suppressKeys += "VAR_TYPE_MISMATCH_ON_OVERRIDE"
        if (suppressKeys.isNotEmpty() && componentName != "TextField") {
            // `distinct()`: the two MUI sets are keyed by bare component name, and Base UI part files
            // named `Input` / `RadioGroup` collide with entries there — without it the Base UI branch
            // above would emit a duplicate key, which `@Suppress` rejects.
            annotations += "@file:Suppress(\n" + suppressKeys.distinct().joinToString(",\n") { "\"$it\"" } + ",\n)"
        }
    }

    var emittedBody: String? = null
    if (componentName != "CalendarPickerView" && componentName != "createTypography") {
        val finalBody = when {
            componentName == "createTransitions" -> body + "\n\n" + STYLE_TRANSITION_CREATE_OPTIONS
            else -> body
        }.demoteOrphanKdoc()

        // Source `.d.ts` may be missing (removed upstream) or parse to no declarations at all
        // (e.g. a generic signature the converter can't translate) — either way there's nothing
        // to emit, so skip writing a stub file that's just a `package` line.
        if (finalBody.isNotBlank()) {
            targetDir.resolve("$componentName.kt")
                .writeText(fileContent(annotations.joinToString("\n\n"), finalBody, pkg))
            emittedBody = finalBody
        }
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

    // A Base UI part additionally gets state-typed accessors for the three props whose type the shared
    // per-tag parent cannot express.
    val allExtensions = if (pkg == Package.baseUi)
        sequenceOf(extensionsBody, baseUiStateHelpers(componentName, body))
            .filter { it.isNotEmpty() }
            .joinToString("\n\n")
    else
        extensionsBody

    if (allExtensions.isNotEmpty() && componentName != "Stepper") {
        val fileName = "$componentName.ext"

        // NB: v6 named this union `Variant` and renamed it here to `TypographyVariant`. v7 already
        // declares `export type TypographyVariant`, so no rename is needed (doing it would double the
        // prefix → `TypographyTypographyVariant`).
        targetDir.resolve("$fileName.kt")
            .writeText(fileContent(body = allExtensions, pkg = pkg))
    }

    if (componentName == "RadioGroup")
        return emittedBody

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

    return emittedBody
}

/**
 * Turns a KDoc block into a plain block comment when the declaration it documented was commented out by
 * an override (`Overrides.kt` does this for `SpeedDial.ariaLabel`, which the ARIA machinery supplies
 * instead).
 *
 * A KDoc attached to nothing reads as a mistake, and the formatter treats it as live documentation —
 * ktfmt reflows KDoc but leaves plain block comments alone, so demoting also keeps the upstream wording
 * intact. Demoting rather than deleting keeps that documentation next to the commented-out member, which
 * is why it was left in place at all.
 *
 * Matched by shape rather than by the documentation text so that rewording upstream cannot silently turn
 * the fix off.
 */
private fun String.demoteOrphanKdoc(): String =
    ORPHAN_KDOC.replace(this) { it.value.replaceFirst("/**", "/*") }

private val ORPHAN_KDOC = Regex("""/\*\*\n(?: \*.*\n)+ \*/\n(?=/\* (?:override )?var )""")

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
