# MUI v6 — backlog: что генератор пока не умеет

Список v6-новинок и форм, которые при бампе с MUI v5 → v6 (`@mui/material` 5.18.0 → 6.5.0, `lab` 5.0.0-alpha.177 →
6.0.1-beta.36, `base` остался на 5.0.0-beta.70) обработать целиком не удалось. Каждый пункт — кандидат на поддержку:
либо отдельный адаптер, либо общий механизм в `Converter.kt` / `KotlinType.kt`.

> **Статус на 2026-10-31.** Репозиторий давно ушёл дальше v6: `@mui/material`/`system` 9.5.0, `lab` 9.0.0-beta.10,
> MUI X 9.17.0 (см. [MUI_V7_TODO.md](MUI_V7_TODO.md), [MUI_V9_TODO.md](MUI_V9_TODO.md)). Каждый пункт ниже перепроверен
> по коду генератора, сгенерированному дереву и `.d.ts` 9.5.0 и помечен: ✅ — сделано, 🚫 — неактуально/не делаем,
> ⏳ — ещё открыто. Закрытые пункты оставлены ради истории решений.

Формат: `<имя>` — `<MUI .d.ts путь>` — Status / Reason / Workaround / To fix.

## Сводка открытых пунктов (по практической ценности)

1. ~~**TextField без `slots`/`slotProps`**~~ — ✅ сделано 2026-10-31, см.
   [TextFieldSlotsAndSlotProps](#средняя-сложность).
2. ~~**Типизация material `Theme`/`ThemeOptions`**~~ — ✅ сделано 2026-10-31: генерируются из
   `createThemeNoVars.d.ts`. См. [`createThemeNoVars`](#material).
3. ~~**`Spacing` — пустой интерфейс**~~ — ✅ сделано 2026-10-31, `theme.spacing(2)` вызывается из Kotlin. См.
   [`createSpacing`](#system).
4. ~~**Компоненты, наследующие `slots`/`slotProps` от родителя**~~ — ✅ сделано 2026-10-31, см.
   [«Известные потери»](#известные-потери--упрощения-в-публичном-api).
5. **`GridSize`** — `Grid.size` типизирован `Union /* … */`; сгенерированный `sealed GridSize` непригоден (теряет
   `number`, `false` эмитится строкой `"false"`). Нужен конвертер смешанных `string | number | boolean`-юнионов.
6. Низкий приоритет: `createColorScheme`/`extendTheme`/CssVars-провайдер, `OverridableComponent`, Pigment-компоненты,
   template literal types, JSDoc для `Omit`-ключей, `Rating.defaultValue`/`StepIcon`/`Autocomplete.key` (приемлемые
   обходы уже есть).

## Почему при миграции на v6 оставили `@mui/base@5.0.0-beta.70`

> Историческое решение для MUI v6. Миграция на `@base-ui/react@1.6.0` уже начата в этом
> репозитории: шесть модулей генерируются в пакет `baseui` параллельно с `mui.base`.
> Текущий статус и оставшиеся задачи — в [BASE_UI_TODO.md](BASE_UI_TODO.md).

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

### Material

- **`createThemeNoVars`** — `@mui/material/styles/createThemeNoVars.d.ts` — ✅ (2026-10-31)
    - Генерируется (types only) в `generateStylesDeclarations`; ручной стаб `Theme`/`ThemeOptions` удалён, так что
      новые члены material-темы приезжают при бампе сами (см.
      [MUI_V9_TODO.md](MUI_V9_TODO.md#the-material-theme-is-converted-from-createthemenovarsdts)).
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

- **`createThemeWithVars`** — `@mui/material/styles/createThemeWithVars.d.ts` — ⏳ (частично ✅)
    - Status: excluded
    - ✅ С v7 типы `CssVarsTheme`, `CssVarsPalette`, `ColorSystem`, `ColorSystemOptions`, `ThemeVars` живут в
      `createThemeFoundation.d.ts` и генерируются оттуда (`adapters/CreateThemeFoundation.kt`).
    - ⏳ Не хватает самой функции — она экспортируется как `extendTheme` из `@mui/material/styles` — и
      `CssVarsThemeOptions`.
    - To fix: ручной стаб `extendTheme` по образцу `createTheme.kt`, либо разбор mapped types (`{ [K in X]: Y }`).

- **`createTheme` (material/styles)** — `@mui/material/styles/createTheme.d.ts` — ✅
    - Сам файл по-прежнему excluded, но функция есть: ручной стаб
      `external fun createTheme(options: ThemeOptions = definedExternally): Theme`, привязан к barrel
      `@mui/material/styles` (`Generator.kt::generateStylesDeclarations`). Покрыт
      `playground/.../samples/material/ThemeFocusVisibleSample.kt`.
    - `ThemeOptions` — сгенерированный (см. `createThemeNoVars`), с `cssVariables`/`colorSchemes`/`defaultColorScheme`.

- **`createColorScheme`** — `@mui/material/styles/createColorScheme.d.ts` — ⏳ (низкий приоритет)
    - Status: excluded
    - Reason: v6 CssVars API. Тип-аргумент `ColorSystemOptions` теперь генерируется, так что блокер — только
      стаб самой функции.
    - To fix: ручной стаб `createColorScheme(options: ColorSystemOptions): ColorSystemOptions`.

- **`useLazyRipple`** — `@mui/material/useLazyRipple/useLazyRipple.d.ts` — 🚫
    - Status: excluded, и так и остаётся.
    - Reason: хук не экспортируется ни из `@mui/material/index.d.ts`, ни через `exports` в `package.json` — это
      внутренний API, привязать его к runtime-модулю нельзя.

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
- **`mui.material.styles.ThemeProvider`**, **`mui.system.ThemeProvider`** — минимальные ручные обёртки (см. выше).
- **`mui.system.SystemProps<T>`** — `typealias SystemProps<T> = react.Props`. Используется Typography.
- **`mui.system.Breakpoints`/`BreakpointsOptions`** — генерируются из
  `@mui/system/createBreakpoints/createBreakpoints.d.ts` (раньше из createTheme.d.ts).
- **`LinkBaseProps`, `TablePaginationBaseProps`** — пустые стабы, см. «Internal-rejected parents».

## Известные потери / упрощения в публичном API

- **CssVars вселенная** — ✅ типы `CssVarsTheme`/`CssVarsPalette`/`ColorSystem`/`ColorSystemOptions`/`ThemeVars`
  генерируются из `createThemeFoundation.d.ts`; `Theme.vars: ThemeVars?` и `ThemeOptions.cssVariables`/
  `colorSchemes`/`defaultColorScheme` есть. ⏳ Нет `extendTheme`, `createColorScheme`, CssVars-провайдера.
- **`Theme.applyStyles`** — `Any? /* ApplyStyles<…> */` в `mui.system.Theme`, material `BaseTheme` и `CssVarsTheme`.
- **`Typography.color`** — теперь `Union? /* OverridableStringUnion<… `text${Capitalize<keyof TypeText>}` …> | string */`
  (был `Any?`). Шаблонный литерал по-прежнему не раскрывается.
- **`Tooltip.placement`** — ✅ `TooltipPlacement?`, где `typealias TooltipPlacement = popper.core.Placement`
  эмитится в `Tooltip.ext.kt`. v7+ `.d.ts` пишет `PopperProps['placement']` вместо `TooltipProps['placement']`, и
  поле успело деградировать до `Any?`; ключ в `TypeAlias.kt::ALIAS_MAP` обновлён (2026-10-31). Тот же тип получил
  `SpeedDialActionTooltipProps.placement`. Покрыт `playground/.../samples/material/TooltipPlacementSample.kt`.
- **`Autocomplete.key`** — переопределена как `react.Key? /* Key */`.
- **`Rating.defaultValue`** — расширена с `Number?` до `Any? /* Number */`, чтобы не клешиться с inherited
  `HTMLAttributes.defaultValue: Any?` (Kotlin var-invariance не позволяет diamond сузить тип).
  В `Rating.ext.kt` есть `inline var RatingProps.defaultValueAsNumber: Number?` для типизированного доступа.
- **`SlotProps` inner typing для всех `XxxSlotsAndSlotProps`** — структура по именам слотов сохранена:
  generator парсит второй аргумент `CreateSlotsAndSlotProps<XxxSlots, { root: SlotProps<…>; input: …; }>` и эмитит
  отдельный `XxxSlotProps` интерфейс с полем на каждый слот. Поля типизируются по RootComponent там, где он
  распознаётся (см. «Низкая сложность — реализовано»), иначе `Any?`; исходный TS-тип — в `/** TS: SlotProps<…> */`.
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
- **`StepIcon`** — SvgIconOwnProps стрипнут из родителей (HTMLAttributes T-параметр не совмещался). Типизированный
  доступ — `StepIcon.ext.kt::asSvgIconOwnProps()`.
- **`GridSize`** — ⏳ Grid2 в v9 удалён, остался один `Grid` с `GridSize = 'auto' | 'grow' | number | false`
  (одинаково в material и system). `STANDARD_TYPE_MAP["GridSize"]` даёт
  `ResponsiveStyleValue<Union /* 'auto' | 'grow' | number | false */>`. Сгенерированный `sealed GridSize` в
  `Grid.ext.kt`/`GridProps.ext.kt` подставить нельзя: `convertUnion` теряет `number` («Lost from union») и эмитит
  булев `false` как `@JsValue("false")` — строку. To fix: научить `convertUnion` смешанным юнионам (фабрики для
  `number`, настоящий `false`), затем убрать запись из `STANDARD_TYPE_MAP`.
- **Internal-rejected parents** — `ParentType.kt::INTERNAL_REJECTED_PARENTS` (`BasePopperProps`,
  `DialogActionsProps`, `CssContainerQueries`, `NormalCssProperties`, `StyledComponentProps`, плюс десяток
  mui-x-внутренних — список см. в коде и в MUI_V7/V9_TODO) отбрасываются из родительских списков. **`LinkBaseProps`
  и `TablePaginationBaseProps`** эмитятся как пустые стабы (`MATERIAL_LINK_BASE_PROPS_STUB`,
  `MATERIAL_TABLE_PAGINATION_BASE_PROPS_STUB`) — цепочка наследования сохранена на уровне символа.
- **`LoadingButton`** — ✅ в lab 9 это `typeof Button`; генерируется как `FC<mui.material.ButtonProps>`, а
  `ButtonProps` уже содержит `loading`/`loadingIndicator`/`loadingPosition`.
- **MUI-X** — ✅ обновлён до 9.17.0, см. [MUI_V9_TODO.md](MUI_V9_TODO.md).

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
  члены своими типами с подавлением `VAR_TYPE_MISMATCH_ON_OVERRIDE` (`adapters/OwnSlotsAndSlotProps.kt`).
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

- **DistributiveOmit/Omit — пометка "omitted keys" в JSDoc.** ⏳ Откладывается — ROI низкий.
  Inline-комментарий (`extends X /* Omitted: 'a' */`) ломает разбор родителя; side-channel
  (`Map<interfaceName, omittedKeys>` + два прохода) требует рефакторинга `adaptRawContent` (сейчас pure pipeline).
  Большинство случаев — `'slots' | 'slotProps'`-омит на OwnerState'ах; интересные (`Omit<TransitionProps, 'children'>`
  в Fade, `Omit<TypographyTypeMap['props'], 'classes'>` в DialogTitle) единичны.

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
    - **Rating** ⏳ — попробовать `override var defaultValue: Any?` в `RatingProps`; откладывается до пробы, обход
      `defaultValueAsNumber` работает.

- **`GridSize` — смешанные литеральные юнионы.** ⏳ См. «Известные потери». Нужен `convertUnion`, который для
  `'auto' | 'grow' | number | false` эмитит строковые значения, фабрику для `number` и настоящий `false`.

### Высокая сложность

- **Типизированный material `Theme`/`ThemeOptions`.** ✅ Сделано 2026-10-31 вариантом «генерировать из файла»
  (см. `createThemeNoVars`). Конфликт с `mui.system.Theme` решён подавлением `VAR_TYPE_MISMATCH_ON_OVERRIDE` в
  сгенерированном файле, system-сторона не менялась.

- **CssVars вселенная** (`extendTheme`, `createColorScheme`, CssVars-`ThemeProvider`). ⏳ Типы и `Theme.vars` уже
  есть; требует:
    - Поддержки TS conditional types в `Converter.kt::adaptRawContent` (или ручных стабов).
    - Поддержки TS mapped types `{ [K in X]: Y }` → Kotlin `Record<X, Y>` или подобное.

- **TS template literal types** (`text${Capitalize<keyof TypeText>}` в `Typography.color`). ⏳ Распознавать
  `` `…${X}…` `` и схлопывать к `String` (либо к `mui.system.Union` с комментарием набора кейсов).

- **TS overloaded call signatures на interface.** ⏳
    - `interface Spacing { (): string; (v): string; (a, b): string; … }` — ✅ точечный конвертер
      `Spacing.kt::convertSpacing` (см. System).
    - `interface OverridableComponent { (props: …): JSX; (props: …): JSX; }` — общий механизм не реализован.

- **TS generic с дефолтом конкретного типа.** ✅ для известных случаев (`useTheme`, `useThemeWithoutDefault`) —
  точечными переписываниями в `FunctionConverter.kt`. Общего механизма нет; при появлении новых — добавлять туда же.

- **OverridableComponent с TypeMap.** ⏳ Типизация `<RootComponent extends ElementType>` для динамического
  тега. Сейчас обходим через `mui.types.PropsWithComponent`, но это сильно упрощённо.

### Не вошло в v6-минимум

- **MUI-X на v6 материале** — ✅ закрыто: MUI X обновлён вместе с core до 9.x.
- **`LoadingButton` миграция** — ✅ закрыто (см. «Известные потери»).
- **Pigment-CSS варианты в отдельный subpackage** (`mui.material.pigment`) вместо полного скипа — ⏳ низкий приоритет.

---

*Файл обновляется по мере новых исключений в `buildSrc/src/main/kotlin/karakum/mui/`. Последняя сверка с кодом —
2026-10-31 (MUI 9.5.0).*
