# Generator backlog

Canonical plan for remaining generator capabilities and known compromises from the MUI migration audits.
Completed v6 work and the original migration decisions are in [MUI v6 history](../history/mui-v6.md);
v7 and v9 records are in [MUI v7 history](../history/mui-v7.md) and [MUI v9 history](../history/mui-v9.md).
Base UI module work has its own [canonical plan](base-ui.md).

## Resume here

- **Recorded phase:** the high-value v6 typing fixes were closed on 2026-10-31 against MUI 9.5.0.
  The highest-priority open generator item is the `TreeViewSelectionValue` follow-up from merged
  [`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927); the remaining v6/v9
  candidates below are low priority.
- **Next step:** select the item requested by the user and recheck it against the current generator and installed
  upstream `.d.ts` / runtime exports. If continuing this backlog without a selected item, start with the
  **highest-priority** `TreeViewSelectionValue` follow-up
  ([details in `future-improvements.md`](future-improvements.md#highest-priority-treeviewselectionvalue-common-singlemulti-value-representation-pr-2927-follow-up)).
- **Prerequisites:** confirm the current package versions in `gradle.properties`, the API's public runtime export,
  and existing generated/sample coverage. Older audit signatures and workarounds are evidence to verify.
- **Completion:** follow [Kotlin type selection](../../.agents/rules/kotlin-types.md) and
  [generator verification](../../.agents/rules/generator-verification.md); update this plan and record the
  completed change and checks in the relevant history file.
- **PR #2924 review (2026-11-05):** the local generator follow-up is complete. The nine Material components
  with own slots now suppress `VIRTUAL_MEMBER_HIDDEN` and `VAR_TYPE_MISMATCH_ON_OVERRIDE` on the affected
  properties; their file-level suppressions are removed. Types and inheritance are unchanged, and the clean
  build, byte-for-byte regeneration and browser checks passed.
  [Scope and verification](../history/mui-v6.md#own-slot-override-suppressions--pr-2924-review-2026-11-05).
  The generator fix is committed as `22700a92`; the user committed its nine-file generated delta as
  `25f85112f6` on the fork's `mui-own-slot-props` branch and confirmed updating PR #2924.
  [Transfer evidence](../history/mui-v6.md#own-slot-suppression-fork-handoff-2026-11-05).
  [Publication status](../history/mui-v6.md#own-slot-suppression-pull-request-updated-2026-11-05).
  Next step for this follow-up: upstream review and any requested changes. Unrelated suppression limitations,
  including Base UI gap 12, remain deferred.
- **Tree View transfer and PR #2927 review (2026-11-06):** [`JetBrains/kotlin-wrappers#2927`](https://github.com/JetBrains/kotlin-wrappers/pull/2927)
  is **merged** into `JetBrains/kotlin-wrappers` (`67f0cceaf3`). One open review thread on
  `TreeViewSelectionValue.kt`
  ([`#discussion_r4484280313`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4484280313),
  [`#discussion_r4485031537`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4485031537) —
  keep `sealed /* union */ external interface TreeViewSelectionValue` or use a common type, and remove the custom
  adapter/factory helpers) was agreed to be addressed in a separate follow-up PR and is tracked as the
  **highest-priority** item in
  [`future-improvements.md`](future-improvements.md#highest-priority-treeviewselectionvalue-common-singlemulti-value-representation-pr-2927-follow-up)
  and in the summary below.
  [Scope, baseline and verification](../history/mui-v9.md#tree-view-fork-handoff-2026-11-05).
  [Model filename follow-up](../history/mui-v9.md#tree-view-model-filenames-follow-up-2026-11-05).
  [Publication status](../history/mui-v9.md#tree-view-pull-request-opened-2026-11-05).
  [PR #2927 review follow-up](../history/mui-v9.md#tree-view-jsplainobject-review-follow-up--pr-2927-2026-11-05).
  [PR #2927 fork handoff](../history/mui-v9.md#tree-view-jsplainobject-fork-handoff-2026-11-05).
  [PR #2927 JSO `val` and union comment follow-up](../history/mui-v9.md#tree-view-jso-val-and-union-comment-follow-up--pr-2927-2026-11-05).
  [PR #2927 JSO `val` and union comment fork handoff](../history/mui-v9.md#tree-view-jso-val-and-union-comment-fork-handoff-2026-11-05).
  [PR #2927 `private` event and `Int` loader properties fork handoff](../history/mui-v9.md#tree-view-private-event-and-int-loader-properties-fork-handoff-2026-11-05).
  [PR #2927 merged and `TreeViewSelectionValue` follow-up](../history/mui-v9.md#tree-view-pull-request-merged-and-treeviewselectionvalue-follow-up-2026-11-06).
  The filename follow-up also records one existing buildSrc test failure reproduced on the unchanged baseline;
  that separate runtime-binding test mismatch remains unresolved.
- **Last recorded evidence:** [v6 audit resolutions](../history/mui-v6.md),
  [material theme](../history/mui-v9.md#the-material-theme-is-converted-from-createthemenovarsdts), and
  [runtime-export verification](../history/mui-v9.md#runtime-imports--resolved-from-npm-exports-during-generation).
  These records do not replace verification of a new change.

## Сводка открытых пунктов (по практической ценности)

**Наивысший приоритет:** `TreeViewSelectionValue` (`@mui/x-tree-view`) — убрать кастомные адаптеры/фабрики
(`TreeViewSelectionValue(...)`, `asMultiple()`, `asSingle()`) из `TreeViewSelectionValue.kt` и использовать общий
подход `kotlin-wrappers` для single/multi-value по замечаниям в
[`PR #2927#discussion_r4484280313`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4484280313) и
[`PR #2927#discussion_r4485031537`](https://github.com/JetBrains/kotlin-wrappers/pull/2927#discussion_r4485031537)
(подробности в
[`future-improvements.md`](future-improvements.md#highest-priority-treeviewselectionvalue-common-singlemulti-value-representation-pr-2927-follow-up)).

Низкий приоритет: `createColorScheme`/`extendTheme`/CssVars-провайдер, `OverridableComponent`, Pigment-компоненты,
template literal types, JSDoc для `Omit`-ключей, `Rating.defaultValue`/`StepIcon`/`Autocomplete.key` (приемлемые
обходы уже есть).

Формат подробностей: `<имя>` — `<MUI .d.ts путь>` — Status / Reason / Workaround / To fix.
Статусы ⏳ и ограничения ниже перенесены из сверки 2026-10-31 (MUI 9.5.0); перепроверьте выбранный пункт перед работой.

## Полностью скипнутые .d.ts файлы

### Material

- **`createThemeWithVars`** — `@mui/material/styles/createThemeWithVars.d.ts` — ⏳ (частично ✅)
    - Status: excluded
    - ✅ С v7 типы `CssVarsTheme`, `CssVarsPalette`, `ColorSystem`, `ColorSystemOptions`, `ThemeVars` живут в
      `createThemeFoundation.d.ts` и генерируются оттуда (`adapters/CreateThemeFoundation.kt`).
    - ⏳ Не хватает самой функции — она экспортируется как `extendTheme` из `@mui/material/styles` — и
      `CssVarsThemeOptions`.
    - To fix: ручной стаб `extendTheme` по образцу `createTheme.kt`, либо разбор mapped types (`{ [K in X]: Y }`).

- **`createColorScheme`** — `@mui/material/styles/createColorScheme.d.ts` — ⏳ (низкий приоритет)
    - Status: excluded
    - Reason: v6 CssVars API. Тип-аргумент `ColorSystemOptions` теперь генерируется, так что блокер — только
      стаб самой функции.
    - To fix: ручной стаб `createColorScheme(options: ColorSystemOptions): ColorSystemOptions`.

- **`OverridableComponent`** — `@mui/material/OverridableComponent/index.d.ts` — ⏳ (низкий приоритет)
    - Status: excluded (`Generator.kt::generateMaterialDeclarations`)
    - Reason: type-only утилита: call signatures + intersection types в `interface OverridableComponent<TypeMap>`.
    - Workaround: компоненты с `: OverridableComponent<` / `= OverridableComponent<` / `ExtendButtonBase<`
      распознаются в `Converter.kt::findMapProps::hasComponent` и получают `mui.types.PropsWithComponent`.
    - To fix: научить генератор обрабатывать call signatures на интерфейсе.

- **`ThemeProvider`/`ThemeProviderNoVars`/`ThemeProviderWithVars`** — `@mui/material/styles/ThemeProvider*.d.ts` — ⏳
    - Status: excluded
    - Reason: TS conditional types для слотов CssVars (`extends X ? {...} : {}`).
    - Workaround: ручной стаб `external val ThemeProvider: react.FC<ThemeProviderProps>` (children, theme), привязан
      к barrel `@mui/material/styles` — deep path `styles/ThemeProvider` отсутствует в `exports`, Vite его не
      резолвит. В `mui.system` с v9 такой же ручной стаб (там `ThemeProvider` стал generic-функцией).
    - To fix: распознавать TS conditional types в `adaptRawContent`; тогда же — CssVars-варианты провайдера.

- **Pigment-CSS варианты** — `@mui/material/Pigment*/...` — ⏳ (низкий приоритет)
    - Status: excluded (`Generator.kt::generateMaterialDeclarations` filter `!startsWith("Pigment")`)
    - В 9.5.0 это `PigmentContainer`, `PigmentGrid`, `PigmentStack`; все три есть в `exports`.
    - Reason: дублируют Container/Grid/Stack-типы в том же пакете → Redeclaration.
    - To fix: при необходимости — отдельный subpackage `mui.material.pigment`.

## Стабы (вместо настоящей генерации)

- **`FocusVisible`** — `typealias FocusVisible = react.CSSProperties` (`FocusVisible.kt`):
  генератор не эмитит простые type alias'ы.
- **`mui.material.styles.createTheme`** — ручной стаб над barrel `@mui/material/styles` (см. [историю](../history/mui-v6.md#material)).
- **`mui.material.styles.ThemeProvider`**, **`mui.system.ThemeProvider`** — минимальные ручные обёртки (см. [Material](#material)).
- **`mui.system.SystemProps<T>`** — `typealias SystemProps<T> = react.Props`. Используется Typography.
- **`LinkBaseProps`, `TablePaginationBaseProps`** — пустые стабы, см. «Internal-rejected parents».

`Theme` / `ThemeOptions` and `Breakpoints` are generated; their restoration is recorded in
[MUI v6 history](../history/mui-v6.md#стабы-вместо-настоящей-генерации).

## Известные потери / упрощения в публичном API

- **CssVars вселенная** — ✅ типы `CssVarsTheme`/`CssVarsPalette`/`ColorSystem`/`ColorSystemOptions`/`ThemeVars`
  генерируются из `createThemeFoundation.d.ts`; `Theme.vars: ThemeVars?` и `ThemeOptions.cssVariables`/
  `colorSchemes`/`defaultColorScheme` есть. ⏳ Нет `extendTheme`, `createColorScheme`, CssVars-провайдера.
- **`Theme.applyStyles`** — `Any? /* ApplyStyles<…> */` в `mui.system.Theme`, material `BaseTheme` и `CssVarsTheme`.
- **`Typography.color`** — теперь `Union? /* OverridableStringUnion<… `text${Capitalize<keyof TypeText>}` …> | string */`
  (был `Any?`). Шаблонный литерал по-прежнему не раскрывается.

- **`Rating.defaultValue`** — расширена с `Number?` до `Any? /* Number */`, чтобы не клешиться с inherited
  `HTMLAttributes.defaultValue: Any?` (Kotlin var-invariance не позволяет diamond сузить тип).
  В `Rating.ext.kt` есть `inline var RatingProps.defaultValueAsNumber: Number?` для типизированного доступа.
- **`SlotProps` inner typing для всех `XxxSlotsAndSlotProps`** — структура по именам слотов сохранена:
  generator парсит второй аргумент `CreateSlotsAndSlotProps<XxxSlots, { root: SlotProps<…>; input: …; }>` и эмитит
  отдельный `XxxSlotProps` интерфейс с полем на каждый слот. Поля типизируются по RootComponent там, где он
  распознаётся (см. [реализованные формы](../history/mui-v6.md#-низкая-сложность--реализовано)), иначе `Any?`;
  исходный TS-тип — в `/** TS: SlotProps<…> */`.

- **`StepIcon`** — SvgIconOwnProps стрипнут из родителей (HTMLAttributes T-параметр не совмещался). Типизированный
  доступ — `StepIcon.ext.kt::asSvgIconOwnProps()`.

- **Internal-rejected parents** — `ParentType.kt::INTERNAL_REJECTED_PARENTS` (`BasePopperProps`,
  `DialogActionsProps`, `CssContainerQueries`, `StyledComponentProps`, плюс десяток
  mui-x-внутренних — список см. в коде и в истории [v7](../history/mui-v7.md) / [v9](../history/mui-v9.md)) отбрасываются из родительских списков. **`LinkBaseProps`
  и `TablePaginationBaseProps`** эмитятся как пустые стабы (`MATERIAL_LINK_BASE_PROPS_STUB`,
  `MATERIAL_TABLE_PAGINATION_BASE_PROPS_STUB`) — цепочка наследования сохранена на уровне символа.

Completed slot, TextField, Grid and literal-union fixes are in
[MUI v6 history](../history/mui-v6.md#известные-потери--упрощения-в-публичном-api).

### Recorded compromises in completed support

Restored APIs still had the following compromises in the 2026-10-31 audit. Keep them discoverable when
working on the affected API; compare each with current code before treating it as an outstanding task.

- **Material theme:** object-form `cssVariables`, the `ThemeOptions.typography` callback, `focusVisible | false`,
  tuple/partial handling for `Shadows` / `ZIndexOptions`, loose theme fields, and CssVars palette access.
  [Recorded details](../history/mui-v6.md#material).
- **Spacing:** string arguments through `SpacingArgument` and the widened `createSpacing.transform` callback.
  [Recorded details](../history/mui-v6.md#system).
- **TextField and Grid:** simplified generic `InputBaseProps` and widened `GridSpacing`.
  [Recorded details](../history/mui-v6.md#известные-потери--упрощения-в-публичном-api).

## Глобальные конвенции (после v6-этапа)

- Фолбэк для непарсимых типов — `Any? /* оригинальный TS-тип */`, а не `dynamic`. Так сохраняется источник и
  nullability. См. `KotlinType.kt::kotlinType` нижний `return`.
- `ResponsiveStyleValue<T>` и `react.Ref<T>` имеют bound `T : Any` — фолбэк внутри них убирает `?`:
  `Any? /* ... */` → `Any /* ... */`.
- `XxxSlotsAndSlotProps` — type-alias из `CreateSlotsAndSlotProps<XxxSlots, {…}>` конвертируется в ДВА
  интерфейса: `XxxSlotsAndSlotProps { slots?: XxxSlots; slotProps?: XxxSlotProps; }` и сам `XxxSlotProps` с
  именованными полями на каждый слот (`Converter.kt::convertSlotsAndSlotPropsAliases` +
  `parseInlineSlotProps` + `mapSlotPropsToKotlin`).
- Компоненты, которые в TS `Omit`-ят родительские `slots/slotProps` ради своих, наследуют оба и переобъявляют
  члены своими типами с подавлением `VAR_TYPE_MISMATCH_ON_OVERRIDE` на самих свойствах
  (`adapters/OwnSlotsAndSlotProps.kt`, `Overrides.kt::overrideOwnSlots`). Остальные скрывающие свойства этих
  девяти Material-компонентов получают точечный `VIRTUAL_MEMBER_HIDDEN`; file suppress для них не нужен.
- При появлении новых `Omit<StandardProps<...>, '...'>` / `StandardProps<Omit<...>>` / `DistributiveOmit<...>` /
  `Pick<...>` / `Partial<...>` форм — `Converter.kt::adaptRawContent` их распарсивает и коллапсирует. Partial
  применяется только в extends-позиции (lookbehind `(?<=\bextends )` / `(?<=,\s{4})`), чтобы не сломать
  `Partial<X>` в member-типах, где `kotlinType()` имеет специальные обработки.
- `(?<!\w)Omit<` lookbehind не даёт регексу для `Omit<…>` "съесть" внутреннее имя из `DistributiveOmit<…>`.
- `findParentType` разбирает multi-parent extends-списки depth-aware split'ом по top-level запятым и с v7
  работает по принципу **filter-and-keep** (`ParentType.kt`): `Omit<X, …>`/`Pick<X, …>`/`Partial<X>` разворачиваются
  до `X`, отвергнутые родители (TS-utilities, `INTERNAL_REJECTED_PARENTS`) выбрасываются, остальные сохраняются.
  (На v6-этапе было «все-или-ничего» с дефолтом в `react.Props`.)
- `Generator.kt::generate(...)` имеет fallback на `index.d.ts` если `${dir.name}/${dir.name}.d.ts` нет
  (полезно для useMediaQuery, OverridableComponent).
- Tooltip, Rating и StepIcon получают дополнительные `*.ext.kt` через спец.case в Generator (TooltipPlacement typealias,
  `Rating.defaultValueAsNumber`, `StepIconProps.asSvgIconOwnProps()`).
- Дефолтные generic-параметры функций переписываются точечно в `FunctionConverter.kt::findDefaultFunction`:
  `<T = Theme>` → `<T : Theme>` (`useTheme`), `<T = null>` → `<T>` (`useThemeWithoutDefault`).

## Будущие доработки типизации

Кандидаты на улучшение генератора. Сгруппировано по сложности.

### Низкая сложность

Existing `SlotProps` conversions and extension points are documented in
[MUI v6 history](../history/mui-v6.md#-низкая-сложность--реализовано).
Add unsupported HTML tags only when needed by an upstream declaration or consumer.

### Средняя сложность

- **DistributiveOmit/Omit — пометка "omitted keys" в JSDoc.** ⏳ Откладывается — ROI низкий.
  Inline-комментарий (`extends X /* Omitted: 'a' */`) ломает разбор родителя; side-channel
  (`Map<interfaceName, omittedKeys>` + два прохода) требует рефакторинга `adaptRawContent` (сейчас pure pipeline).
  Большинство случаев — `'slots' | 'slotProps'`-омит на OwnerState'ах; интересные (`Omit<TransitionProps, 'children'>`
  в Fade, `Omit<TypographyTypeMap['props'], 'classes'>` в DialogTitle) единичны.

- **Rating — более тонкая правка для diamond.** ⏳ Попробовать `override var defaultValue: Any?` в `RatingProps`;
  откладывается до пробы, обход `defaultValueAsNumber` работает. Switch became obsolete with v7 and
  StepIcon's extension workaround was implemented; see [history](../history/mui-v6.md#средняя-сложность).

### Высокая сложность

- **CssVars вселенная** (`extendTheme`, `createColorScheme`, CssVars-`ThemeProvider`). ⏳ Типы и `Theme.vars` уже
  есть; требует:
    - Поддержки TS conditional types в `Converter.kt::adaptRawContent` (или ручных стабов).
    - Поддержки TS mapped types `{ [K in X]: Y }` → Kotlin `Record<X, Y>` или подобное.

- **TS template literal types** (`text${Capitalize<keyof TypeText>}` в `Typography.color`). ⏳ Распознавать
  `` `…${X}…` `` и схлопывать к `String` (либо к `mui.system.Union` с комментарием набора кейсов).

- **TS overloaded call signatures на interface.** ⏳
    - `interface Spacing { (): string; (v): string; (a, b): string; … }` — ✅ точечный конвертер
      `Spacing.kt::convertSpacing` (см. [историю System](../history/mui-v6.md#system)).
    - `interface OverridableComponent { (props: …): JSX; (props: …): JSX; }` — общий механизм не реализован.

- **OverridableComponent с TypeMap.** ⏳ Типизация `<RootComponent extends ElementType>` для динамического
  тега. Сейчас обходим через `mui.types.PropsWithComponent`, но это сильно упрощённо.

- **TS generic с дефолтом конкретного типа.** Known `useTheme` / `useThemeWithoutDefault` cases already have
  targeted rewrites in `FunctionConverter.kt`; there is no general mechanism. Revisit only when a new case appears.

### Не вошло в v6-минимум

- **Pigment-CSS варианты в отдельный subpackage** (`mui.material.pigment`) вместо полного скипа — ⏳ низкий приоритет.

## Follow-ups recorded during the v9 migration

These are known limitations or maintenance triggers from the v9 record, not a new verification or a new
priority order. Recheck the affected installed typings and current samples before selecting work.

- **DateCalendar view options:** `views`, `openTo` and `onViewChange` were omitted with
  `ExportedUseViewsOptions` because a sibling callback uses optional function-type parameters.
  [Context](../history/mui-v9.md#phase-5c--type-quality-pass-on-the-restored-components-no-more-anylost-inheritance).
- **Pickers CSS-class browser coverage:** the v9 pass exercised the 16 restored class objects and
  `dayCalendarClasses`; ten pre-existing objects were not covered by that sample.
  [Context](../history/mui-v9.md#phase-5d--the-pickers-css-class-objects-16-of-27-were-never-generated).
- **Tree View version bumps:** compare hand-written `TREE_VIEW_*` shapes with the upstream files named in
  their comments, and exercise the consumer sample. This is a maintenance step on a bump, rather than a
  request to replace all stubs now.
  [Sources and evidence](../history/mui-v9.md#tree-view-the-behavioural-props-and-the-typing-gaps-fixed-2026-10-31).
- **Base UI / Menubar and eventual `@mui/base` retirement:** continue through
  [the Base UI plan](base-ui.md#execution-order-by-predictability), preserving its task order and checkpoints.
  NumberField's initial integration is complete; full type fidelity is tracked separately there.
