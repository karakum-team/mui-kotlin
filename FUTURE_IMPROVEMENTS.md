# Future improvements (standing backlog)

Cross-cutting work that is intentionally out of scope for the current task but must not be forgotten.
Add new items here rather than burying them in a one-off TODO.

---

## Migrate `@mui/base` → Base UI (`@base-ui/react`)

**Status:** not started · **Decoupled from the MUI core release cadence.**

`@mui/base` is frozen at `5.0.0-beta.70` and is **npm-deprecated** ("This package has been replaced by
`@base-ui/react`"). It will not receive a v9 (or any further) release, so it stayed at beta.70 through the
MUI v9 migration. The headless layer now lives as a **separate library**, Base UI (`@base-ui/react`, latest
`1.6.0` as of this writing), with its own package, versioning, and — importantly — **different `.d.ts`
shapes** (component anatomy split into parts, different slot/render-prop conventions, no `componentsProps`).

**Why this is a new generator target, not a version bump:**

- New npm package (`@base-ui/react`), not a new version of `@mui/base`.
- Different API surface and type shapes → the current `mui/base` generation path won't map cleanly.
- Likely needs its own generator module (à la `generatePickersDeclarations` / `generateTreeViewDeclarations`)
  and its own output package (e.g. `baseui/…` instead of `mui/base/…`).

**Scope sketch (when picked up):**

1. Add `@base-ui/react` to `gradle.properties` + lockfile; keep `@mui/base` until parity is reached.
2. New `generateBaseUiDeclarations(...)` in `Generator.kt` + a `Package.baseUi`.
3. Map Base UI's part-based anatomy / `render` prop / `Props` conventions; reuse the existing
   slots/slotProps, ARIA, `@JsName`-dashed, and deprecated-removal machinery where it fits.
4. Decide migration vs. coexistence for the existing `mui/base/*` output (probably: generate Base UI
   alongside, then retire `@mui/base` once consumers move).
