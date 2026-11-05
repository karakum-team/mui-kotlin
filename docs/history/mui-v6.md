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
