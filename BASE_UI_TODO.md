# Base UI (`@base-ui/react`) target — status and known gaps

New generator target for `@base-ui/react`, the successor of the frozen `@mui/base`. Plan and its review
are in `base-ui-plan.md` / `base-ui-plan-review.md`; both were written before `buildSrc` and the real
`.d.ts` had been read, so the corrections below take precedence over them.

**Target version:** `base-ui-react.version=1.6.0` (published 2026-06-18). Pinned deliberately — Base UI
had breaking changes across 1.x.

**Where this stands:** three modules of the 44 — `menu`, `slider` and `field` — are generated end to end
and render in a browser. `BASE_UI_MODULES` in `Generator.kt` is the allow-list. The open gaps below are
all things the three survive without; none of them blocks adding a fourth.

`slider` and `field` were both picked for finding new defects rather than re-exercising `menu`'s
machinery, and both did. Worth repeating for whoever picks the fourth: a module that shares `menu`'s
portal/positioner/popup shape mostly re-runs code that already works.

`field` is also the point at which two of this document's own predictions turned out wrong — both in the
generated output's favour. Verify against the emitted `.kt`, not against the entry that predicts it.

## Done

- **P0** — dependency + generator plumbing. `generateKotlinDeclarations` takes the `node_modules` root
  instead of `build/js/node_modules/@mui`; `Package` carries a `scope` so `moduleDeclaration` no longer
  hardcodes `@mui`.
- **P1/P2** — `menu` module: 19 own parts + the shared `Separator`, plus the values to render them.
  (`index.parts.d.ts` lists 22 bindings: those 20 components plus the non-component `Handle` /
  `createHandle`.) `:mui-kotlin` and `:playground` both green.
- **Namespace object** — `baseui/Menu.kt`: `external object Menu` under
  `@file:JsModule("@base-ui/react/menu")`, one `val <Alias>: FC<<Part>Props>` per part, synthesized from
  `index.parts.d.ts`. This is the module's only value export, so it is the only way to reach a part at
  runtime. `Handle` / `createHandle` are not components and have no props type; they are logged and
  recorded in the object's KDoc rather than dropped silently. The namespace name is read from
  `export * as Menu` in `index.d.ts` rather than derived from the module id — `otp-field` exports
  `OTPField`, which kebab-to-Pascal would get wrong.
- **State-typed `.ext.kt` helpers** — `className` / `style` / `render` as callbacks over the part's own
  state type, for the 18 `menu` parts that render an element. See gap 11.
- **`Menu.Portal`'s lost parent** — `resolveNamespaceStubs` in `BaseUi.kt` rewrites `FloatingPortal.Props`
  (an `interface` declared inside a namespace, in the ungenerated `floating-ui-react/`) to the
  hand-written `FloatingPortalProps : BaseUiDivProps` stub, dropping the use-site type argument the way
  `EVENT_DETAILS_ALIAS` does. `MenuPortalProps` gets `children`, `container` and the div attributes back,
  and the canonical `Root > Trigger + Portal > Positioner > Popup` tree can finally be written. The table
  is where `AriaCombobox.Props` / `.Actions` go when combobox and autocomplete are added. A rewrite that
  silently stops matching would put the parent straight back in the bin, so `unusedNamespaceStubs`
  reports any stub that no generated declaration ended up referring to.
- **`slider` module** — 7 parts, no portal / positioner / backdrop, so none of `menu`'s machinery is
  re-used. What it cost, each item a generator fix rather than a slider-specific workaround:
  - **Cross-module declaration dependencies.** `SliderRootState extends FieldRootState`, declared in
    `field/`, which was not a module then. `BASE_UI_EXTRA_FILES` in `Generator.kt` names such a `.d.ts`
    and converts it exactly as a part (types and `.ext.kt`, no namespace object). Preferred to a
    hand-written stub because `distinctBy` on the absolute path deduplicates it the day the module joins
    the allow-list, instead of colliding — which is what happened, verified, when `field` did. 21
    declarations across 12 modules inherit `FieldRootState` (17 spell it `extends FieldRootState`, four
    go through `FieldRoot.State`).
  - **Function types that were not Kotlin.** `toFunctionType`'s replacement list is curated for MUI
    shapes and emitted whatever it did not recognize as if it were Kotlin — a TS conditional type, a
    union in return position and the `unknown` keyword all came out as text that ktfmt — the formatter
    at the time — refused to parse. It now checks its own output and widens to `Any?` with the TypeScript
    recorded, printing a line. See gap 14 for what that costs.
  - **A type parameter resolving to the other package's type.** Parameters are dropped from the emitted
    declaration (gap 4), which is harmless while the name stays unknown — `menu`'s `Payload` — but
    `Value` is `Autocomplete`'s parameter in `KNOWN_TYPES`, so `SliderRootProps<Value extends number |
    readonly number[]>` produced `var value: Value?` against a MUI type. `substituteTypeParameterBounds`
    replaces a member whose type *is* such a parameter with its bound, or its default where it declares
    none — `radio` and `radio-group` (`<Value = any>`) will need the latter.
  - **Empty-bodied declarations losing their parents.** `findAdditionalProps` drops the `extends` of any
    interface with an empty body, which is right for `@mui/*` and wrong here: 39 of the package's 67
    `<Part>State` declarations *are* nothing but an `extends`, so `SliderControlState` and its four
    siblings came out as bare markers and the state a `className { state -> … }` helper closes over had
    no members. Gated on the package. The same branch used to hardcode `BaseUIChangeEventDetails` as the
    parent of anything named `<X>EventDetails`, which cost three `menu` types their real parent and
    `preventUnmountOnClose` with it — reading the declared parent fixed both at once.
  - **Event-details aliases outside `Change` / `Highlight`.** `Commit`, `Submit`, `Invalid` and
    `Complete` were left as TS aliases, which emit nothing: `SliderRootCommitEventDetails` was named by
    `onValueCommitted` and existed nowhere, and the `BaseUIGenericEventDetails` stub written for it had
    no referrer at all. The base's second type argument, a bag of extra fields rather than a
    parameterization, now becomes a second parent — `SliderRootChangeEventDetails.activeThumbIndex`.
  - **`Slider.Value`'s `children` typed backwards.** The member converter treated any children type
    *mentioning* `ReactNode` as React children, but here the node is what the callback returns and the
    component reads nothing else. It allowed the plain node that is ignored at runtime and rejected the
    formatting callback the part exists for. The fallback now requires an arm that *is* a node type.
    `Meter.Value` and `Progress.Value` share the shape.
  - **The first member of every namespace block was missing from the alias map.** `NAMESPACE_ALIAS`
    anchored on a preceding newline, which `NAMESPACE_BLOCK`'s captured body does not have. `menu` has
    been generated with `MenuPopup.Props`, `MenuRoot.State` and 18 others unmapped all along; nothing
    referenced a first member until `SliderLabelProps`, declared against `SliderLabel.State`.
- **Playground sample** — `playground/src/jsMain/kotlin/BaseUiMenu.kt`, wired into `App.kt`. Mounts all
  20 members of the namespace object, uses both arms of every value-or-callback prop, and sets the
  anchor-positioning props on both `Menu.Positioner` sites (`side` / `align` as seskar unions, the two
  offsets and `collisionPadding` as the `Any?` members). Driven in a
  browser once: the portal lands in `<body>`, the popup's class flips `--closed` ↔ `--open` and its
  `min-width` 8rem ↔ 16rem while Base UI's own inline styles survive alongside, highlight follows the
  arrow keys, checkbox and radio round-trip, the submenu opens a second portal, the backdrop reports
  `close:outside-press`, console clean. CI runs `./gradlew build`, which compiles `:playground`, so the
  sample is an enforced guard from here on — see "What only the sample can catch" below.
- **Playground sample for `slider`** — `BaseUiSlider.kt`, all 7 namespace members, a controlled
  horizontal slider and an uncontrolled vertical range. Driven in a browser: the `children` callback
  formats (`40 of 100 (raw 40)`, `20 – 70`), keyboard and pointer report `change=keyboard@0` /
  `change=drag@0` / `commit=drag` — the `@0` being `activeThumbIndex` through the second parent and
  `commit` a details type that did not exist before — `state.dragging` flips two class names and the
  `style` callback's opacity mid-drag, `state.orientation` reads as the bare string, and
  `render { props, _ -> div.create { +props } }` carries `className` / ref / `data-*` through. One
  expected warning, see gap 15.
- **Anchor positioning** (was gap 9) — `UseAnchorPositioningSharedParameters` was an empty stub, so
  every Positioner part accepted no `side` / `align` / `sideOffset` at all and every menu was stuck on
  `bottom` / `center` with zero offset. `utils/useAnchorPositioning.d.ts` now goes through
  `BASE_UI_EXTRA_FILES` and the 12 props are generated from upstream, KDoc included. Eight Positioner
  parts inherit them; only `menu` is in the allow-list today, so the other seven arrive with the surface
  already there. What it cost:
  - **`Side` and `Align` were hardcoded twice.** `BASE_UI_SIDE` / `BASE_UI_ALIGN` in `Generator.kt`
    spelled out unions that this very file declares, so generating it redeclared both. The hardcodes are
    gone and both now come from upstream (they land in `useAnchorPositioning.ext.kt`, so `Side.kt` and
    `Align.kt` no longer exist). A rename upstream is now a build failure rather than a silent
    divergence. `Orientation` and `TransitionStatus` are declared elsewhere and stay hardcoded.
  - **`Padding` resolved to nothing.** `collisionPadding: Padding` comes from `@floating-ui/utils`,
    which is not generated, and would ordinarily widen to `Any?` with a marker the way `Middleware` and
    `FloatingContext` do — except that `Padding` ends with one of `KNOWN_TYPE_SUFFIXES`, which is seeded
    with the capitalized `UNION_PROPERTIES` and `padding` is one of them. `kotlinType` therefore claimed
    to know the name and emitted it bare, against nothing. `inlineForeignAliases` (BaseUi.kt) substitutes
    the upstream definition, giving `Any? /* number | Partial<SideObject> */`. It is the same accident
    `substituteTypeParameterBounds` handles for type *parameters*; this is its counterpart for imported
    names, and `Padding` is the only one of this file's eight imports it catches.
  - **A dead union named after the file.** `findDefaultUnions` runs over the whole `.d.ts`, including
    the two non-exported `SideFlipMode` / `SideShiftMode` interfaces, and built a
    `useAnchorPositioningAlign` sealed type — lowercase, because unions are named for the component —
    whose only referrer was then dropped, since the converter does not emit a non-exported interface.
    `dropNonExportedInterfaces` removes them up front so the earlier passes agree with the later ones.
    Three such interfaces exist across the modules generated so far, all internal helpers.
  - **`side` had to stop being `Any?`** or the flagship prop of the fix would have been untyped; see
    gap 5.

  Driven in a browser, measured against the DOM rather than eyeballed: the popup sits exactly `8` below
  the trigger (`sideOffset`, plain-number arm) with `data-align="start"` where the default is `center`;
  the submenu reports `data-side="inline-end"` and sits exactly `4` right of its trigger item, which is
  both the kebab `@JsValue("inline-end")` mapping and the *callback* arm of `sideOffset` returning from
  Kotlin; `collisionPadding = 12` pins the popup's left edge at exactly `12` although its trigger is at
  `8`. The state readback still works through the now-typed `Side`:
  `bui-positioner--bottom bui-positioner--start` on the parent, `--inline-end` on the submenu. Console
  carries only the two known warnings (emotion double-load, and gap 15's `render` name).

  One prop is passed but **not** independently verified: `alignOffset = -4` is masked, because the same
  collision shift that proves `collisionPadding` pins the left edge regardless of it. Moving the trigger
  away from the viewport edge would expose it.

- **`number-field` module** — 7 bindings, plus `utils/types.d.ts`. What it cost:
  - Custom event properties (`ChangeEventCustomProperties`) aren't declared in the same file; added `number-field/utils/types.d.ts` to `BASE_UI_EXTRA_FILES` to translate it and make it available.
  - Negative integers in string literal unions (like `Direction`'s `-1`) failed to compile as `val s-1`; updated the generator to output `val sMinus1`.
  - Added support for `Direction` and `DirectionalChangeReason` known types to correctly parse.

- **`accordion` module** — 5 parts (`Root`, `Item`, `Header`, `Trigger`, `Panel`), picked as the fourth for its unique
  declaration shapes. What it cost, each item a generator fix rather than a workaround:
    - **`Pick<X>` and `Partial<X>` in multi-parent `extends` lists.** The multi-parent split path in
      `findParentType` used `isAcceptableParent` which categorically rejected `TS_UTILITY_PREFIXES` (like
      `Pick`, `Partial`, and `Omit`). Thus, `AccordionPanelProps` silently dropped `AccordionRootProps` and
      `AccordionItemProps` dropped `UseCollapsibleRootParameters`. The fix explicitly unwraps `Pick`,
      `Partial`, and `Omit` in the multi-parent split, allowing the interfaces to keep their intended parents.
    - **The `BASE_UI_EXTRA_FILES` required for `UseCollapsibleRootParameters`.** Unwrapping `Pick` revealed that
      `UseCollapsibleRootParameters` wasn't generated at all. It was added to `BASE_UI_EXTRA_FILES`
      (along with `CollapsibleRoot.d.ts` for its `ChangeEventDetails`), providing a typed inheritance hierarchy instead
      of `Any`.
    - **Bound-less type parameter `<Value = any>`.** `substituteTypeParameterBounds` only substituted bounds declared
      with `extends`. The signature `<Value = any>` lost its substitution entirely. The fix checks the `=` clause and
      falls back to the default parameter, resulting in the correct generic substitution.
    - **Generic Type Wrappers inside property types.** `AccordionRootProps.value` uses `AccordionValue<Value>`, which
      `substituteTypeParameterBounds` failed to map since it only looked for exact member type matches via
      `wholeMemberType`. Since `AccordionValue<X>` is functionally just `X[]`, the substitution logic now folds
      `AccordionValue<...>` into `ReadonlyArray<...>` during substitution.
- **Playground sample for `accordion`** — `BaseUiAccordion.kt`, all 5 namespace members: an uncontrolled accordion with
  two items. Driven in a browser: the elements render correctly, the styles are correctly applied using `ClassName` on
  the `className` props, and clicking the triggers updates the values while firing `onValueChange` with the array of
  expanded items.

- **`field` module** — 7 parts (`Root`, `Item`, `Error`, `Label`, `Description`, `Control`, `Validity`),
  picked as the third for what it unblocks: 12 modules inherit `FieldRootState`. It cost far less than
  `slider` did, and most of what the "Next up" analysis predicted did not happen:
  - **Two predictions were wrong, both because the output was better than expected.**
    `FieldValidityProps.children` came out fully typed, `(state: FieldValidityState) -> ReactNode`, not
    a second occurrence of gap 17 — unlike `Slider.Value`'s, it is a single unadorned function type with
    no `null` arm to break `toFunctionType`, so gap 17's population is still one.
    `FieldControlProps.onValueChange` came out typed too, `((value: String, eventDetails:
    FieldControlChangeEventDetails) -> Unit)?`, so gap 14 does not bite here either — and
    `FieldControlChangeEventDetails` is the first Base UI event-details type reached from a *type
    position* rather than only from a marker. Its `reason` reads `none` at runtime, which is exactly
    what `FieldControlChangeEventReason = typeof REASONS.none` says upstream.
  - **`FieldValidityData.state` no longer widens to a markerless `Any`** (was the one widening in the
    tree that recorded nothing, and `Field.Validity` exists to read exactly it). The cause was not the
    `{`-branch in `KotlinType.kt`, which never saw the member: `dropMemberValueObjects`
    (`Adapter.kt`) replaced the whole balanced brace span with the literal token `any`, which resolves
    through `STANDARD_TYPE_MAP` carrying nothing. It now *collapses* the object onto one line instead
    and lets it through — what breaks member splitting is the inner `;\n` and comments, not the object
    — so `kotlinType` widens it to `Any? /* { badInput: boolean; … } */`. It stops being non-null in
    the process, which is the widened form's doing rather than a decision: upstream declares `state`
    required. Nothing reads it directly — `FieldValidity.State.validity` is an indexed access into the
    same shape, and that is what the sample reads — so the marker is the whole value of the change.

    Gated on the package, and the gate is the point. Unconditionally, three `@mui/*` declarations
    changed, and one of them regressed: `InputBase.renderSuffix` is `(state: { … }) => React.ReactNode`,
    where the same span is a function *parameter*, and replacing it with `any` is precisely what lets
    `toFunctionType` translate the callback. Keeping it costs the whole function type (gap 14). No Base
    UI function type has an inline object parameter today.
  - **The `BASE_UI_EXTRA_FILES` dedupe is no longer a claim.** `field/root/FieldRoot.d.ts` was there for
    `slider`, and its KDoc asserted that `distinctBy` on the absolute path would drop it once `field`
    joined the allow-list. Generated both ways, the tree is byte-identical, so the entry was redundant
    rather than merely harmless — and it is gone.
  - **What it did *not* fix, as decided rather than missed.** `FieldValidityState extends
    Omit<FieldValidityData, 'state'>` — `findParentType` unwraps the `Omit` and the omitted member comes
    back, see the new gap 19. `Form.ValidationMode` / `Form.Values` stay widened (gap 6). The two new
    indexed-access shapes widen with a marker, which is gap 10 behaving as designed:
    `FieldError.match` (`boolean | keyof ValidityState`) and `FieldControl.defaultValue`
    (`React.ComponentProps<'input'>['defaultValue']`).
- **Playground sample for `field`** — `BaseUiField.kt`, all 7 namespace members: a validating email
  field and a `Field.Item` group. Driven in a browser:
  - Validation actually fails. The Kotlin `validate` lambda's return renders as the error text; the
    `match = true` arm shows Base UI's own native message (`Please fill in this field.`) and the
    `match = "valueMissing"` arm shows ours, so both arms of that `Any?` are exercised.
  - `Field.Validity`'s callback renders, and reads `FieldValidityData` through the state: `valid`,
    `valueMissing`, `typeMismatch`, `customError` come out of the now-marked `state` member, `errors`
    out of the inherited one.
  - The label/description wiring is generated by Base UI and comes out right: `<label for>` matches the
    control's `id`, and `aria-describedby` lists both the description and the error, which the
    accessibility tree reports as the control's description.
  - `render { props, _ -> p.create { +props } }` on `Field.Description` carries through the `id` that
    `aria-describedby` points at, plus all four `data-*` state attributes.
  - `FieldRootActions` works through the `Any?` `actionsRef`: clicking the button on a pristine,
    untouched field runs validation from scratch and both errors appear with nothing typed.
  - One upstream quirk, recorded rather than chased: the control inside `Field.Item` gets
    `aria-labelledby` from the item's own label but **no** `aria-describedby` from the item's own
    description, where the same three parts directly under `Field.Root` do get it. Nothing the generator
    controls; worth re-checking when `checkbox-group` / `radio-group` land, since that is the shape
    `Field.Item` exists for.

## Facts that contradict the plan documents

- **`MenuSeparator` does not exist.** `Menu.Separator` re-exports the shared standalone `Separator`
  from `../separator`. Part names are *not* universally module-prefixed.
- **Flat part *values* are impossible.** The package's `exports` map has 81 keys and no wildcard, so
  `@base-ui/react/menu/popup/MenuPopup` is not importable; `menu/index.d.ts` re-exports the flat names
  via `export type *` only. Only the `Menu` namespace object is a value export. Flat *types* are real
  declarations and are what we generate. `hard_rules` #10 in `.claude/agents/mui-code-review.md` was
  corrected accordingly.
- **44 public modules** (31 with `index.parts.d.ts`, 13 flat), 283 bindings across the 31 — not
  "37 components". `combobox` (28) and `autocomplete` (23) are larger than `menu` (22).
- **Base UI `.d.ts` have no trailing newline**, which matters to every regex that anchors on `\n}\n`.

## Open gaps in the generated output

Ordered by how much API they cost. The numbers are stable identifiers, not positions: gaps 1, 2 and 9
are closed and their entries removed, so the list starts at 3 and skips 9. Gaps 5 and 11 are closed too
but their entries are kept — struck through — because what each of them settled is still worth knowing:
gap 5, how a name is resolved for one target without resolving it for the other; gap 11, that
`className` / `style` / `render` stay `Any?` with a typed helper beside them. 14–17 came out of
`slider`, 19 out of `field`.

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

   `slider` sharpened this. A dropped parameter is only *quietly* lossy while its name stays unknown to
   `KotlinType`; when the name is one of that table's own (`Value`, `T`, `TValue`, …) the member resolves
   to an unrelated MUI type and the tree stops compiling. `substituteTypeParameterBounds` (BaseUi.kt)
   handles that for a member whose whole type is the parameter, using its bound or default; anywhere
   else — `(value: Value extends number ? number : Value, …)` — the enclosing construct is widened whole
   and upstream's own text survives in the marker.
5. ~~**`TransitionStatus` widens to `Any?`**~~ — done, and `baseui/TransitionStatus.kt` stopped being a
   dead declaration reachable only from inside a `/* … */` marker. `MenuRootOrientation` (the per-part
   alias of `Orientation`) is still unresolved; see the follow-up at the end of this entry.

   The fix is `BASE_UI_KNOWN_TYPES` in `Generator.kt`, threaded to `kotlinType` as `knownTypes` the way
   `keepEmptyBodyParents` is threaded to `findAdditionalProps`. It could not be a `KNOWN_TYPES` entry:
   that table is global, `@mui/material/Collapse/Collapse.d.ts` declares `state: TransitionStatus`, and
   an entry there would resolve `mui/material/Collapse.kt` against the `baseui` type. `Side` has moved
   out of `KNOWN_TYPES` into the same map — it was safe there only because no MUI declaration happens to
   use the bare name, which was luck rather than design, and the generated tree is unchanged by the move.

   It is a `Map<String, String>` (name → emitted type), not a `Set`, and `TransitionStatus` maps to
   `TransitionStatus?`. Upstream folds `undefined` into the alias
   (`'starting' | 'ending' | 'idle' | undefined`) rather than marking the members optional, and all six
   members that use it are declared non-optional — so a set would have traded a widening for a lie. The
   browser confirms it: `transitionStatus` reads `null` whenever nothing is transitioning.

   Both uses were driven in a browser through `BaseUiField`. Interpolating it still yields the bare
   JavaScript string, the way every seskar union does (`status=starting`, `status=ending`). *Comparing*
   it — `state.transitionStatus == TransitionStatus.ending`, which is what the gap actually cost — reads
   true exactly when the value is `ending`. The comparison has to live on a part that stays mounted:
   `Field.Error`'s own status becomes `ending` as it is removed, so nothing can observe it there.

   `Align` was deliberately left alone. It resolves already, but through `KNOWN_TYPE_SUFFIXES`
   (`KotlinType.kt`), which is seeded with the capitalized `UNION_PROPERTIES` — and `align` is one of
   them. Moving it into the map would not retire that accident; only dropping `align` from
   `UNION_PROPERTIES` would, and that would stop `findDefaultUnions` generating every MUI `<Name>Align`
   union. The same suffix table is what made `Padding` fail to compile (see Done), so it cuts both ways.
   `Orientation` stays in the global `KNOWN_TYPES`: `mui.base.Orientation` is in `IMPORTED_FQNS` and MUI
   files depend on the name resolving.

   `toFunctionType` gets the map as well, applied before its replacement chain. That chain is a curated
   list of MUI shapes and would otherwise let `TransitionStatus` through untouched in a parameter or
   return position — bare, and so without the `?` the map exists to carry, which would compile and
   silently lie. No such shape exists in the three generated modules today; `radio/` and `tooltip/` have
   them, so this is preventive rather than observed.

   **Follow-up the map makes cheap.** `MenuRoot.kt` emits `Any? /* MenuRootOrientation */` although
   `MenuRootOrientation` *is* generated by the same run — `MenuRoot.d.ts` declares
   `type Orientation = MenuRootOrientation` inside its namespace and the `enums` pass writes the sealed
   type into `MenuRoot.ext.kt`. Now that the parameter exists, `generateBaseUiDeclarations` could seed it
   by harvesting `export type X = 'a' | 'b'` names across the file set up front, the way
   `buildBaseUiAliases` already pre-scans, and close this and every future per-part union at once. That
   is a design change rather than a line change, hence not in this commit.
6. **The alias map is built only from part files, not the whole package.** Enough for `menu`, but
   combobox and autocomplete each lose 3 references (`AriaCombobox.ChangeEventDetails`,
   `.ChangeEventReason`, `.HighlightEventDetails`), tooltip and toast lose `FloatingPortalLite.Props`,
   field loses `Form.ValidationMode` / `Form.Values`. Also the two-level form `Menu.Root.Props`
   (`context-menu`) and `Field.Control.Props` (`input`) is not expressible by the current
   `Namespace.Member` keys at all — the needed mapping already exists in `BaseUiPart(alias,
   declaredName)` as `"$Namespace.$alias" -> declaredName`.

   Distinct from the alias-map bug `slider` closed (see Done): that one dropped the *first* member of
   every block it did read. This one is about which files get read at all.
7. **`flattenBaseUiNamespaces` deletes namespace members it could not parse.** It removes the whole
   block, and `NAMESPACE_ALIAS` only understands `type` members — so an `interface` declared inside a
   namespace is dropped silently rather than flattened. That is what cost `MenuPortal` its parent (now
   closed, see Done), and `resolveNamespaceStubs` works around it one entry at a time rather than fixing
   the flattening. Outside `internals/` and `floating-ui-react/` there is exactly one such namespace
   (`AriaCombobox`), so it is latent for now. Still unlogged: the new `unusedNamespaceStubs` only notices
   when a *known* stub stops being referenced, not when a namespace member is dropped in the first place,
   which is the failure that has to be found by hand today.
8. **`instant` is typed `mui.system.Union`** (= `String`) in `MenuPopup` / `MenuViewport` — both a
   `hard_rules` #6 violation (a string-literal union that should be sealed) and the only `mui.*`
   dependency left in `baseui` (`import mui.system.Union`, nothing else), which should not exist. Emit a
   per-part sealed type instead. `slider` added two more: `SliderRootProps.thumbAlignment`
   (`'center' | 'edge' | 'edge-client-only'`) and `.thumbCollisionBehavior` (`'push' | 'swap' | 'none'`),
   so the sample has to write `thumbCollisionBehavior = "swap"` as a bare string. Anchor positioning
   added one more, and it is the one that keeps the `mui.system.Union` import alive in a second file:
   `UseAnchorPositioningSharedParameters.positionMethod` (`'absolute' | 'fixed'`).
   Note `MenuPositionerState.instant` comes out as plain
   `String` and that is *correct*: upstream declares that one `string | undefined`, not as the literal
   union — the two shapes differ in Base UI itself, so do not "fix" them into one.
10. **`BaseUiDivProps['onClick']` indexed-access types stay `Any?`** (4 members, `MenuItem` /
   `MenuLinkItem`). Resolvable by substituting the parent member's type. Until then a call site has to
   annotate the parameter itself — the sample writes `onClick = { _: MouseEvent<*, *> -> … }`, which is
   the real runtime type and assigns to `Any?` fine. `slider` adds two more, from a different table:
   `SliderThumbProps.'aria-valuetext'` (`React.AriaAttributes['aria-valuetext']`) and
   `ThumbMetadata.inputId` (`LabelableContext['controlId']`).
11. ~~**`className` / `style` / `render` are `Any?`**~~ — done. The props stay `Any?` (each is a
   value-or-callback union over the part's own state type, which the per-tag parent shared by 126 parts
   cannot name), and the callback arm now has a typed spelling in `<Part>.ext.kt`:
   `className { state -> … }`, `style { state -> … }`, `render { props, state -> … }`. 18 of the 20
   `menu` parts get them — the two that do not, `MenuRoot` and `MenuSubmenuRoot`, render no element and
   so have no such props to type. The value arm needs no helper: `className = ClassName("popup")` already
   assigns to `Any?`. `render`'s `props` parameter is typed `HTMLAttributes<HTMLElement>`, which is
   exactly Base UI's own
   `HTMLProps = HTMLAttributes<any> & { ref }` — the per-tag third argument of `BaseUIComponentProps` is
   never used anywhere in the package, so even `MenuLinkItem`'s `<a>` gets plain `HTMLAttributes`
   upstream. Three limits worth knowing:
   - **The marker is looked for in the part's own `extends` list only**, against a hand-maintained
     alternation (`ELEMENT_PROPS_MARKER` = the 17 `BaseUi<Tag>Props` plus `FloatingPortalProps`). There is
     no transitive resolution, because `declarationParents` reads one file's body and that body does not
     contain its parents' declarations — so every stub that stands in for an element-props parent has to
     be added to the alternation by hand, or its part silently loses the helpers. A part inheriting the
     props through *another part's* props gets nothing either: in 1.6.0 that is `AlertDialogTriggerProps`
     and `ToastManagerPositionerProps`, latent until `alert-dialog` and `toast`.

     (An earlier revision of this entry said those two were safe because "the converter drops `Omit<…>`
     anyway". It does not — `findParentType` unwraps `Omit<`, which is why `slider`'s three parts
     declared `Omit<BaseUIComponentProps<'div', …>, 'id'>` keep `BaseUiDivProps` and their helpers. What
     makes those two latent is the *indirection*, not the `Omit`.)
   - **A generic props declaration is skipped**, which today is nobody: `MenuTriggerProps<Payload>` is
     generic upstream and gets helpers only because gap 4 strips the parameter. Closing gap 4 would
     remove `MenuTrigger.ext.kt` — a source-breaking removal for consumers — unless the helpers learn to
     carry the parameter at the same time. 12 of the package's 192 element-rendering props are generic.
   - **`render` does not merge.** `useRenderElement` calls `render(props, state)` and takes the result as
     it is (only the non-callback arm is merged), so a callback that ignores `props` silently drops
     `ref`, the event handlers and the `data-*` state attributes. Applying them is one character:
     `render { props, _ -> hr.create { +props } }` — `react.Props` declares
     `inline operator fun Props?.unaryPlus()` over `Object.assign`, and the repository already uses it
     (`playground/src/jsMain/kotlin/MyAutocomplete.kt:19`). Caveat, now in the generated KDoc: `+props`
     copies `children` too, so a builder that uses it must not add children of its own — the wrappers'
     `jsx` reports "Both `children` source options used" and keeps the builder's. The *element* arm
     (`render = span.create { … }`) is merged by Base UI itself; confirmed in the browser, where a
     `GroupLabel` rendered that way still came out with the `id` and `role` its group's
     `aria-labelledby` points at.
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

14. **A function type is widened whole, not per parameter.** When `toFunctionType` cannot translate one
   parameter, the guard added for `slider` throws the entire callback away and emits `Any?` with the
   TypeScript in a marker. `SliderRootProps.onValueChange` and `.onValueCommitted` are the visible cost —
   a slider's two most important props, both untyped, so the sample has to spell out
   `{ next: Any?, details: SliderRootChangeEventDetails -> … }` and gets no arity check. The generated
   `SliderRootChangeEventDetails` / `SliderRootCommitEventDetails` are consequently referenced from
   markers only.

   Fixing it means giving `toFunctionType` a real parameter-list parser instead of its replacement chain,
   so each parameter and the return type can be widened independently
   (`(value: Any? /* … */, eventDetails: SliderRootChangeEventDetails) -> Unit`). That is a change with a
   wide MUI blast radius, hence deferred. The three shapes that trip the guard today are a conditional
   type, a union in return position, and `unknown`; each one prints a line, so the population is visible
   rather than guessed at.
15. **Base UI warns about every `render` callback written in Kotlin.** It rejects a `render` function
   whose name starts with a capital, assuming a React component was passed by mistake. Kotlin/JS names a
   lambda after the declaration enclosing it, and a component `val` is `PascalCase` by convention, so the
   name is `BaseUiSlider$lambda$lambda$…` and the heuristic fires. Harmless — the callback calls no
   hooks — and cheaper than an earlier revision of this entry said: Base UI warns **once per page**, not
   once per call site. `BaseUiField` adds a second `render` call site and the console still carries
   exactly one such warning, naming whichever lambda got there first. The `.ext.kt` helper cannot rename
   the function it is handed. Worth a look if a cheap rename at the assignment turns out to exist.
16. **`BASE_UI_STUBS` has no unused-stub guard.** `unusedNamespaceStubs` reports a `NAMESPACE_STUBS`
   entry that nothing referred to, which is what catches a rewrite that silently stopped matching. The
   hand-written declarations in `BASE_UI_STUBS` have no equivalent, and `BaseUIGenericEventDetails` sat
   dead in the tree from the day it was written until `slider` — the alias that was supposed to reach it
   never converted. The same check applies verbatim and would have found it.
17. **The `children` formatting callback has no typed spelling** — for one part, not the three an
   earlier revision of this entry counted, and `field` is what corrected it. `Slider.Value.children` is
   honestly `Any?` (see Done), which is an improvement on being wrong, but the call site still writes the
   parameter types by hand. It is the same situation `render` was in before its `.ext.kt` helper, except
   that the signature is per part rather than shared.

   What makes it `Any?` is the `null` arm, not the callback: `null | ((formattedValues, values) =>
   ReactNode)` is a union `toFunctionType` cannot translate, so the whole thing widens.
   `FieldValidity.children` is a bare `(state: FieldValidityState) => React.ReactNode` and came out
   properly typed with no work at all. `Meter.Value` and `Progress.Value` share `Slider.Value`'s shape
   and will widen the same way; the population to design a helper for is those three, and `field` is not
   one of them.
18. **`T | null | undefined` on a function type yields a doubly-nullable type.**
   `SliderThumbProps.getAriaLabel` and `.getAriaValueText` come out
   `(((index: Number) -> String)?)?` — the `| null` branch in `KotlinType` makes it nullable and
   `MemberConverter`'s optional-member handling wraps it again. Cosmetic (`(A?)?` is `A?`) and the only
   two occurrences in the tree, but it will be ported downstream as written.

   The obvious fix is not right: moving `type.endsWith("?")` above the `type.startsWith("(")` branch in
   `MemberConverter.convertProperty` would also stop wrapping a function type whose *return* is nullable
   (`(a: X) -> Y?`), turning a nullable property into a non-null one. Telling the two apart needs the
   paren structure, not the last character.
19. **An `Omit<…>` parent gives back the members upstream removed.** `findParentType` unwraps
   `Omit<X, 'k'>` to `X` and drops the omit list, which is merely lossy everywhere it has fired so far
   and is wrong for the first time in `field`. `FieldValidityState extends Omit<FieldValidityData,
   'state'>` and then re-adds the same data under a different name (`validity:
   FieldValidityData['state']`), so the generated `FieldValidityState : FieldValidityData` carries both
   — and reading the inherited `state` yields `undefined`, the same class of wrongness as gap 3.

   Deliberately not fixed. Honouring the list means *inlining* the parent's members minus the omitted
   ones rather than inheriting, which is a new pass in the converter, and the decision is not
   `field`-specific: `toast` needs the same call (see "Remaining phases"). One occurrence is not enough
   to design against. Note the sample reads `validity`, never `state`, which is what a call site should
   do anyway.

   Distinct from the `Omit` problem in "Remaining phases", which is the opposite failure: there the
   `Omit<` is the *second* parent, `findParentType`'s branch does not fire at all, and the parent is
   dropped whole.

## Deliberately not generated

- `internals/`, `floating-ui-react/`, `types/`, per-module `utils/`, and every `*Context.d.ts` —
  internal plumbing, not public API. The *package-level* `utils/` is a different directory and not
  excluded wholesale: `utils/useAnchorPositioning.d.ts` is pulled in by `BASE_UI_EXTRA_FILES` for the
  props every Positioner inherits, and `utils/FloatingPortalLite.d.ts` is the next candidate (gap 6). Note `store/` is *not* excluded: `menu/store/MenuHandle.d.ts` is
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

## Next up: the fourth module

`field` unblocked the form controls, and the cheapest of them is the obvious next step — but "cheapest"
is now also "least informative", which is the tension `slider` and `field` were both picked against.

- **`checkbox` / `switch` / `radio` (2 bindings each)** and the flat `checkbox-group` / `radio-group`.
  All inherit `FieldRootState`, which now arrives from a real module rather than an extras entry, and
  `Field.Item` is the wrapper they are built around — the `aria-describedby` quirk recorded in Done is
  first checkable here. `radio-group` also brings the bound-less `<Value = any>` type parameter, which
  `substituteTypeParameterBounds` handles through the *default* arm and has never exercised.
- **`dialog` / `popover`** reuse `menu`'s whole machinery and would mostly re-run code that works.
  Cheap, but they prove little.
- **`tooltip` / `toast`** are the first to need `FloatingPortalLite.Props` in the stub table (gap 6),
  and both hit the second-parent `Omit<` failure described below. `toast` also forces the gap 19
  decision.

Whichever is taken, the bar `slider` and `field` set holds: each defect closed in the generator rather
than worked around in the sample, a playground sample driven in a browser, and this document updated
with what it actually cost — including where it contradicts what was predicted here.

## Remaining phases

`menu` is the vertical slice, `slider` the proof it generalizes; `BASE_UI_MODULES` in `Generator.kt` is
the allow-list to extend.

- A playground sample per module as modules land — `menu` and `slider` have one. See "What only the
  sample can catch".
- `field` is done — see Done. One detail that belongs nowhere else: `field/index.parts.d.ts` binds
  `FieldValidityData` with `export type { … } from`, which `EXPORT_CLAUSE` does not match at all. That
  is correct — it names no value, and the type is generated from `FieldRoot.d.ts` anyway — but it means
  `Field.ValidityData` is absent from the namespace object by design rather than by omission, and
  nothing logs it.
- Utils and the 13 flat modules: `use-render`, `merge-props`, `csp-provider`, `direction-provider`,
  `button`, `separator`, `input`, `form`, `toggle`, `toggle-group`, `radio-group`, `checkbox-group`,
  `menubar`, `unstable-use-media-query`.
- The other 29 part modules. Priority to those with `@mui/base` predecessors (select, switch, tabs,
  tooltip, dialog, popover, number-field, checkbox, radio). Two still need their own design: imperative
  Toast (`createToastManager` / `useToastManager`) and generic `Select<Value>` / `Combobox`. `Form` was
  the third; `field` has now taken half of it, and what is left is the `Form` module itself and the two
  types `FieldRootProps` still widens against it (gap 6).

  Which to take next is a real choice, not an ordering detail — see "Next up" above. `combobox` and
  `autocomplete` are the largest and the first to hit gaps 4, 6 and 13 at once.

  Anything with a Positioner used to be gated on gap 9; that is closed, and the anchor-positioning props
  are inherited by all eight Positioner parts, so `select` / `popover` / `tooltip` / `navigation-menu` /
  `preview-card` / `combobox` / `toast` arrive with that surface already working. Two of them will **not** inherit it at
  all, and the failure is silent: `TooltipPositionerProps extends BaseUIComponentProps<'div', …>,
  Omit<UseAnchorPositioningSharedParameters, 'side'>` and `ToastPositionerProps extends …, Omit<…,
  'side' | 'anchor'>` spell the `Omit<` as the *second* parent. `findParentType`'s `Omit` branch only
  fires when the whole parent source starts with `Omit<` (`ParentType.kt:40`) — which is why `slider`'s
  single-parent `Omit<BaseUIComponentProps<'div', …>, 'id'>` keeps `BaseUiDivProps` — so these two fall
  to the multi-parent split instead, where `isAcceptableParent` rejects any prefix in
  `TS_UTILITY_PREFIXES`. The positioning parent is dropped entirely, and `unresolvedParents` cannot
  report it: the name is filtered out, not left unresolved. First thing to check when either lands.

  Two smaller candidates that would still break new ground: `accordion` (a `Pick<AccordionRoot.Props, …>`
  parent, an `'h3'` tag, and `AccordionRootProps<Value = any>` — the bound-less parameter shape) and
  `field` itself (above).
- `@mui/base` stays generated and frozen. Only two generated files depend on it —
  `mui/material/Snackbar.kt` (`ClickAwayListenerProps`) and `mui/material/Autocomplete.kt`
  (`UseAutocompleteProps`) — so retiring it later is cheap.

## What only the sample can catch

`:mui-kotlin:compileKotlinJs` type-checks the declarations against each other. Three things it cannot
see, all of which the sample does, and all of which CI now enforces because `./gradlew build` compiles
`:playground`:

- **Call-site resolution of the `.ext.kt` helpers.** `className { … }` has to resolve to the extension
  function and not to the inherited `className: Any?` property it shadows. Nothing in `mui-kotlin`
  exercises that, since the helpers are only usable from outside.
- **That the namespace object's keys exist at runtime.** `external object Menu { val Popup: … }` compiles
  whatever the module actually exports; only mounting a part proves the mapping. The sample mounts all
  20, so a key that stops existing takes the page down rather than failing at some user's call site.
- **That hand-written stubs still match upstream.** `FloatingPortalProps.container` is spelled by hand;
  passing it once from the sample is what would notice a rename. Same for the shape of the state types
  the helpers close over.
- **That a member reaches the right declaration when two are in scope.** `SliderValueProps` redeclares
  `children` over the one it inherits from the per-tag parent, with a different type; only assigning a
  lambda and watching Base UI call it proves the redeclaration is what a call site binds to.

`slider` also showed the sample catching a plain rendering fault the DOM assertions did not: giving
`Slider.Track` a `render` callback without a `className` produced a zero-width, invisible track. Take a
screenshot, not only a `querySelector` dump.

`field` added a fifth thing only the sample can see: **that an imperative handle behind an `Any?` ref
matches the interface generated for it.** `FieldRootProps.actionsRef` is
`Any? /* React.RefObject<FieldRootActions | null> */`, so nothing type-checks what Base UI puts in the
ref against `FieldRootActions` — only calling `validate()` on it and watching the field validate does.
The sample reports which of the two happened rather than calling it and hoping.

Two caveats for whoever extends the sample. `Menu.Viewport` is not decoration — mounting it sets
`hasViewport`, which switches the positioner to its `adaptiveOrigin` middleware, and it is meant to wrap
the popup's content rather than sit beside it. And a sibling sample (`SliderStylization`) lays an
absolutely positioned element over the whole viewport, so anything interactive needs its own
`position: relative; z-index: 1` or its clicks land in the slider — `document.elementFromPoint` is how
that gets diagnosed.

## Repository note: running the playground

`./gradlew :playground:jsViteDev` in the background, then `http://localhost:5173/` (vite's default port;
its root is the generated `kotlin` directory). Read the port out of the task's own output rather than
assuming it: vite takes the next free one if 5173 is occupied by an earlier run, and the stale server on
5173 will happily keep serving the previous bundle.

**`:playground:compileKotlinJs` alone does not update what the dev server serves.** The bundle vite reads
is produced by `:playground:jsDevelopmentExecutableCompileSync`, so a code change needs that task and
then a page reload — otherwise the browser keeps showing the previous build, which looks exactly like the
change not working.

Do not run `:mui-kotlin:generateDeclarations` on its own either: `formatDeclarations` hangs off the
`compileKotlinJs` chain, so a standalone run leaves the whole generated tree unformatted and `git status`
shows hundreds of files of pure formatting churn. Finish with `:mui-kotlin:compileKotlinJs`.

## Repository note: the generated tree is now reproducible

It previously was not. The generator emits Kotlin with no indentation and `X:` instead of `X :`, and
readable output depended on running an IDE reformat by hand — so the committed tree could not be
reproduced from the generator, and every regeneration showed ~520 files of pure formatting churn that
masked real changes.

The pipeline now formats `src/jsMain/kotlin` itself
(`generateDeclarations` → `formatDeclarations` → `compileKotlinJs`), so **a regeneration produces a
byte-identical tree** — verified by hashing it across two runs. A non-empty diff under
`mui-kotlin/src/jsMain/kotlin` now means something actually changed, and is worth reading.

The formatter is **IntelliJ IDEA's own**, run headless:

    format.sh -s gradle/idea-code-style.xml -charset UTF-8 -r -m '*.kt' <tree>

so a local IDEA is a build requirement. It is found automatically in `~/Applications` and
`/Applications`; `idea.home` (in `~/.gradle/gradle.properties`, or `-Pidea.home=…`) or `$IDEA_HOME` names
a different one, and finding two installations is an error rather than a guess. `IDEA_PROPERTIES` points
the spawned IDE at `build/idea-format/`, which is not optional: without it the run collides with the
desktop IDE's locked configuration directory and dies with "Only one instance of IDEA can be run at a
time", so a build could not be started from the IDE that has the project open. The isolation also keeps
third-party plugins and personal settings out of the output.

Because the formatter is a local application, **CI can no longer check that the committed tree is up to
date** — no IntelliJ IDEA a Linux runner can install matches this one (newest Community is 2025.3 / build
253, against 262 here). The workflow builds with `-Pdeclarations.skip=true`, which makes
`generateDeclarations` and `formatDeclarations` no-ops, so it checks that the committed declarations
compile and nothing more. The up-to-date check is now yours to run, and is the last step of a
regeneration:

    ./gradlew :mui-kotlin:compileKotlinJs && git diff --exit-code -- mui-kotlin/src/jsMain/kotlin

**Reproducibility is now keyed to the IDEA build, not to a pinned library version.** An IDE upgrade can
legitimately change the tree with no change in this repository. The build number is logged on every run
(`Formatting 653 Kotlin file(s) with IU-262.9437.185`) so that such churn is diagnosable; the committed
tree was produced by IU-262.

**The formatter cannot insert trailing commas.** It honours `ALLOW_TRAILING_COMMA` when deciding how to
wrap — which is why chopped lists are one element per line — but inserting the comma is a post-format
cleanup step that the command-line entry point does not run. `IdeaFormatTask` adds it afterwards, in the
24 files that hold such a list; without that, ⌥⌘L on those files would dirty a freshly built tree.

This replaced **ktfmt** (via Spotless), which had replaced a by-hand IDE reformat. ktfmt was chosen over
ktlint because ktlint enforces a style policy that generated code cannot satisfy at the source: it rejects
the inline `/* … */` markers recording the original TypeScript type (`var side: Any? /* Side */`) and the
lowercase `@JsValue` union members whose names must mirror the JavaScript API — 47 unfixable violations,
and suppressing those rules still left 3551 KDoc blocks with the opening `/**` at column 0. ktfmt had no
rules to satisfy. It was dropped because its layout is not the IDE's — switching changed 582 of the 653
files — so ⌥⌘L dirtied the tree and the committed tree could not survive one. Chaining the two cannot reconcile that
— IDEA's formatter re-indents and wraps overlong lines but never re-joins lines another tool has already
broken, so whichever runs last does not win.

One thing ktfmt did is consequently no longer done: KDoc is not reflowed to a column limit, nor are its
block tags reordered, so the committed tree preserves upstream's own wrapping and `@default`/`@param`
order.

Imports, which ktfmt also tidied, are handled by the generator instead. `inIdeaImportOrder` sorts them the
way Optimize Imports does — plain lexicographic on the full path, uppercase before lowercase, so `react.FC`
sorts above `react.dom.html.HTMLAttributes` — because `format` lays out code but leaves the import list
alone. IDEA's default layout additionally moves `java.**`, `javax.**` and `kotlin.**` into trailing groups
of their own; the tree contains none, so that part is unimplemented and fails loudly if one appears rather
than guessing. `retainReferencedImports` then drops an import naming the file's own package, or one whose
short name is referenced nowhere in the file. Both import mechanisms over-produce: `DEFAULT_IMPORTS` is keyed on a plain substring,
so `Element` fired inside `HTMLDivElement` (201 times) and `Event` inside `MouseEventHandler` (62), and
`resolveImportedFqns` skips `import` lines when looking for a real occurrence but not comments, so an FQN
appearing only inside a `/* … */` TypeScript marker left an import behind. Pruning after the fact rather
than tightening the triggers keeps the change subtractive: a trigger that stopped firing would silently
lose a needed import, whereas this can only drop one nothing refers to, and `compileKotlinJs` runs on the
result.

Only `mui-kotlin/src/jsMain/kotlin` is in scope — hand-written code (`buildSrc`, `playground`) is
deliberately left alone so that adding the formatter did not reformat anything a human wrote.
