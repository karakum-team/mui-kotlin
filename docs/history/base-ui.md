# Base UI migration — execution and acceptance log

## Current handoff

Group 1 was committed in `32dc8f4a` (2026-10-01); its human commit checkpoint has passed.
Status checked against git history on 2026-11-27.
The bounded prerequisite for Group 2 — **direct component exports (gap 21)** — has been implemented
in the generator and verified with comprehensive unit tests (`BaseUiDirectExportsTest.kt`).
Continue from the [live plan's Resume here block](../plans/base-ui.md#resume-here), proceeding to
integrate the Group 2 modules (`separator`, `button`, `csp-provider`, `checkbox-group`, and `menubar`),
one independently verifiable task at a time. The dated acceptance records below retain their original
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
