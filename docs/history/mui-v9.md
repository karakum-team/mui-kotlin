# MUI v9 migration — history

> **Migration record.** Version tables, counts, warnings and verification results below describe their
> recorded iterations, not a new check of the current tree. The later Phase 4 record completes the
> type-quality pass previously marked pending in the initial handoff. Current generator limitations and
> maintenance triggers are in [the generator plan](../plans/generator.md); Base UI / Menubar work is in
> [the Base UI plan](../plans/base-ui.md). Use current [verification rules](../../.agents/rules/generator-verification.md)
> rather than historical commands or baseline exceptions when continuing work.

Migration of the generator from MUI v7 → **v9** (v8 skipped; the suite was realigned so MUI X is also v9).

## Versions (`gradle.properties`)

| package                                    | version                                                                        |
|--------------------------------------------|--------------------------------------------------------------------------------|
| `@mui/material` / `@mui/system`            | `9.5.0`                                                                        |
| `@mui/icons-material`                      | `9.5.0`                                                                        |
| `@mui/lab`                                 | `9.0.0-beta.10`                                                                |
| `@mui/x-date-pickers` / `@mui/x-tree-view` | `9.15.0`                                                                       |
| `@mui/base`                                | `5.0.0-beta.70` (frozen; retained during [Base UI migration](../plans/base-ui.md)) |
| `@base-ui/react`                           | `1.8.0` (nine modules included; see [current status](../plans/base-ui.md))         |
| kotlin-wrappers BOM                        | `2026.9.3`                                                                     |
| kfc                                        | `19.16.0`                                                                      |
| kotlin / seskar                            | `2.4.20` / `4.66.0`                                                            |

> mui-x **had** to bump together with core: `@mui/x-tree-view@7` pins its peer to `@mui/material@"^5||^6||^7"`,
> so npm refuses to install it next to `@mui/material@9`. The whole v9 suite installs as one.

## Status

- ✅ `:mui-kotlin:compileKotlinJs` — **0 errors**
- ✅ `:playground:compileKotlinJs` — **0 errors**
- ✅ Type-quality / review-remark polish — **completed in the recorded Phase 4 pass** (see below)

## @date-io/core 2.17 → 3.2 update (2026-10-01)

- `mui-kotlin/build.gradle.kts` now pins `@date-io/core` to `3.2.0`; refreshed the active Gradle
  npm lock with `kotlinUpgradePackageLock --rerun-tasks`. `npm ls` confirms one core 3.2.0 across
  all workspaces; `.kotlin-locks/js/package-lock.json` matches `build/js/package-lock.json`.
  The historical root `package-lock.json` is not used by this Gradle build and is unchanged.
- The generated interface is now `IUtils<TDate, TLocale>` with `locale: TLocale?` and
  `getWeek(value: TDate): Int`. Upstream removed `ExtendableDateType` and the bound on `TDate`;
  both changes are reflected in Kotlin. Direct consumers of `IUtils<TDate>` must supply the second
  type argument. `DateAdapter`'s existing alias was adjusted to `JsClass<IUtils<*, *>>`.
- The conditional generic `date` return type becomes two safe overloads: `date(): TDate` and
  `date(value: Any?): TDate?`. Explicit null/undefined inputs conservatively retain `TDate?` rather
  than their more specific TypeScript result. No caller-selected unchecked return type is exposed.
  All 65 upstream method names survive (66 Kotlin methods because of the overload).
- Date-io-only preprocessing removes the commented-out constructor before shared parsing; otherwise
  comment/semicolon normalization can unbalance its braces and swallow the interface's body.
  The converter also preserves blank/closing lines in the new date JSDoc. Three regression tests
  cover the overloads, changed-signature rejection, and full conversion with constructor comments.
- Added `DateIo.kt` (now `playground/src/jsMain/kotlin/playground/samples/dateio/DateIoUtilsSample.kt`) to the playground using real `@date-io/date-fns 3.2.1` (playground-only; its peer
  range supports our existing `date-fns 4.4.0`). It tests generated IUtils directly, separately from
  MUI's different adapter API. Chrome shows all seven checks passing: no-argument/null/string/Date
  inputs, typed en-GB locale, week 42, and `15/10/2026` localized formatting. Existing MUI DatePicker
  selects 2026-10-15 and the digital clock selects 03:30 AM. No new runtime errors; existing Base UI
  callback-name warning and favicon 404 remain.
- Both Kotlin/JS modules compile with zero errors; all six generator tests pass. Clean `build`
  (including the production Vite bundle) succeeds, and SHA-256 manifests of all 744 generated files
  match before/after clean regeneration. Only `IUtils.kt` and `DateAdapter.kt` change versus HEAD.
  Existing build warnings remain; no unrelated generator type approximations were changed.

## Gradle wrapper 9.0 → 9.7 update (2026-10-01)

- Pinned Gradle `9.7.0` in the root `build.gradle.kts` and regenerated the wrapper properties,
  JAR, and both launcher scripts with Gradle 9.7.0. Added the official distribution SHA-256;
  the wrapper JAR SHA-256 also matches the published Gradle checksum. `./gradlew --version`
  confirms 9.7.0. Library versions in `gradle.properties` are unchanged.
- `:mui-kotlin:clean build`, both `compileKotlinJs` tasks, development executable sync, and
  `:buildSrc:test` pass (3 tests, zero failures). The production Vite bundle was rebuilt successfully.
  All 744 generated files match HEAD after clean regeneration and formatting.
  npm packages are unchanged; discarded an incidental workspace-order-only lockfile diff.
- Restarted `:playground:jsViteDev` with Gradle 9.7.0 and reloaded Chrome without cache:
  the app renders, NumberField increments 5 → 6, and TreeView switches from loading to `Loaded file`.
  No JavaScript runtime exceptions; the existing Base UI callback-name warning and favicon 404 remain.
- Follow-ups, not blockers for 9.7: local JDK 27 still exceeds Kotlin's supported JVM target
  (now falls back to 26); Gradle warns about the two delegated `by registering` declarations in
  `mui-declarations.gradle.kts`, scheduled for removal in Gradle 10. Existing production bundler
  `use client`/chunk-size warnings remain. Windows launcher CRLF endings are preserved as generated.

## Playground runtime dependencies update (2026-10-01)

- `playground/build.gradle.kts`: `@emotion/react` 11.9.0 → 11.14.0,
  `@emotion/styled` 11.8.1 → 11.14.1, and `date-fns` 4.1.0 → 4.4.0.
  Library/toolchain pins in `gradle.properties` are unchanged.
- Refreshed `.kotlin-locks/js/package-lock.json` with `kotlinUpgradePackageLock --rerun-tasks`.
  The old nested Emotion installations and their old `weak-memoize` copies disappeared.
  `npm ls` and workspace resolution confirm one version of each requested package; the checked-in
  lock matches `build/js/package-lock.json` byte-for-byte.
- Both Kotlin modules compile. `:mui-kotlin:clean build` passes and the generated tree has no diff
  against HEAD. Development executable sync passes; generator tests remain green (3 tests, cached).
  Forced `:playground:jsBrowserProductionVite --rerun` as well: the initial build incorrectly considered
  the existing bundle up to date after npm changes. The production bundle succeeds with the existing
  `use client`/chunk-size warnings. Gradle browser tests have no sources.
- Chrome on the restarted dev server: DatePicker selects 2026-10-15, the digital clock selects
  03:30 AM, and the picker's styled grid/purple borders/weekday colors remain intact. Keyboard Tab
  preserves the default 2px blue Button/Chip focus rings and the custom 4px magenta ring with 6px offset.
  Base UI Menu styles/positioning and Toast create/update/close also work.
- The duplicate-Emotion warning recorded in earlier checks is now gone. No JS runtime exceptions;
  the existing Base UI Kotlin `render` callback-name warning and favicon 404 remain.

## MUI X 9.12 → 9.14 update (2026-10-01)

- Both MUI X packages and `@mui/x-internals` are now `9.14.0`. Material/System/Icons stay at `9.4.0`;
  Base UI stays at `1.6.0`. The installed packages satisfy the current peer dependency ranges.
- Date Pickers' published `.d.ts` files are byte-identical between `9.12.0` and `9.14.0`, so its
  generated declarations did not change. Tree View adds `RichTreeView.loading`, the `loading` and
  `itemLoader` slots, and the public `TreeItemLoader` component (introduced in `9.13.0`).
- `generateTreeViewDeclarations` now includes `TreeItemLoader.types.d.ts`. Its `ownerState` keeps the
  generated `TreeItemLoaderOwnerState` type via a Tree-View-scoped known-type mapping, and
  `React.HTMLAttributes<HTMLLIElement>` survives as its real Kotlin parent (`title`, `className`, etc.).
- `findClassesDeclaration` now extracts an interface's own body even when its utility-type parent
  cannot be retained. This exposes `richTreeViewClasses.itemLoader`; it does not expand the inherited
  keys hidden behind `Omit<TreeViewClasses, ...>` (flattened since — see "Tree View: the behavioural props and
  the typing gaps" below). Existing picker class inheritance is unchanged.
- New `playground/src/jsMain/kotlin/TreeViewLoading.kt` (now `playground/src/jsMain/kotlin/playground/samples/treeview/RichTreeViewLoadingSample.kt`) exercises `loading`, both new slots, slot props,
  typed loading-row state, and both new CSS-class objects. The existing `items` gap is supplied by a
  sample-local props interface, not a change to the library API. `RichTreeViewSlotProps.loading` and
  `.itemLoader` still use the generator's existing `react.Props` approximation for `SlotComponentProps`;
  callback forms and the loading slot's `itemsCount`/`message` are not given dedicated Kotlin types (both
  since fixed by the `SlotComponentProps<P, S>` carrier and `RichTreeViewLoadingSlotOwnProps`, see below).
- npm upgrade caveat: the first incremental install left the playground workspace at `9.12.0`,
  hoisted those old types to `build/js/node_modules`, and nested `9.14.0` under the library workspace.
  `./gradlew kotlinUpgradePackageLock --rerun-tasks` refreshed every workspace and removed the split.
  Verified both the generator's root packages and Node resolution from the playground at `9.14.0`;
  `.kotlin-locks/js/package-lock.json` matches `build/js/package-lock.json` byte-for-byte.

Verification:

- Both `compileKotlinJs` tasks pass with zero errors. `:mui-kotlin:clean build` passes, including the
  playground production bundle; the development executable was also rebuilt.
- SHA-256 manifests of all **743** generated files match before and after the clean regeneration,
  including the three new `TreeItemLoader` files. The six generated changes versus HEAD are intentional:
  three new files and three changed Tree View files; no picker or Material output changed.
- Chrome DevTools: five disabled loading rows with `aria-busy=true`, owner-state row indices/count/depth,
  forwarded `className`, and both CSS selectors; finish loading shows `Loaded file` and removes busy
  state; the custom `loading` slot shows one semantic `TreeItemLoader` row.
- Regression smoke: DatePicker selects `2026-10-15`; the digital clock selects `03:30 AM`; SimpleTreeView
  expands and selects a leaf, while the disabled item stays disabled and Archive expands without being
  selected; Toast creates and closes without unmounting the app.
- No JavaScript runtime exceptions. The existing duplicate-Emotion and Kotlin `render` callback-name
  warnings remain, along with the missing favicon (404). Gradle browser test tasks are skipped; the
  browser checks above were interactive. No Pro lazy-loading behavior was exercised.

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

### Phase 5d — the pickers CSS-class objects (16 of 27 were never generated)

The rule at the tail of `generate()` emits `<Component>.classes.kt` only for a
`{componentName}Classes.d.ts` sitting beside the component's own `.d.ts`, and derives the
`@file:JsModule` subpath from the component name. Both halves are wrong for `@mui/x-date-pickers`:

- **Coverage.** Pickers ship **27** `*Classes.d.ts` under component directories; the rule reached **11**.
  It can only ever find the one class object named after the component, but `TimeClock/` owns four
  (`timeClock`, `clock`, `clockNumber`, `clockPointer`), `DateCalendar/` owns four (`dateCalendar`,
  `dayCalendar`, `pickersFadeTransitionGroup`, `pickersSlideTransition`), and `PickersTextField/` owns
  five — one at its own root plus one in each of four nested directories. Two owning directories —
  `PickersTextField`, `PickersLayout` — are additionally in the component exclusion set, so their class
  objects were unreachable twice over.
- **Module path.** `dayCalendarClasses.d.ts` lives in `DateCalendar/`, so the component-name rule emitted
  `@file:JsModule("@mui/x-date-pickers/DayCalendar")` — not a key of the package's `exports` map (54 keys,
  no wildcard). That was the one pickers entry in the audit table further down this file.

Replaced for pickers by `generatePickersClasses` (`Generator.kt`), which walks the types tree for
`*Classes.d.ts` and takes the subpath from the **top-level** directory. That set is exactly the `exports`
keys, and each such directory's `index.d.ts` re-exports every class object below it, nested ones included
(`PickersTextField/index.d.ts` does `export * from "./PickersOutlinedInput/index.js"`). `generate()` gained
`emitClasses` and is called with `emitClasses = false` throughout `generatePickersDeclarations` — replacing
rather than supplementing, so `DayCalendar.classes.kt` cannot depend on which pass runs last.

Discovered rather than listed on purpose: the 16-file gap is what a static list costs at a version bump.
The first-segment `isComponentName()` filter is what keeps `internals/` and the bundled
`node_modules/@mui/utils` out — the latter matters, because its `composeClasses.d.ts` and
`generateUtilityClasses.d.ts` both match a naive `*Classes.d.ts` glob. A class object at the package root
would be dropped by that filter too; upstream ships none, and one would need the `.` export rather than a
subpath anyway.

Duplicate `.classes.kt` filenames are rejected in `generatePickersClasses`. The shared runtime-binding
resolver now validates both the candidate subpath and the exported JS name, rebinding when necessary
(see below), rather than only checking for a textual key in `package.json`.

**`convertClasses` learned inheritance.** Three of the sixteen have a non-empty body *and* an `extends`:
`PickersInputClasses`, `PickersFilledInputClasses` and `PickersOutlinedInputClasses` all extend
`PickersInputBaseClasses` (15 keys). The first two add `underline`, for 16 keys each; the third only
*re-declares* `notchedOutline`, which the base already has, so it stays at 15. They used to hit the
empty-marker branch, which would have dropped every one of those keys. They now emit
`sealed external interface X : PickersInputBaseClasses`, and the re-declared `notchedOutline` is emitted
`override`.

The parent is kept only when it is a **bare identifier** naming another interface the same pass emits.
That gate is what leaves x-tree-view alone: `Simple/RichTreeViewClasses extends Omit<TreeViewClasses, '…'>`
is neither a bare identifier nor a generated type, so both keep the empty marker they had.

**Deliberately out of scope:** the five class objects under `internals/` — the `pickersToolbar*` trio plus
`PickerPopper/pickerPopperClasses` and `PickersArrowSwitcher/pickersArrowSwitcherClasses`. They would have
to bind to `@mui/x-date-pickers/internals`, which MUI can change without a semver signal.

Covered in `playground/src/jsMain/kotlin/Pickers.kt` (now `playground/src/jsMain/kotlin/playground/samples/pickers/PickersClassesSample.kt`) and checked in the browser: all 16 new class objects
are referenced through typed members, as is `dayCalendarClasses` (the one whose subpath changed).
`pickersOutlinedInputClasses.root` — an *inherited* key — is confirmed to reach the DOM, which is what
proves the supertype carries the base's keys at runtime. The other 10 pre-existing pickers class objects
are still uncovered.
That page previously read `FC { Fragment.create { MonthCalendar { … } } }`, which discards the element it
builds, so no picker had ever actually rendered there.

## The material theme is converted from `createThemeNoVars.d.ts`

Until 2026-10-31 `mui.material.styles.Theme` / `ThemeOptions` were hand-written stubs over `mui.system.*`:
upstream gates the material theme's CSS-variables parts behind TS conditional types keyed on the
augmentable `CssThemeVariables`, which the converter cannot follow. Material-only members had to be added
by hand (`focusVisible`, MUI 9.4.0, was the first), and a version bump could not surface an omission.

Now `generateStylesDeclarations` converts `styles/createThemeNoVars.d.ts` (types only) after
`adapters/CreateThemeNoVars.kt` collapses each conditional type to its default — not-enabled — branch.
The result is `ThemeOptions`, `BaseTheme` and `Theme` with typed `palette`, `typography`, `mixins`,
`motion`, `shape`, `shadows`, `transitions`, `zIndex`, `focusVisible`, `vars`, `alpha`/`lighten`/`darken`,
so members MUI adds to that file flow in on a bump. Implementation details and the recorded simplifications
are in [MUI v6 history](mui-v6.md#material); current follow-ups are in
[the generator plan](../plans/generator.md#стабы-вместо-настоящей-генерации). The short version:

- The material theme narrows `mui.system.Theme`/`ThemeOptions` members (`palette: Any?` → `Palette`), so
  `createThemeNoVars.kt` suppresses `VAR_TYPE_MISMATCH_ON_OVERRIDE`.
- The public `ThemeOptions` of `createTheme.d.ts` (same name) cannot be generated beside it; the adapter
  adds its `cssVariables` / `colorSchemes` / `defaultColorScheme` to the NoVars one.
- `createTheme` stays a hand-written binding to the `@mui/material/styles` barrel; `FocusVisible` stays a
  hand-written typealias (the generator does not emit plain type aliases).
- `ThemeOptions` inherits `zIndex` instead of `Omit`-ing it, as before.

Exercised by `playground/src/jsMain/kotlin/playground/samples/material/ThemeTypingSample.kt` and
`ThemeFocusVisibleSample.kt`.

`styles/focusVisible.d.ts` is intentionally NOT generated — it holds only private helpers
(`resolveFocusVisible`, `wireFocusVisibleVars`, …), no public API.

### Runtime imports — resolved from npm exports during generation

The old path-only audit missed a second failure mode: an importable module can lack the requested
default/named export. Kotlin compilation does not check either condition, and unused external
declarations emit no JS import. This is the class of defect behind kotlin-wrappers#2894 (`useTheme`).

`RuntimeBindings.kt` records every emitted runtime declaration with its original `.d.ts` source;
`runtime-exports.cjs` checks the installed package's `exports` map (conditional and wildcard entries
included), then parses the ESM export graph without executing the package. Legacy packages without
`exports` use their `module`/`main` metadata. Original typings identify default exports; JS symbol
provenance prevents rebinding to an unrelated function with the same name. Explicit templates such as
icons, adapters and Base UI namespaces are checked too, including namespace members.

Resolution preserves a valid binding first, then prefers the current module with a corrected name,
the nearest exported ancestor, another package entrypoint, and finally the package root. Ambiguity or
an unknown missing export fails generation. Generation and validation happen in a staging directory
before replacing the existing declarations. If values from one source need different modules, they
are split into Kotlin files: declaration-level `@JsModule` would incorrectly import a default.

The shared pass currently checks **11,168 top-level runtime bindings**, plus namespace members:

| Kotlin API | Verified JS binding |
|---|---|
| Material `useTheme`, `createStyles` | Named exports from `@mui/material/styles` |
| Material `createMixins` | `@mui/material/styles#private_createMixins` |
| System `createTheme` | `@mui/system/createTheme#default` |
| System `createBreakpoints` | `@mui/system/createBreakpoints#unstable_createBreakpoints` |
| Material `ClickAwayListener` | `@mui/material/ClickAwayListener#default` |
| Material `touchRippleClasses` | `@mui/material/ButtonBase#touchRippleClasses` |
| Pickers `PickersSectionList` | `@mui/x-date-pickers/PickersSectionList#Unstable_PickersSectionList` |
| Base `CssAnimation`, `CssTransition` | Named exports from `@mui/base/Transitions` |
| 14 Base hooks (`useBadge` through `useTabsList`, including `useSelect` and `useSwitch`) | Named, not default, exports from their existing hook entrypoints |

Kotlin names and signatures are preserved, including bindings to `private_` / `unstable_` exports.
Those names remain upstream-internal/unstable contracts; validation will surface future removal.
Four explicit exclusions remove **only runtime values**: Material `createPalette`, `createMotion`,
`SwitchBase` and `switchBaseClasses`. Their types remain generated. These values have no accessible
package export, so moving all of them to `styles` would still produce broken imports. An exclusion
whose value becomes accessible is an error too, prompting its removal rather than silently hiding API.

Fixture tests cover resolution, aliases, symbol identity, namespaces, cycles, type-only exports,
missing targets and stale exclusions; Kotlin tests cover declaration extraction, rendering and module
splitting. `:mui-kotlin:testRuntimeExports` is part of `check`. Playground coverage lives in
`RuntimeImports.kt` and `RuntimePickerField.kt` (now split by library into
`playground/src/jsMain/kotlin/playground/samples/material/RuntimeImportsSample.kt`,
`playground/src/jsMain/kotlin/playground/samples/system/SystemFactoriesSample.kt` and
`playground/src/jsMain/kotlin/playground/samples/base/RuntimeHooksSample.kt` + `base/TransitionFixture.kt`; the picker
field is `playground/src/jsMain/kotlin/playground/samples/pickers/RuntimePickerField.kt`, mounted by
`pickers/PickersSectionListSample.kt`), exercising the repaired imports through the generated
API, with local provider/slot scaffolding for legacy transitions and `PickersSectionList`.

Verified locally with 21 export-graph fixtures and 5 Kotlin binding tests, a clean full build
(including both Kotlin/JS modules and the production Vite bundle), and a byte-for-byte comparison of
the generated tree before/after `:mui-kotlin:clean build`. Chrome DevTools MCP confirmed the provider
theme, 14 loaded hooks, factory results, ripple classes, mounted transitions/section list and the
inside/outside click behavior. No import/runtime errors remained; the console still has the existing
Base UI Slider render-callback warning, a missing favicon, and the expected deprecation warning when
calling `createStyles`.

## Excluded components

- **lab `TreeView` / `TreeItem`** (`EXCLUDED_TYPES`) — v9 `@mui/x-tree-view` exposes no plain `TreeView`
  component (only `SimpleTreeView` / `RichTreeView`), so the old lab re-exports are genuinely gone. Correct to
  exclude — not a coverage loss. (The real tree-view components are generated under `muix.tree.view`.)

**All other mui-x components are generated and green**, including the full pickers surface (responsive +
calendars + clocks + PickerDay + PickersCalendarHeader + fields + adapters) and tree-view
(SimpleTreeView / RichTreeView / TreeItem / TreeItemLabelInput / TreeItemLoader + icons/provider/hook).

### Tree View: the behavioural props and the typing gaps (fixed 2026-10-31)

"Generated and green" understated one gap. Compilation could not see it because nothing in the repo
consumed the tree-view declarations until `playground/src/jsMain/kotlin/TreeView.kt` (now `playground/src/jsMain/kotlin/playground/samples/treeview/SimpleTreeViewSample.kt`) was added.

- **Fixed in the 9.12 bump:** `TreeItemProps` had neither `itemId` (which the component *requires*) nor
  `label`. Both come from `Omit<UseTreeItemParameters, 'rootRef'>`. `useTreeItem/useTreeItem.types.d.ts`
  is now generated `typesOnly` and `UseTreeItemParameters` left `INTERNAL_REJECTED_PARENTS`, so
  `TreeItemProps` extends it and picks up `itemId` / `label` / `disabled` / `disableSelection` / `id`.
    - Caveat: Kotlin also inherits `rootRef`, which the TS `Omit` removes. Harmless (React ignores it),
      but it is one prop of over-exposure.
- **Fixed 2026-10-31 — the store parameters.** `items`, `multiSelect`, `expandedItems`, `selectedItems`, the
  selection / expansion callbacks, `selectionPropagation`, `domStructure`, `expansionTrigger`, … sit behind
  `UseTreeViewStoreParameters<TStore>`, an indexed access into a method's parameter tuple that a text-based
  converter cannot follow. They are hand-written in `TREE_VIEW_STUBS` (`Generator.kt`) as
  `TreeViewStoreParameters` / `RichTreeViewStoreParameters<R : Any>`, maintained against
  `internals/MinimalTreeViewStore/MinimalTreeViewStore.types.d.ts` and
  `internals/RichTreeViewStore/RichTreeViewStore.types.d.ts`. `RichTreeViewProps<R : Any>` keeps the item model
  (`findProps`), so the call form is `RichTreeView<RichTreeViewProps<Item>> { items = … }` — a bare
  `RichTreeView { items = … }` no longer compiles against the star-projected props.
- **The other hand-written files of `muix/tree/view`** (all `TREE_VIEW_*` constants in `Generator.kt`) and the
  upstream shape each stands in for:
    - `TreeViewSelectionValue` — the conditional `TreeViewSelectionValue<Multiple>`; a factory per shape plus
      `asMultiple()` / `asSingle()` readers, so `selectedItems` / `onSelectedItemsChange` are never `Any?`.
    - `TreeViewExpansionTrigger` — the inline `'content' | 'iconContainer'` union.
    - `SlotComponentProps<P, S>` — `@mui/utils` `SlotComponentProps<Component, Overrides, OwnerState>` for the
      slots whose owner state is non-empty (`RichTreeViewSlotProps.item` / `root` / `loading` / `itemLoader`);
      slots with a `{}` owner state are the element's own props (`TREE_VIEW_KNOWN_TYPES`).
    - `RichTreeViewItems.kt` / `RichTreeViewLoading.kt` — the slot interfaces of the two non-convertible
      `internals/components` files, so `RichTreeViewSlots` / `RichTreeViewSlotProps` keep both upstream parents.
    - `TreeViewCancellableEvent` + the `defaultMuiPrevented` extension — `models/events.d.ts`; every
      `TreeViewCancellableEventHandler<E>` member is the plain React handler (`kotlinType`).
    - `TreeViewPublicAPI<R>` / `RichTreeViewPublicAPI<R>` with their `@JsPlainObject` parameter objects —
      `ReturnType<TStore['buildPublicAPI']>`; `apiRef` is `RefObject<…>`, and `useRichTreeViewApiRef` /
      `useSimpleTreeViewApiRef` bind to `@mui/x-tree-view/hooks`.
    - `TreeViewCssVars` / `TreeItemDataAttributes` — the runtime-only `--TreeView-*` custom properties and
      `data-<status>` attributes.
- **Generated since, not hand-written:** `models/items.d.ts` / `models/domStructure.d.ts`
  (`TreeViewSelectionPropagation`, the `TreeViewItemSelectionStatus` / `TreeViewItemsReorderingAction` /
  `TreeViewDOMStructure` unions); the `useTreeItem` slot own-props with the selection and label-editing plugin
  augmentations spliced in (`adaptUseTreeItemAugmentations`); `RichTreeViewClasses` / `SimpleTreeViewClasses`
  with the `Omit<TreeViewClasses, …>` keys flattened in (`convertClasses`); `@JsPlainObject` factories on the
  `*Slots` family and the model / parameter objects (`convertDefinitions(jsPlainObject = …)`).
- **Staleness cost:** the stubs are pinned to upstream shapes — after an `x-tree-view` bump, diff them against
  the files their comments name (`adaptUseTreeItemAugmentations` fails the build on its own when a plugin file
  or declaration disappears). The consumer-style sample
  `playground/src/jsMain/kotlin/playground/samples/treeview/ConsumerTreeViewSample.kt` (with `consumer/`)
  exercises the whole surface in a browser, with the public API driving the selection.

### Other tree-view findings from the 9.8 → 9.12 bump

- **`TreeItemProvider` newly emits, and needed its props.** 9.12 rewrote `TreeItemProvider.d.ts`'s
  return annotation to `React.JSX.Element`, which makes `findComponent` recognise it; the emitted
  `FC<TreeItemProviderProps>` then referenced a type nobody generated. `TreeItemProvider` was added to
  the `.types.d.ts` branch of `generateTreeViewDeclarations`.
- **`react.Ref<T>` has a `T : Any` bound**, like `ResponsiveStyleValue`. `React.Ref<HTMLLIElement>`
  fell through to `Ref<Any? /* HTMLLIElement */>` and failed to compile. Fixed twice over: a precise
  `React.Ref<HTMLLIElement>` entry in `STANDARD_TYPE_MAP`, plus the same non-null guard
  `ResponsiveStyleValue` already had on the generic `React.Ref<…>` fallback.
- **Six stale `INTERNAL_REJECTED_PARENTS` entries removed** — `RichTreeViewPluginSlots` /
  `SlotProps` / `Parameters` and the three `SimpleTreeViewPlugin*` equivalents. Those identifiers
  appear nowhere in the 9.x `.d.ts` (v9 is store-based, not plugin-based); verified inert by a
  byte-identical regeneration before deleting.
- **Dead v7-era tree-view code removed (2026-10-31)**, each verified absent from the 9.x `.d.ts` and the
  regeneration byte-identical: the `TreeItem2` / `TreeItem2Icon` / `TreeItem2Provider` / `useTreeItem2`
  exclusion set, the `TreeItemContent.d.ts` call and the `TreeView` / `TreeItem2DragAndDropOverlay` branches in
  `generateTreeViewDeclarations`; `adaptTreeView()` in full; the `TreeItem2Props` replacement in
  `adaptRichTreeView()` (its `RichTreeViewSlotProps<R, Multiple>` replacement *is* still live); the lab
  `TreeItemProps` / `TreeViewProps` entries of `findComponent`; `TreeViewItemsReorderingAction →
  TreeItem2DragAndDropOverlayAction` and the `TreeViewExperimentalFeatures<…>` branch of `kotlinType`.

## Phase 4 — type-quality / reviewer pass (DONE)

The kotlin-wrappers reviewer pass over core/material/system (mui-x was polished in 5b/5c). Both projects stay
at **0 errors**. Findings refined the original task premises:

- **Diff regressions (sub-task 1): none.** The v9-vs-v7 output diff has no `dynamic`, no `(((…)))`, no
  `: react.Props`-only lost parents, and no named-type→`Any?` collapse where v7 had a real type. The diff is
  dominated by doc-URL rewrites (`v7.mui.com` → `mui.com`), the intentional Phase-5 picker de-genericization,
  the excluded lab `TreeView`/`TreeItem`, and new v9 props landing as `Any?`. **NB:** a fresh
  `generateDeclarations` no longer reproduces the committed Step-3 output byte-for-byte (≈524-file dep-drift
  nondeterminism), so the *committed* output — not a re-run — is the diff baseline.
    - **Fixed one real lost-enum:** `createMotion`'s `reducedMotion` was `Any? /* ReducedMotionMode */`. v9
      defines `type ReducedMotionMode = 'never' | 'system' | 'always'` (a string-literal union the generator
      drops as an alias). Added `ReducedMotionMode` to `KotlinType.STANDARD_TYPE_MAP` (mirrors
      `TimeViewWithMeridiem`) → now `String /* 'never' | 'system' | 'always' */`.
    - Left as-is (correct): `Motion` in `createThemeFoundation` stays `Any? /* Motion */` (cross-module type not
      imported into that generated file — same limitation as `SxProps`/`ThemeCssVar` there); transition
      `addEndListener` and Autocomplete `input` are new v9 indexed-access/slot members where `Any?` is the
      convention.
- **Dead deprecated-coping code (sub-task 2): all removed.** Every trigger string is gone from the v9 `.d.ts`,
  so the code was inert. Deleted `adapters/ComponentsAndSlots.kt` in full (its four `cleanup*` branches +
  `adaptComponentsAndSlots()`, dropped from `Adapter.adaptRawContent`) and `Converter.removeDeprecated()` +
  its call (it actually stripped a `MuiMediaQuery` block, which no longer exists in v9 — the task mis-named
  it). Verified inert: with the deletions, output is **byte-identical** to the pristine generator (diffed two
  fresh regenerations, not against the committed nondeterministic baseline).
- **Data tables (sub-task 3): ARIA no-op, numerics extended.** v9 adds no new uncovered `aria-*` to
  Stepper/StepButton/Tabs (only `aria-label`/`aria-labelledby`, already mapped via dashed `@JsName`) — ARIA
  tables unchanged, but verified. There is **no** material/lab `NumberField` in this v9. Added to
  `KotlinType.kt`: `minutesStep`, `fixedWeekNumber` → `NUMBER_AS_INT_PROPERTIES`; `min`, `max` →
  `NUMBER_AS_DOUBLE_PROPERTIES` (continuous bounds on CircularProgress/LinearProgress/Slider — all sites now
  `Double?`, none want integer).

## Remaining work (post-green)

This records the original post-green handoff. Active Base UI tasks continue in
[its canonical plan](../plans/base-ui.md); generator limitations and maintenance follow-ups are indexed in
[the generator plan](../plans/generator.md#follow-ups-recorded-during-the-v9-migration).
The icons-version statement below refers to package availability at that iteration, not today's latest version.

1. ~~Align `mui-icons-material` to `9.1.2` for consistency.~~ **N/A** — `@mui/icons-material@9.1.2`
   was never published; `9.1.1` is the latest on npm (the icons package doesn't ship a release for
   every core patch). Already on the newest available version; nothing to bump.
2. `@mui/base` → Base UI migration is **in progress**: `menu`, `slider`, `field`, `fieldset`, `switch`,
   `checkbox`, `accordion`, `number-field`, and `toast` are generated with playground samples.
   Full type and module coverage
   remains open — see [the Base UI plan](../plans/base-ui.md).
3. **`NumberField` is included; `Menubar` remains pending.** These belong to the separate
   `@base-ui/react` target, not the old `@mui/base` bindings. `NumberField` was added in `1dfec5cd`
   with all seven parts and a playground sample. `menubar` is not yet in `BASE_UI_MODULES`.
   The original v9 review recorded the other changes from the
   [v9 blog post](https://mui.com/blog/introducing-material-ui-v9/) as covered:
   type-surface changes (removed `disableEscapeKeyDown`,
   Autocomplete slots, dropped deprecated `component`/`componentsProps`, `MuiTouchRipple` off theme types) are
   generated automatically from the installed `.d.ts`; `InitColorSchemeScript`, `createMotion`/`ReducedMotionMode`
   are generated; and the rest (roving tabindex, Backdrop `aria-hidden`, `sx` perf, `color-mix()` runtime) are
   runtime-only behaviours with no declaration impact.

## Tree View fork handoff (2026-11-05)

Source revision: `9108550c`; generated Tree View delta: `f9a3a111..39ebe7ea` (unchanged since the latter).
The functional changes end at `7dd944ce`; `39ebe7ea` aligns two React import blocks with the fork's existing style.

- Prepared the pending Tree View API updates in the `aerialist7/kotlin-wrappers` fork,
  branch `muix-tree-view`, based on
  `e874c92b9acd648714d2ecb1d41a4d30fd05e189`. A fresh fetch confirmed that upstream `master` is at the same revision.
- The fork baseline matched generator `f9a3a111` except for existing `react.*` imports in
  `SimpleTreeView.types.kt` and `useTreeItem.types.kt`. Applied the generated delta with file-level three-way
  merging; resolved those two import conflicts by retaining every existing import and adding the imports
  required by the generated API changes.
- The diff contains **23 files: 11 modified and 12 added**, all under
  `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view`. It includes typed selection and expansion,
  generic RichTreeView items, typed slots and slot factories, cancellable event handlers, useTreeItem plugin
  props, flattened CSS classes, public API/hooks, and CSS-variable/data-attribute constants.
- MUI X `9.17.0` was already installed in the fork. No version files, lockfiles, other modules, or hand-written
  sources were changed. All transferred changes remain **unstaged and uncommitted**; `HEAD`, local `master`,
  and the index SHA-256 stayed unchanged. Human verification and commit are the next step; nothing was pushed.

Verification at this handoff:

- `./gradlew :mui-kotlin:clean build` passed, including library and playground Kotlin/JS compilation with
  zero errors and all 21 runtime-export tests.
- `git diff --exit-code -- mui-kotlin/src/jsMain/kotlin` passed after clean generation and formatting.
- All **32** files in the fork's Tree View package match the clean generator output byte-for-byte; the file
  sets are identical. Inspected the scoped diff and confirmed `git diff --check` passes in the fork.
- No compilation, builds, or tests were run in the fork, as required by the transfer rules. No browser run
  was performed for this transfer-only task, and the review subagent was stopped at the user's request.

## Tree View model filenames follow-up (2026-11-05)

Source baseline: `df82e282`, with the generator changes left uncommitted. The user requested model filenames
that match their declarations before pushing the Tree View handoff.

- `ConversionResult.extensions` now retains individual converted declarations. The component emitter joins
  them as before; the Tree View model emitter writes each model interface or union to its own named file.
- Generated `TreeViewSelectionPropagation.kt` instead of `items.kt`, split `items.ext.kt` into
  `TreeViewItemsReorderingAction.kt` and `TreeViewItemSelectionStatus.kt`, and generated `TreeViewDOMStructure.kt`
  instead of `domStructure.ext.kt`. All four declaration bodies and their imports are unchanged.
- Refreshed the same `muix-tree-view` fork checkout. All **33** Tree View files match the generator byte-for-byte.
  The user had staged the earlier 12 additions since the initial handoff, including the three old model files;
  their staged content and the index hash were preserved. The three removals and four new files remain unstaged
  for human verification and staging. `HEAD` and local `master` are unchanged; nothing was committed or pushed.

Verification:

- The clean build and explicit library/playground Kotlin/JS compilation passed with zero errors; all 21
  runtime-export tests passed. A second `:mui-kotlin:clean build` reproduced all **778** generated files byte-for-byte.
- Compared the generated tree against the pre-change SHA-256 manifest: only the three removed and four new model
  files differ; all **774** other files are unchanged. The two renames are byte-identical to their originals, and
  each split union retains its complete original body and imports.
- `git diff --check` passes in both repositories, including separate whitespace checks of the four new fork files.
  No fork builds/tests or browser run were performed; this follow-up changes filenames only.
- Additional `./gradlew -p buildSrc test` run: **14/15 passed**. The existing
  `RuntimeBindingsTest.separatesModulesWithoutChangingKotlinNamesOrOverloads` fails at line 120 because it expects
  the unused `other.Options` import in the split classes file, while `renderRuntimeBindings` removes it.
  Reproduced the same failure by running that test against an isolated export of the unmodified `df82e282`
  baseline. The runtime-binding implementation and test are unchanged; resolving that mismatch is outside this
  filename follow-up.

## Tree View pull request opened (2026-11-05)

The user confirmed pushing the `muix-tree-view` changes to the `aerialist7/kotlin-wrappers` fork and opening a
pull request after the model filename follow-up. This supersedes the pending staging/commit status recorded
at the earlier handoffs. The next step is upstream review and any requested follow-up changes.

## Tree View `@JsPlainObject` review follow-up — PR #2927 (2026-11-05)

Source baseline: `3e465547`. Upstream review on
[`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927) requested
`@JsPlainObject` on `RichTreeViewItemsOwnerState` and `RichTreeViewLoadingSlotOwnerState`, and asked whether
`SimpleTreeViewProps.apiRef` uses `RefObject` upstream.

- **Hand-written templates (`Generator.kt`):** added `@JsPlainObject` to `RichTreeViewItemsOwnerState`
  (`TREE_VIEW_RICH_TREE_VIEW_ITEMS`), `RichTreeViewLoadingSlotOwnerState` (`TREE_VIEW_RICH_TREE_VIEW_LOADING`),
  `TreeViewCancellableEvent` (`TREE_VIEW_CANCELLABLE_EVENT`), and `SimpleTreeViewItem` (`TREE_VIEW_PUBLIC_API`).
- **Converted Tree View interfaces (`Generator.kt`):** extended the `Package.treeView` `jsPlainObject` predicate
  to include `*OwnerState` (`TreeItemOwnerState`, `TreeItemLoaderOwnerState`) plus `TreeViewClasses`,
  `TreeViewStyleContextValue`, `UseTreeItemStatus`, and `UseTreeItemReturnValue`. Every plain-object interface
  in `muix.tree.view` without `react.Props` or method declarations now carries `@JsPlainObject`.
- **`apiRef` type retained as `RefObject<…>`:** upstream `SimpleTreeView.types.d.ts` and `RichTreeView.types.d.ts`
  define `SimpleTreeViewApiRef` and `RichTreeViewApiRef` as `React.RefObject<Partial<TreeViewPublicAPI<…>> | undefined>`,
  `useSimpleTreeViewApiRef` / `useRichTreeViewApiRef` return `React.RefObject<…>`, and runtime
  `initializeInputApiRef` (`useTreeViewBuildContext.js`) mutates `apiRef.current = publicAPI` directly without
  invoking callback refs.

Verification:

- `./gradlew :mui-kotlin:clean build :playground:compileKotlinJs` passed with zero errors, including all 21
  runtime-export tests and the production Vite bundle.
- A second `./gradlew :mui-kotlin:clean build` reproduced the generated tree byte-for-byte
  (`git diff --exit-code` on the post-regeneration tree compared to the first run, with only the 8 expected
  `muix/tree/view` files differing from `HEAD`).
- No types, component exports, or inheritance hierarchies changed; browser verification was not required.

## Tree View `@JsPlainObject` fork handoff (2026-11-05)

The user committed the generator fix as `e654e5f74081488ff09944761a85738e63988fc1` and requested transferring
its generated delta to the `muix-tree-view` branch of the
[`aerialist7/kotlin-wrappers`](https://github.com/aerialist7/kotlin-wrappers) fork.

- Applied only the 8 generated `muix/tree/view` file changes from `3e465547..e654e5f7` to the fork's
  `muix-tree-view` branch at `e5259e791c371b294118ebce363ae28f36d3d0f7` (based on `master` at
  `e874c92b9acd648714d2ecb1d41a4d30fd05e189`).
- Every file in `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view` matched the `3e465547` baseline
  byte-for-byte prior to transfer, and all **33** files now match the `e654e5f7` generator output byte-for-byte.
  The scoped diff against `HEAD` (`e5259e791c`) is **8 modified files, +14 lines**:
  `RichTreeViewItems.kt`, `RichTreeViewLoading.kt`, `TreeItem.types.kt`, `TreeItemLoader.types.kt`,
  `TreeViewCancellableEvent.kt`, `TreeViewPublicAPI.kt`, `TreeViewStyleContext.kt`, and `useTreeItem.types.kt`.
- All transferred changes remain **unstaged and uncommitted**. Fork `HEAD`, local `master`, index bytes, and
  the pre-existing `AD` index entries for `domStructure.ext.kt`, `items.ext.kt`, and `items.kt` were preserved
  unchanged.
- `git diff --check` passes in both repositories. No compilation, builds, or tests were run in the fork, and
  nothing was staged, committed, or pushed.

Next step: human verification, staging, and commit of the 8 `kotlin-muix-tree-view` changes on `muix-tree-view`,
followed by pushing to update [PR #2927](https://github.com/JetBrains/kotlin-wrappers/pull/2927).
Prepared title-only commit message:
`MUI X. Mark Tree View owner-state and model interfaces with @JsPlainObject`.

## Tree View JSO `val` and union comment follow-up — PR #2927 (2026-11-05)

Source baseline: `d55cd621` (fork `muix-tree-view` at `5be28b876db6a7acf38158d71fe2c0aaf2498e53`).
Upstream review on [`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927)
requested `val` properties in `@JsPlainObject` interfaces (`RichTreeViewItems.kt`) and `sealed /* union */`
comments on union interfaces (`TreeViewDOMStructure.kt` and `SlotComponentProps.kt`).

- **`val` properties in `@JsPlainObject` interfaces (`MemberConverter.kt`, `Converter.kt`, `Overrides.kt`,
  `Override.kt`, `Generator.kt`):** `convertMembers` now accepts `readOnly = isPlainObject` so every converted
  `@JsPlainObject` interface across `muix.tree.view`, `mui.system`, and `mui.material.styles` emits `val`
  properties; `createThemeNoVars` switches from `VAR_TYPE_MISMATCH_ON_OVERRIDE` to
  `PROPERTY_TYPE_MISMATCH_ON_OVERRIDE`; and hand-written `@JsPlainObject` templates in `Generator.kt`
  (`RichTreeViewItemsOwnerState`, `RichTreeViewItemsSlots`, `RichTreeViewLoadingSlotOwnerState`,
  `SimpleTreeViewItem`, `SetIsItemDisabledParameters`, `SetItemExpansionParameters`,
  `SetItemSelectionParameters`, `AddItemsParameters`) use `val`. Updated `RuntimeImportsSample.kt`,
  `ThemeFocusVisibleSample.kt`, `SpacingSample.kt`, and `ThemeWithoutDefaultSample.kt` to construct
  `ThemeOptions` via its `@JsPlainObject` factory instead of `unsafeJso`.
- **`TreeViewCancellableEvent` (`Generator.kt`):** removed `@JsPlainObject`, made `TreeViewCancellableEvent`
  a `private external interface`, and removed `inline` from `SyntheticEvent<*, *>.defaultMuiPrevented` while
  keeping `var defaultMuiPrevented: Boolean?`. At runtime `@mui/x-tree-view` (`useTreeItem.js` and the focus,
  keyboard-navigation, selection, and label-editing plugins) invokes `onKeyDown(event)` (`Unit` return) and
  reads `event.defaultMuiPrevented` from the same `SyntheticEvent` instance, so `SyntheticEvent.defaultMuiPrevented`
  must mutate the event in place rather than copying a plain object; because Kotlin callers access the flag
  through the extension property rather than referencing `TreeViewCancellableEvent` directly, the helper
  interface is kept `private`.
- **Union comments (`UnionConverter.kt`, `Spacing.kt`, `Generator.kt`):** emitted `sealed /* union */` before
  `external interface` in `convertUnion`, `convertSealed`, `convertSpacingOptions`, `MATERIAL_SIZE`,
  `TREE_VIEW_SELECTION_VALUE`, and `TREE_VIEW_SLOT_COMPONENT_PROPS`, covering all 6 Tree View union files
  (`SlotComponentProps.kt`, `TreeViewDOMStructure.kt`, `TreeViewExpansionTrigger.kt`,
  `TreeViewItemSelectionStatus.kt`, `TreeViewItemsReorderingAction.kt`, `TreeViewSelectionValue.kt`) and all
  other generated union files across the repository.
- **Integer properties (`KotlinType.kt`):** added `index`, `itemsCount`, and `itemDepth` to
  `NUMBER_AS_INT_PROPERTIES` so `TreeItemLoaderOwnerState` (`TreeItemLoader.types.kt`) and item/step/tab/thumb
  index properties in `mui.base` and `mui.material` emit `Int` instead of `Number`, and removed the redundant
  `.toInt()` calls in `RichTreeViewLoadingSample.kt`.

Verification:

- `./gradlew :mui-kotlin:clean build :playground:compileKotlinJs` passed with zero errors, including all 21
  runtime-export tests and the production Vite bundle.
- A second `./gradlew :mui-kotlin:clean build :playground:compileKotlinJs` reproduced all 778 generated files
  byte-for-byte.
- Verified that zero `@JsPlainObject` interfaces in `mui-kotlin/src/jsMain/kotlin` declare `var` members and
  that the only `sealed external interface` declarations without `/* union */` are the two callable function
  interfaces (`mui.system.Spacing` and `baseui.ToastManagerUpdate`).

## Tree View JSO `val` and union comment fork handoff (2026-11-05)

Transferred only the Tree View (`muix/tree/view`) generated delta to the `muix-tree-view` branch of the
fork, leaving non-Tree View changes (`mui/base`, `mui/material`, `mui/system`, `muix/pickers`) out of
`muix-tree-view` for separate PRs.

- Applied only the 18 generated `muix/tree/view` file changes to the fork's `muix-tree-view` branch at
  `5be28b876db6a7acf38158d71fe2c0aaf2498e53` (based on `master` at
  `e874c92b9acd648714d2ecb1d41a4d30fd05e189`).
- Every file in `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view` matched the `d55cd621` baseline
  byte-for-byte prior to transfer, and all **33** files now match the generator's `muix/tree/view` output
  byte-for-byte. The scoped diff against `HEAD` (`5be28b876d`) is **18 modified files, +99 / -95 lines**:
  `RichTreeView.types.kt`, `RichTreeViewItems.kt`, `RichTreeViewLoading.kt`, `SimpleTreeView.types.kt`,
  `SlotComponentProps.kt`, `TreeItem.types.kt`, `TreeItemIcon.types.kt`, `TreeItemLoader.types.kt`,
  `TreeViewCancellableEvent.kt`, `TreeViewDOMStructure.kt`, `TreeViewExpansionTrigger.kt`,
  `TreeViewItemSelectionStatus.kt`, `TreeViewItemsReorderingAction.kt`, `TreeViewPublicAPI.kt`,
  `TreeViewSelectionPropagation.kt`, `TreeViewSelectionValue.kt`, `TreeViewStyleContext.kt`, and
  `useTreeItem.types.kt`.
- All transferred changes remain **unstaged and uncommitted**. Fork `HEAD`, local `master`, and the pre-existing
  staged `AD` index entries for `domStructure.ext.kt`, `items.ext.kt`, and `items.kt` were preserved unchanged.
- `git diff --check` passes in both repositories. No compilation, builds, or tests were run in the fork, and
  nothing was staged, committed, or pushed.

Next step: human verification, staging, and commit of the 18 `kotlin-muix-tree-view` changes on `muix-tree-view`,
followed by pushing to update [PR #2927](https://github.com/JetBrains/kotlin-wrappers/pull/2927).
Prepared title-only commit message:
`MUI X. Use val in Tree View JSOs and mark union interfaces`.

## Tree View `private` event and `Int` loader properties fork handoff (2026-11-05)

Transferred only the Tree View (`muix/tree/view`) generated delta from local commits `281573ce` and `2ecdba0e` to
the `muix-tree-view` branch of the fork, leaving non-Tree View `index: Int` changes (`baseui`, `mui/base`,
`mui/material`) out of `muix-tree-view`.

- Applied only the 2 generated `muix/tree/view` file changes from `2d025ca7..2ecdba0e` to the fork's
  `muix-tree-view` branch at `e6df0fc7891e2895648b74991a5c9f92f906589a` (based on `master` at
  `e874c92b9acd648714d2ecb1d41a4d30fd05e189`).
- Every file in `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view` now matches the `2ecdba0e`
  generator output byte-for-byte (**33** files). The scoped diff against `HEAD` (`e6df0fc789`) is **2 modified
  files, +5 / -5 lines**:
  `TreeItemLoader.types.kt` (`index: Int`, `itemsCount: Int`, `itemDepth: Int`) and
  `TreeViewCancellableEvent.kt` (`private external interface TreeViewCancellableEvent` and non-`inline`
  `SyntheticEvent<*, *>.defaultMuiPrevented`).
- All transferred changes remain **unstaged and uncommitted**. Fork `HEAD`, local `master`, index bytes, and
  the pre-existing staged `AD` index entries for `domStructure.ext.kt`, `items.ext.kt`, and `items.kt` were
  preserved unchanged.
- `git diff --check` passes in both repositories. No compilation, builds, or tests were run in either
  repository during transfer, and nothing was staged, committed, or pushed.

Next step: human verification, staging, and commit of the 2 `kotlin-muix-tree-view` changes on `muix-tree-view`,
followed by pushing to update [PR #2927](https://github.com/JetBrains/kotlin-wrappers/pull/2927).
Prepared title-only commit message:
`MUI X. Use Int in TreeItemLoaderOwnerState and hide TreeViewCancellableEvent`.

## Tree View pull request merged and `TreeViewSelectionValue` follow-up (2026-11-06)

[`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927) was merged into
`JetBrains/kotlin-wrappers` as `67f0cceaf30b75a69ac864e1fe8725707d744123` on 2026-11-06.

- One remaining review thread on
  `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view/TreeViewSelectionValue.kt`
  ([`#discussion_r4484280313`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4484280313),
  [`#discussion_r4485031537`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4485031537))
  noted that single/multi-value selection typing is a common wrapper problem (analogous to `kotlin-react-select`)
  and that `TreeViewSelectionValue.kt` may keep `sealed /* union */ external interface TreeViewSelectionValue`
  (or use a shared wrapper type) while dropping the four custom adapter/factory helper functions
  (`TreeViewSelectionValue(ids: ReadonlyArray<TreeViewItemId>)`, `TreeViewSelectionValue(id: TreeViewItemId?)`,
  `asMultiple()`, `asSingle()`).
- Upstream agreed to handle this adjustment in a separate follow-up pull request so the current Tree View bindings
  could ship in the nearest release.
- Recorded as the **highest-priority** open task in
  [`docs/plans/future-improvements.md`](../plans/future-improvements.md#highest-priority-treeviewselectionvalue-common-singlemulti-value-representation-pr-2927-follow-up)
  and [`docs/plans/generator.md`](../plans/generator.md#сводка-открытых-пунктов-по-практической-ценности).

## Full generated-tree master fork handoff (2026-11-06)

The user explicitly requested the complete current generated-declaration delta directly in the fork's
existing `master` working tree for review before deciding whether to prepare upstream PRs. This request
overrides the usual topic-branch and topic-only transfer defaults for this handoff.

- Source revision: `8f9292b4`; target: fork `master` at `6cf8ce4e66`, initially clean with an empty staged diff.
  All nine published package mappings from the transfer rules were synchronized, including additions and
  deletions. The WIP `baseui` package, hand-written sources, package versions and lockfiles were excluded.
- The resulting unstaged, uncommitted delta has **109 files**: 102 modified, 5 added and 2 deleted.
  Module counts: Material 80, Base 15, System 8, Lab 2, Date Pickers 3 and Tree View 1.
  Icons, `mui/types` and `dateio/core` already matched. All **632** mapped generated files and their file
  sets now match the generator byte-for-byte.
- Changes include union markers, concrete numeric types, `SpeedDial.ariaLabel`, typed Material theme and
  typography declarations, plain-object theme interfaces, and callable spacing declarations. The old
  `styles/Theme.kt` and `system/Spacing.kt` stubs are replaced by the generator's current files.
- **Review caveat:** `master` had already removed the custom `TreeViewSelectionValue` factories and
  `asMultiple`/`asSingle` helpers. Exact synchronization restores them from the generator. This handoff
  does not resolve or reverse the separate cleanup decision recorded above; its canonical backlog remains
  [the highest-priority follow-up](../plans/future-improvements.md#highest-priority-treeviewselectionvalue-common-singlemulti-value-representation-pr-2927-follow-up).
- PR #2924 is present in the current fork `master` as `88b72cebfa`; all nine affected Material component
  files already matched the generator and needed no transfer. This supersedes their earlier pending
  handoff status.
- Verification: `./gradlew :mui-kotlin:clean build` passed, compiling the library and playground with
  zero errors and passing all 21 runtime-export tests. All **778** source declaration files matched their
  pre-build hashes, and `git diff --exit-code -- mui-kotlin/src/jsMain/kotlin` passed. The transferred
  contents match the planned delta exactly. Fork `git diff --check` and separate checks of all five new
  files passed; `HEAD`, all refs, index bytes and the empty staged diff are unchanged.
- No generator implementation changed. Browser verification and `buildSrc:test` were not rerun for this
  transfer-only task. No compilation, builds or tests were run in the fork. Nothing was staged, committed,
  pushed or posted to GitHub.

Next step: human review of the aggregate `master` diff and selection of any upstream PR scope.
Prepared title-only commit message: `MUI. Update generated declarations`.

## Open-PR subtraction from master review diff (2026-11-06)

The user requested removing changes already represented in open upstream PRs from the aggregate fork diff.
This supersedes the 109-file working-tree status recorded above; the source remains `8f9292b4` and fork
`master` remains at `6cf8ce4e66`.

- Scanned all **six** open PRs using the public GitHub API, their file lists and complete patches.
  Three overlap the aggregate diff:
  [#2914](https://github.com/JetBrains/kotlin-wrappers/pull/2914) (`23208afd16`),
  [#2922](https://github.com/JetBrains/kotlin-wrappers/pull/2922) (`19a6aa90f0`), and
  [#2923](https://github.com/JetBrains/kotlin-wrappers/pull/2923) (`34dcc9073c`).
  #2925, #2912 and #2910 affect other modules. A second API check confirmed the same six PRs and head revisions.
- Restored all **12** paths covered by the three MUI PRs to fork `HEAD`, excluding their SpeedDial/slider,
  spacing and typed-theme changes from the aggregate review diff. Five added files had been staged by the
  human after the initial handoff; only those duplicate additions were unstaged. All other index entries,
  `HEAD` and refs were preserved. The resulting staged diff is empty.
- #2923 still uses mutable properties in its plain-object theme/typography declarations. Preserved the
  nonduplicate generator corrections separately in `build/transfers/pr-2923-follow-up.patch`:
  **3 files, +104 / -104 lines**. The patch changes properties to `val` in Material `createThemeNoVars.kt`,
  Material `createTypography.kt` and System `createTheme.kt`, and changes the first file's suppression to
  `PROPERTY_TYPE_MISMATCH_ON_OVERRIDE`. It applies after #2923 and #2922, keeping their shared theme
  hierarchy consistent without repeating new-file additions in the `master` diff.
- The remaining fork diff is **97 modified files, +341 / -155 lines**, unstaged and uncommitted: Material
  73, Base 14, System 4, Lab 2, Date Pickers 3 and Tree View 1. Every remaining file is byte-identical to
  its pre-filter version. Union markers, numeric-index changes and the custom `TreeViewSelectionValue`
  helpers remain; the separate helper-cleanup decision is unchanged.
- Verification: applied the three exact PR patches and the separate follow-up over a scratch copy of the
  remaining aggregate tree. All **632** mapped generated files and their complete file sets then match
  the generator byte-for-byte. `git diff --check` passes, and no open-PR path remains in the aggregate diff.
  All **778** source declaration hashes still match the preceding successful clean build; no additional
  source build or browser run was needed for this subtraction-only step. No fork builds or tests were run.
  Nothing was staged, committed, pushed or posted to GitHub.

Next step: human review of the remaining `master` diff and the separate patch for #2923.
Prepared aggregate title-only commit message: `MUI. Update generated declarations`.

## Union-marker and numeric-index topic commits (2026-11-06)

The user explicitly requested splitting the `sealed` and numeric changes into independent branches and
committing them locally, while retaining the rest as a `master` working-tree diff. This authorization
supersedes the diff-only handoff default for these two scoped commits; pushing and opening PRs remain
the human's responsibility.

- Both branches start directly from fork `master` at `6cf8ce4e66` and contain one local commit each:
  `mui-union-markers` at `7eebf700b3146f351d5c9a90fe72e682cb3efa6b`, titled
  ``MUI. Add `/* union */` markers to sealed unions`` (**88 files, +293 / -146 lines**), and
  `mui-int-indices` at `129933643ff14cbce0d35128f8d61c0549f12c7d`, titled
  ``MUI. Use `Int` for indices`` (**8 files, +9 / -9 lines**).
- The union branch contains only the generated union markers and related spacing between adjacent
  union declarations. The numeric branch changes nine index properties in Base and Material to `Int`,
  preserving optionality, including `Step.index: Int?`. The topics have disjoint file sets and neither
  branch includes the other's changes or changes from open PRs #2914, #2922 and #2923.
- Fork `master` is still at `6cf8ce4e66` and checked out in the original checkout. Its sole remaining
  working-tree change is `TreeViewSelectionValue.kt` (**1 file, +39 lines**), unstaged and uncommitted,
  exactly matching the pre-split helper delta. The staged diff is empty. Existing branch, tag and remote
  refs are unchanged. The separate `build/transfers/pr-2923-follow-up.patch` is preserved.
- Verification: each committed patch exactly matches its selected pre-split generator delta, each
  commit's parent is the original `master`, and whitespace checks pass. Combining both committed
  patches with the `master` remainder exactly reconstructs every file of the original 97-file diff.
  Adding the three excluded open PRs and the separate theme follow-up then reproduces all **632** mapped
  generated files and their file sets byte-for-byte.
- All **778** source declaration hashes still match the preceding successful clean build, including
  successful library/playground compilation and the 21 runtime-export tests. No generator code changed;
  no additional source build or browser run was needed for this Git-only split. No fork builds or tests
  were run. Temporary topic worktrees were removed after the verified commits; both local branches remain.
  Nothing was pushed or posted to GitHub. Generator-repository documentation remains uncommitted.

Next step: human push and upstream PR creation for the two topic branches, followed by separate review
of the remaining helper diff and theme follow-up.

## Corrected upstream ancestry for topic branches (2026-11-06)

After pushing the two topic branches, the user found nine commits in each upstream PR comparison.
The initial split had verified each topic against fork `master`, but had missed that the fork contained
copies of upstream commits with different SHAs. Those copies were therefore additional ancestors in
the comparison with actual upstream `master`; a one-commit delta against the fork was insufficient.

- Fetched actual `upstream/master` at `2d429a93fb`. All 96 topic-file baselines matched their upstream
  versions byte-for-byte, so each original topic commit was cherry-picked independently onto that exact
  upstream revision, preserving its complete patch and title.
- Updated local `mui-union-markers` to `706d6b761f92142b7cb071361156ff29f5aa45af` and
  `mui-int-indices` to `39afca4b07bae4cb6b30bb23e3609b8701d0779b`. Their previous tips remain available
  under `refs/backup/mui-union-markers-before-upstream-base` and
  `refs/backup/mui-int-indices-before-upstream-base`.
- Verified each parent and merge base is the fetched upstream revision and
  `git rev-list --left-right --count upstream/master...<topic-branch>` returns `0 1`.
  The corresponding three-dot diffs exactly equal the original selected patches: **88 files,
  +293 / -146 lines** for union markers and **8 files, +9 / -9 lines** for numeric indices.
  Both comparisons pass `git diff --check` and contain no unrelated commits or files.
- The fork's original checkout was already on `master` at `b52c5a7c98` when this correction began.
  That revision, its index entries and its exact unstaged `TreeViewSelectionValue.kt` diff
  (**1 file, +39 lines**) are unchanged. The separate theme follow-up patch is preserved.
- All **778** source declaration hashes still match the preceding successful clean build. No generator
  code or declaration content changed, so no additional source build, browser run or fork build/test
  was performed. Temporary detached worktrees were removed after verification. No push or GitHub write
  was performed; the published branches still need the human's update using `--force-with-lease`.

Next step: human force-with-lease push of the two corrected topic branches, then upstream PR creation.
Commit titles remain ``MUI. Add `/* union */` markers to sealed unions`` and
``MUI. Use `Int` for indices``.

## Shared `mui.types.Value` and `TreeViewSelectionValue` typealias (2026-11-06)

Addressed the deferred review thread from [`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927)
([`#discussion_r4484280313`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4484280313),
[`#discussion_r4485031537`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4485031537)) by
introducing a shared generic single/multi-value union type `mui.types.Value<T : Any>` and aliasing
`TreeViewSelectionValue` to it:

- **`mui/types/Value.kt` (`TYPES_VALUE` in `Generator.kt`):** emits `sealed /* union */ external interface Value<T : Any>`,
  `inline fun <T : Any> Value(value: T?): Value<T>? = unsafeCast(value)` (preserving single-select `T | null` runtime
  shape required by `TreeViewSelectionPlugin.js`), `inline fun <T : Any> Value(value: ReadonlyArray<T>): Value<T> = unsafeCast(value)`,
  and strict `Value<T>?.asArray(): ReadonlyArray<T>` and `Value<T>.single(): T` extensions matching
  `react.select.Value` (`require(value is Array<*>)` / `require(value !is Array<*>)`).
- **`muix/tree/view/TreeViewSelectionValue.kt` (`TREE_VIEW_SELECTION_VALUE` in `Generator.kt`):** replaced the custom
  `TreeViewSelectionValue(...)` factories and lenient `asMultiple()` / `asSingle()` adapters with
  `typealias TreeViewSelectionValue = Value<TreeViewItemId>` (and added `"mui.types.Value"` to `IMPORTED_FQNS`).
- **Playground samples:** updated `RichTreeViewSelectionSample.kt` (`Value(selectedItem)` and `itemIds?.single()`) and
  `playground/samples/treeview/consumer/TreeView.kt` (`Value(selected)` and `keys.asArray().toSet()`).

Verification:

- `./gradlew :mui-kotlin:clean build :playground:compileKotlinJs` passed with 0 errors and 21/21
  `testRuntimeExports` tests passing.
- A second `./gradlew :mui-kotlin:clean build` produced byte-for-byte identical output across all **779** generated
  files (`shasum -a 256` before and after matched with empty `diff -u`).
- Browser verification in Chrome against `./gradlew :playground:jsViteDev`:
  - `#/treeview/rich-tree-view-selection`: mounted with `selected=item-1 lastClick=-`; clicking `"Item 2"` updated
    status to `selected=item-2 lastClick=item-2`.
  - `#/treeview/consumer-tree-view`: completed with `selected=a-2 changes=2 phase=Done` and
    `[playground] treeview/consumer-tree-view consumer-tree-view-checks: 9/9 PASS`.
  - `#/all`: settled at `[playground] all: 36 samples mounted, 105/105 checks PASS` (`#all-summary` =
    `samples=36 checks=105/105`, `failed=0`).
- `buildSrc:test` was not run (see pre-existing baseline mismatch noted above).

## Shared `mui.types.Value` fork branch commit (2026-11-06)

Following the user's commit of the `mui.types.Value` generator change in `mui-kotlin` (`db33c3d8`), the user
explicitly requested transferring the generated delta to a new fork branch and committing it locally without pushing:

- **Branch & ancestry:** created local branch `mui-value` directly from `upstream/master` (`f018fed8af9501498c2028cfb5d909653e032d75`,
  which already includes merged PRs `#2927`, `#2928`, and `#2929`) and committed `a58a7347ab3330a20f0651d4801a112a2087354c`
  with title ``MUI. Add shared `Value` type for `TreeViewSelectionValue` ``.
- **Transferred files (2 files, `+42 / -4` lines):**
  - `kotlin-mui-system/src/jsMain/generated/mui/types/Value.kt` (new file, `+40` lines)
  - `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view/TreeViewSelectionValue.kt` (`+2 / -4` lines)
- **Verification:** `git rev-list --left-right --count upstream/master...mui-value` is `0 1`; all files in
  `kotlin-mui-system/src/jsMain/generated/mui/types` and `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view`
  match the generator (`db33c3d8`) byte-for-byte (`diff -ru` empty); `git diff --check upstream/master...mui-value`
  passes; branch remote config is set to `origin` (`refs/heads/mui-value`); fork `master` working tree and index
  were preserved untouched, and nothing was pushed.
