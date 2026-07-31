# Base UI (`@base-ui/react`) target — status and known gaps

New generator target for `@base-ui/react`, the successor of the frozen `@mui/base`. Plan and its review
are in `base-ui-plan.md` / `base-ui-plan-review.md`; both were written before `buildSrc` and the real
`.d.ts` had been read, so the corrections below take precedence over them.

**Target version:** `base-ui-react.version=1.6.0` (published 2026-06-18). Pinned deliberately — Base UI
had breaking changes across 1.x.

## Done

- **P0** — dependency + generator plumbing. `generateKotlinDeclarations` takes the `node_modules` root
  instead of `build/js/node_modules/@mui`; `Package` carries a `scope` so `moduleDeclaration` no longer
  hardcodes `@mui`.
- **P1/P2 (partial)** — `menu` module: 20 parts + `Separator`, types only, compiling.
  `:mui-kotlin` and `:playground` both green.
- **Namespace object** — `baseui/Menu.kt`: `external object Menu` under
  `@file:JsModule("@base-ui/react/menu")`, one `val <Alias>: FC<<Part>Props>` per part, synthesized from
  `index.parts.d.ts`. This is the module's only value export, so it is the only way to reach a part at
  runtime. `Handle` / `createHandle` are not components and have no props type; they are logged and
  recorded in the object's KDoc rather than dropped silently. The namespace name is read from
  `export * as Menu` in `index.d.ts` rather than derived from the module id — `otp-field` exports
  `OTPField`, which kebab-to-Pascal would get wrong.

## Facts that contradict the plan documents

- **`MenuSeparator` does not exist.** `Menu.Separator` re-exports the shared standalone `Separator`
  from `../separator`. Part names are *not* universally module-prefixed.
- **Flat part *values* are impossible.** The package's `exports` map has 81 keys and no wildcard, so
  `@base-ui/react/menu/popup/MenuPopup` is not importable; `menu/index.d.ts` re-exports the flat names
  via `export type *` only. Only the `Menu` namespace object is a value export. Flat *types* are real
  declarations and are what we generate. `hard_rules` #10 in `.claude/agents/mui-code-review.md` was
  corrected accordingly.
- **44 public modules** (31 with `index.parts.d.ts`, 13 flat), ~190 public parts — not "37 components".
  `combobox` (28) and `autocomplete` (23) are larger than `menu` (21).
- **Base UI `.d.ts` have no trailing newline**, which matters to every regex that anchors on `\n}\n`.

## Open gaps in the generated `menu` output

Ordered by how much API they cost. The numbers are stable identifiers, not positions: gap 1 (the missing
namespace object) is closed and its entry removed, so the list starts at 2.

2. **`MenuPortalProps` lost its parent, so `Menu.Portal` has no `children` — this is now the one thing
   keeping `menu` from rendering.** Upstream it extends `FloatingPortal.Props<MenuPortalState>`, declared
   as an `interface` *inside* a namespace (`floating-ui-react/components/FloatingPortal.d.ts`).
   `buildBaseUiAliases` only collects `type` members, so the reference is never flattened and
   `ParentType.isAcceptableParent()` drops it (a dotted name is not an identifier). Cost: `container`,
   plus everything inherited from `BaseUIComponentProps<'div', …>` — `children`, `className`, `style`,
   `render`, all div attributes. `AriaCombobox.Props` / `.Actions` have the same shape and will hit
   combobox and autocomplete.

   Measured with the namespace object in place: `Menu.Root { Menu.Trigger { +"Open" } }` and
   `Menu.Positioner { Menu.Popup { Menu.Item { … } } }` compile, but wrapping the positioner in
   `Menu.Portal { … }` does not — *"`ElementType<P>.invoke(noinline block: P.() -> Unit)` cannot be
   called in this context with an implicit receiver"*, because `MenuPortalProps : Props` never reaches
   `PropsWithChildren` (the other 19 parts get there through `DOMAttributes`). And the Portal is not
   optional: `useMenuPortalContext()`, which `MenuPositioner` calls, throws
   `Base UI: <Menu.Portal> is missing.` So the canonical `Root > Trigger + Portal > Positioner > Popup`
   tree cannot be assembled from Kotlin at all, which is also why there is no `menu` sample in the
   playground yet.

   Two facts for whoever fixes it: `FloatingPortal.Props<TState>` adds only `container` over
   `BaseUIComponentProps<'div', TState>`, so a hand-written `FloatingPortalProps : BaseUiDivProps` stub
   would restore the whole surface; and the flattening must drop the *use-site* type argument
   (`FloatingPortal.Props<MenuPortalState>` → `FloatingPortalProps`, not
   `FloatingPortalProps<MenuPortalState>`) — the same thing `EVENT_DETAILS_ALIAS` already does for the
   event-details stubs — or else the stub has to be generic in a state type it does not use.
3. **Members declared `T | undefined` without `?` come out non-null.** Base UI writes optional props
   this way in 137 places; MUI always pairs `| undefined` with `?`, so `KotlinType.kt:181` /
   `KotlinType.kt:372` strip the suffix and return a non-null type. Already visible on 4 members:
   `MenuViewport.activationDirection`, `MenuViewport.instant`, `MenuPopup.instant`,
   `MenuPositioner.instant`. Reading them yields `undefined` in a non-null Kotlin variable. The fix is
   the existing TODO at `KotlinType.kt:179` — treat ` | undefined` as nullable — applied to the Base UI
   path only.
4. **Generics are dropped from part props** — violates the "generics are preserved" rule.
   `MenuRootProps<Payload>` and `MenuTriggerProps<Payload>` both lose it: `handle: Any? /*
   MenuHandle<Payload> */`, `payload: Any? /* Payload */`, and `children` documented against `Payload`.
   `MenuHandle` itself is a `declare class` and is not generated at all (empty body → correctly skipped).
   39 interfaces across the package are generic, so this gets more expensive with every module added.
5. **`Side` and `TransitionStatus` widen to `Any?`** (7 members) although both enums *are* generated as
   seskar sealed types — they need `KNOWN_TYPES` entries. `MenuRootOrientation` (the per-part alias of
   `Orientation`) is likewise unresolved. Note that the two that *do* resolve only do so by accident:
   `align` because the member name is in `UnionFinder.kt::UNION_PROPERTIES`, and `Orientation` because a
   MUI `KNOWN_TYPES` entry (`KotlinType.kt:54`) happens to share the name — while `IMPORTED_FQNS` also
   carries `mui.base.Orientation`. Worth making deliberate before it bites.
6. **The alias map is built only from part files, not the whole package.** Enough for `menu`, but
   combobox and autocomplete each lose 3 references (`AriaCombobox.ChangeEventDetails`,
   `.ChangeEventReason`, `.HighlightEventDetails`), tooltip and toast lose `FloatingPortalLite.Props`,
   field loses `Form.ValidationMode` / `Form.Values`. Also the two-level form `Menu.Root.Props`
   (`context-menu`) and `Field.Control.Props` (`input`) is not expressible by the current
   `Namespace.Member` keys at all — the needed mapping already exists in `BaseUiPart(alias,
   declaredName)` as `"$Namespace.$alias" -> declaredName`.
7. **`flattenBaseUiNamespaces` deletes namespace members it could not parse.** It removes the whole
   block, and `NAMESPACE_ALIAS` only understands `type` members — so an `interface` declared inside a
   namespace is dropped silently rather than flattened (this is the mechanism behind gap 2). Outside
   `internals/` and `floating-ui-react/` there is exactly one such namespace (`AriaCombobox`), so it is
   latent for now; it should at least log.
8. **`instant` is typed `mui.system.Union`** (= `String`) in `MenuPopup` / `MenuViewport` — both a
   `hard_rules` #6 violation (a string-literal union that should be sealed) and the only `mui.*`
   dependency in `baseui`, which should not exist. Emit a per-part sealed type instead.
9. **`UseAnchorPositioningSharedParameters` is an empty stub.** Upstream it is ~60 anchor-positioning
   props (side, align, offsets, collision handling) shared by every Positioner part, in
   `utils/useAnchorPositioning.d.ts`. Generating it type-only would restore real API surface.
10. **`BaseUiDivProps['onClick']` indexed-access types stay `Any?`** (4 members, `MenuItem` /
   `MenuLinkItem`). Resolvable by substituting the parent member's type.
11. **`className` / `style` / `render` are `Any?`** — 51 of the 70 widened members. This is intentional
   (each is a value-or-callback union over the part's own state type, which the shared per-tag parent
   cannot know), but the state-typed ergonomic helpers in `<Part>.ext.kt` are **not written yet**, so
   today there is no type-safe way to pass a callback.
12. **`VIRTUAL_MEMBER_HIDDEN` and `VAR_TYPE_MISMATCH_ON_OVERRIDE` are suppressed package-wide.**
   Justified — Base UI re-declares inherited props, and `WithBaseUIEvent<T>` re-types every inherited
   DOM handler — but a real `override` would be better than a blanket suppress.
13. **`combobox` re-exports `Separator` through the module's `index.d.ts`, not the part file.**
   `export { Separator } from "../separator/index.js"` resolves to `separator/index.d.ts`, from which
   `generate()` takes the *parent directory* name as the component name — so it would write
   `separator.kt`, lowercase, next to the `Separator.kt` that `menu` produces from
   `../separator/Separator.js`. On a case-insensitive filesystem those are one file, and which of the two
   wins depends on generation order. `menu` alone is unaffected. (`toolbar`'s other odd shape,
   `export { type Orientation }`, is handled: `parseBaseUiParts` now drops type-only bindings, which
   would otherwise have become an alias spelled `type Orientation` and pulled `internals/types.d.ts`
   into the generated set.)

## Deliberately not generated

- `internals/`, `floating-ui-react/`, `types/`, per-module `utils/`, and every `*Context.d.ts` —
  internal plumbing, not public API. Note `store/` is *not* excluded: `menu/store/MenuHandle.d.ts` is
  listed in `index.parts.d.ts` and does reach `generate()`; it produces no file only because
  `declare class` converts to an empty body.
- `*DataAttributes.d.ts` / `*CssVars.d.ts` — **these are `declare enum`s with string values**
  (`open = "data-open"`, `availableWidth = "--available-width"`), i.e. a machine-readable source for the
  data-attribute and CSS-variable constants, and a better one than the `docs/*.md` the plan proposed.
  Not wired up yet.
- Callable interfaces (`export interface MenuTrigger { <Payload>(props): JSX.Element }`) are dropped:
  they type the component value, which the namespace object expresses as `FC<…Props>` instead.
- The non-component members of a namespace object: `Menu.Handle` / `Menu.createHandle` and, in the
  modules still to come, `Dialog.Handle`, `Toast.useToastManager` / `createToastManager`,
  `Combobox.useFilter` / `useFilteredItems`, `DirectionProvider.useDirection`. 25 of the package's 283
  parts are one of these — a `declare class` or a hook/factory function rather than a component — and
  each needs its own design (see the imperative-API note under "Remaining phases"). `baseUiNamespaceObject`
  detects them by the absence of a generated `<Part>Props` and logs each one.

## Remaining phases

`menu` is the vertical slice; `BASE_UI_MODULES` in `Generator.kt` is the allow-list to extend.

- `Menu.Portal`'s lost parent (gap 2) — without it the module still cannot be rendered, so it comes
  before adding any further module. Then `.ext.kt` state helpers (gap 11) and a playground sample.
- Utils and the 13 flat modules: `use-render`, `merge-props`, `csp-provider`, `direction-provider`,
  `button`, `separator`, `input`, `form`, `toggle`, `toggle-group`, `radio-group`, `checkbox-group`,
  `menubar`, `unstable-use-media-query`.
- The other 30 part modules. Priority to those with `@mui/base` predecessors (select, slider, switch,
  tabs, tooltip, dialog, popover, number-field, checkbox, radio). Three need their own design:
  imperative Toast (`createToastManager` / `useToastManager`), generic `Select<Value>` / `Combobox`,
  and `Form` / `Field` validation.
- `@mui/base` stays generated and frozen. Only two generated files depend on it —
  `mui/material/Snackbar.kt` (`ClickAwayListenerProps`) and `mui/material/Autocomplete.kt`
  (`UseAutocompleteProps`) — so retiring it later is cheap.

## Repository note: the generated tree is now reproducible

It previously was not. The generator emits Kotlin with no indentation and `X:` instead of `X :`, and
readable output depended on running an IDE reformat by hand — so the committed tree could not be
reproduced from the generator, and every regeneration showed ~520 files of pure formatting churn that
masked real changes.

Spotless now formats `src/jsMain/kotlin` as part of the pipeline
(`generateDeclarations` → `spotlessApply` → `compileKotlinJs`), so **a regeneration produces a
byte-identical tree** — verified by hashing it across two `--rerun-tasks` runs. A non-empty diff under
`mui-kotlin/src/jsMain/kotlin` now means something actually changed, and is worth reading.

It uses **ktfmt**, not ktlint. ktlint enforces a style policy that generated code cannot satisfy at the
source: it rejects the inline `/* … */` markers recording the original TypeScript type
(`var side: Any? /* Side */`) and the lowercase `@JsValue` union members whose names must mirror the
JavaScript API — 47 unfixable violations. Suppressing those rules still left 3551 KDoc blocks with the
opening `/**` at column 0, because ktlint indents a comment's continuation lines but not its first line.
ktfmt has no rules to satisfy and reformats comments too.

`isEnforceCheck = false`: a `spotlessCheck` wired into `check` could run before the sources exist. The
tree is kept formatted by construction instead.

Only `mui-kotlin/src/jsMain/kotlin` is in scope — hand-written code (`buildSrc`, `playground`) is
deliberately left alone so that adding the formatter did not reformat anything a human wrote.
