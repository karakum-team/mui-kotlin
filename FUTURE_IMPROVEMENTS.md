# Future improvements (standing backlog)

Cross-cutting work that is intentionally out of scope for the current task but must not be forgotten.
Add new items here rather than burying them in a one-off TODO.

---

## Migrate `@mui/base` → Base UI (`@base-ui/react`)

**Status:** in progress · **Decoupled from the MUI core release cadence.**
Current status and implementation backlog: [BASE_UI_TODO.md](BASE_UI_TODO.md).

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

1. Follow [the backlog ordered by predictability](BASE_UI_TODO.md#execution-order-by-predictability):
   group 1 (`fieldset`, `switch`, `checkbox`, and diagnostics) is implemented and verified; next are
   bounded generator fixes and module batches in group 2 after the human checkpoint,
   then components requiring more interaction verification.
2. Leave the most uncertain design work until the last implementation group: `select`, `combobox`,
   `autocomplete`, `form`, `use-render`, `merge-props`, and the shared generic/utility-type/imperative
   API work. Bring a specific prerequisite forward only if it blocks the selected earlier task.
3. Complete type fidelity and exercise a playground sample for every module. Track initial
   integration and full API completion separately; existing Toast and other module gaps remain open.
4. Retire `@mui/base` as the final compatibility step, after migrating its consumers.
   The four generated Material references are
   `Autocomplete`, `Snackbar`, `Popper`, and `Orientation`; `SliderStylization` in the playground
   still uses the old Slider. Both generation targets and npm dependencies remain enabled today.
