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
- **`SlotProps` inner typing для всех `XxxSlotsAndSlotProps`** — структура по именам слотов теперь сохранена:
  generator парсит второй аргумент `CreateSlotsAndSlotProps<XxxSlots, { root: SlotProps<…>; input: …; }>` и эмитит
  отдельный `XxxSlotProps` интерфейс с полем на каждый слот (например
  `CheckboxSlotProps { var root: Any?; var input: Any? }`).
  `XxxSlotsAndSlotProps.slotProps` теперь типизируется как `XxxSlotProps?`. Имена слотов автокомплитятся.
  **Что НЕ типизируется:** конкретный inner `SlotProps<RootComponent, Overrides, OwnerState>` каждого поля
  остаётся `Any?` — это TS-шаблон с тремя generic-параметрами, корректно мапить в Kotlin без перебора всех
  возможных RootComponent невозможно. Оригинальный TS-тип сохранён в `/** TS: SlotProps<…> */` JSDoc-комментарии
  над каждым полем — для справки. Если нужен типизированный доступ, делается через `unsafeCast`.
  To fix позже: при необходимости вытащить `RootComponent`-тип из первого аргумента SlotProps и эмитить
  `react.PropsOf<…>`/`mui.types.PropsWithComponent`-подобный shape, но это значительная работа.
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
- `XxxSlotsAndSlotProps` — type-alias из `CreateSlotsAndSlotProps<XxxSlots, {…}>` теперь конвертируется в ДВА
  интерфейса: `XxxSlotsAndSlotProps { slots?: XxxSlots; slotProps?: XxxSlotProps; }` и сам `XxxSlotProps` с
  именованными полями на каждый слот (`Converter.kt::convertSlotsAndSlotPropsAliases` +
  `parseInlineSlotProps`). Inner SlotProps<…> каждого слота — `Any?` с TS-типом в JSDoc-комментарии над полем.
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

## Будущие доработки типизации

Кандидаты на улучшение генератора. Сгруппировано по сложности.

### ✅ Низкая сложность — реализовано

Покрывается `Converter.kt::mapSlotPropsToKotlin` (вызывается из `convertExportSlotsAndSlotPropsAliases`) +
`Converter.kt::TAG_TO_HTML_ATTRS_TS` + расширенный `KotlinType.kt::STANDARD_TYPE_MAP`. Оставлено для документации.

- **`SlotProps<'tag', …>` со строковым литералом тегом** ✅. Покрытые теги:
  `'input'`, `'div'`, `'span'`, `'button'`, `'a'`, `'label'`, `'li'`, `'fieldset'`, `'form'`, `'img'`.
    - Пример: `CheckboxSlotProps.input: react.dom.html.InputHTMLAttributes<web.html.HTMLInputElement>?`.
- **`SlotProps<typeof X, …>`** ✅ → `XProps`.
    - Пример: `PopoverSlotProps.root: ModalProps?` (`SlotProps<typeof Modal, …>`),
      `PopoverSlotProps.paper: PaperProps?`.
- **`SlotProps<React.ElementType<X>, …>`** ✅ → `X`.
    - Пример: `CheckboxSlotProps.root: SwitchBaseProps?`,
      `TooltipSlotProps.popper: PopperProps?`,
      `PopoverSlotProps.backdrop: BackdropProps?`.
- **`SlotComponentProps<…>`** ✅ — тот же regex `^Slot(?:Component)?Props<…>` обрабатывает обе формы.
- **`SlotProps<React.ElementType<React.HTMLProps<X>>, …>`** ✅ — nested-generic ElementType (использует
  PaginationItem.first/.last/.next/.previous). Маппится на `HTMLAttributes<X>`.

Если тег не в таблице (`'h1'`, `'h2'`, `'p'` и т.п.) или RootComponent неузнан — fallback на `Any?` (как раньше).
Новые теги легко добавлять — extend `TAG_TO_HTML_ATTRS_TS` + парный entry в `STANDARD_TYPE_MAP`.

### Средняя сложность

- **Bare `React.ElementType` (без параметра).** `SlotProps<React.ElementType, TransitionProps & …>` —
  например `TooltipSlotProps.transition`. Без RootComponent fallback на `react.Props?` (лучше чем `Any?`).
    - Дополнительно: при наличии intersection во втором аргументе (`TransitionProps & X`) — попробовать
      эмитнуть `TransitionProps?`.

- **DistributiveOmit/Omit — пометка "omitted keys" в JSDoc.** Сейчас просто разворачиваем в первый аргумент,
  теряя список omit-ключей. Можно прицеплять JSDoc-комментарий типа `/** Omitted: 'a' | 'b' */` к
  сгенерированному interface для документации.
    - Where: `Converter.kt::adaptRawContent` — при unwrap сохранять keys в side-channel и эмитить как JSDoc.

- **`Partial<X>` в member-позиции — обобщить.** Сейчас есть spec-handling в `KotlinType.kt::STANDARD_TYPE_MAP`
  для `Partial<StandardInputProps>` → `InputProps`, etc. Универсально превращать `Partial<X>` в Kotlin `X?`
  с пометкой "все поля опциональны". Уменьшит число special-cases.

- **TextFieldSlotsAndSlotProps<InputPropsType> — generic над type alias.**
    - Сейчас: strip из extends-цепочки (теряем slots/slotProps на TextField).
    - Хочется: распознать `type X<T> = CreateSlotsAndSlotProps<…>;` и эмитнуть
      `external interface TextFieldSlotsAndSlotProps<InputPropsType> { … }`.
    - Where: `Converter.kt::convertSlotsAndSlotPropsAliases` — добавить опциональное чтение `<T>` после имени.

- **Internal-rejected parents — эмитить пустые стабы.** `LinkBaseProps`, `BasePopperProps`,
  `TablePaginationBaseProps`, `DialogActionsProps` — сейчас отбрасываем из родителей. Если эмитить
  `external interface LinkBaseProps` (и т.п.) пустыми стабами в соответствующих пакетах, можно
  сохранить inheritance-цепочки.
    - Where: `Generator.kt::generateMaterialDeclarations` — собрать referenced-but-not-emitted имена,
      эмитнуть стабы в конце прохода.

- **Switch/Rating/StepIcon — более тонкие правки для diamond.**
    - Switch: вместо стрипа `UseSwitchParameters` — оставить, но в `SwitchProps` body инжектнуть
      `override var onBlur: …<*>?; override var onChange: …; override var onFocus: …` чтобы устранить
      конфликт invariance.
    - Rating: попробовать `override var defaultValue: Any?` в RatingProps body (Kotlin var-invariance
      скорее всего не разрешит, но проверить экспериментально).
    - StepIcon: вместо стрипа `SvgIconOwnProps` — emit extension property
      `inline fun StepIconProps.asSvgIconProps(): SvgIconOwnProps = unsafeCast(this)` в Rating.ext.kt-стиле.

### Высокая сложность

- **CssVars вселенная** (`CssVarsTheme`, `CssVarsPalette`, `extendTheme`, `CssVarsProvider`, `ColorSystem`,
  `createColorScheme`, `ThemeProvider*`-варианты). Требует:
    - Поддержки TS conditional types в `Converter.kt::adaptRawContent`.
    - Поддержки TS mapped types `{ [K in X]: Y }` → Kotlin `Record<X, Y>` или подобное.
    - Ручных адаптеров для `extendTheme`, `CssVarsProvider`.
    - Отдельная задача — большой заход.

- **TS template literal types** (`text${Capitalize<keyof TypeText>}` в `Typography.color`). Распознавать
  `` `…${X}…` `` и схлопывать к `String` (либо к `mui.system.Union` с комментарием набора кейсов). Сейчас
  падает в `Any?` fallback.

- **TS overloaded call signatures на interface.**
    - `interface Spacing { (): string; (v): string; (a, b): string; … }` (см. `createSpacing.d.ts`)
    - `interface OverridableComponent { (props: …): JSX; (props: …): JSX; }`
    - Нужно эмитить `inline operator fun invoke(…)` в companion object или серию overloaded extension functions.

- **TS generic с дефолтом конкретного типа** (`useThemeWithoutDefault<T = null>`).
    - Дефолт `null` → emit `Any?` без default.
    - Дефолт `XxxType` → emit без default, но с явным `<T : XxxType>` bound (или просто без).

- **OverridableComponent с TypeMap.** Типизация `<RootComponent extends ElementType>` для динамического
  тега. Сейчас обходим через `mui.types.PropsWithComponent`, но это сильно упрощённо. Полная типизация
  потребует генерации generic-функций компонента с RootComponent параметром.

### Не вошло в v6-минимум

- **MUI-X `7.28.0` на v6 материале** — формально совместим по peer-deps, но возможны несоответствия типов
  в Pickers/TreeView с v6 Theme. Тестировать отдельно после обновления.
- **`LoadingButton` миграция.** Lab 6.4+ перенесла его в `@mui/material/Button`. Текущий `6.0.1-beta.36`
  ещё имеет старый. При обновлении lab выше — поправить генерацию (`Button.loading`, `Button.loadingPosition`).
- **Pigment-CSS варианты в отдельный subpackage** (`mui.material.pigment`) вместо полного скипа.

---

*Файл обновляется по мере новых исключений в `buildSrc/src/main/kotlin/karakum/mui/`.*
