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

Ordered by how much API they cost.

1. **No namespace object yet — the module has no values.** Only types are generated. `external object
   Menu { val Root: FC<MenuRootProps>; … }` under `@file:JsModule("@base-ui/react/menu")` is still to
   be synthesized from `index.parts.d.ts` (already parsed into `BaseUiPart`, including the aliases).
   Until then nothing is renderable. Pitfall found in review: `Package.baseUi` has `id = ""`, and
   `generate()` derives `subpackage` as `null` for this package, so a naive `moduleDeclaration` call
   would emit `@file:JsModule("@base-ui/react")` instead of `.../menu`. The module segment has to be
   passed explicitly.
2. **`MenuPortalProps` lost its parent, so `Menu.Portal` has no `children`.** Upstream it extends
   `FloatingPortal.Props<MenuPortalState>`, declared as an `interface` *inside* a namespace
   (`floating-ui-react/components/FloatingPortal.d.ts`). `buildBaseUiAliases` only collects `type`
   members, so the reference is never flattened and `ParentType.isAcceptableParent()` drops it (a dotted
   name is not an identifier). Cost: `container`, plus everything inherited from
   `BaseUIComponentProps<'div', …>` — `children`, `className`, `style`, `render`, all div attributes.
   A Portal without `children` cannot render. `AriaCombobox.Props` / `.Actions` have the same shape and
   will hit combobox and autocomplete.
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
  they type the component value, which the namespace object will express as `FC<…Props>`.

## Remaining phases

`menu` is the vertical slice; `BASE_UI_MODULES` in `Generator.kt` is the allow-list to extend.

- Namespace objects + `.ext.kt` helpers (gaps 1 and 7 above).
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

## Repository note

The committed `mui-kotlin/src/jsMain/kotlin` tree is **IDE-formatted and not reproducible from the
generator**: the generator emits unindented code with explicit imports, and there is no formatter in the
build (no ktlint, no spotless). A full regeneration therefore reports ~523 changed files under `mui/*`
that carry no semantic change — verified by normalizing whitespace and line order, the only differences
are collapsed star imports and dropped unused imports. Commits in this series discard that churn and
keep only `baseui/*`, so `baseui/*` is unformatted while `mui/*` stays formatted. Adding a formatter to
the build would make the tree reproducible and remove the churn for good.
