package playground.samples.pickers

import muix.pickers.AdapterDateFns
import muix.pickers.LocalizationProvider
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import react.FC
import react.Props
import react.useEffectOnce
import react.useState
import web.dom.ElementId
import web.dom.document

val PickersSectionListSample: Sample = Sample(
    id = "section-list",
    library = Libraries.Pickers,
    title = "PickersSectionList",
    kind = Kind.ImportSmoke,
    covers = listOf(
        "PickersSectionList.elements/sectionListRef/contentEditable",
        "PickersSectionListRef",
        "DatePicker.slots.field",
    ),
    fileName = "PickersSectionListSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — Runtime imports",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#runtime-imports--resolved-from-npm-exports-during-generation",
        ),
        Ref("a3919d02", "https://github.com/karakum-team/mui-kotlin/commit/a3919d02"),
    ),
    notes = "Only running this proves that the Kotlin `PickersSectionList` binds to " +
            "`@mui/x-date-pickers/PickersSectionList#Unstable_PickersSectionList` and mounts with the private " +
            "picker context a `DatePicker` field slot supplies (see `RuntimePickerField.kt`); MUI's own internal " +
            "import of the component is deliberately not what renders here.",
    content = FC<Props> {
        // Null until the first commit: the field slot's DOM only exists once the picker has mounted.
        var mounted by useState<Boolean?>(null)

        useEffectOnce {
            mounted = document.getElementById(ElementId("runtime-picker-sections")) != null
        }

        LocalizationProvider {
            dateAdapter = AdapterDateFns

            RuntimePicker()
        }

        mounted?.let { isMounted ->
            CheckList {
                id = "section-list-checks"
                checks = listOf(Check("#runtime-picker-sections is mounted", isMounted))
            }
        }
    },
)
