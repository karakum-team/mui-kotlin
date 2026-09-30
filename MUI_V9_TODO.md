# MUI v9 migration — handoff / TODO

Migration of the generator from MUI v7 → **v9** (v8 skipped; the suite was realigned so MUI X is also v9).

## Versions (`gradle.properties`)

| package                                    | version                                                              |
|--------------------------------------------|----------------------------------------------------------------------|
| `@mui/material` / `@mui/system`            | `9.4.0`                                                              |
| `@mui/icons-material`                      | `9.4.0`                                                              |
| `@mui/lab`                                 | `9.0.0-beta.9`                                                       |
| `@mui/x-date-pickers` / `@mui/x-tree-view`  | `9.14.0`                                                             |
| `@mui/base`                                | `5.0.0-beta.70` (frozen; retained during [Base UI migration](BASE_UI_TODO.md)) |
| `@base-ui/react`                           | `1.8.0` (six modules included; see [current status](BASE_UI_TODO.md))   |
| kotlin-wrappers BOM                        | `2026.9.3`                                                           |
| kfc                                        | `19.16.0`                                                            |
| kotlin / seskar                            | `2.4.20` / `4.66.0`                                                  |

> mui-x **had** to bump together with core: `@mui/x-tree-view@7` pins its peer to `@mui/material@"^5||^6||^7"`,
> so npm refuses to install it next to `@mui/material@9`. The whole v9 suite installs as one.

## Status

- ✅ `:mui-kotlin:compileKotlinJs` — **0 errors**
- ✅ `:playground:compileKotlinJs` — **0 errors**
- ⏳ Type-quality / review-remark polish — **not yet done** (see "Remaining" below)

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
- Added `DateIo.kt` to the playground using real `@date-io/date-fns 3.2.1` (playground-only; its peer
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
  keys hidden behind `Omit<TreeViewClasses, ...>`. Existing picker class inheritance is unchanged.
- New `playground/src/jsMain/kotlin/TreeViewLoading.kt` exercises `loading`, both new slots, slot props,
  typed loading-row state, and both new CSS-class objects. The existing `items` gap is supplied by a
  sample-local props interface, not a change to the library API. `RichTreeViewSlotProps.loading` and
  `.itemLoader` still use the generator's existing `react.Props` approximation for `SlotComponentProps`;
  callback forms and the loading slot's `itemsCount`/`message` are not given dedicated Kotlin types.
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
- `RichTreeViewSlots` keeps `TreeViewSlots` but not `RichTreeViewItemsSlots` — the internal `RichTreeViewItems`
  type drags in a `<TProps>` generic / `Ref` / slot overrides that don't translate.

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

Covered in `playground/src/jsMain/kotlin/Pickers.kt` and checked in the browser: all 16 new class objects
are referenced through typed members, as is `dayCalendarClasses` (the one whose subpath changed).
`pickersOutlinedInputClasses.root` — an *inherited* key — is confirmed to reach the DOM, which is what
proves the supertype carries the base's keys at runtime. The other 10 pre-existing pickers class objects
are still uncovered.
That page previously read `FC { Fragment.create { MonthCalendar { … } } }`, which discards the element it
builds, so no picker had ever actually rendered there.

## The material theme is a hand-written stub — upstream additions do NOT flow in

`mui.material.styles.Theme` / `ThemeOptions` are emitted as stubs over `mui.system.*`
(`Generator.kt`, `generateStylesDeclarations`), because upstream splits them across
`createThemeNoVars` / `createThemeWithVars` / `createTheme` behind TS conditional types the
converter cannot follow. Consequence: **any member that exists only on the material theme has to
be added to the stub by hand, and a version bump will not surface the omission** — the
regeneration diff stays empty and `compileKotlinJs` stays green.

First instance: `theme.focusVisible`, added in `@mui/material@9.4.0`. It is now on the stub
(`FocusVisible` typealias + `Theme.focusVisible` + `ThemeOptions.focusVisible`) and exercised by
`playground/src/jsMain/kotlin/Theming.kt`. When bumping, diff `styles/createThemeNoVars.d.ts`
between the old and new tarball rather than trusting an empty regeneration diff.

Deliberate simplification kept from before: upstream declares
`ThemeOptions extends Omit<SystemThemeOptions, 'zIndex'>`; the stub inherits `zIndex`.

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
`RuntimeImports.kt` and `RuntimePickerField.kt`, exercising the repaired imports through the generated
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

### Tree View: the behavioural props are still missing (found during the 9.8 → 9.12 bump)

"Generated and green" understated one gap. Compilation could not see it because nothing in the repo
consumed the tree-view declarations until `playground/src/jsMain/kotlin/TreeView.kt` was added.

- **Fixed in the 9.12 bump:** `TreeItemProps` had neither `itemId` (which the component *requires*) nor
  `label`. Both come from `Omit<UseTreeItemParameters, 'rootRef'>`. `useTreeItem/useTreeItem.types.d.ts`
  is now generated `typesOnly` and `UseTreeItemParameters` left `INTERNAL_REJECTED_PARENTS`, so
  `TreeItemProps` extends it and picks up `itemId` / `label` / `disabled` / `disableSelection` / `id`.
    - Caveat: Kotlin also inherits `rootRef`, which the TS `Omit` removes. Harmless (React ignores it),
      but it is one prop of over-exposure.
- **Still missing — `items`, `multiSelect`, `expandedItems`, `selectedItems` and every selection /
  expansion callback** on `SimpleTreeViewProps` / `RichTreeViewProps`. These sit behind
  `UseTreeViewStoreParameters<TStore>`, declared as
  `Omit<Parameters<TStore['updateStateFromParameters']>[0], 'isRtl'>` — an indexed access into a
  method's parameter tuple. Resolving it needs a real TypeScript checker; this generator is
  text-based, so no `typesOnly` trick can recover it. The only route is a hand-written stub in the
  `PICKERS_STUBS` style (`Generator.kt`), which carries a real staleness cost and was left out of the
  version bump deliberately. **Consequence: the public Kotlin API still cannot supply `RichTreeView.items`;
  only the children-driven `SimpleTreeView` is usable without a local extension.** The 9.14 loading sample
  supplies a sample-local `items` shape to exercise the new API; it does not close this library gap.

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
- **Still-dead v7-era tree-view code, not yet removed** (each verified absent from the 9.12 `.d.ts`,
  left alone to keep the bump reviewable): the `TreeItem2` / `TreeItem2Icon` / `TreeItem2Provider` /
  `useTreeItem2` exclusion set and the `TreeItem2DragAndDropOverlay` branches in
  `generateTreeViewDeclarations`; `adaptTreeView()` in full (there is no `TreeView` directory in v9);
  and the `TreeItem2Props` replacement in `adaptRichTreeView()` (its `RichTreeViewSlotProps<R, Multiple>`
  replacement *is* still live).

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

1. ~~Align `mui-icons-material` to `9.1.2` for consistency.~~ **N/A** — `@mui/icons-material@9.1.2`
   was never published; `9.1.1` is the latest on npm (the icons package doesn't ship a release for
   every core patch). Already on the newest available version; nothing to bump.
2. `@mui/base` → Base UI migration is **in progress**: `menu`, `slider`, `field`, `accordion`,
   `number-field`, and `toast` are generated with playground samples. Full type and module coverage
   remains open — see [BASE_UI_TODO.md](BASE_UI_TODO.md).
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
