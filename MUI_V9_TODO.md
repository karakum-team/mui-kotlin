# MUI v9 migration — handoff / TODO

Migration of the generator from MUI v7 → **v9** (v8 skipped; the suite was realigned so MUI X is also v9).

## Versions (`gradle.properties`)

| package                                    | version                                                              |
|--------------------------------------------|----------------------------------------------------------------------|
| `@mui/material` / `@mui/system`            | `9.1.2`                                                              |
| `@mui/icons-material`                      | `9.1.1`                                                              |
| `@mui/lab`                                 | `9.0.0-beta.5`                                                       |
| `@mui/x-date-pickers` / `@mui/x-tree-view` | `9.7.0`                                                              |
| `@mui/base`                                | `5.0.0-beta.70` (frozen / deprecated — see `FUTURE_IMPROVEMENTS.md`) |
| kotlin-wrappers BOM                        | `2026.6.10`                                                          |
| kfc                                        | `19.10.0`                                                            |
| kotlin / seskar                            | `2.4.0` / `4.60.0` (unchanged)                                       |

> mui-x **had** to bump together with core: `@mui/x-tree-view@7` pins its peer to `@mui/material@"^5||^6||^7"`,
> so npm refuses to install it next to `@mui/material@9`. The whole v9 suite installs as one.

## Status

- ✅ `:mui-kotlin:compileKotlinJs` — **0 errors**
- ✅ `:playground:compileKotlinJs` — **0 errors**
- ⏳ Type-quality / review-remark polish — **not yet done** (see "Remaining" below)

## Generator changes made for v9 (all in `buildSrc/.../karakum/mui/`)

- `Converter.convertClasses` — fixed an empty `extends-Omit` `check` that threw during generation
  (`SimpleTreeView`/`RichTreeView` classes).
- `ParentType.INTERNAL_REJECTED_PARENTS` — reject v9 internal/`internals/` base types that aren't generated:
  `PickerOwnerState`, `ExportedUseViewsOptions`, `ExportedValidateDateProps`, `ExportedDayCalendarProps`,
  `ExportedBaseClockProps`, `DayCalendarSlots`, `DayCalendarSlotProps`, `UseTreeItemParameters`,
  `TreeViewSlots`, `TreeViewSlotProps`, `RichTreeViewItemsSlots`, `TreeItemIconSlots`, `TreeItemIconSlotProps`.
- `KotlinType.STANDARD_TYPE_MAP` + `FunctionType` — widen opaque v9 model types to `Any`:
  `PickerValidDate`, `PickerValue`, `PickerOwnerState`, `TimeViewWithMeridiem`, and the leaking generic
  params `TDate` / `TView` / `TSectionValue`.
- `Converter.findComponent` — dropped the stale **v7-era** `<*>`/`<*,*>`/`<*,*,*>` arity entries for the
  pickers that v9 made **non-generic** (TDate removed in favour of the global `PickerValidDate`): DatePicker,
  TimePicker, DateTimePicker, Desktop*/Mobile* variants, LocalizationProvider, MonthCalendar, YearCalendar, …
  They now emit a bare `propsName`.
- `MemberConverter.convertProperty` — drop TS index signatures (e.g. v9's `[x: \`data-${string}\`]: string`).

### Phase 5b — per-component fixes that let ALL the initially-excluded mui-x components be restored

The first pass reached green by *excluding* DateCalendar / the digital clocks / PickerDay /
PickersCalendarHeader / TimeClock / RichTreeView / TreeItem / TreeItemLabelInput. Phase 5b fixed the real
causes so nothing mui-x is excluded anymore (lab `TreeView`/`TreeItem` aside — genuinely gone in v9):

- **TreeItem** — `Overrides.kt`: its parents (`UseTreeItemParameters` + `Omit<HTMLAttributes<HTMLLIElement>>`)
  don't survive as Kotlin supertypes, so `onKeyDown`/`onFocus` are now emitted as plain members (no dangling
  `override`).
- **DigitalClock / MultiSectionDigitalClock** — `Converter.kt` empty-body path: an empty `*Props` aggregator
  (e.g. `ExportedDigitalClockProps {}`) now extends `react.Props`, so `FC<…Props>` satisfies `P : Props`.
  `FunctionType` widens `TSectionValue` inside callback signatures.
- **RichTreeView** — `adaptRawContent`: strip the `<R, Multiple>` params from `RichTreeViewSlotProps` (decl +
  usage) so they agree; it's referenced param-less.
- **DateCalendar / PickersCalendarHeader** — `Converter.kt`: removed the **v7-era** `<TDate>` injection (lines
  ~1348/1366) for `DateCalendarSlots` / `DateCalendarSlotProps` / `ExportedDateCalendarProps` /
  `PickersCalendarHeaderSlotProps` (all non-generic in v9), and dropped `DateCalendarProps` from
  `findComponent`'s `<*>` arity map.
- **TimeClock** — `KotlinType`: `readonly TView[]` → `ReadonlyArray<Any>`.
- **PickerDay** — generate its `.types`; `PickerDayOwnerStateBase` rejected as parent; `MuiEvent<T>` unwrapped
  to its inner event; and **dropped the `Omit<ButtonBaseProps, …>` parent** (`adaptRawContent`) because
  PickerDay re-declares 8 handlers with an extra `day` arg — incompatible signatures that can't `override`
  ButtonBase's, so it can't extend `ButtonBaseProps` in Kotlin's invariant model. PickerDay keeps its own
  refined handlers; it loses only the *non-refined* ButtonBase props (component/ripple/etc.).

### Phase 5c — type-quality pass on the restored components (no more `Any`/lost inheritance)

Phase 5b reached green but degraded types (widened to `Any`, dropped inheritance). Phase 5c restores them:

- **Opaque model types are NAMED, not `Any`** (`PICKERS_STUBS` + `KNOWN_TYPES`): `PickerValidDate`/`PickerValue`
  → `typealias`; `PickerOwnerState` → real `interface`; `PickerVariant`/`PickerOrientation`/`TimeView`/`DateView`
  → named aliases. So members read `var value: PickerValidDate?`, `var day: PickerValidDate`, and
  `DigitalClockOwnerState`/`MonthButtonOwnerState`/`YearButtonOwnerState` extend `PickerOwnerState` again.
- **Generics preserved**: `MultiSectionDigitalClockOption<TSectionValue>` with `value: TSectionValue`
  (`TSectionValue` added to `KNOWN_TYPES`); `TimeClock` `views: ReadonlyArray<String /* TimeViewWithMeridiem */>`.
- **PickerDay** `day` + handlers are `PickerValidDate` (precise), not `Any`.
- **Inheritance restored via type-only generation** — new `typesOnly` flag on `generate()`/`convertDefinitions`
  emits interfaces without the broken `declare const` vals. Generated type-only sources:
    - `DateCalendar/DayCalendar.d.ts` → `ExportedDayCalendarProps` (loading/renderLoading) + DayCalendar slots.
    - `internals/models/validation.d.ts` + `validation/validateDate.d.ts` → `ExportedValidateDateProps` now
      extends `Day/Month/Year/BaseDateValidationProps` (so `minDate`/`maxDate`/`shouldDisable*` reach DateCalendar).
      `FutureAndPastValidationProps` is force-`export`ed in `adaptRawContent` (it's a non-exported `interface`).
    - tree-view `internals/TreeViewProvider/TreeViewStyleContext.d.ts` (`TreeViewSlots`/`SlotProps`) +
      `TreeItemIcon/TreeItemIcon.types.d.ts` (`TreeItemIconSlots`/`SlotProps`) → `SimpleTreeViewSlots`/
      `RichTreeViewSlots : TreeViewSlots` and `TreeItemSlots : TreeItemIconSlots` restored.

**Known partial limitations (documented, deliberately not generated):**

- DateCalendar loses only `views`/`openTo`/`onViewChange` — they come from internal `ExportedUseViewsOptions`,
  whose sibling `UseViewsOptions.onChange` has optional function-type params Kotlin can't express.
- `RichTreeViewSlots` keeps `TreeViewSlots` but not `RichTreeViewItemsSlots` — the internal `RichTreeViewItems`
  type drags in a `<TProps>` generic / `Ref` / slot overrides that don't translate.

## Excluded components

- **lab `TreeView` / `TreeItem`** (`EXCLUDED_TYPES`) — v9 `@mui/x-tree-view` exposes no plain `TreeView`
  component (only `SimpleTreeView` / `RichTreeView`), so the old lab re-exports are genuinely gone. Correct to
  exclude — not a coverage loss. (The real tree-view components are generated under `muix.tree.view`.)

**All other mui-x components are generated and green**, including the full pickers surface (responsive +
calendars + clocks + PickerDay + PickersCalendarHeader + fields + adapters) and tree-view
(SimpleTreeView / RichTreeView / TreeItem / TreeItemLabelInput + icons/provider/hook).

## Remaining work (post-green)

1. **Type-quality / review-remark polish** (the kotlin-wrappers reviewer pass). Diff generated v9 output vs the
   committed v7 output (`git diff mui-kotlin/src/jsMain/kotlin/`) and look for regressions (named interfaces
   collapsing to `Any?`, stray `(((…)))`, lost enums, `ComponentType` vs `FC`). Audit the v7 deprecated-coping
   adapters (`adapters/ComponentsAndSlots.kt` `cleanupDeprecatedComponentsProps()`, `removeDeprecated()`) — v9
   deleted the deprecated props at source, so much of that is now dead code. Extend `ARIA_ATTR_*` /
   `NUMBER_AS_INT/DOUBLE_PROPERTIES` for new v9 members (Stepper/Tabs ARIA, NumberField).
2. Align `mui-icons-material` to `9.1.2` (currently `9.1.1`) for consistency, if desired.
