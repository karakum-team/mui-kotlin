# MUI v6 migration — history and resolved follow-ups

This file records the original v5 → v6 migration decisions and the later resolution of its backlog.
The implementation descriptions and verification counts below are records of those iterations, primarily
last reconciled on **2026-10-31 against MUI 9.5.0**; they are not a fresh verification of today's tree.
For remaining generator work and current limitations, read [the generator plan](../plans/generator.md).
For the current Base UI migration, read [the Base UI plan](../plans/base-ui.md).

## Исходный контекст

Список v6-новинок и форм, которые при бампе с MUI v5 → v6 (`@mui/material` 5.18.0 → 6.5.0, `lab` 5.0.0-alpha.177 →
6.0.1-beta.36, `base` остался на 5.0.0-beta.70) обработать целиком не удалось. Каждый пункт — кандидат на поддержку:
либо отдельный адаптер, либо общий механизм в `Converter.kt` / `KotlinType.kt`.

The repository later moved to MUI 9.5.0 / Lab 9.0.0-beta.10 / MUI X 9.17.0; see
[MUI v7 history](mui-v7.md) and [MUI v9 history](mui-v9.md).
The original ✅ / 🚫 marks below retain their recorded meaning: completed / deliberately excluded or obsolete.

## Сводка закрытых пунктов (2026-10-31)

1. ~~**TextField без `slots`/`slotProps`**~~ — ✅ сделано 2026-10-31, см.
   [TextFieldSlotsAndSlotProps](#средняя-сложность).
2. ~~**Типизация material `Theme`/`ThemeOptions`**~~ — ✅ сделано 2026-10-31: генерируются из
   `createThemeNoVars.d.ts`. См. [`createThemeNoVars`](#material).
3. ~~**`Spacing` — пустой интерфейс**~~ — ✅ сделано 2026-10-31, `theme.spacing(2)` вызывается из Kotlin. См.
   [`createSpacing`](#system).
4. ~~**Компоненты, наследующие `slots`/`slotProps` от родителя**~~ — ✅ сделано 2026-10-31, см.
   [«Известные потери»](#известные-потери--упрощения-в-публичном-api).
5. ~~**`GridSize`**~~ — ✅ сделано 2026-10-31, заодно исправлены булевы/числовые литералы во всех юнионах. См.
   [«Известные потери»](#известные-потери--упрощения-в-публичном-api).
6. ~~**`Grid` без HTML-атрибутов**~~ — ✅ сделано 2026-10-31 (вместе с system `Grid`/`Stack` и `GridOffset`). См.
   [«Известные потери»](#известные-потери--упрощения-в-публичном-api).

## Почему при миграции на v6 оставили `@mui/base@5.0.0-beta.70`

> Историческое решение для MUI v6. Миграция на `@base-ui/react@1.6.0` уже начата в этом
> репозитории: шесть модулей генерируются в пакет `baseui` параллельно с `mui.base`.
> Текущий статус и оставшиеся задачи — в [плане Base UI](../plans/base-ui.md).

`@mui/base` помечен в npm как deprecated в пользу `@base-ui/react`. На этапе v6 миграцию отложили:

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

На этапе v6 рассматривали отдельный проект `kotlin-base-ui-react`. Впоследствии выбрали отдельный
путь генерации и пакет `baseui` внутри текущего проекта; старые обёртки пока сохраняются для
совместимости с существующими потребителями.

## Полностью скипнутые .d.ts файлы

These entries record how formerly skipped APIs were restored, or why an API remains intentionally excluded.
Open exclusions are tracked in [the generator plan](../plans/generator.md#полностью-скипнутые-dts-файлы).

### Material

- **`createThemeNoVars`** — `@mui/material/styles/createThemeNoVars.d.ts` — ✅ (2026-10-31)
    - Генерируется (types only) в `generateStylesDeclarations`; ручной стаб `Theme`/`ThemeOptions` удалён, так что
      новые члены material-темы приезжают при бампе сами (см.
      [историю MUI v9](mui-v9.md#the-material-theme-is-converted-from-createthemenovarsdts)).
    - `adapters/CreateThemeNoVars.kt` (стоит первым в `adaptRawContent`, до `dropMemberValueObjects`) схлопывает
      conditional types `CssVarsOptions` / `palette & (… ? CssVarsPalette : {})` / `CssVarsProperties` в ветку
      «CSS variables не включены через аугментацию» и убирает хук `CssThemeVariables`.
    - Результат: `ThemeOptions : mui.system.ThemeOptions`, `BaseTheme : mui.system.Theme`, `Theme : BaseTheme` с
      `palette: Palette`, `typography: TypographyVariants`, `mixins: Mixins`, `motion: Motion`, `shape: Shape`,
      `shadows: ReadonlyArray<String>`, `transitions: Transitions`, `zIndex: ZIndex`, `focusVisible: FocusVisible?`,
      `vars: ThemeVars?`, `alpha`/`lighten`/`darken`, `unstable_sx`, и зеркальными `*Options` в `ThemeOptions`.
    - Material сужает `var`-члены `mui.system.Theme`/`ThemeOptions` (`palette: Any?` → `Palette`,
      `mixins: mui.system.Mixins?` → material `Mixins`) — файл подавляет `VAR_TYPE_MISMATCH_ON_OVERRIDE`
      (`Override.kt`); недостающие `override` (`unstable_sx`, `unstable_sxConfig`, `applyStyles`, `shape`) —
      `Overrides.kt`.
    - Упрощения:
        - публичный `ThemeOptions` из `createTheme.d.ts` (то же имя, `extends Omit<CssVarsThemeOptions, …>`) рядом не
          сгенерировать; адаптер добавляет его `cssVariables: Boolean?`, `colorSchemes: Any?`,
          `defaultColorScheme: DefaultColorScheme?`. Объектная форма конфигурации `cssVariables` не типизирована;
        - `Shadows` (кортеж из 25 строк) → `ReadonlyArray<String>`, `ZIndexOptions` (`Partial<ZIndex>`) → `ZIndex`;
        - `ThemeOptions.typography` — только объектная форма `TypographyVariantsOptions` (callback
          `(palette) => options` — через `unsafeCast`);
        - `focusVisible: FocusVisible | false` → `FocusVisible?`; `ThemeOptions.focusVisible` — `Any?`
          (`boolean | FocusVisible`);
        - `ThemeOptions` наследует `zIndex`, который апстрим `Omit`-ит; `components`, `unstable_sxConfig`,
          `applyStyles`, `colorSchemes` — `Any?`;
        - `Theme.palette` — обычный `Palette`: поля `CssVarsPalette` при `cssVariables = true` доступны через
          `unsafeCast` (Kotlin не умеет аугментацию).
    - Покрыт `playground/.../samples/material/ThemeTypingSample.kt` (22 проверки по дефолтной теме, `ThemeOptions`,
      `cssVariables`, `createTypography`).

- **`createTypography`** — `@mui/material/styles/createTypography.d.ts` — ✅ (2026-10-31)
    - Раньше генерировался только enum `TypographyVariant` (тело файла пропускалось всегда, ещё до v6).
    - `adapters/CreateTypography.kt` раскрывает `Record<TypographyVariant, TypographyStyle>` в члены `h1`…`overline`
      (список литералов читается из того же файла). `TypographyStyle`/`TypographyStyleOptions` (alias и пустое
      расширение material `CSSProperties`) заменены на `react.CSSProperties`.
    - `FontStyle.fontFamily`/`fontWeight*` → `web.cssom.FontFamily`/`FontWeight` (`TypeAlias.kt`).
    - `createTypography` привязан к `@mui/material/styles#private_createTypography` (как `createMixins`); параметр
      `typography` — `Any /* options | (palette) => options */`.
    - Попутно: material `BaseCSSProperties` теперь `: react.CSSProperties` — `NormalCssProperties`
      (`CSS.Properties<number | string>`) больше не отбрасывается, а маппится в `ParentType.kt`; `Mixins.toolbar` получил
      CSS-члены. Material `Shape`/`ShapeOptions` — `: mui.system.Shape`/`ShapeOptions` (белый список пустых
      интерфейсов в `Converter.kt::findAdditionalProps`), т.е. снова с `borderRadius`.

- **`createTheme` (material/styles)** — `@mui/material/styles/createTheme.d.ts` — ✅
    - Сам файл по-прежнему excluded, но функция есть: ручной стаб
      `external fun createTheme(options: ThemeOptions = definedExternally): Theme`, привязан к barrel
      `@mui/material/styles` (`Generator.kt::generateStylesDeclarations`). Покрыт
      `playground/.../samples/material/ThemeFocusVisibleSample.kt`.
    - `ThemeOptions` — сгенерированный (см. `createThemeNoVars`), с `cssVariables`/`colorSchemes`/`defaultColorScheme`.

- **`useLazyRipple`** — `@mui/material/useLazyRipple/useLazyRipple.d.ts` — 🚫
    - Status: excluded, и так и остаётся.
    - Reason: хук не экспортируется ни из `@mui/material/index.d.ts`, ни через `exports` в `package.json` — это
      внутренний API, привязать его к runtime-модулю нельзя.

### System

- **`createSpacing` (system)** — `@mui/system/createTheme/createSpacing.d.ts` — ✅ (2026-10-31)
    - На v6-этапе был исключён из-за нового параметра
      `transform?: Spacing | ((abs: number | string) => number | number) | (() => undefined)`, а `Spacing` заменён
      пустым стабом — `theme.spacing(…)` из Kotlin не вызывался.
    - Теперь генерируется, как до v6 (коммит 529f36e6): `Converter.kt` по имени `Spacing` вызывает
      `Spacing.kt::convertSpacing` → `sealed external interface Spacing` с пятью `operator fun invoke(…): Length`;
      `SpacingOptions` — фабрики `convertSpacingOptions` (`Int`, `Length`, `Spacing`, `(abs: Int) -> Length`,
      `(abs: Length) -> Length`, `ReadonlyArray<Int>`). v6+ юнион начинается с `number | string |`, поэтому
      `Int` и `Length` разведены в отдельные фабрики, иначе пропадал бы числовой шаг (`spacing: 4`).
    - `createSpacing(spacingInput: SpacingOptions?, transform: Any? /* … */)` — `transform` (юнион функциональных
      типов) оставлен `Any?` в `FunctionConverter.kt`; функция привязана к named export `@mui/system#createSpacing`
      (deep path нет в `exports`). `ThemeOptions.spacing` теперь `SpacingOptions?` (`SpacingOptions` в `KNOWN_TYPES`).
    - Упрощение, унаследованное с v5: `SpacingArgument = number | string` эмитится как `typealias SpacingArgument = Int`,
      так что строковые аргументы (`theme.spacing(1, 'auto')`) требуют `unsafeCast`.
    - Покрыт `playground/.../samples/system/SpacingSample.kt` (все пять перегрузок, все фабрики, material-тема,
      `padding = spacing(1, 2)` в CSS).

- **`useThemeWithoutDefault`** — `@mui/system/useThemeWithoutDefault/useThemeWithoutDefault.d.ts` — ✅
    - Генерируется: `FunctionConverter.kt` нормализует `declare function` в default-форму и переписывает
      `<T = null>` в `<T>`; получается `external fun <T> useThemeWithoutDefault(defaultTheme: T? = definedExternally): T`
      из `@mui/system/useThemeWithoutDefault#default`. Nullability задаёт вызывающий:
      `useThemeWithoutDefault<Theme?>()`.
    - Покрыт `playground/.../samples/system/ThemeWithoutDefaultSample.kt` (без провайдера — `null`/fallback, внутри
      `mui.system.ThemeProvider` — тема провайдера).

## Стабы (вместо настоящей генерации)

- ~~**`mui.material.styles.Theme` / `ThemeOptions`**~~ — ✅ больше не стабы, генерируются из
  `createThemeNoVars.d.ts` (см. выше). Вручную остался только `typealias FocusVisible = react.CSSProperties`
  (`FocusVisible.kt`): генератор не эмитит простые type alias'ы.
- **`mui.material.styles.createTheme`** — ручной стаб над barrel `@mui/material/styles` (см. выше).
- **`mui.system.Breakpoints`/`BreakpointsOptions`** — генерируются из
  `@mui/system/createBreakpoints/createBreakpoints.d.ts` (раньше из createTheme.d.ts).

The remaining stubs and their limitations are tracked in
[the generator plan](../plans/generator.md#стабы-вместо-настоящей-генерации).

## Известные потери / упрощения в публичном API

Resolved items from the original audit:

- **`Tooltip.placement`** — ✅ `TooltipPlacement?`, где `typealias TooltipPlacement = popper.core.Placement`
  эмитится в `Tooltip.ext.kt`. v7+ `.d.ts` пишет `PopperProps['placement']` вместо `TooltipProps['placement']`, и
  поле успело деградировать до `Any?`; ключ в `TypeAlias.kt::ALIAS_MAP` обновлён (2026-10-31). Тот же тип получил
  `SpeedDialActionTooltipProps.placement`. Покрыт `playground/.../samples/material/TooltipPlacementSample.kt`.
- **`Autocomplete.key`** — переопределена как `react.Key? /* Key */`.

- **Компоненты со своими slots/slotProps поверх родительских** — ✅ (2026-10-31). Dialog/Drawer/Popover (Modal),
  Menu (Popover), SwipeableDrawer (Drawer), OutlinedInput (InputBase), Checkbox/Radio/Switch (SwitchBase) в TS
  `Omit`-ят родительские `slots`/`slotProps` и подмешивают свой `XxxSlotsAndSlotProps`. Раньше этот родитель
  срезался (diamond `ParentSlots? vs ChildSlots?`), и `slotProps` был типизирован родительским.
    - Теперь `XxxProps` наследует оба, а `adapters/OwnSlotsAndSlotProps.kt` переобъявляет
      `override var slots: XxxSlots?` / `override var slotProps: XxxSlotProps?` (`override` — в
      `Overrides.kt::overrideOwnSlots`). Тип расходится с родительским → файл подавляет
      `VAR_TYPE_MISMATCH_ON_OVERRIDE` (`Generator.kt`, только `Package.material`: у `@mui/base` есть одноимённые
      Menu/Switch). Список компонентов — `OWN_SLOTS_AND_SLOT_PROPS_COMPONENTS`.
    - SwipeableDrawer и OutlinedInput в TS пересекают свои слоты с родительскими (`DrawerSlotsAndSlotProps & …`,
      `… & { slots?: InputBaseProps['slots'] }`): в Kotlin родительские стали супертипами своих —
      `SwipeableDrawerSlots : DrawerSlots`, `SwipeableDrawerSlotProps : DrawerSlotProps`,
      `OutlinedInputSlots : InputBaseProps.Slots`, `OutlinedInputSlotProps : InputBaseProps.SlotProps`.
      `SwipeableDrawerSlotProps`/`SwipeableDrawerSlotsAndSlotProps` раньше не генерировались вовсе.
    - FilledInput в v9 своих слотов не имеет и честно использует слоты InputBase — не трогаем.
    - Покрыт `playground/.../samples/material/OwnSlotPropsSample.kt` (маркер-класс из каждого своего слота
      доходит до своего элемента; Dialog/Menu/Popover закрыты с `keepMounted`).
- **TextField** — ✅ `BaseTextFieldProps` (он же Kotlin-`TextFieldProps`) наследует `TextFieldSlotsAndSlotProps`
  с `slots: TextFieldSlots?` и `slotProps: TextFieldSlotProps?`. Упрощение: TS-параметр `InputPropsType` (тип
  слота `input`, разный у Standard/Filled/Outlined) зафиксирован как `InputBaseProps` — общий родитель трёх
  вариантов; вариантные `InputProps`/`FilledInputProps`/`OutlinedInputProps` ему присваиваются. См. «Средняя
  сложность».
- **`Switch.onBlur` / `onChange` / `onFocus`** — 🚫 неактуально: с v7 Material `SwitchProps` extends только
  `StandardProps<SwitchBaseProps, …>`, `UseSwitchParameters` в цепочке нет.

- **`GridSize` и литералы в юнионах** — ✅ (2026-10-31). Grid2 в v9 удалён, остался один `Grid` с
  `GridSize = 'auto' | 'grow' | number | false` (одинаково в material и system); `Grid.size` теперь
  `ResponsiveStyleValue<GridSize>` вместо `Union /* … */` (запись `STANDARD_TYPE_MAP["GridSize"]` удалена).
    - `UnionConverter.kt::convertUnion`: примитив рядом с литералами (`number` / `string` / `boolean`) больше не
      «Lost from union», а фабрика по образцу `SpacingOptions` — `GridSize(6)` (`inline fun GridSize(value: Number)`).
    - Найденный попутно рантайм-баг: булевы и числовые литералы без кавычек эмитились как
      `@JsValue("false")` / `@JsValue("1")`, т.е. уходили в JS строками. Теперь `@JsRawValue("false")` (соглашение
      kotlin-wrappers, ср. popper/table) — исправлены 14 булевых значений в `TabsScrollButtons`, `IconButtonEdge`,
      `SwitchBaseEdge`, `SkeletonAnimation`, `SortDirection`, `ClickAwayListener*Event`, `GridSize`, base
      `BlurOnSelect` / `ClickAwayListener*`, и числовые `baseui.Direction` (`-1 | 1`). Например,
      `TableCell.sortDirection = SortDirection.false` раньше ставил `aria-sort="descending"` (строка `"false"`
      truthy). Строковые литералы в кавычках (`'false'`) остаются `@JsValue`.
    - Покрыт `playground/.../samples/material/LiteralUnionsSample.kt` (классы `grid-*` для фабрики/`auto`/`grow`/
      брейкпоинтов, отсутствие `grid-md-false`, `aria-sort`, анимация Skeleton).
- **`Grid` / system `Grid` / system `Stack` — атрибуты корневого `<div>`** — ✅ (2026-10-31). В TS их props —
  `OverrideProps<XxxTypeMap, D>`, т.е. свои + props компонента по умолчанию. Ветка
  `Converter.kt::findMapProps` для TypeMap вида `props: P & XxxBaseProps & { … }` отдавала только
  `XxxBaseProps` — не было даже `id`/`className`/`style`. Теперь к нему добавляется
  `INTRINSIC_TYPE_MAP[defaultComponent]` (`HTMLAttributes<HTMLDivElement>`), как у Stack/Container/Paper в material;
  ветку задевают ровно эти три интерфейса (Link и List-компоненты обрабатываются как раньше).
    - `GridOffset = 'auto' | number` → `sealed GridOffset` (`auto`) + фабрика `GridOffset(Number)`;
      `Grid.offset: ResponsiveStyleValue<GridOffset>` (было `Any /* GridOffset */`). `convertUnion` теперь
      принимает юнионы из литералов и примитивов в любом порядке, если есть хотя бы один литерал в кавычках.
    - `GridSpacing = number | string` (без литералов) осознанно оставлен `Any /* GridSpacing */`: правило «хотя бы
      один литерал» не даёт превратить каждый `number | string` в новый тип по всему API.
    - Покрыт `playground/.../samples/material/GridSample.kt` (`id`/`className`/`title`/`style` на `<div>`, отступ от
      `GridOffset(2)` и `GridOffset.auto`, `title` у system `Grid`/`Stack`).

- **`LoadingButton`** — ✅ в lab 9 это `typeof Button`; генерируется как `FC<mui.material.ButtonProps>`, а
  `ButtonProps` уже содержит `loading`/`loadingIndicator`/`loadingPosition`.
- **MUI-X** — ✅ обновлён до 9.17.0, см. [историю MUI v9](mui-v9.md).

## Будущие доработки типизации

The following subsections preserve the implementation decisions that closed the original proposals.
Remaining candidates are in [the generator plan](../plans/generator.md#будущие-доработки-типизации).

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
      `PopoverSlotProps.backdrop: BackdropProps?`, `SpeedDialActionSlotProps.tooltip: TooltipProps?`.
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

- **TextFieldSlotsAndSlotProps<InputPropsType> — generic над type alias.** ✅ Сделано 2026-10-31 без generic'ов.
  В v9 у TextField не осталось `InputProps`/`inputProps`/`InputLabelProps`/`FormHelperTextProps`/`SelectProps`, так
  что `slots`/`slotProps` — единственный способ настроить внутренние компоненты.
    - `adapters/TextField.kt::adaptTextField` подставляет `InputBaseProps` вместо `InputPropsType` и снимает параметр
      с alias'а — дальше его разбирает обычный `convertSlotsAndSlotPropsAliases`. Тот же адаптер вешает alias на
      `BaseTextFieldProps` (Kotlin-`TextFieldProps` — alias на него, `Converter.kt::findProps`); вариантные
      `TextFieldSlotsAndSlotProps<…>`-родители по-прежнему срезаются в `adaptRawContent`.
    - Почему не generic: `TextFieldSlotProps<InputPropsType>` упирается в `findAdditionalProps`, который срезает
      generic params у непустых интерфейсов, а поле `slotProps` в `BaseTextFieldProps` всё равно пришлось бы
      объявлять со star-projection. `input: InputBaseProps?` принимает любой из трёх вариантных типов.
    - Сопутствующее: `mapSlotPropsToKotlin` понимает indexed access `React.ElementType<X['key']>`
      (`htmlInput: InputBaseComponentProps?` через `TypeAlias.kt`), а голый `SelectProps` в
      `KotlinType.kt::STANDARD_TYPE_MAP` → `SelectProps<*>` (слот `select`).
    - Слоты: `root: FormControlProps?`, `input: InputBaseProps?`, `inputLabel: InputLabelProps?`,
      `htmlInput: InputBaseComponentProps?`, `formHelperText: FormHelperTextProps?`, `select: SelectProps<*>?`.
      Покрыт `playground/.../samples/material/TextFieldSlotPropsSample.kt`.
    - JSDoc `/** TS: … */` над полем `input` показывает уже подставленный `React.ElementType<InputBaseProps>`, а не
      исходный `InputPropsType`.

- **`Partial<X>` в member-позиции — обобщить.** ✅ В большой степени реализовано: `KotlinType.kt::kotlinType`
  делает generic unwrap `Partial<XxxProps>` → `XxxProps`; точечные исключения (`TouchRippleProps`,
  `NativeSelectInputProps` → `Any?`; `StandardInputProps` → `InputProps`; `SelectProps` → `SelectProps<*>`) остались
  для типов без корректного Kotlin-аналога.

- **Internal-rejected parents — эмитить пустые стабы.** ✅ для `LinkBaseProps` и `TablePaginationBaseProps`.
  Остальные остаются в `INTERNAL_REJECTED_PARENTS` — либо имеют дубликаты в собственных файлах, либо нужны более
  тонкие правки (например `StyledComponentProps<never>`).

- **Switch/Rating/StepIcon — более тонкие правки для diamond.**
    - **StepIcon** ✅ — `StepIcon.ext.kt::asSvgIconOwnProps()`.
    - **Switch** 🚫 — неактуально с v7 (см. «Известные потери»).

  Rating remains a low-priority candidate in [the generator plan](../plans/generator.md#средняя-сложность).

- **`GridSize` — смешанные литеральные юнионы.** ✅ См. «Известные потери» (включая `GridOffset`). Юнионы из одних
  примитивов (`GridSpacing = number | string`) по-прежнему `Any`.

### Высокая сложность

- **Типизированный material `Theme`/`ThemeOptions`.** ✅ Сделано 2026-10-31 вариантом «генерировать из файла»
  (см. `createThemeNoVars`). Конфликт с `mui.system.Theme` решён подавлением `VAR_TYPE_MISMATCH_ON_OVERRIDE` в
  сгенерированном файле, system-сторона не менялась.

- **TS generic с дефолтом конкретного типа.** ✅ для известных случаев (`useTheme`, `useThemeWithoutDefault`) —
  точечными переписываниями в `FunctionConverter.kt`. Общего механизма нет; при появлении новых — добавлять туда же.

### Не вошло в v6-минимум

- **MUI-X на v6 материале** — ✅ закрыто: MUI X обновлён вместе с core до 9.x.
- **`LoadingButton` миграция** — ✅ закрыто (см. «Известные потери»).

## Own-slot override suppressions — PR #2924 review (2026-11-05)

Local follow-up to [kotlin-wrappers PR #2924](https://github.com/JetBrains/kotlin-wrappers/pull/2924), based on
`a49cc277`; changes are an uncommitted generator-repository diff. The reviewer
[requested property-level suppressions for both diagnostics](https://github.com/JetBrains/kotlin-wrappers/pull/2924#discussion_r4480563304).
No fork files or GitHub discussions were changed.

- `Overrides.kt::overrideOwnSlots` emits `@Suppress("VAR_TYPE_MISMATCH_ON_OVERRIDE")` on the 18 `slots` /
  `slotProps` overrides and `@Suppress("VIRTUAL_MEMBER_HIDDEN")` on the 35 hiding properties of Checkbox,
  Dialog, Drawer, Menu, OutlinedInput, Popover, Radio, SwipeableDrawer and Switch. The property list comes from
  compiling those declarations with their file suppressions removed: all mismatch diagnostics are on the
  two slot properties, including in Menu; the other diagnostics are hidden members.
- `Generator.kt` no longer emits either file suppress for these nine Material components. Their originating
  `XxxSlotsAndSlotProps` declarations and same-named `mui.base` / `baseui` components are unaffected. Other
  suppression rules remain outside this PR's scope, including [Base UI gap 12](../plans/base-ui.md).
- All nine outputs were regenerated and formatted, with no manual edits to generated files. A comparison
  after removing suppress annotations confirms the remaining declarations match `HEAD`: types, inheritance,
  imports, module bindings and documentation are unchanged.

Verification on the final generator code, using the pinned Material `9.5.0` typings and IDEA
`IU-262.9437.185`:

- `./gradlew :mui-kotlin:build :playground:compileKotlinJs` — passed; library and playground compile with zero
  errors, and all 21 runtime-export tests pass.
- `./gradlew :mui-kotlin:clean build :playground:jsDevelopmentExecutableCompileSync` — passed; SHA-256 manifests
  of all 778 generated files match the preceding generation byte-for-byte. The generated diff contains
  exactly the nine Material files listed above. `git diff --check` passes.
- Browser at `#/material/own-slot-props` — the sample mounts and reports `9/9 PASS`.
  At `#/all`, all 36 samples mount, all checks pass, and the settled console aggregate agrees with
  `#all-summary`: `samples=36 checks=100/100`; zero failing checks. Console inspection found the unrelated
  `/favicon.ico` 404 and the already-recorded Base UI render-callback-name warning; no application exception
  was observed. No additional sample or playground source changes were needed for this annotation-only fix.
- A separate read-only agent checked property scoping and unchanged declaration content with no findings.
  No separate `:buildSrc:test` run was performed; its previously recorded RuntimeBindings failure is outside
  this change. No transfer or commit was performed; the next step is human review of this local diff.

## Own-slot suppression fork handoff (2026-11-05)

The user committed the generator fix as `22700a927489e05461fc17ba491f057873c82c58` and requested its transfer to
`mui-own-slot-props`. This supersedes the pending local-commit/transfer status in the review record above.

- Applied only the nine generated Material-file changes from `a49cc277..22700a92` to
  the [`aerialist7/kotlin-wrappers`](https://github.com/aerialist7/kotlin-wrappers) fork, already on `mui-own-slot-props` at
  `7e88c633c1e05f236a7b070649c75fb31cb69987`. The public GitHub API confirmed that revision is the current head
  of PR #2924; upstream `master` is `e874c92b9acd648714d2ecb1d41a4d30fd05e189`.
- Each fork file matched the generator's pre-fix baseline byte-for-byte. File-level three-way merges were
  clean, and all nine resulting files match the committed generator output exactly. The scoped fork patch
  equals the source commit's generated patch after path mapping: **9 modified files, +53 / -45 lines**,
  covering only property-level suppressions and removal of the old file-level suppressions.
- The transferred changes are **unstaged and uncommitted**. Fork `HEAD`, local `master`, index bytes, staged
  diff and unrelated unstaged diff are unchanged. In particular, the pre-existing staged-added / working-tree-
  deleted Tree View files `domStructure.ext.kt`, `items.ext.kt` and `items.kt` were preserved.
- `git diff --check` passes in the fork. An independent read-only agent verified the scope, exact file
  contents and preserved Git state with no findings. The source tree still matches all 778 hashes from the
  preceding successful clean build; no generator code changed after that verification. No fork compilation,
  builds or tests were run, and no additional source build or browser run was needed for this transfer-only
  step. Nothing was staged, committed, pushed or posted to GitHub.

Next step: human review, staging and commit of the nine Material changes, then push to update the existing PR.
Prepared title-only commit message: `MUI. Scope override suppressions to properties`.

## Own-slot suppression pull request updated (2026-11-05)

The user confirmed committing the transferred changes on `mui-own-slot-props` and updating
[PR #2924](https://github.com/JetBrains/kotlin-wrappers/pull/2924). The local fork records commit
`25f85112f6` (`MUI. Scope override suppressions to properties`), and the nine Material files have no remaining
working-tree changes. This supersedes the pending staging/commit/push status at the handoff above.
The next step is upstream review and any requested follow-up changes. Only the plan and history were updated
in this status-only step; no builds, tests or browser checks were repeated.

## Own-slot inherited property overrides — PR #2924 follow-up (2026-11-06)

Implemented the [missing-override review comment](https://github.com/JetBrains/kotlin-wrappers/pull/2924#discussion_r4481926744)
in the generator, based on `bd845fc2`. The changes remain an uncommitted local diff.

- Checked all 35 properties previously annotated with `VIRTUAL_MEMBER_HIDDEN` in Checkbox, Dialog, Drawer,
  Menu, OutlinedInput, Popover, Radio, SwipeableDrawer and Switch. `Overrides.kt::overrideOwnSlots` now emits
  `override` on every one. Seventeen match their inherited types and require no suppression.
- Compiling with the new overrides and no additional suppressions confirmed 18 real type mismatches:
  eight component-specific `classes`, six nullable `checkedIcon` / `icon` properties, `Dialog.role`,
  `Drawer.open`, `Menu.anchorEl`, and `SwipeableDrawer.onClose`. These properties now suppress only
  `VAR_TYPE_MISMATCH_ON_OVERRIDE`; upstream types and optionality are preserved. Together with the existing
  18 slot-property suppressions, the nine files contain 36 property-level mismatch suppressions and no
  `VIRTUAL_MEMBER_HIDDEN`.
- Removed the three own `@JsName` annotations from Dialog's `ariaDescribedBy`, `ariaLabelledBy` and
  `ariaModal`: overrides inherit the ARIA names from `AriaAttributes`. No other types, imports, documentation,
  module bindings or declarations were changed. The originating `XxxSlotsAndSlotProps` interfaces are
  unaffected; only the nine expected generated files differ from the baseline.
- Extended the existing registered `material/own-slot-props` sample with five DOM checks: the three actual
  dashed ARIA attributes on Dialog Paper, plus the Checkbox input's checked and disabled states. The original
  nine slot checks remain. An independent read-only audit confirmed the complete property classification,
  exact generated transformation and meaningful sample assertions with no findings.
- A separate read-only audit also inspected older `VIRTUAL_MEMBER_HIDDEN` emission outside these nine
  components. Those existing file-level rules, including the intentional `SpeedDial.ariaLabel` JS-name
  distinction and Base UI's recorded limitations, remain outside this PR-scoped fix.

Verification on the final generator code and sample:

- `./gradlew :mui-kotlin:build :playground:compileKotlinJs` — passed with zero compilation errors and all
  21 runtime-export tests passing.
- `./gradlew :mui-kotlin:clean build :playground:jsDevelopmentExecutableCompileSync` — passed. SHA-256
  manifests of all 778 generated files match the preceding generation byte-for-byte. `git diff --check`
  passes. No separate `:buildSrc:test` run was performed.
- Browser at `#/material/own-slot-props` — sample mounted, `14/14 PASS`, including all five new checks.
  At `#/all`, all 36 samples mounted and the settled console aggregate matched `#all-summary`:
  `samples=36 checks=105/105`, with zero failing checks. Console inspection found only the unrelated favicon
  404 and the previously recorded Base UI render-callback-name warning; no application exception occurred.
- No fork files were changed, and no transfer, staging, commit, push or GitHub reply was performed.
  Next step: human review and commit of the local generator fix.

## Own-slot inherited override fork handoff (2026-11-06)

The generator fix is committed as `4a526f76`. The user requested transfer of its generated delta to the
fork's existing `mui-own-slot-props` branch. This supersedes the pending local-commit/transfer status above.

- The fork was already on `mui-own-slot-props` at `25f85112f6`, with a clean working tree and index. The
  public GitHub API confirmed that revision as the current head of PR #2924; upstream `master` was
  `b2bea1c45ce4b8a0aa0c528cabd965c93c991154` at this check.
- Applied only the nine generated Material-file changes from `bd845fc2..4a526f76`: Checkbox, Dialog, Drawer,
  Menu, OutlinedInput, Popover, Radio, SwipeableDrawer and Switch. Each fork file matched the pre-fix
  generator baseline byte-for-byte, all file-level three-way merges were clean, and all results exactly
  match the committed generator output. Generator sources, playground samples and documentation were not
  transferred to the fork.
- The scoped fork patch matches the source commit's generated patch after path mapping:
  **9 modified files, +53 / -73 lines**. The transferred changes remain **unstaged and uncommitted**.
  Fork `HEAD`, local `master`, index bytes, staged diff and all other working-tree content are unchanged.
- `git diff --check` passes in the fork. An independent read-only agent verified the exact file contents,
  scope and preserved Git state with no findings. All 778 source files still match the hashes from the
  preceding successful clean generation; the source commit contains that verified implementation.
  No additional source build or browser run was needed for this transfer-only step, and no compilation,
  builds or tests were run in the fork. Nothing was staged, committed, pushed or posted to GitHub.

Next step: human review, staging, commit and push of the nine Material changes to update PR #2924.
Prepared title-only commit message: `MUI. Add missing overrides to inherited props`.

## Theme and typography review follow-up — PR #2923 (2026-11-06)

Applied the recurring review conventions to the generator following the general comment on
[PR #2923](https://github.com/JetBrains/kotlin-wrappers/pull/2923#issuecomment-6693746527).
Starting source revision: `2886fc3a`; the working tree was clean. The ten theme/typography
`@JsPlainObject` interfaces already had `val` properties locally, although the open PR still used `var`.

- Removed the file-wide `PROPERTY_TYPE_MISMATCH_ON_OVERRIDE` rule for `createThemeNoVars` from
  `Override.kt` / `Generator.kt`. `Overrides.kt` now emits property-level suppressions only for
  `ThemeOptions.{mixins, components, transitions, zIndex}`, `BaseTheme.{mixins, transitions, zIndex}`,
  and `Theme.components`. Their existing types and optionality are preserved; other overrides receive
  no suppression. Material marker types differ from their System counterparts, and `components`
  widens the System record type to the existing `Any?` fallback.
- Added a narrow `KotlinType.kt` conversion for the upstream `pxToRem: (px: number) => string`
  signature to `(px: Double) -> String`; other numeric callback conversions are unchanged.
- Added `web.cssom.FontFamily` and `web.cssom.FontWeight` to `Generator.kt` import resolution.
  Only `createTypography.kt` uses these previously qualified names; unrelated required qualifications
  are preserved.
- Updated `ThemeTypingSample.kt` calls to use `Double` and added two meaningful checks:
  the real MUI function maps `16.5` pixels to `1.03125rem`, and a `TypographyUtils` factory accepts an
  explicitly `Double`-typed callback and preserves its fractional result. The latter also rejects the
  old `(Number) -> String` factory signature at compilation.

Verification:

- `./gradlew :mui-kotlin:build :playground:compileKotlinJs :playground:jsDevelopmentExecutableCompileSync`
  and `./gradlew :mui-kotlin:clean build` passed with zero compilation errors. All 21 runtime-export
  tests passed; no separate `:buildSrc:test` run was performed.
- SHA-256 manifests of all 778 generated filenames and contents match across clean regeneration.
  Exactly two generated files differ from the starting revision: `createThemeNoVars.kt` and
  `createTypography.kt`, both under `mui.material.styles`. No generated files were hand-edited.
- Chrome DevTools MCP verified `#/material/theme-typing`: mounted sample, 32/32 PASS, including both
  fractional-pixel checks. The screenshot showed all checks passing.
- At `#/all`, all 36 samples mounted, all 107 checks passed, and the settled console aggregate matched
  `#all-summary` (`samples=36 checks=107/107`). No application exception or failed check occurred.
  The only console error was the unrelated `favicon.ico` 404; the previously recorded Base UI
  render-callback-name warning remains.

Next step: human review and commit of the local generator fix, then a separate scoped transfer to update
PR #2923 if requested. No fork files were changed and no staging, commit, push or GitHub reply was performed.
The older `build/transfers/pr-2923-follow-up.patch` predates these fixes and must not be used as the current handoff.

## Theme and typography fork handoff — PR #2923 (2026-11-06)

The source fix is committed as `631fac2e`. At the user's request, transferred its generated corrections
and the pending JSO `var` → `val` changes to the fork's existing `mui-theme` branch, without staging or committing.
This supersedes the pending transfer step above.

- The fork started clean on `mui-theme` at `34dcc9073ce8e7b3db92e2076132185e79b82f82`.
  Changed exactly three generated files: Material `styles/createThemeNoVars.kt` and
  `styles/createTypography.kt`, plus System `createTheme.kt`.
- Both Material files match the current generator output byte-for-byte, including the eight targeted
  override suppressions, CSS imports and `pxToRem(Double)`. The System file receives only `var` → `val`;
  its `ThemeOptions.spacing` remains `Any? /* SpacingOptions */`, preserving the separate PR #2922 delta.
  `createThemeFoundation.kt` already matches the generator and was not changed.
- The fork diff is **3 files, +113 / -107 lines**. `git diff --check` passes. Exact-content comparisons
  confirm the scoped result; fork HEAD, `master` and index bytes are unchanged. The transferred changes
  remain unstaged and uncommitted, with no other working-tree changes.
- All 778 source files still match the previously verified clean-generation hashes. No new source build
  or browser run was needed for this transfer-only step. No compilation, builds or tests were run in
  the fork; nothing was staged, committed, pushed or posted to GitHub.

Next step: human review, commit and push of the three-file diff to update PR #2923.
Prepared title-only commit message: `MUI. Fix theme JSO properties and typography types`.

## Theme type compatibility without override suppressions — PR #2923 (2026-11-09)

Resolved the type incompatibilities behind the eight property-level suppressions following
[the latest PR #2923 review](https://github.com/JetBrains/kotlin-wrappers/pull/2923#pullrequestreview-5807836971).
Starting source revision: `1532524c`, with a clean working tree. The earlier fork handoff has been
committed and published as `73482f080d` on `mui-theme`.

- `Overrides.kt` explicitly makes Material `Mixins`, `Transitions`, `TransitionsOptions` and `ZIndex`
  inherit their matching System marker interfaces. `MixinsOptions` inherits the System marker through
  Material `Mixins`. Upstream's empty System interfaces are structural extension points; these explicit
  parents express their compatibility in Kotlin without weakening the named property types.
- `Generator.kt` annotates the three System markers and their five Material descendants with
  `@JsPlainObject`, keeping every parent and child annotated. The corresponding data properties use
  `val`; none of these interfaces inherits from `react.Props` or represents an object mutated in place.
- `KotlinType.kt` converts theme `Components<...>` properties to `Record<String, *>`, preserving
  optionality and the dictionary shape inherited from System. Generic per-component configuration
  typing remains unsupported; the original TypeScript type is retained in a comment.
- Removed `suppressThemePropertyMismatches` and its call. All eight
  `PROPERTY_TYPE_MISMATCH_ON_OVERRIDE` annotations disappear from `createThemeNoVars.kt`.
- Expanded the existing registered `ThemeTypingSample` with ten checks. Material option values pass
  directly to the System `ThemeOptions` factory, and built Material mixins, transitions and z-index values assign to
  System marker types without casts or suppressions. The sample also reads `components` as a record
  and verifies that custom toolbar height, transition duration and component configuration reach MUI.
  The newly annotated objects are constructed with their generated factories.

Verification:

- `./gradlew :mui-kotlin:build :playground:compileKotlinJs :playground:jsDevelopmentExecutableCompileSync`
  and `./gradlew :mui-kotlin:clean build` passed with zero compilation errors. All 21 runtime-export
  tests passed. No separate `:buildSrc:test` run was performed.
- SHA-256 manifests of all 778 generated filenames and contents match across clean regeneration and
  the later dev-server generation. Exactly five generated files differ from the starting revision:
  Material `styles/createMixins.kt`, `styles/createTransitions.kt`, `styles/zIndex.kt`,
  `styles/createThemeNoVars.kt`, and System `createTheme.kt`. No generated files were hand-edited.
- Chrome DevTools MCP verified `#/material/theme-typing`: the sample mounted and all 42/42 checks
  passed. The full-page screenshot was inspected.
- At `#/all`, all 36 samples mounted, all 117/117 checks passed, and the settled console aggregate
  matched `#all-summary` (`samples=36 checks=117/117`). There were zero failing checks and no application
  exception. The existing favicon 404 and Base UI render-callback-name warning remain unrelated.
- `git diff --check` passed. No fork compilation, builds or tests were run.

Next step: human review and commit of the generator changes and generated output, then a separate scoped
fork transfer when requested. The local diff is unstaged and uncommitted; no fork files were changed.

## Theme compatibility fork handoff — PR #2923 (2026-11-09)

The source compatibility fix is committed as `9c013653`. At the user's request, transferred only its
generated delta to the fork's existing `mui-theme` branch, without staging or committing.
This completes the transfer step recorded above.

- The fork started clean on `mui-theme` at `73482f080d5c3cea1dddce1378950ada152552d7`.
  Changed exactly five generated files: Material `styles/createMixins.kt`, `styles/createTransitions.kt`,
  `styles/zIndex.kt`, `styles/createThemeNoVars.kt`, and System `createTheme.kt`.
- Applied the source delta from `1532524c` to `9c013653` using three-way file merging in scratch files.
  All four Material files match the source output byte-for-byte. The System file receives only the
  three marker annotations; its `ThemeOptions.spacing` remains `Any? /* SpacingOptions */`, preserving
  the separate PR #2922 difference. No unrelated declarations or hand-written fork sources were copied.
- The fork diff is **5 files, +36 / -31 lines**. Exact-content comparisons and `git diff --check` pass.
  The target branch, HEAD, `master` and index bytes are unchanged. Only the five intended generated
  files are modified, all unstaged and uncommitted.
- All 778 source files still match the clean-generation hashes verified for the compatibility fix.
  Its successful source builds, 21 runtime-export tests, 42/42 theme checks and 117/117 aggregate checks
  remain applicable; no new source build or browser run was needed for this transfer-only step.
  No compilation, builds or tests were run in the fork. Nothing was staged, committed, pushed or
  posted to GitHub.

Next step: human review, commit and push of the five-file diff to update PR #2923.
Prepared title-only commit message: `MUI. Fix Material and System theme type compatibility`.

## Spacing PR synchronization and type audit — PR #2922 (2026-11-09)

Reviewed [PR #2922](https://github.com/JetBrains/kotlin-wrappers/pull/2922) and the fork's
`mui-create-spacing` branch at `19a6aa90f0a220799495fc758feb501ccb3fe773` against source revision
`d2d48ecb`. The source and fork checkouts started clean; the fork's active `mui-theme` branch was at
`0ed69404cb64c6db3451078ffbb1086047f46ea3`. A separate read-only `mui-code-review` subagent checked
the spacing conversion rules against installed upstream declarations and runtime source.

- The published PR contains four generated-file changes: removal of the empty `Spacing.kt`, addition
  of `createSpacing.kt` and `createSpacing.ext.kt`, and the `ThemeOptions.spacing` type refinement.
  Both spacing files at the latest cleanup commit match the current generator output byte-for-byte.
  Union markers, the callable interface, all five call arities, optionality and the named runtime export
  are consistent; no new convention blocker was found.
- Differences in System `createTheme.kt` are the annotations and `var` → `val` changes owned by
  PR #2923. Applying only the spacing property type to the current theme-branch file reproduces the
  generator's System theme exactly. Copying that whole file into #2922 would mix the two PR topics.
- Two inherited type-fidelity gaps remain in the generator: `Int` limits valid fractional units,
  factors and scale values; global union replacements lose numeric callback inputs/results and
  string/mixed scale arrays. MUI's integer restriction applies to array indices, not to all spacing
  numbers. These rules predate the latest cleanup, but refining `ThemeOptions.spacing` from `Any?`
  exposes their restrictions to consumers.
- String arguments such as `auto` are also excluded by `SpacingArgument = Int`; this is the already
  documented v5 compromise. The optional internal `createSpacing.transform` parameter is deliberately
  omitted since the cleanup in `8f9292b4`, so the older widened-callback plan entry was stale.
  The canonical follow-up is [GEN-SPACING-01](../plans/generator.md#recorded-compromises-in-completed-support).
- Chrome DevTools MCP showed the PR still open at the same head, with four successful checks and no
  formal reviews or inline comments. The maintainer's only published comment asks for another check
  before the latest cleanup; no later maintainer feedback is visible.

No source or fork code was changed, and no branches, commits or index entries were modified. No local
builds, tests, generation or browser sample verification were run for this read-only audit. All 778
source declaration hashes still match the previously verified generation; the existing spacing sample
does not cover the additional cases identified above. The fork's builds and tests remain out of scope.

No synchronization transfer is needed. Revisit `GEN-SPACING-01` in the generator when spacing API
improvements are requested, extend its registered sample, then prepare a focused generated delta.

## Spacing type fidelity — GEN-SPACING-01 (2026-11-09)

Implemented the spacing audit follow-up against installed MUI System / Material 9.5.0, starting from
source revision `d2d48ecb`. The chosen idiomatic Kotlin API uses `Double` for numbers and typed CSS
`Length` for CSS values. It deliberately omits parallel `Int` / raw `String` overloads and custom
mixed primitive union factories. No changes were transferred to the fork.

- `Spacing.kt` splits only the outer `SpacingOptions` union, respecting callback arrows and nested
  argument/result and array-element unions. Each supported branch is mapped explicitly; an unknown
  branch fails generation instead of silently losing part of its type.
- Numeric units and scale entries use `Double`. Both upstream callback input forms map to
  a numeric `Double` factor and a CSS `Length` result. There is one callback factory, without
  experimental type-inference or overload-resolution annotations; numeric calculations return
  typed lengths such as `(abs * 3).px`.
- `UnionConverter.kt` emits `typealias SpacingArgument = Double`. `Spacing` retains exactly the five
  zero-to-four-argument signatures: calls use `spacing(1.0, 2.0)` or fractional factors such as
  `spacing(0.5, -1.5)`, and return CSS `Length` values.
- The six `SpacingOptions` factories accept `Double`, `Length`, `Spacing`, `(Double) -> Length`,
  `ReadonlyArray<Double>` and `ReadonlyArray<Length>`. CSS scales express
  values uniformly with typed units, for example `arrayOf(0.px, 6.5.px, 1.rem)`. Only scale-array
  indices must have integral values, even though passed as `Double`. The optional internal
  `createSpacing.transform` argument remains deliberately omitted.
- Regenerated the declarations without manual edits. Only System `createSpacing.ext.kt` changes;
  `createSpacing.kt`, theme declarations and runtime bindings are unchanged.
- Expanded the existing registered `SpacingSample` from 10 to 27 checks. They exercise all call
  arities, fractional units and negative factors, CSS-length callbacks with inferred input types,
  fractional and CSS-length scales, spacing-function reuse, System/Material theme integration, and assignment to
  a CSS property.

Verification:

- `./gradlew :mui-kotlin:build :playground:compileKotlinJs :playground:jsDevelopmentExecutableCompileSync`
  and `./gradlew :mui-kotlin:clean build` passed with zero compilation errors. All 21 runtime-export
  tests passed. No separate `:buildSrc:test` run was performed.
- SHA-256 manifests of all 778 generated filenames and contents match across the candidate build,
  clean regeneration and dev-server generation.
- Chrome DevTools MCP verified `#/system/spacing`: the sample mounted and all 27/27 checks passed.
  The DOM snapshot was inspected; the padding example has both inline and computed `padding: 8px 16px`.
- At `#/all`, all 36 samples mounted, all 134/134 checks passed, and the settled console aggregate
  matched `#all-summary` (`samples=36 checks=134/134`). There were zero failing checks and no console
  errors or application exceptions. The existing Base UI render-callback-name warning is unrelated.
- `git diff --check` passed. No fork compilation, builds or tests were run. Nothing was staged,
  committed, pushed or posted to GitHub.

Next step: human review and commit of the generator, generated declaration, sample and documentation
changes. A later requested fork transfer should carry only the generated spacing delta and preserve
the separate theme PR's changes.
