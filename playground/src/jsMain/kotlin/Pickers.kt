import emotion.styled.styled
import mui.material.Box
import mui.material.Typography
import muix.pickers.AdapterDateFns
import muix.pickers.DateCalendar
import muix.pickers.DatePicker
import muix.pickers.LocalizationProvider
import muix.pickers.MultiSectionDigitalClock
import muix.pickers.StaticDatePicker
import muix.pickers.StaticTimePicker
import muix.pickers.TimeClock
import muix.pickers.clockClasses
import muix.pickers.clockNumberClasses
import muix.pickers.clockPointerClasses
import muix.pickers.datePickerToolbarClasses
import muix.pickers.dateTimePickerTabsClasses
import muix.pickers.dateTimePickerToolbarClasses
import muix.pickers.dayCalendarClasses
import muix.pickers.multiSectionDigitalClockSectionClasses
import muix.pickers.pickersFadeTransitionGroupClasses
import muix.pickers.pickersFilledInputClasses
import muix.pickers.pickersInputBaseClasses
import muix.pickers.pickersInputClasses
import muix.pickers.pickersLayoutClasses
import muix.pickers.pickersOutlinedInputClasses
import muix.pickers.pickersSlideTransitionClasses
import muix.pickers.pickersTextFieldClasses
import muix.pickers.timePickerToolbarClasses
import react.FC
import react.Props
import web.cssom.AlignItems
import web.cssom.Border
import web.cssom.Color
import web.cssom.Display
import web.cssom.FontWeight
import web.cssom.LineStyle.Companion.dashed
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.px
import web.cssom.rem

// Live check of the generated `muix.pickers` class objects, and the only usage reference for them in the
// repository. Every selector below goes through a *typed* member, so a class key that stops being
// generated breaks the build here rather than silently producing unstyled output at a consumer.
//
// Three things this can establish that `:mui-kotlin:compileKotlinJs` cannot:
//  - The `@file:JsModule` subpath of each class object resolves. 16 of these 27 objects were never
//    generated before, and `dayCalendarClasses` pointed at `@mui/x-date-pickers/DayCalendar` — not a key
//    of the package's `exports` map. A bad subpath is a Vite resolve failure at load, invisible to kotlinc.
//  - `pickersOutlinedInputClasses.root` reaches the DOM at all. It is *inherited* from
//    `PickersInputBaseClasses`, which upstream spells as a TS `extends` — only a rendered, outlined
//    `DatePicker` proves the supertype the generator emits carries the base's 15 keys.
//  - That a picker mounts at all. This page used to be `FC { Fragment.create { MonthCalendar { … } } }`,
//    which discards the element it builds — the builder's return value is not what gets rendered — so no
//    picker had ever actually run here. Verified by rendering the old file: zero `MuiPickers*`/
//    `MuiMonthCalendar*` nodes, and no error either. Mounting one for real is also what makes the
//    `LocalizationProvider` below load-bearing: every picker calls `usePickerAdapter`, which throws when
//    no provider supplies a `dateAdapter`.
val Pickers = FC<Props> {
    Typography {
        +"Pickers — muix.pickers"
    }

    LocalizationProvider {
        dateAdapter = AdapterDateFns

        PickersShowcase {
            // Owns dayCalendar / pickersFadeTransitionGroup / pickersSlideTransition classes.
            DateCalendar {}

            // Static variants mount PickersLayout and the per-picker toolbars.
            StaticDatePicker {}
            StaticTimePicker {}

            TimeClock {}
            MultiSectionDigitalClock {}

            // The only one here that mounts PickersTextField → PickersOutlinedInput.
            DatePicker {}
        }
    }
}

private val PickersShowcase = Box.styled {
    display = Display.grid
    gap = 16.px

    // --- PickersTextField family (@mui/x-date-pickers/PickersTextField) ---------------------------
    // The three input variants inherit their keys from PickersInputBaseClasses; `root` below is the
    // inherited one, `notchedOutline` the re-declared one the generator marks `override`.
    pickersTextFieldClasses.root {
        pickersOutlinedInputClasses.root {
            borderRadius = 8.px
        }

        pickersOutlinedInputClasses.notchedOutline {
            borderColor = NamedColor.rebeccapurple
            borderWidth = 2.px
        }

        // Referenced through the BASE interface rather than through one of its three children, so that
        // `PickersInputBase.classes.kt`'s own `@file:JsModule` is loaded and proven to resolve. Reaching
        // the same element via `pickersOutlinedInputClasses` would not import that file at all.
        pickersInputBaseClasses.sectionsContainer {
            letterSpacing = 0.5.px
        }
    }

    // Not mounted by any picker on this page (they default to the outlined variant), so these two are
    // here purely as compile-time references to the generated members.
    pickersInputClasses.underline {
        borderBottomColor = NamedColor.rebeccapurple
    }

    pickersFilledInputClasses.underline {
        borderBottomColor = NamedColor.rebeccapurple
    }

    // --- PickersLayout (@mui/x-date-pickers/PickersLayout) ----------------------------------------
    pickersLayoutClasses.root {
        border = Border(1.px, dashed, Color("#c8c8c8"))
    }

    pickersLayoutClasses.toolbar {
        backgroundColor = Color("#f3eefa")
    }

    pickersLayoutClasses.contentWrapper {
        backgroundColor = NamedColor.white
    }

    pickersLayoutClasses.actionBar {
        borderTop = Border(1.px, solid, Color("#e4e4e4"))
    }

    pickersLayoutClasses.landscape {
        alignItems = AlignItems.start
    }

    pickersLayoutClasses.shortcuts {
        padding = 4.px
    }

    pickersLayoutClasses.tabs {
        minHeight = 40.px
    }

    // --- Toolbars ---------------------------------------------------------------------------------
    datePickerToolbarClasses.root {
        paddingBottom = 8.px
    }

    datePickerToolbarClasses.title {
        color = NamedColor.rebeccapurple
        fontWeight = FontWeight.bold
    }

    timePickerToolbarClasses.root {
        paddingBottom = 8.px
    }

    timePickerToolbarClasses.separator {
        color = NamedColor.rebeccapurple
    }

    dateTimePickerToolbarClasses.root {
        paddingBottom = 8.px
    }

    dateTimePickerToolbarClasses.dateContainer {
        alignItems = AlignItems.start
    }

    dateTimePickerTabsClasses.root {
        borderTop = Border(1.px, solid, Color("#e4e4e4"))
    }

    // --- DateCalendar internals (@mui/x-date-pickers/DateCalendar) --------------------------------
    dayCalendarClasses.weekDayLabel {
        color = NamedColor.rebeccapurple
        fontWeight = FontWeight.bold
    }

    pickersFadeTransitionGroupClasses.root {
        minHeight = 240.px
    }

    pickersSlideTransitionClasses.root {
        minHeight = 240.px
    }

    // --- TimeClock internals (@mui/x-date-pickers/TimeClock) --------------------------------------
    clockClasses.clock {
        backgroundColor = Color("#f3eefa")
    }

    clockNumberClasses.root {
        fontSize = 0.85.rem
    }

    clockPointerClasses.thumb {
        borderColor = NamedColor.rebeccapurple
    }

    // --- MultiSectionDigitalClock (@mui/x-date-pickers/MultiSectionDigitalClock) ------------------
    multiSectionDigitalClockSectionClasses.item {
        borderRadius = 4.px
    }
}
