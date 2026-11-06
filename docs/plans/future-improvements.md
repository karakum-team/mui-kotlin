# Future improvements (standing backlog)

Cross-cutting work that is intentionally out of scope for the current task but must not be forgotten.
Add new items here rather than burying them in a one-off TODO.

## Resume here

- **Highest priority:** replace the custom `TreeViewSelectionValue` factory/adapter helpers in `muix.tree.view` with a
  common single/multi-value representation (follow-up to merged
  [`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927); see
  [TreeViewSelectionValue common single/multi-value representation](#highest-priority-treeviewselectionvalue-common-singlemulti-value-representation-pr-2927-follow-up)
  below).
- **Recorded phase:** Base UI migration is in progress; the detailed order and checkpoint live in
  [the Base UI plan](base-ui.md). This file is the cross-cutting backlog, not a second module checklist.
- **Next step:** start with the highest-priority `TreeViewSelectionValue` follow-up below unless the user selects
  another item. For a Base UI continuation, read that plan's resume block and
  [verification history](../history/base-ui.md) first. For general generator capabilities, use
  [the generator plan](generator.md).
- **Prerequisites:** compare recorded versions, completion claims and follow-ups with current code and installed
  dependencies; do not treat historical build warnings as newly verified defects.
- **Completion:** apply the relevant [agent rules](../../AGENTS.md#task-specific-rules), update the owning plan's
  next step and append results to the relevant history. Keep only the strategic summary here.
- **Latest recorded evidence:** [Base UI history](../history/base-ui.md) and
  [MUI v9 history](../history/mui-v9.md); new work needs its own verification.

---

## Highest priority: `TreeViewSelectionValue` common single/multi-value representation (PR #2927 follow-up)

**Status:** open · **Priority:** highest (agreed follow-up PR after merging
[`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927) in `67f0cceaf3`).

Upstream review on `kotlin-muix-tree-view/src/jsMain/generated/muix/tree/view/TreeViewSelectionValue.kt`
([`#discussion_r4484280313`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4484280313),
[`#discussion_r4485031537`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4485031537)):

- **Problem statement:** single-or-multiple value typing (`TreeViewSelectionValue<Multiple>` =
  `TreeViewItemId | null` vs `ReadonlyArray<TreeViewItemId>`, controlled by `multiSelect?: Multiple`) is a common
  pattern across wrappers (analogous to `kotlin-react-select`), so custom per-component adapter helpers are not
  expected in `TreeViewSelectionValue.kt`.
- **Reviewer guidance:**
  - *"Common type expected instead. Problem is common."*
    ([`#discussion_r4484280313`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4484280313))
  - *"You can leave interface. Adapters aren't expected"*
    ([`#discussion_r4485031537`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4485031537))
- **Current state in generator (`buildSrc/src/main/kotlin/karakum/mui/Generator.kt`, `TREE_VIEW_SELECTION_VALUE`):**
  emits `sealed /* union */ external interface TreeViewSelectionValue` together with four custom `inline` helper
  functions (`TreeViewSelectionValue(ids: ReadonlyArray<TreeViewItemId>)`,
  `TreeViewSelectionValue(id: TreeViewItemId?)`, `asMultiple()`, `asSingle()`), which are also used in playground
  samples (`SimpleTreeViewSample.kt`, `RichTreeViewSelectionSample.kt`, `ConsumerTreeViewSample.kt`).
- **Required follow-up:**
  1. Inspect how single/multi-value selection is represented in `kotlin-wrappers` (including `kotlin-react-select` and
     shared wrapper types/conventions).
  2. Remove the four custom adapter/factory functions from `TREE_VIEW_SELECTION_VALUE` in `Generator.kt` (keeping
     `sealed /* union */ external interface TreeViewSelectionValue` or aligning it with the shared wrapper type as
     appropriate).
  3. Update `SimpleTreeViewProps` / `RichTreeViewProps` (`selectedItems`, `defaultSelectedItems`,
     `onSelectedItemsChange`) and playground Tree View samples to use the agreed representation, verify with clean
     builds and tests, and prepare a separate follow-up PR to `JetBrains/kotlin-wrappers`.

---

## Migrate `@mui/base` → Base UI (`@base-ui/react`)

**Status:** in progress · **Decoupled from the MUI core release cadence.**
Current status and implementation backlog: [Base UI plan](base-ui.md).

`@mui/base` is frozen at `5.0.0-beta.70` and is **npm-deprecated** ("This package has been replaced by
`@base-ui/react`"). It will not receive a v9 (or any further) release, so it stayed at beta.70 through the
MUI v9 migration. The headless layer now lives as a **separate library**, Base UI (`@base-ui/react`, pinned
to `1.8.0` here), with its own package, versioning, and **different `.d.ts`
shapes** (component anatomy split into parts, different slot/render-prop conventions, no `componentsProps`).

**Already implemented:**

- npm dependency and lockfile entry, `Package.baseUi`, and `generateBaseUiDeclarations(...)`.
- Separate `baseui` output with namespace objects for compound components, props/state declarations,
  state-typed `className` / `style` / `render` helpers, and shared positioning props.
- Nine allow-listed modules with playground samples: `menu`, `slider`, `field`, `fieldset`, `switch`,
  `checkbox`, `accordion`, `number-field`, and `toast`. Toast also exposes `createToastManager` /
  `useToastManager`.

**Remaining:**

1. Follow [the backlog ordered by predictability](base-ui.md#execution-order-by-predictability):
   group 1 (`fieldset`, `switch`, `checkbox`, and diagnostics) is implemented, verified and committed
   in `32dc8f4a`; next is the direct-export prerequisite (gap 21), followed by bounded generator fixes and
   module batches in group 2, then components requiring more interaction verification.
2. Leave the most uncertain design work until the last implementation group: `select`, `combobox`,
   `autocomplete`, `form`, `use-render`, `merge-props`, and the shared generic/utility-type/imperative
   API work. Bring a specific prerequisite forward only if it blocks the selected earlier task.
3. Complete type fidelity and exercise a playground sample for every module. Track initial
   integration and full API completion separately; existing Toast and other module gaps remain open.
4. Retire `@mui/base` as the final compatibility step, after migrating its consumers.
   The four generated Material references are
   `Autocomplete`, `Snackbar`, `Popper`, and `Orientation`; `SliderStylization` (now `playground/src/jsMain/kotlin/playground/samples/base/SliderStylingSample.kt`) in the playground
   still uses the old Slider. Both generation targets and npm dependencies remain enabled today.

## Toolchain follow-ups

Recorded during the [Gradle 9.7 update](../history/mui-v9.md#gradle-wrapper-90--97-update-2026-10-01)
on 2026-10-01. Recheck the current toolchain before acting; these were non-blocking observations.

- Revisit the local JDK / Kotlin JVM-target mismatch when updating either toolchain.
- Replace the two delegated `by registering` declarations in `mui-declarations.gradle.kts` before Gradle 10
  if the deprecation still applies.
- Reassess existing production Vite `use client` / chunk-size warnings when working on bundling.
