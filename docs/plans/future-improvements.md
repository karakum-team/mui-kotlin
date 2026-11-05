# Future improvements (standing backlog)

Cross-cutting work that is intentionally out of scope for the current task but must not be forgotten.
Add new items here rather than burying them in a one-off TODO.

## Resume here

- **Recorded phase:** Base UI migration is in progress; the detailed order and checkpoint live in
  [the Base UI plan](base-ui.md). This file is the cross-cutting backlog, not a second module checklist.
- **Next step:** for a Base UI continuation, read that plan's resume block and
  [verification history](../history/base-ui.md) first. For general generator capabilities, use
  [the generator plan](generator.md). Select another item here only when it matches the user's request.
- **Prerequisites:** compare recorded versions, completion claims and follow-ups with current code and installed
  dependencies; do not treat historical build warnings as newly verified defects.
- **Completion:** apply the relevant [agent rules](../../AGENTS.md#task-specific-rules), update the owning plan's
  next step and append results to the relevant history. Keep only the strategic summary here.
- **Latest recorded evidence:** [Base UI history](../history/base-ui.md) and
  [MUI v9 history](../history/mui-v9.md); new work needs its own verification.

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
