package playground.shell

import AppIcons
import BaseUiAccordion
import BaseUiCheckbox
import BaseUiField
import BaseUiFieldset
import BaseUiMenu
import BaseUiNumberField
import BaseUiSlider
import BaseUiSwitch
import BaseUiToast
import MaterialV9Improvements
import MyAutocomplete
import RuntimeImports
import SliderStylization
import Theming
import TreeViewLoading
import DateIo as DateIoSample
import Pickers as PickersSample
import TreeView as TreeViewSample

object Libraries {
    val Material = Library(
        id = "material",
        title = "Material UI",
        kotlinPackage = "mui.material",
        npmPackage = "@mui/material",
    )

    val Icons = Library(
        id = "icons",
        title = "Material Icons",
        kotlinPackage = "mui.icons.material",
        npmPackage = "@mui/icons-material",
    )

    val System = Library(
        id = "system",
        title = "MUI System",
        kotlinPackage = "mui.system",
        npmPackage = "@mui/system",
    )

    val Lab = Library(
        id = "lab",
        title = "MUI Lab",
        kotlinPackage = "mui.lab",
        npmPackage = "@mui/lab",
    )

    val Base = Library(
        id = "base",
        title = "MUI Base (legacy)",
        kotlinPackage = "mui.base",
        npmPackage = "@mui/base",
        note = "legacy, frozen @mui/base — see BASE_UI_TODO.md",
    )

    val BaseUi = Library(
        id = "baseui",
        title = "Base UI",
        kotlinPackage = "baseui",
        npmPackage = "@base-ui/react",
    )

    val Pickers = Library(
        id = "pickers",
        title = "MUI X Date Pickers",
        kotlinPackage = "muix.pickers",
        npmPackage = "@mui/x-date-pickers",
    )

    val TreeView = Library(
        id = "treeview",
        title = "MUI X Tree View",
        kotlinPackage = "muix.tree.view",
        npmPackage = "@mui/x-tree-view",
    )

    val DateIo = Library(
        id = "dateio",
        title = "date-io",
        kotlinPackage = "dateio.core",
        npmPackage = "@date-io/core",
    )

    val all: List<Library> = listOf(Material, Icons, System, Lab, Base, BaseUi, Pickers, TreeView, DateIo)
}

// Phase 1: samples still live in the root package; Phase 2 moves them under samples/<library>/.
val samples: List<Sample> = listOf(
    Sample(
        id = "app-icons",
        library = Libraries.Icons,
        title = "Icons",
        kind = Kind.Showcase,
        covers = listOf("SvgIcon + path", "Abc", "SouthEast"),
        fileName = "AppIcons.kt",
        content = AppIcons,
    ),
    Sample(
        id = "autocomplete",
        library = Libraries.Material,
        title = "Autocomplete",
        kind = Kind.Showcase,
        covers = listOf("Autocomplete", "TextField"),
        fileName = "MyAutocomplete.kt",
        content = MyAutocomplete,
    ),
    Sample(
        id = "classes",
        library = Libraries.Pickers,
        title = "Pickers classes",
        kind = Kind.ClassesCoverage,
        covers = listOf("DateCalendar", "picker *Classes", "PickersSectionList"),
        fileName = "Pickers.kt",
        content = PickersSample,
    ),
    Sample(
        id = "iutils",
        library = Libraries.DateIo,
        title = "date-io IUtils",
        kind = Kind.Check,
        covers = listOf("IUtils<Date, Locale>"),
        fileName = "DateIo.kt",
        content = DateIoSample,
    ),
    Sample(
        id = "slider-styling",
        library = Libraries.Base,
        title = "Slider styling",
        kind = Kind.ClassesCoverage,
        covers = listOf("Slider", "sliderClasses"),
        fileName = "SliderStylization.kt",
        content = SliderStylization,
    ),
    Sample(
        id = "menu",
        library = Libraries.BaseUi,
        title = "Menu",
        kind = Kind.Check,
        covers = listOf("Menu.*", "className/style/render helpers"),
        fileName = "BaseUiMenu.kt",
        content = BaseUiMenu,
    ),
    Sample(
        id = "slider",
        library = Libraries.BaseUi,
        title = "Slider",
        kind = Kind.Check,
        covers = listOf("Slider.*"),
        fileName = "BaseUiSlider.kt",
        content = BaseUiSlider,
    ),
    Sample(
        id = "field",
        library = Libraries.BaseUi,
        title = "Field",
        kind = Kind.Check,
        covers = listOf("Field.*"),
        fileName = "BaseUiField.kt",
        content = BaseUiField,
    ),
    Sample(
        id = "fieldset",
        library = Libraries.BaseUi,
        title = "Fieldset",
        kind = Kind.Check,
        covers = listOf("Fieldset.*"),
        fileName = "BaseUiFieldset.kt",
        content = BaseUiFieldset,
    ),
    Sample(
        id = "switch",
        library = Libraries.BaseUi,
        title = "Switch",
        kind = Kind.Check,
        covers = listOf("Switch.*"),
        fileName = "BaseUiSwitch.kt",
        content = BaseUiSwitch,
    ),
    Sample(
        id = "checkbox",
        library = Libraries.BaseUi,
        title = "Checkbox",
        kind = Kind.Check,
        covers = listOf("Checkbox.*"),
        fileName = "BaseUiCheckbox.kt",
        content = BaseUiCheckbox,
    ),
    Sample(
        id = "accordion",
        library = Libraries.BaseUi,
        title = "Accordion",
        kind = Kind.Check,
        covers = listOf("Accordion.*"),
        fileName = "BaseUiAccordion.kt",
        content = BaseUiAccordion,
    ),
    Sample(
        id = "number-field",
        library = Libraries.BaseUi,
        title = "NumberField",
        kind = Kind.Check,
        covers = listOf("NumberField.*"),
        fileName = "BaseUiNumberField.kt",
        content = BaseUiNumberField,
    ),
    Sample(
        id = "toast",
        library = Libraries.BaseUi,
        title = "Toast",
        kind = Kind.Check,
        covers = listOf("Toast.*", "createToastManager"),
        fileName = "BaseUiToast.kt",
        content = BaseUiToast,
    ),
    Sample(
        id = "simple-tree-view",
        library = Libraries.TreeView,
        title = "SimpleTreeView",
        kind = Kind.Check,
        covers = listOf("SimpleTreeView", "TreeItem"),
        fileName = "TreeView.kt",
        content = TreeViewSample,
    ),
    Sample(
        id = "rich-tree-view-loading",
        library = Libraries.TreeView,
        title = "RichTreeView loading",
        kind = Kind.Check,
        covers = listOf("RichTreeView", "TreeItemLoader"),
        fileName = "TreeViewLoading.kt",
        content = TreeViewLoading,
    ),
    Sample(
        id = "v9-improvements",
        library = Libraries.Material,
        title = "Material v9 improvements",
        kind = Kind.Check,
        covers = listOf("Material v9 props", "RichTreeView selection"),
        fileName = "MaterialV9Improvements.kt",
        notes = "Mixed sample (also covers muix.tree.view); split in Phase 3.",
        content = MaterialV9Improvements,
    ),
    Sample(
        id = "theme-focus-visible",
        library = Libraries.Material,
        title = "Theme focusVisible",
        kind = Kind.Check,
        covers = listOf("createTheme", "focus rings", "CSS variables"),
        fileName = "Theming.kt",
        content = Theming,
    ),
    Sample(
        id = "runtime-imports",
        library = Libraries.Material,
        title = "Runtime imports",
        kind = Kind.ImportSmoke,
        covers = listOf("mui.material.styles", "mui.system factories", "mui.base hooks"),
        fileName = "RuntimeImports.kt",
        notes = "Mixed sample (also covers mui.system and mui.base); split in Phase 3.",
        content = RuntimeImports,
    ),
)

fun samplesOf(library: Library): List<Sample> =
    samples.filter { it.library === library }

fun findSample(libraryId: String, sampleId: String): Sample? =
    samples.find { it.library.id == libraryId && it.id == sampleId }
