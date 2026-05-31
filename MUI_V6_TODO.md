# MUI v6 — backlog: что генератор пока не умеет

Список v6-новинок и форм, которые при бампе с MUI v5 → v6 (`@mui/material` 5.18.0 → 6.5.0, `lab` 5.0.0-alpha.177 →
6.0.1-beta.36, `base` остался на 5.0.0-beta.70) обработать целиком не удалось. Каждый пункт — кандидат на поддержку:
либо отдельный адаптер, либо общий механизм в `Converter.kt` / `KotlinType.kt`.

Формат: `<имя>` — `<MUI .d.ts путь>` — Status / Reason / Workaround / To fix.

## Полностью скипнутые .d.ts файлы

### Material

- **`createThemeNoVars`** — `@mui/material/styles/createThemeNoVars.d.ts`
    - Status: excluded (`Generator.kt::isStyleDefinition`)
    - Reason: использует TS conditional types (`CssThemeVariables extends { enabled: true }`), которые конвертер не
      разбирает; даёт несинтаксический Kotlin.
    - Workaround: ручной stub `external interface Theme : mui.system.Theme` +
      `typealias ThemeOptions = mui.system.ThemeOptions` в `generateStylesDeclarations`.
    - To fix: научить `adaptRawContent` распознавать conditional type и схлопывать в простой union/`Any?`.

- **`createThemeWithVars`** — `@mui/material/styles/createThemeWithVars.d.ts`
    - Status: excluded
    - Reason: вся декларация про CssVarsTheme — TS generics над `SupportedColorScheme`, `ThemeCssVar<...>`, mapped
      types.
    - Workaround: типы `CssVarsTheme`/`CssVarsPalette`/`ColorSystemOptions` отсутствуют в Kotlin-стороне.
    - To fix: либо отдельный адаптер `adapters/CssVarsTheme.kt`, либо расширить `Converter.kt` для mapped types (
      `{ [K in X]: Y }`).

- **`createTheme` (material/styles)** — `@mui/material/styles/createTheme.d.ts`
    - Status: excluded
    - Reason: сигнатура
      `createTheme(options?: Omit<ThemeOptions, 'components'> & Pick<CssVarsThemeOptions, ...> & { cssVariables?: ... })`.
      Парсер function-args не справляется с intersection + Omit + Pick + inline.
    - Workaround: функция отсутствует. Пользователи временно используют `mui.system.createTheme`.
    - To fix: handler для intersection-типов в `KotlinType.kt::toFunctionType`, либо подмена 1-го аргумента на простой
      `ThemeOptions` через адаптер.

- **`createColorScheme`** — `@mui/material/styles/createColorScheme.d.ts`
    - Status: excluded
    - Reason: использует `ColorSystemOptions` (v6 CssVars).
    - Workaround: функция и тип отсутствуют.
    - To fix: связано с поддержкой `createThemeWithVars`.

- **`useLazyRipple`** — `@mui/material/useLazyRipple/useLazyRipple.d.ts`
    - Status: excluded (`Generator.kt::generateMaterialDeclarations` filter set)
    - Reason: возвращает `LazyRipple` — внутренний v6 тип без публичной декларации.
    - Workaround: хук отсутствует.
    - To fix: ручной адаптер или раскрытие `LazyRipple` как `Any /* LazyRipple */`.

- **`OverridableComponent`** — `@mui/material/OverridableComponent/index.d.ts`
    - Status: excluded (даже после index.d.ts fallback)
    - Reason: TS call signatures + intersection types в `interface OverridableComponent<TypeMap>`. Конвертер выдаёт
      несинтаксический Kotlin.
    - Workaround: эмиссия пропущена. Тип используется только внутри других компонентов через спец.ветки
      `Converter.kt::findMapProps::hasComponent`.
    - To fix: научить генератор обрабатывать call signatures на интерфейсе.

- **`ThemeProvider`/`ThemeProviderNoVars`/`ThemeProviderWithVars`** — `@mui/material/styles/ThemeProvider*.d.ts`
    - Status: excluded
    - Reason: используют TS conditional types для слотов CssVars (`extends X ? {...} : {}`); генератор оставляет
      несбалансированные `}` в выводе.
    - Workaround: ручной stub `external val ThemeProvider: react.FC<ThemeProviderProps>` в `generateStylesDeclarations`
      с минимальным `ThemeProviderProps` (children, theme).
    - To fix: распознавать TS conditional types в `adaptRawContent`.

- **Pigment-CSS варианты** — `@mui/material/Pigment*/...` (PigmentGrid, PigmentHidden, …)
    - Status: excluded (`Generator.kt::generateMaterialDeclarations` filter `!startsWith("Pigment")`)
    - Reason: Pigment-CSS дублирует Hidden/GridBaseProps в том же пакете, что и Emotion-варианты → Redeclaration.
    - Workaround: эмиттим только основные компоненты.
    - To fix: при необходимости — выделить Pigment в отдельный subpackage `mui.material.pigment`.

### System

- **`createSpacing` (system)** — `@mui/system/createTheme/createSpacing.d.ts`
    - Status: excluded
    - Reason: overloaded call signatures на `interface Spacing` + union из function types в параметре `transform`.
    - Workaround: функция `createSpacing` отсутствует. Тип `Spacing` теперь стаб (`external interface Spacing` через
      `SYSTEM_SPACING_STUB`).
    - To fix: поддержать TS call-signatures на интерфейсе.

- **`useThemeWithoutDefault`** — `@mui/system/useThemeWithoutDefault/useThemeWithoutDefault.d.ts`
    - Status: excluded
    - Reason: сигнатура `useThemeWithoutDefault<T = null>(defaultTheme?: T): T` — TS generic с дефолтом `null`.
      Генератор кладёт `<T = null>` после имени функции (невалидно для Kotlin).
    - Workaround: хук отсутствует.
    - To fix: обработка default-generic-params в `findDefaultFunction` или родственном; дефолт `null` → `Any?`.

## Стабы (вместо настоящей генерации)

- **`mui.material.styles.Theme`** — `external interface Theme : mui.system.Theme` (пустое). Не содержит CssVars-полей.
  См. `Generator.kt::generateStylesDeclarations`.
- **`mui.material.styles.ThemeOptions`** — `typealias ThemeOptions = mui.system.ThemeOptions`. Тоже без
  CssVars-расширений.
- **`mui.material.styles.ThemeProvider`** — захардкоженный минимальный wrapper, см. выше.
- **`mui.system.Spacing`** — пустой `external interface Spacing`.
- **`mui.system.SystemProps<T>`** — `typealias SystemProps<T> = react.Props`. Используется Typography.
- **`mui.system.Breakpoints`/`BreakpointsOptions`** — теперь генерируются из
  `@mui/system/createBreakpoints/createBreakpoints.d.ts` (раньше из createTheme.d.ts).

## Известные потери / упрощения в публичном API

- **CssVars вселенная** (`CssVarsTheme`, `CssVarsPalette`, `extendTheme`, `CssVarsProvider`, `ColorSystem`,
  `createColorScheme`) — нет в Kotlin-обёртке. Большая v6-фича, требует отдельного захода.
- **`Theme.applyStyles<'light' | 'dark'>`** — emitted as `Any? /* ApplyStyles<'light' | 'dark'> */`.
- **`Typography.color`** — TS template literal type (`text${Capitalize<keyof TypeText>}`) превращается в `Any? /* … */`.
- **`Tooltip.placement`** — теперь `TooltipPlacement`, где `typealias TooltipPlacement = popper.core.Placement`
  эмитится в `Tooltip.ext.kt` (Generator-special-case). Точный union из MUI потеряется, но Popper-плейсменты
  достаточно широки.
- **`Autocomplete.key`** — переопределена как `react.Key? /* Key */`.
- **`Rating.defaultValue`** — расширена с `Number?` до `Any? /* Number */`, чтобы не клешиться с inherited
  `HTMLAttributes.defaultValue: Any?` (Kotlin var-invariance не позволяет diamond сузить тип).
  В `Rating.ext.kt` есть `inline var RatingProps.defaultValueAsNumber: Number?` для типизированного доступа.
- **`SlotProps` inner typing для всех `XxxSlotsAndSlotProps`** — `slotProps?: any` (Kotlin → `Any?`).
  Точные типы (`popper: SlotProps<…>`, `transition: SlotProps<…>`) не парсятся. Для типизированного доступа
  пользуйся `unsafeCast` или приводи к ad-hoc интерфейсу. To fix: научить generator парсить
  `CreateSlotsAndSlotProps<XxxSlots, { …inline… }>` второй аргумент.
- **Компоненты, наследующие slots/slotProps от родителя** — Dialog/Drawer/Menu/Popover/SwipeableDrawer (extend Modal),
  OutlinedInput/FilledInput (extend InputBase), Checkbox/Radio/Switch (extend SwitchBase) — теряют их собственные
  `XxxSlotsAndSlotProps` parent, чтобы избежать diamond `ParentSlots? vs ChildSlots?`. Сам интерфейс
  `XxxSlotsAndSlotProps` всё ещё эмитится — пользователь может работать с ним напрямую через `unsafeCast`.
  То же для `TextFieldSlotsAndSlotProps<InputPropsType>` (TS generic — наш парсер пока не делает interface из
  обобщённого type alias).
- **`Switch.onBlur` / `Switch.onChange` / `Switch.onFocus`** — UseSwitchParameters стрипнут из SwitchOwnProps
  (Kotlin не разрешает diamond `<*>` vs `<HTMLSpanElement>` для var). Hook-типизированные хендлеры теряются;
  остаются HTMLAttributes-типизированные.
- **`StepIcon`** — SvgIconOwnProps стрипнут из родителей (HTMLAttributes T-параметр не совмещался). Теряется
  SVG-специфичный API. Пользователь может приводить к `SvgIconOwnProps`.
- **Grid2 / PigmentGrid `GridSize`** — у каждого свой union в v6 (`'auto' | 'grow' | number [| false]`). Сейчас все идут
  через общий `mui.system.Union /* 'auto' | 'grow' | number | false */` в `STANDARD_TYPE_MAP["GridSize"]`. `'grow'` и
  `false` могут пропустить compile-time проверку для Grid (классическая, без grow/false).
- **Internal-rejected parents** — `LinkBaseProps`, `BasePopperProps`, `TablePaginationBaseProps`,
  `DialogActionsProps`, `PickersArrowSwitcherSlots`, `CssContainerQueries`, `NormalCssProperties`,
  `StyledComponentProps`, `RichTreeViewPluginSlots`, `SimpleTreeViewPluginSlots` — внутренние MUI типы, не
  экспортируются и не имеют Kotlin-стороны. В `ParentType.kt::INTERNAL_REJECTED_PARENTS` они отбрасываются из
  родительских списков — лучше потерять часть наследования, чем эмитить unresolved.
- **`LoadingButton`** — в v6.4+ перенесён из `@mui/lab` в `@mui/material/Button`. На текущей версии lab `6.0.1-beta.36`
  он ещё есть и генерится. Когда обновим lab выше — потребуется адаптация.
- **MUI-X `7.28.0`** на этом этапе **не обновлялся** — формально совместим с Material v6 по peer-deps, но реально может
  быть много несоответствий типов при использовании Pickers/TreeView с v6 Theme. Тестировать отдельно.

## Глобальные конвенции (после v6-этапа)

- Фолбэк для непарсимых типов — `Any? /* оригинальный TS-тип */`, а не `dynamic`. Так сохраняется источник и
  nullability. См. `KotlinType.kt::kotlinType` нижний `return`.
- `ResponsiveStyleValue<T>` имеет bound `T : Any` — фолбэк внутри него убирает `?`: `Any? /* ... */` → `Any /* ... */`.
- `XxxSlotsAndSlotProps` — type-alias из `CreateSlotsAndSlotProps<XxxSlots, {…}>` теперь конвертируется в реальный
  `interface XxxSlotsAndSlotProps { slots?: XxxSlots; slotProps?: any; }`
  (`Converter.kt::convertSlotsAndSlotPropsAliases`). Inner slotProps теряет точное типирование (см. выше).
- Компоненты, наследующие `slots/slotProps` от родителя (Dialog/Drawer/Menu/Popover/SwipeableDrawer/OutlinedInput/
  FilledInput/Checkbox/Radio/Switch), теряют свою собственную `XxxSlotsAndSlotProps` parent — избегаем diamond.
- При появлении новых `Omit<StandardProps<...>, '...'>` / `StandardProps<Omit<...>>` / `DistributiveOmit<...>` /
  `Pick<...>` / `Partial<...>` форм — `Converter.kt::adaptRawContent` их распарсивает и коллапсирует. Partial
  применяется только в extends-позиции (lookbehind `(?<=\bextends )` / `(?<=,\s{4})`), чтобы не сломать
  `Partial<X>` в member-типах, где `kotlinType()` имеет специальные обработки.
- `(?<!\w)Omit<` lookbehind не даёт регексу для `Omit<…>` "съесть" внутреннее имя из `DistributiveOmit<…>`.
- `findParentType` имеет fallback на multi-parent extends-списки (depth-aware split по top-level запятым).
  Каждый parent должен пройти `isAcceptableParent` (отвергает TS-utilities и internal-rejected names) —
  все-или-ничего: если хоть один parent невалиден, возвращаем null, потомок дефолтится в `react.Props`.
- `Generator.kt::generate(...)` имеет fallback на `index.d.ts` если `${dir.name}/${dir.name}.d.ts` нет
  (полезно для useMediaQuery, OverridableComponent).
- Tooltip и Rating получают дополнительные `*.ext.kt` через спец.case в Generator (TooltipPlacement typealias,
  Rating.defaultValueAsNumber extension).

---

*Файл обновляется по мере новых исключений в `buildSrc/src/main/kotlin/karakum/mui/`.*
