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
to `1.6.0` here), with its own package, versioning, and **different `.d.ts`
shapes** (component anatomy split into parts, different slot/render-prop conventions, no `componentsProps`).

**Already implemented:**

- npm dependency and lockfile entry, `Package.baseUi`, and `generateBaseUiDeclarations(...)`.
- Separate `baseui` output with namespace objects for compound components, props/state declarations,
  state-typed `className` / `style` / `render` helpers, and shared positioning props.
- Six allow-listed modules with playground samples: `menu`, `slider`, `field`, `accordion`,
  `number-field`, and `toast`. Toast also exposes `createToastManager` / `useToastManager`.

**Remaining:**

1. Extend module coverage; `combobox` / `autocomplete` are the recommended next candidates.
2. Complete type fidelity in existing modules: generics, nullable values, aliases, callbacks,
   utility types, and the remaining Toast API gaps. Module inclusion does not mean full API parity.
3. Add and exercise a playground sample for each new module, including its imperative APIs.
4. Retire `@mui/base` only after migrating its consumers. The four generated Material references are
   `Autocomplete`, `Snackbar`, `Popper`, and `Orientation`; `SliderStylization` in the playground
   still uses the old Slider. Both generation targets and npm dependencies remain enabled today.
