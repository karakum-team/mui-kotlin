# Base UI migration — execution and acceptance log

## Current handoff

Group 1 was committed in `32dc8f4a` (2026-10-01); its human commit checkpoint has passed.
Status checked against git history on 2026-11-27.
The bounded prerequisites for Group 2 — **direct component exports (gap 21)** and **simple named aliases/unions (gap 5 follow-up and gap 8)** — have been implemented and verified.
All five direct component export modules, `avatar` (BUI-010), `tabs` (BUI-011), and `collapsible` (BUI-012) are integrated, browser-verified (177/177 checks in `#/all`), and accepted.
`mui.system.Union` has been completely eliminated from `package baseui`.
Continue from the [live plan's Resume here block](../plans/base-ui.md#resume-here), proceeding to
the next Group 2 task: resolve concrete cross-module references (the bounded part of gap 6), then integrate
`input` and `toolbar`. The dated acceptance records below retain their original
pre-commit wording and test results.
Append new work and verification records here.

## Group 1 acceptance record (2026-10-01)

Scope: groups 1 and 2 of [Base UI plan](../plans/base-ui.md#execution-order-by-predictability).
The user moved the first human review/commit checkpoint to the end of group 1 (Fieldset, Switch,
Checkbox, and the listed diagnostics). Component samples have separate implementation agents and
disjoint ownership; generation, compilation, and acceptance are coordinated centrally. Compilation
and independent review are restored. Stop before group 2.
Integration and full API completion are recorded separately; an unchecked acceptance item has not passed.

**Group 1 status (2026-10-01):** coordinator acceptance and independent review complete; ready for
human review and commit. No autonomous staging or commits; group 2 has not started.

## BUI-001 — Fieldset

- **Status:** integrated, verified, and accepted; awaiting human review and commit with group 1.
- **Baseline:** `a3919d02`, clean working tree at task start.
- **Upstream:** installed `@base-ui/react@1.8.0`; the earlier organizational plan was drafted before
  the separately committed dependency/toolchain updates. Use current installed typings and exports.
- **Implementation/review model:** separate `gpt-6.1-sol` agents, reasoning `xhigh`.
- **Goal:** expose `Fieldset.Root` and `Fieldset.Legend`, their props/state and state-typed helpers,
  and add a browser-verifiable playground sample.
- **Scope:** generator allow-list and any strictly necessary bounded conversion fix; generated
  Fieldset output; playground sample and App wiring; migration status and this acceptance record.
- **Not included:** Switch/Checkbox, dependency upgrades, unrelated generator fixes, or groups 3–5.
- **Expected contract:** Root renders a native fieldset; Legend defaults to a div whose generated id
  labels the fieldset through `aria-labelledby`. Both states expose `disabled: Boolean`.
- **Existing convention:** value-or-callback element props retain their established shared types
  with typed extension helpers. No Fieldset-specific type widening or new exceptions are planned.

### Acceptance checklist

- [x] Root/Legend runtime exports, props, state, inheritance, and helpers match upstream.
- [x] Both parts are mounted; helper calls compile against their own state types.
- [x] Browser verifies accessible group/label linkage, enabled → disabled → enabled behavior,
  native descendant control behavior, keyboard navigation, and render-prop forwarding.
- [x] Library and playground compile with zero errors.
- [x] Clean build succeeds; the candidate generated file list and hashes are reproduced exactly.
- [x] Existing generated declarations are unchanged; any deviation is separately explained.
- [x] Independent review has no unresolved blocker/major findings.
- [x] Coordinator acceptance complete; changes ready for human review and commit.

### Evidence and decisions

- Modern Web Guidance `forms` guide consulted before playground work: visible associated labels,
  accessible grouping, preserved focus behavior, and keyboard verification. Base UI's documented
  div-based Legend with automatic ARIA association remains the runtime contract being tested.
- Initial compile and clean build passed; all 749 generated filenames/content hashes were identical
  across clean regeneration (aggregate SHA-256
  `d454011554e7133d5761afaa15c6e27f3f64bd6a4425581675dc44f3c5fca670`). Existing generated files unchanged.
- Independent Fieldset review and final combined group 1 review: zero findings.
- Browser: edits/actions round-trip; all five native descendant controls disable and ignore activation;
  Tab skips from the outside toggle to the outside ref-check button. Enabling restores the full six-stop
  Tab sequence and visible focus outlines without losing values. Nested disabled state, both Legend
  links, render markers/children, and `FIELDSET` / `DIV` refs match upstream.
- Human commit: pending until the group 1 checkpoint. Switch/Checkbox implementation is included in
  the same batch; group 2 starts only after that checkpoint.

## BUI-002 — Switch

- **Status:** integrated, compiled, browser-verified, and independently reviewed; accepted for handoff.
- **Scope:** allow-list, Root/Thumb declarations and typed helpers, controlled/uncontrolled playground
  use, Field label/description, disabled/read-only state, hidden-input ref and form values.
- **Known limitation:** shared `Omit` member selection (gap 19) and the existing element-props/ref
  conventions remain open. Integration does not close full API fidelity.
- **Implementation:** separate `gpt-6.1-sol` agent, reasoning `xhigh`; coordinator owns shared wiring.
- **Browser:** click/Space and native-label activation round-trip; controlled/uncontrolled switches
  submit `enabled/disabled` and `on/off` respectively. `inputRef` reports the native checkbox and
  expected id/name/checked state. Disabled inputs/roots reject activation and are skipped by Tab;
  read-only roots retain their values and callback status. Root/Thumb state classes/styles/titles and
  automatic accessible names/descriptions are present.
- **Fixes during acceptance:** two ids changed to `ElementId`; local React state variables renamed
  to prevent DSL prop shadowing/render-time state updates. Clean build and browser checks repeated.

## BUI-003 — Checkbox

- **Status:** integrated, compiled, browser-verified, and independently reviewed; accepted for handoff.
- **Scope:** allow-list, Root/Indicator declarations and typed helpers, checked/unchecked/mixed state,
  Field label/description and disabled precedence, render forwarding, and focus ref.
- **Known limitation:** shared `Omit` member selection (gap 19) and the existing element-props/ref
  conventions remain open. CheckboxGroup and its parent/group scenarios belong to group 2.
- **Implementation:** separate `gpt-6.1-sol` agent, reasoning `xhigh`; coordinator owns shared wiring.
- **Browser:** click/Space, label activation, and three-state cycling work. Mixed state is reflected
  in `aria-checked="mixed"`, native input `indeterminate`, indicator mark, and typed-state markers.
  Field disabled state overrides the Root's explicit false, suppresses activation and Tab focus;
  enabling restores interaction. The ref-focus button targets the root; keyboard focus is visible.

## BUI-004 — Diagnostics

- **Status:** implemented, tested, and independently reviewed; accepted for handoff.
- **Scope:** unused hand-written `BASE_UI_STUBS` reporting (gap 16); remove misleading Toast factory/
  hook omission messages while retaining real omission diagnostics (gap 20).
- **Expected effect:** diagnostics only; existing generated public API remains byte-identical.
- **Validation:** all four new diagnostic tests pass. Generation emits one unused `FloatingPortalProps`
  warning, omits both false Toast warnings, and retains real Menu Handle/factory omissions. Existing
  generated declarations, including Toast's namespace, remain byte-identical.

## Group 1 — combined acceptance

- [x] All three modules and all six parts are generated and mounted; inherited state and callback
  types inspected against installed upstream 1.8.0.
- [x] `:mui-kotlin:compileKotlinJs` / `:playground:compileKotlinJs` — zero errors.
- [x] `:buildSrc:test` — 15 tests passed, including four new diagnostics tests.
- [x] `:mui-kotlin:testRuntimeExports` — all 21 tests passed; generation validates 11,171 bindings.
- [x] `:mui-kotlin:clean build` — successful, including the corrected production playground bundle.
- [x] The sorted filename/content-hash aggregate of all 759 generated files matches before/after
  clean regeneration: `a1509d3ad0368ff423566dc15113c9ed0564e6eec9fd31191969bd09e88e1e22`.
  There are 15 new files; all 744 pre-existing generated files are unchanged.
- [x] Fresh `:playground:jsDevelopmentExecutableCompileSync` / `:playground:jsViteDev` browser checks
  on the task's own port 5175. Screenshots inspected for all three samples, including content fitting
  the actual narrow viewport of 500 CSS pixels. NumberField increment/decrement and Toast create/
  component-close smoke checks pass with the new samples still mounted.
- [x] No JS runtime exceptions after the Switch sample correction. The existing render callback-name
  warning and favicon 404 remain. Gradle browser tests have no sources; checks above are interactive.
- [x] Independent combined review has no unresolved findings.
- [x] Coordinator acceptance complete; ready for human review and commit.

### Final independent review (2026-10-01)

Separate `gpt-6.1-sol` / `xhigh` read-only review following `.agents/subagents/mui-code-review.md`: **zero findings**
across all 27 changed paths (staged and unstaged). All new declarations and samples were checked
against installed upstream typings and JS semantics; diagnostics and test results were inspected.
The reviewer independently reproduced the current 759-file fingerprint above and confirmed that
all 744 pre-existing generated files are unchanged. Compilation, clean-regeneration, and browser
results are coordinator evidence, not additional executions by the reviewer. No new undocumented
type losses were found; the known shared limitations below remain open.

Modern Web Guidance influenced visible associated labels, non-color state indicators, preserved
render props/refs, and keyboard focus checks. Integration is complete for these modules; shared
utility-type selection, reason aliases, value-or-callback unions, and polymorphic ref/FC fidelity
remain explicit backlog items. No group 2 implementation has started.

## Documentation relocation and handoff refresh (2026-11-04)

- Moved the canonical plan to `docs/plans/base-ui.md` and this log to `docs/history/base-ui.md`;
  updated repository links, sample references, and the generated stub's documentation through its generator.
- Confirmed `32dc8f4a` is an ancestor of HEAD. Group 1's commit checkpoint is complete; direct exports
  (gap 21) remain the next prerequisite. No group 2 implementation was added in this task.
- `:mui-kotlin:build :playground:compileKotlinJs` and `:mui-kotlin:clean build` succeeded;
  all 21 runtime-export tests passed. All 777 generated filenames/content hashes match across clean
  regeneration. The only generated change from the starting revision is the plan path in `baseui/Stubs.kt`.
- Browser checks were not repeated: changes affect documentation paths and sample reference metadata,
  with no changes to types, components, inheritance, or sample interaction logic.

## Base UI 1.8.0 → 1.9.0 update (2026-11-24)

- Bumped `base-ui-react.version` to `1.9.0` in `gradle.properties`.
- Synchronized `.kotlin-locks/js/package-lock.json` via `kotlinUpgradePackageLock --rerun-tasks`.
- Integrated `filter-dropdown` internal types (`FilterDropdownInput`, `FilterDropdownClear`, `FilterDropdownEmpty`)
  and `menu/filter-provider/MenuFilterProviderOptions` in `BASE_UI_EXTRA_FILES` in `Generator.kt`.
- Added `BaseUIHighlightEventDetails` stub in `baseui/Stubs.kt`.
- Generated 5 new Menu filtering components on the `Menu` namespace object:
  `Menu.FilterProvider`, `Menu.Input`, `Menu.List`, `Menu.Clear`, `Menu.Empty` (25 parts total).
- Updated `MenuSample.kt` with `Menu.FilterProvider`, `Menu.Input`, `Menu.Clear`, `Menu.List`, and `Menu.Empty`,
  and updated covers to 25 parts.
- Verified in Chrome DevTools MCP: `#/baseui/menu` mounted and searchable without errors, and `#/all` smoke run
  (146/146 checks PASS, 0 failures).
- All 21 runtime export tests passed; `./gradlew :mui-kotlin:clean build` completed with 0 errors.

## Group 2 prerequisite: Direct component exports (gap 21) (2026-11-27)

- **Scope:** Generator prerequisite for Group 2 (gap 21). Implemented direct component value generation
  for Base UI modules without a namespace object (`module.namespace == null`).
- **Generator enhancements:**
  - Added `resolveOuterExportRenames`: parses outer re-exports from `index.d.ts` when a module exports
    directly from `./index.parts.js` (e.g. `direction-provider`, preserving `Provider as DirectionProvider`).
  - Added `extractComponentKDoc`: extracts official JSDoc documentation preceding `declare const` or
    `declare function` declarations from upstream definition files.
  - Added `baseUiDirectComponentDeclaration`: generates typed `external val ${part.alias}: react.FC<${part.declaredName}Props>`.
  - Added `baseUiDirectExports`: filters and exposes components whose props exist in `declaredTypes`, logging
    unexposed diagnostics for missing props interfaces.
  - Updated `Generator.kt`: records `bodiesByFile`, groups direct exports by file, attaches `@file:JsModule("@base-ui/react/<module.id>")`
    and `@file:Suppress(...)`, appends the component declarations, and registers them in `RuntimeBindings`.
- **Verification:**
  - Added unit test suite `BaseUiDirectExportsTest.kt` covering outer rename resolution, KDoc extraction,
    direct component declaration formatting, namespace object filtering, unexposed diagnostics, and installed package discovery.
    100% pass via `./gradlew :buildSrc:test`.
  - Re-generation check: with existing 9 modules in `BASE_UI_MODULES`, generated declarations under `mui-kotlin/src/jsMain/kotlin`
    remain byte-for-byte identical (clean working tree).
  - Test verification: tested enabling `separator`, successfully generated `Separator.kt` with `@file:JsModule("@base-ui/react/separator")`
    and `external val Separator: FC<SeparatorProps>`, verified 11182 runtime bindings with Babel parser in `runtime-exports.cjs`,
    and compiled library and playground with 0 errors. Reverted `separator` from allow-list to maintain bounded task scope.
  - Full build: `./gradlew build` completed with 0 errors, all 21 runtime-export tests PASS.

## Group 2 — BUI-005: Separator direct export (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with Group 2 direct component export prerequisite in place.
- **Scope:** add `"separator"` to `BASE_UI_MODULES` in `Generator.kt`; emit `@file:JsModule("@base-ui/react/separator")` and `external val Separator: FC<SeparatorProps>` with official JSDoc; verify runtime export bindings; create interactive playground sample `SeparatorSample.kt` with horizontal, vertical, and dynamic orientations, state-typed `className`/`style`/`render` helpers, and register route `#/baseui/separator` in `Registry.kt`.
- **Generated declarations:** `Separator.kt` gained `@file:JsModule("@base-ui/react/separator")` and `external val Separator: FC<SeparatorProps>`. Pre-existing `SeparatorProps`, `SeparatorState`, and `Separator.ext.kt` helpers remain intact. All other 758 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,182 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/separator`:
      - All 3/3 checks PASS (`Separator component is loaded`, `Orientation.horizontal is distinct from vertical`, `dynamicOrientation initialized`).
      - Horizontal separator renders `<div role="separator" data-orientation="horizontal" aria-orientation="horizontal" class="bui-separator bui-separator--horizontal">`.
      - Vertical separator renders `<div role="separator" data-orientation="vertical" aria-orientation="vertical" class="bui-separator bui-separator--vertical">`.
      - Interactive toggle button switches dynamic separator between horizontal and vertical, updating `data-orientation`, state-dependent classes and styles, and `StatusLine` (`orientation=vertical toggles=1`).
      - Custom element rendering verified: `render { props, _ -> hr.create { +props } }` renders `<hr role="separator" data-orientation="horizontal" aria-orientation="horizontal" class="bui-separator-hr">`, forwarding accessibility attributes.
    - `#/all`: all 37 samples mounted, settled summary `samples=37 checks=149/149` (all PASS, 0 failures).

## Group 2 — BUI-006: Button direct export (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with `separator` integrated.
- **Scope:** add `"button"` to `BASE_UI_MODULES` in `Generator.kt`; emit `@file:JsModule("@base-ui/react/button")` and `external val Button: FC<ButtonProps>` with official JSDoc; generate `ButtonProps` (inheriting `NativeButtonProps`, `BaseUiButtonProps`), `ButtonState` (`disabled`), and `Button.ext.kt` (`className`, `style`, `render` helpers); verify runtime export bindings; create interactive playground sample `ButtonSample.kt` with clicks counter, disabled toggle, `focusableWhenDisabled` accessible disabled button check, state-typed `className`/`style` helpers, and custom element rendering via `render` with `nativeButton = false`; register route `#/baseui/button` in `Registry.kt`.
- **Generated declarations:** `Button.kt` and `Button.ext.kt` added under `baseui/`. Pre-existing 759 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,183 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/button`:
      - All 3/3 checks PASS (`Button component is loaded`, `clicks state is non-negative`, `button disabled state is boolean`).
      - Click interaction: clicks increment on primary button.
      - Disabled interaction: primary button disables, sets `disabled` and `data-disabled` attributes, applies `.bui-btn--disabled` class via `state.disabled`.
      - `focusableWhenDisabled`: renders `<button aria-disabled="true" data-disabled="" tabindex="0">` without native `disabled` attribute, remaining keyboard focusable while communicating disabled state.
      - Custom element rendering: `render { props, state -> a.create { +props; href = "#custom-link" } }` with `nativeButton = false` renders `<a role="button" href="#custom-link" class="bui-btn bui-btn--link">` with 0 console warnings or errors.
      - `StatusLine` (`clicks=1 disabled=false focusable=true`) updates reactively.
    - `#/all`: all 38 samples mounted, settled summary `samples=38 checks=152/152` (all PASS, 0 failures).

## Group 2 — BUI-007: CSPProvider direct export (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with `separator` and `button` integrated.
- **Scope:** add `"csp-provider"` to `BASE_UI_MODULES` in `Generator.kt`; emit `@file:JsModule("@base-ui/react/csp-provider")` and `external val CSPProvider: FC<CSPProviderProps>` with official JSDoc; generate `CSPProviderProps` (`PropsWithChildren`, `nonce`, `disableStyleElements`); verify runtime export bindings; create interactive playground sample `CSPProviderSample.kt` with nonce rotation, disableStyleElements toggle, and child content verification; register route `#/baseui/csp-provider` in `Registry.kt`.
- **Generated declarations:** `CSPProvider.kt` added under `baseui/`. Pre-existing 761 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,184 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/csp-provider`:
      - All 3/3 checks PASS (`CSPProvider component is loaded`, `nonce state is non-empty`, `disableStyleElements is boolean`).
      - Nonce rotation interaction: clicking "Rotate nonce" updates active nonce in `StatusLine` (`nonce=csp-nonce-2`).
      - Disable style elements toggle: clicking button updates `disableStyleElements` in `StatusLine` (`disableStyleElements=true`).
      - Provider wraps child content seamlessly without errors.
    - `#/all`: all 39 samples mounted, settled summary `samples=39 checks=155/155` (all PASS, 0 failures).

## Group 2 — BUI-008: CheckboxGroup direct export (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with `separator`, `button`, and `csp-provider` integrated.
- **Scope:** add `"checkbox-group"` to `BASE_UI_MODULES` in `Generator.kt`; emit `@file:JsModule("@base-ui/react/checkbox-group")` and `external val CheckboxGroup: FC<CheckboxGroupProps>` with official JSDoc; generate `CheckboxGroupProps` (inheriting `BaseUiDivProps`, with `value`, `defaultValue`, `onValueChange`, `allValues`, `disabled`), `CheckboxGroupState` (`FieldRootState`, `disabled`), and `CheckboxGroup.ext.kt` (`className`, `style`, `render` helpers); verify runtime export bindings; create interactive playground sample `CheckboxGroupSample.kt` with parent/child checkboxes, automatic indeterminate state, group-level disabled state, and select/clear controls; register route `#/baseui/checkbox-group` in `Registry.kt`.
- **Generated declarations:** `CheckboxGroup.kt` and `CheckboxGroup.ext.kt` added under `baseui/`. Pre-existing 762 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,185 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/checkbox-group`:
      - All 3/3 checks PASS (`CheckboxGroup component is loaded`, `selected count is non-negative`, `group disabled is boolean`).
      - Initial state: 1 fruit selected (`apple`), parent checkbox displays minus indicator (`aria-checked="mixed"`).
      - "Select all" interaction: all 3 fruits selected, parent checkbox updates to checkmark (`aria-checked="true"`, `data-checked=""`), `StatusLine` shows `selected=apple,banana,orange count=3`.
      - "Clear all" interaction: all fruits deselected, parent checkbox displays unchecked (`aria-checked="false"`).
      - "Toggle group disabled" interaction: sets `data-disabled` on both `CheckboxGroup` and child checkboxes.
    - `#/all`: all 40 samples mounted, settled summary `samples=40 checks=158/158` (all PASS, 0 failures).

## Group 2 — BUI-009: Menubar direct export (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with `separator`, `button`, `csp-provider`, and `checkbox-group` integrated.
- **Scope:** add `"menubar"` to `BASE_UI_MODULES` in `Generator.kt`; emit `@file:JsModule("@base-ui/react/menubar")` and `external val Menubar: FC<MenubarProps>` with official JSDoc; generate `MenubarProps` (inheriting `BaseUiDivProps`, with `disabled`, `modal`, `loopFocus`, `orientation`), `MenubarState` (`orientation`, `modal`, `hasSubmenuOpen`), and `Menubar.ext.kt` (`className`, `style`, `render` helpers); verify runtime export bindings; create interactive playground sample `MenubarSample.kt` coordinating File and Edit dropdown menus, menu item actions, and menubar-level disabled state; register route `#/baseui/menubar` in `Registry.kt`.
- **Generated declarations:** `Menubar.kt` and `Menubar.ext.kt` added under `baseui/`. Pre-existing 764 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,186 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/menubar`:
      - All 3/3 checks PASS (`Menubar component is loaded`, `lastAction state is non-empty`, `menubar disabled is boolean`).
      - Menubar mounts with `role="menubar"`, `aria-orientation="horizontal"`.
      - File menu opens on trigger click, displaying "New File" and "Open File".
      - Clicking "New File" updates `StatusLine` (`lastAction=New File disabled=false`).
      - "Disable Menubar" sets `data-disabled` on triggers and disables menu interactions.
    - `#/all`: all 41 samples mounted, settled summary `samples=41 checks=161/161` (all PASS, 0 failures).

## Group 2 — Gap 5 follow-up & Gap 8: Simple named aliases & literal unions (2026-11-27)

- **Status:** implemented, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with all five direct component export modules (`separator`, `button`, `csp-provider`, `checkbox-group`, and `menubar`) integrated.
- **Scope:**
  - In `BaseUi.kt`, implement `harvestBaseUiExportedTypes` to scan all active `.d.ts` files for top-level `export type` definitions and string-literal unions (`MenuRootOrientation`, `InputMode`, etc.), seeding them into `baseUiKnownTypes` in `Generator.kt`.
  - In `UnionFinder.kt`, add Base UI literal union properties (`instant`, `thumbAlignment`, `thumbCollisionBehavior`, `positionMethod`, `priority`) to `UNION_PROPERTIES` and capitalize property prefixes for hook/utility containers.
  - In `adaptBaseUiContent`, introduce `ToastRootSwipeDirection` and map `useToastManager` `transitionStatus` to `TransitionStatus`.
  - Completely eliminate `import mui.system.Union` from `package baseui`.
  - Update `SliderSample.kt` in playground to use typed `SliderRootThumbCollisionBehavior.swap`.
- **Generated declarations:**
  - `MenuRoot.kt` & `Menubar.kt`: `orientation` typed as `MenuRootOrientation?` (and `MenubarState.orientation` as `MenuRootOrientation`).
  - `MenuPopup.kt` & `MenuViewport.kt`: `instant` typed as `MenuPopupInstant` / `MenuViewportInstant`.
  - `SliderRoot.kt`: `thumbAlignment` typed as `SliderRootThumbAlignment?` and `thumbCollisionBehavior` as `SliderRootThumbCollisionBehavior?`.
  - `useAnchorPositioning.kt`: `positionMethod` typed as `UseAnchorPositioningPositionMethod?`.
  - `useToastManager.kt`: `priority` typed as `UseToastManagerPriority?` and `transitionStatus` typed as `TransitionStatus?`.
  - `ToastRoot.kt`: `swipeDirection` typed as `ToastRootSwipeDirection` in state and `ToastRootSwipeDirection | ReadonlyArray<ToastRootSwipeDirection>` in props.
  - Zero imports of `mui.system.Union` remain in `package baseui`.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,186 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - `./gradlew build`: full build successful.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/slider`: interactive slider with `thumbCollisionBehavior = SliderRootThumbCollisionBehavior.swap` verified working.
    - `#/all`: all 41 samples mounted, settled summary `samples=41 checks=161/161` (all PASS, 0 failures).

## Group 2 — BUI-010: Avatar namespace & ImageLoadingStatus (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with Gap 5 follow-up and Gap 8 resolved.
- **Scope:** add `"avatar"` to `BASE_UI_MODULES` in `Generator.kt`; emit namespace `external object Avatar` (`Root`, `Image`, `Fallback`); verify `ImageLoadingStatus` (`idle`, `loading`, `loaded`, `error`) is strongly typed in `AvatarRootState` and `AvatarImageProps.onLoadingStatusChange`; create interactive playground sample `AvatarSample.kt` with dynamic image loading status, fallback rendering on error, and standalone fallback; register route `#/baseui/avatar` in `Registry.kt`.
- **Generated declarations:** `Avatar.kt`, `AvatarRoot.kt`, `AvatarImage.kt`, `AvatarFallback.kt`, and their `.ext.kt` files added under `baseui/`. Pre-existing 773 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,187 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/avatar`:
      - All 5/5 checks PASS (`Avatar namespace is loaded`, `Avatar.Root is loaded`, `Avatar.Image is loaded`, `Avatar.Fallback is loaded`, `ImageLoadingStatus values are distinct`).
      - Valid image loads synchronously: status updates to `loaded`, `Avatar.Image` renders, fallback is hidden.
      - Broken image simulation: clicking button switches to broken image, status transitions to `error`, `Avatar.Fallback` renders fallback initials ("JD").
      - Restore valid image: status returns to `loaded`, image displays again.
      - Standalone Fallback-only Avatar: renders initials ("AB") directly without Image component.
    - `#/all`: all 42 samples mounted, settled summary `samples=42 checks=166/166` (all PASS, 0 failures).

## Group 2 — BUI-011: Tabs namespace & ActivationDirection (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with `avatar` integrated.
- **Scope:** add `"tabs"` to `BASE_UI_MODULES` in `Generator.kt`; emit namespace `external object Tabs` (`Root`, `List`, `Tab`, `Panel`, `Indicator`); verify `TabsRootOrientation` resolves to `Orientation` in `TabsRootProps` / `TabsRootState` / `TabsTabState` and `TabsTabActivationDirection` (`left`, `right`, `up`, `down`, `none`) is strongly typed in `TabsRootState` and `TabsTabState`; create interactive playground sample `TabsSample.kt` with tab switching, moving indicator, horizontal and vertical orientation, and disabled tab handling; register route `#/baseui/tabs` in `Registry.kt`.
- **Generated declarations:** `Tabs.kt`, `TabsRoot.kt`, `TabsList.kt`, `TabsTab.kt`, `TabsPanel.kt`, `TabsIndicator.kt`, and their `.ext.kt` files added under `baseui/`. Pre-existing 780 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,188 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/tabs`:
      - All 6/6 checks PASS (`Tabs namespace is loaded`, `Tabs.Root is loaded`, `Tabs.List is loaded`, `Tabs.Tab is loaded`, `Tabs.Panel is loaded`, `TabsTabActivationDirection values distinct`).
      - Tab switching interaction: clicking "Details" tab selects it, updates `StatusLine` (`activeTab=details`), and displays "Details" panel.
      - Orientation toggle: clicking "Toggle orientation" switches layout to vertical, updating `Tabs.Root` and `Tabs.List` classes and `StatusLine` (`orientation=vertical`).
      - Disabled tab: "Disable Settings Tab" sets `data-disabled` and `aria-disabled="true"` on Settings tab, preventing interaction.
      - `Tabs.Indicator` renders and follows active tab.
    - `#/all`: all 43 samples mounted, settled summary `samples=43 checks=172/172` (all PASS, 0 failures).

## Group 2 — BUI-012: Collapsible namespace & Panel (2026-11-27)

- **Status:** integrated, compiled, browser-verified, and accepted.
- **Baseline:** clean working tree with `tabs` integrated.
- **Scope:** add `"collapsible"` to `BASE_UI_MODULES` in `Generator.kt`; emit namespace `external object Collapsible` (`Root`, `Trigger`, `Panel`); generate `CollapsibleTriggerProps`, `CollapsibleTriggerState`, `CollapsiblePanelProps`, `CollapsiblePanelState` (`transitionStatus`), and their `.ext.kt` helpers; remove redundant `CollapsibleRoot.d.ts` from `BASE_UI_EXTRA_FILES` (retaining `useCollapsibleRoot.d.ts`); create interactive playground sample `CollapsibleSample.kt` covering interactive panel expansion/collapse, animated transition state, and disabled state; register route `#/baseui/collapsible` in `Registry.kt`.
- **Generated declarations:** `Collapsible.kt`, `CollapsibleTrigger.kt`, `CollapsiblePanel.kt`, and their `.ext.kt` files added under `baseui/`. Pre-existing `CollapsibleRoot.kt` and 785 generated files remain byte-for-byte identical.
- **Verification:**
  - `./gradlew :mui-kotlin:formatDeclarations`: 0 errors; verified 11,189 runtime bindings.
  - `./gradlew :mui-kotlin:clean build`: clean build passed, reproducing `mui-kotlin/src/jsMain/kotlin` byte-for-byte with zero uncommitted drift (`git diff --exit-code`).
  - `./gradlew :buildSrc:test`: all unit tests passed.
  - `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs`: 0 errors.
  - Browser verification (Chrome DevTools MCP) on `:playground:jsViteDev` (port 5173):
    - `#/baseui/collapsible`:
      - All 5/5 checks PASS (`Collapsible namespace is loaded`, `Collapsible.Root is loaded`, `Collapsible.Trigger is loaded`, `Collapsible.Panel is loaded`, `open state is boolean`).
      - Panel expansion: clicking trigger button expands panel, updates trigger text to "Collapse content ▲", and renders content smoothly.
      - Disabled interaction: "Disable Collapsible" disables root and trigger (`data-disabled`, `aria-disabled="true"`).
    - `#/all`: all 44 samples mounted, settled summary `samples=44 checks=177/177` (all PASS, 0 failures).
