package playground.shell

import playground.samples.base.RuntimeHooksSample
import playground.samples.base.SliderStylingSample
import playground.samples.baseui.AccordionSample
import playground.samples.baseui.CheckboxSample
import playground.samples.baseui.FieldSample
import playground.samples.baseui.FieldsetSample
import playground.samples.baseui.MenuSample
import playground.samples.baseui.NumberFieldSample
import playground.samples.baseui.SliderSample
import playground.samples.baseui.SwitchSample
import playground.samples.baseui.ToastSample
import playground.samples.dateio.DateIoUtilsSample
import playground.samples.icons.IconsSample
import playground.samples.lab.MasonrySample
import playground.samples.material.AutocompleteSample
import playground.samples.material.MaterialV9Sample
import playground.samples.material.RuntimeImportsSample
import playground.samples.material.SvgIconSample
import playground.samples.material.TableCellSample
import playground.samples.material.ThemeFocusVisibleSample
import playground.samples.material.TooltipPlacementSample
import playground.samples.pickers.PickersClassesSample
import playground.samples.pickers.PickersSectionListSample
import playground.samples.system.ResponsiveSample
import playground.samples.system.SystemFactoriesSample
import playground.samples.system.ThemeWithoutDefaultSample
import playground.samples.treeview.RichTreeViewLoadingSample
import playground.samples.treeview.RichTreeViewSelectionSample
import playground.samples.treeview.SimpleTreeViewSample

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

// Display order. Adding a sample = a new `<Subject>Sample.kt` under samples/<library>/ + one line here.
val samples: List<Sample> = listOf(
    // material
    AutocompleteSample,
    SvgIconSample,
    TableCellSample,
    ThemeFocusVisibleSample,
    TooltipPlacementSample,
    MaterialV9Sample,
    RuntimeImportsSample,

    // icons
    IconsSample,

    // system
    ResponsiveSample,
    SystemFactoriesSample,
    ThemeWithoutDefaultSample,

    // lab
    MasonrySample,

    // base (legacy)
    SliderStylingSample,
    RuntimeHooksSample,

    // baseui
    MenuSample,
    SliderSample,
    FieldSample,
    FieldsetSample,
    SwitchSample,
    CheckboxSample,
    AccordionSample,
    NumberFieldSample,
    ToastSample,

    // pickers
    PickersClassesSample,
    PickersSectionListSample,

    // treeview
    SimpleTreeViewSample,
    RichTreeViewSelectionSample,
    RichTreeViewLoadingSample,

    // dateio
    DateIoUtilsSample,
)

fun samplesOf(library: Library): List<Sample> =
    samples.filter { it.library === library }

fun findSample(libraryId: String, sampleId: String): Sample? =
    samples.find { it.library.id == libraryId && it.id == sampleId }
