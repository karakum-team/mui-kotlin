# mui-kotlin — project instructions

A fork of the karakum-team generator that converts MUI's TypeScript `.d.ts` into Kotlin/JS external
declarations. The output is later ported into `JetBrains/kotlin-wrappers`, so a wrong type mapping
ships downstream.

## The one invariant

`mui-kotlin/src/jsMain/kotlin/**` is **100% generated**. `generateDeclarations` runs `delete(sourceDir)`
and rewrites the whole tree on every build (`buildSrc/src/main/kotlin/mui-declarations.gradle.kts`).
Never hand-edit it — the edit is lost *and* it masks the real defect. Every fix belongs in
`buildSrc/src/main/kotlin/karakum/mui/**`. Read the generated tree only as evidence of what the
generator emits.

Ground truth for any TypeScript shape is the installed upstream typings at `build/js/node_modules/@mui/**`.

## Cover new surface on the playground

`playground/` is the test stand. **Any change that makes new API reachable from Kotlin must gain a
playground sample, and that sample must be driven in a real browser before the work is called done.**
This applies to:

- a new component, hook, or prop becoming available;
- a library version bump where upstream added or changed something;
- a generator fix that restores inheritance or un-widens a type — the point of the fix is that the
  member is now *usable*, and only a call site proves that.

Rules for the sample:

- **Typed call sites only.** Set props through real members, not `unsafeJso`/`asDynamic` casts, so a
  declaration that stops being generated breaks the build instead of failing silently downstream.
  (`unsafeJso<SomeSlots> { … }` to *construct* an external interface is fine — that is the idiom.)
- **Mount enough surface that a missing binding takes the page down.** Precedent:
  `playground/src/jsMain/kotlin/BaseUiSlider.kt` and `BaseUiMenu.kt` mount every member of their
  namespace object for exactly this reason.
- **Register it in `playground/src/jsMain/kotlin/App.kt`** or it never renders.
- **Write down what running it established** — in the file header and in the commit body. Each bullet
  should be something `compileKotlinJs` cannot see. See commit `b105b29b` for the shape.

`compileKotlinJs` proves the declarations *typecheck*. It cannot prove a Kotlin member maps to the JS
prop the component actually reads. Only the browser can.

## Verify in the browser (Chrome DevTools MCP)

```
./gradlew :playground:jsViteDev      # no --continuous
```
then open **http://localhost:5173/** with the `chrome-devtools` MCP tools.

Gotchas, each of which has already produced a wrong conclusion here:

- **`jsViteDev` does not rebuild Kotlin.** After recompiling, *restart the task* — reloading the page
  serves the previous bundle. Confirm you are on fresh output (check a CSS rule or string you just
  changed) before trusting anything you observe.
- **Drive the UI with real input** (`mcp__chrome-devtools__click`, `fill`, `press_key`). A JS
  `element.click()` via `evaluate_script` does not reproduce the pointer sequence MUI listens for, and
  will make working behaviour look broken.
- **The a11y snapshot is the authoritative state read**, not hand-rolled class-name checks — MUI's
  selected/expanded/disabled state does not always live where you would guess. Use `evaluate_script`
  for computed styles and DOM attributes, `take_snapshot` for semantics.
- **A uid from the snapshot is the whole element box.** Clicking an *expanded* container can land on a
  nested child. Collapse first, or target the specific row.
- **Check the console** (`list_console_messages`) and separate new errors from the known pre-existing
  warnings (emotion double-load; the documented Base UI `render`-prop capitalisation warning).

## Version bumps

Order and rationale are in `MUI_V9_TODO.md` / `MUI_V*_TODO.md`. Essentials:

- Reset before re-resolving: `rm -rf build/js .kotlin-locks/js/package-lock.json`, then
  `./gradlew kotlinUpgradePackageLock`. `.kotlin-locks/js/package-lock.json` is the **active** lock;
  the root `./package-lock.json` is stale legacy — leave it alone.
- Verify the **top-level** `build/js/node_modules/@mui/*` versions. The generator reads top-level only;
  nested per-workspace copies at old versions are the classic trap.
- MUI X packages ship in lockstep and share a hoisted `@mui/x-internals` — move `x-tree-view` and
  `x-date-pickers` together.
- **Read the regeneration diff with `git status --short`, not `git diff --stat`.** `--stat` hides
  untracked files, so a newly-emitted declaration (which is exactly where breakage tends to be) is
  invisible.
- Update the relevant `MUI_V*_TODO.md` when the exclusion / known-limitation surface changes.

## Definition of done

1. `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs` — 0 errors.
2. Regeneration diff reviewed and every hunk explained (including untracked files).
3. New surface exercised by a playground sample and confirmed in the browser.
4. CI gate, run the strong way — `./gradlew :mui-kotlin:clean build` then
   `git diff --exit-code -- mui-kotlin/src/jsMain/kotlin`. The committed tree must reproduce
   byte-for-byte from an empty directory; this is what `.github/workflows/declarations.yml` enforces.
5. Report honestly: what you observed, what you skipped, what is still missing.
