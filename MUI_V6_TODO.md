# MUI v6 — backlog: что генератор пока не умеет

Список v6-новинок и форм, которые при бампе с MUI v5 → v6 (`@mui/material` 5.18.0 → 6.5.0, `lab` 5.0.0-alpha.177 →
6.0.1-beta.36, `base` остался на 5.0.0-beta.70) обработать целиком не удалось. Каждый пункт — кандидат на поддержку:
либо отдельный адаптер, либо общий механизм в `Converter.kt` / `KotlinType.kt`.

Формат: `<имя>` — `<MUI .d.ts путь>` — Status / Reason / Workaround / To fix.

## Почему `@mui/base@5.0.0-beta.70` остаётся (а не мигрируем на `@base-ui/react`)

`@mui/base` помечен в npm как deprecated в пользу `@base-ui/react`. Мы намеренно не мигрируем сейчас:

- **`@base-ui/react@1.5.0` — это другой продукт**, не drop-in. Compound API (`Popover.Root` +
  `.Trigger` + `.Popup`), свои subpath exports (`@base-ui-components/react/popover`), не используется
  внутри `@mui/material@6` или `@mui/lab@6`. Команда Material UI + Radix + Floating UI собрала это
  как новую библиотеку, а не как переименование `@mui/base`.
- **`@mui/material@6` уже не зависит от `@mui/base`** — внутри лежат локальные копии типов
  (`@mui/material/Popper/BasePopper.types.d.ts` с полным `PopperOwnProps` и т.д.). Эту часть
  deprecation апстрим уже решил у себя.
- **`@mui/lab@6.0.1-beta.36` всё ещё требует `@mui/base` в runtime** — `composeClasses`,
  `useAutocomplete`, `createFilterOptions` импортятся из `@mui/base`. Дроп npm-зависимости ломает Lab.
- **Наша Kotlin-сторона активно использует `mui.base.*`** — Material наследует ~13 полей
  `PopperOwnProps` и ~7 полей `BadgeOwnProps` через цепочку `mui.material.X : mui.base.X`. Playground
  импортит `mui.base.Slider` и `mui.base.sliderClasses` как runtime-значения. Минимальные стабы это
  ломают.

Deprecation в npm — informational warning, не runtime. Пакет работает и будет работать. Когда апстрим
(вероятно `@mui/material@7`) полностью уберёт зависимость и в Lab — пересмотрим.

Будущая альтернатива — отдельный проект `kotlin-base-ui-react` с обёртками для `@base-ui/react`.
Это новый параллельный пакет (compound API, новые адаптеры в генераторе) — недели работы.
К текущему `mui-kotlin` точек соприкосновения мало.

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
- **Internal-rejected parents** — `BasePopperProps`, `DialogActionsProps`, `PickersArrowSwitcherSlots`,
  `CssContainerQueries`, `NormalCssProperties`, `StyledComponentProps`, `RichTreeViewPluginSlots`,
  `SimpleTreeViewPluginSlots` — внутренние MUI типы, не экспортируются и не имеют Kotlin-стороны. В
  `ParentType.kt::INTERNAL_REJECTED_PARENTS` они отбрасываются из родительских списков — лучше потерять
  часть наследования, чем эмитить unresolved. **`LinkBaseProps` и `TablePaginationBaseProps`** теперь
  эмитятся как пустые стабы в `Generator.kt::generateMaterialDeclarations` (constants
  `MATERIAL_LINK_BASE_PROPS_STUB`, `MATERIAL_TABLE_PAGINATION_BASE_PROPS_STUB`) — наследование
  `LinkOwnProps : LinkBaseProps` и `TablePaginationOwnProps : TablePaginationBaseProps` восстановлено
  на уровне символа (поверхность пустая, но цепочка не разорвана).
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
- **Bare `React.ElementType` (без параметра)** ✅. Покрытые формы:
    - `SlotProps<React.ElementType, X & Overrides, OwnerState>` → `X` (например
      `TooltipSlotProps.transition: TransitionProps?` через `TransitionProps & TooltipTransitionSlotPropsOverrides`).
    - `SlotProps<React.ElementType, Overrides, OwnerState>` (без intersection) →
      `react.dom.html.HTMLAttributes<web.html.HTMLElement>?` (общий fallback).

Если тег не в таблице (`'h1'`, `'h2'`, `'p'` и т.п.) или RootComponent неузнан — fallback на `Any?` (как раньше).
Новые теги легко добавлять — extend `TAG_TO_HTML_ATTRS_TS` + парный entry в `STANDARD_TYPE_MAP`.

### Средняя сложность

- **DistributiveOmit/Omit — пометка "omitted keys" в JSDoc.** Откладывается — оказалось сложнее средней.
  Inline-комментарий (`extends X /* Omitted: 'a' */`) ломает `findParentType`: парсер ищет идентификатор
  родителя, а `/* */` ломает `IDENTIFIER_RE` и all-or-nothing fallback отбрасывает родителя →
  потомок дефолтится в `react.Props`. Side-channel (`Map<interfaceName, omittedKeys>` + два прохода)
  работает, но требует существенного рефакторинга `adaptRawContent` (сейчас pure pipeline без контекста).
  Большинство случаев Omit — `'slots' | 'slotProps'`-омит на OwnerState'ах (репетативные, малоценные).
  Интересные случаи (`Omit<TransitionProps, 'children'>` в Fade, `Omit<TypographyTypeMap['props'], 'classes'>`
  в DialogTitle) — единичные. ROI для JSDoc-comment-only задачи низкий.
    - Where: при возврате к задаче — `Converter.kt::adaptRawContent` пред-pass для сбора
      `Map<interfaceName, List<omittedKeys>>`, потом второй pass с инжекцией JSDoc над interface headers.

- **`Partial<X>` в member-позиции — обобщить.** ✅ В большой степени реализовано:
  `KotlinType.kt::kotlinType` (строки 460-481) уже делает generic unwrap `Partial<XxxProps>` → `XxxProps`
  через `STANDARD_TYPE_MAP[partialResult] ?: partialResult` fallback. Точечные исключения остались для
  типов, которые не имеют корректного Kotlin-аналога (`TouchRippleProps`, `NativeSelectInputProps` —
  `Any?`; `StandardInputProps` → `InputProps`; `SelectProps` → `SelectProps<*>`). Дополнительно
  добавлен Pattern 6 в `Converter.kt::mapSlotPropsToKotlin` для
  `SlotProps<React.ElementType<Partial<X>>, …>` → `X` — будет автоматически подхватываться, когда Step
  "TextFieldSlotsAndSlotProps<InputPropsType>" разблокирует generic SlotsAndSlotProps aliases
  (Autocomplete — единственный текущий потребитель).

- **TextFieldSlotsAndSlotProps<InputPropsType> — generic над type alias.** Пробовал — откатил.
  Конкретный блокер: парсер `Converter.kt::convertSlotsAndSlotPropsAliases` УДАЛОСЬ научить читать
  `<InputPropsType>` после имени alias'а (regex `(\w+)SlotsAndSlotProps(<[^>]+>)?\s*=…`) и эмитить
  TS-форму `export interface TextFieldSlotProps<InputPropsType> { … }`. Но **`findAdditionalProps`
  на строке 771** делает `body.substringBefore("<")` для извлечения имени интерфейса — generic
  params срезаются на этом этапе и НЕ попадают в Kotlin LHS (`external interface TextFieldSlotProps`
  без `<InputPropsType>`). При этом ссылки на интерфейс в полях типа (`slotProps: TextFieldSlotProps<InputPropsType>?`)
  уже корректные — но эта ссылка указывает на не-generic интерфейс → unresolved reference.
    - Где чинить: `Converter.kt::findAdditionalProps` (строки 752-960) — извлекать generic params
      рядом с `interfaceName`, прокидывать через `props()`/декларацию (`else -> "external interface
      $interfaceName$typeParams"`). Сейчас generic params извлекаются ТОЛЬКО для empty-body
      интерфейсов (строки 800-820); для non-empty этой логики нет.
    - Дополнительно: придётся снять `replace(Regex(""",\s+TextFieldSlotsAndSlotProps<[^<>]*>"""), "")`
      из `adaptRawContent` (сейчас strip'ает TextField'овский extends — иначе diamond с
      `BaseTextFieldProps`'ами). Без strip — диамант возможен; проверять отдельно.
    - Объём работы: ~1-2 часа аккуратного рефакторинга с верификацией всех существующих
      generic интерфейсов (UseAutocompleteProps&lt;Value&gt;, DatePickerSlots&lt;TDate&gt; и т.п.).

- **Internal-rejected parents — эмитить пустые стабы.** ✅ Реализовано для `LinkBaseProps` и
  `TablePaginationBaseProps` через constants `MATERIAL_LINK_BASE_PROPS_STUB` /
  `MATERIAL_TABLE_PAGINATION_BASE_PROPS_STUB` в `Generator.kt::generateMaterialDeclarations`. Остальные
  (`BasePopperProps`, `DialogActionsProps`, `StyledComponentProps`, Pickers/TreeView плагинные) пока
  остаются в `INTERNAL_REJECTED_PARENTS` — либо имеют дубликаты в собственных файлах, либо нужны более
  тонкие правки (например `StyledComponentProps<never>` — TS generic с `never`).

- **Switch/Rating/StepIcon — более тонкие правки для diamond.**
    - **StepIcon** ✅ — добавлено `StepIcon.ext.kt::asSvgIconOwnProps()` через спец.case в
      `Generator.kt`. Родитель `SvgIconOwnProps` остаётся стрипнутым (Kotlin diamond), но типизированный
      доступ к SvgIcon API теперь есть через `props.asSvgIconOwnProps().color = ...`.
    - **Switch**: вместо стрипа `UseSwitchParameters` — оставить, но в `SwitchProps` body инжектнуть
      `override var onBlur: …<*>?; override var onChange: …; override var onFocus: …` чтобы устранить
      конфликт invariance. Откладывается — требует кастомного override-injection в Generator/Overrides.
    - **Rating**: попробовать `override var defaultValue: Any?` в RatingProps body (Kotlin var-invariance
      скорее всего не разрешит — диамант идёт от `HTMLAttributes.defaultValue: Any?` который УЖЕ `Any?`,
      т.е. поле не меняет тип; на практике Kotlin компилятор может не разрешить переоткрытие сериализуемых
      var-полей). Откладывается до пробы.

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
