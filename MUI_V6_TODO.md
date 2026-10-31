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

1. **TextField без `slots`/`slotProps`** — в v9 удалены `InputProps`/`inputProps`/`InputLabelProps`/
   `FormHelperTextProps`/`SelectProps`, а `TextFieldSlotsAndSlotProps<InputPropsType>` по-прежнему не генерируется:
   у Kotlin-`TextField` сейчас нет типизированного способа настроить input. См.
   [TextFieldSlotsAndSlotProps](#средняя-сложность) — пункт поднят до первого приоритета.
2. **Типизация material `Theme`/`ThemeOptions`** — `palette`, `typography`, `shadows` приходят из `mui.system.Theme`
   как `Any?`, `mixins`/`transitions` — пустые интерфейсы `mui.system`; нет `motion`, `vars`, `alpha`/`lighten`/
   `darken`, `cssVariables`, `colorSchemes`. См. [Стабы](#стабы-вместо-настоящей-генерации).
3. **`Spacing` — пустой интерфейс**, `theme.spacing(2)` из Kotlin не вызвать. Конвертер для call signatures уже есть
   (`Converter.kt` → `Spacing.kt::convertSpacing`), мешает только исключение `createSpacing`. См.
   [`createSpacing`](#system).
4. **Компоненты, наследующие `slots`/`slotProps` от родителя** (Dialog/Drawer/Menu/Popover/…) — собственный
   `XxxSlotProps` через `slotProps` недоступен.
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

- **`createThemeNoVars`** — `@mui/material/styles/createThemeNoVars.d.ts` — ⏳
    - Status: excluded (`Generator.kt::generateStylesDeclarations::isStyleDefinition`)
    - Reason: использует TS conditional types (`CssThemeVariables extends { enabled: true }`), которые конвертер не
      разбирает; даёт несинтаксический Kotlin.
    - Workaround: ручной стаб `Theme : mui.system.Theme` / `ThemeOptions : mui.system.ThemeOptions` с одним
      material-only членом `focusVisible` (MUI 9.4). Подробности и риск «апстрим-добавления не приезжают сами» — в
      [MUI_V9_TODO.md](MUI_V9_TODO.md#the-material-theme-is-a-hand-written-stub--upstream-additions-do-not-flow-in).
    - To fix: научить `adaptRawContent` схлопывать conditional type в одну ветку (для `CssVarsOptions` /
      `CssVarsProperties` — в «выключенную»), после чего генерировать `Theme`/`ThemeOptions`/`BaseTheme` из файла.

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
    - Остаток: `cssVariables`/`colorSchemes` в `ThemeOptions` нет (см. Theme-стаб).

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

- **`createSpacing` (system)** — `@mui/system/createTheme/createSpacing.d.ts` — ⏳ (высокий приоритет)
    - Status: excluded (`Generator.kt::generateSystemDeclarations`)
    - Reason: в v6 добавился параметр
      `transform?: Spacing | ((abs: number | string) => number | number) | (() => undefined)`.
    - Workaround: `Spacing` — пустой стаб (`SYSTEM_SPACING_STUB`), поэтому `theme.spacing(…)` из Kotlin не
      вызывается.
    - To fix: до v6 файл конвертировался (коммит 529f36e6): `Converter.kt` по имени `Spacing` вызывает
      `Spacing.kt::convertSpacing`, который превращает call signatures в `operator fun invoke`, а
      `SpacingOptions` разбирает `convertSpacingOptions`. Нужно снять исключение, адаптером упростить `transform`
      (например до `Spacing`) и убрать стаб.

- **`useThemeWithoutDefault`** — `@mui/system/useThemeWithoutDefault/useThemeWithoutDefault.d.ts` — ✅
    - Генерируется: `FunctionConverter.kt` нормализует `declare function` в default-форму и переписывает
      `<T = null>` в `<T>`; получается `external fun <T> useThemeWithoutDefault(defaultTheme: T? = definedExternally): T`
      из `@mui/system/useThemeWithoutDefault#default`. Nullability задаёт вызывающий:
      `useThemeWithoutDefault<Theme?>()`.
    - Покрыт `playground/.../samples/system/ThemeWithoutDefaultSample.kt` (без провайдера — `null`/fallback, внутри
      `mui.system.ThemeProvider` — тема провайдера).

## Стабы (вместо настоящей генерации)

- **`mui.material.styles.Theme`** — `external interface Theme : mui.system.Theme` + `focusVisible`. ⏳ Нет
  типизированных `palette: Palette`, `typography`, `mixins: Mixins`, `shadows`, `motion: Motion`, `transitions`,
  `vars`, `alpha`/`lighten`/`darken`, `cssVariables` — всё это приходит из `mui.system.Theme` как `Any?` или пустые
  интерфейсы. Playground обходит это через `unsafeCast` (`ThemeFocusVisibleSample.kt`).
- **`mui.material.styles.ThemeOptions`** — `external interface ThemeOptions : mui.system.ThemeOptions` +
  `focusVisible`. Без CssVars-расширений (`cssVariables`, `colorSchemes`) и типизированных `palette`/`typography`/
  `motion`. Упрощение: наследует `zIndex`, который апстрим `Omit`-ит.
- **`mui.material.styles.createTheme`** — ручной стаб над barrel `@mui/material/styles` (см. выше).
- **`mui.material.styles.ThemeProvider`**, **`mui.system.ThemeProvider`** — минимальные ручные обёртки (см. выше).
- **`mui.system.Spacing`** — пустой `external interface Spacing` (см. `createSpacing`).
- **`mui.system.SystemProps<T>`** — `typealias SystemProps<T> = react.Props`. Используется Typography.
- **`mui.system.Breakpoints`/`BreakpointsOptions`** — генерируются из
  `@mui/system/createBreakpoints/createBreakpoints.d.ts` (раньше из createTheme.d.ts).
- **`LinkBaseProps`, `TablePaginationBaseProps`** — пустые стабы, см. «Internal-rejected parents».

## Известные потери / упрощения в публичном API

- **CssVars вселенная** — ✅ типы `CssVarsTheme`/`CssVarsPalette`/`ColorSystem`/`ColorSystemOptions`/`ThemeVars`
  генерируются из `createThemeFoundation.d.ts`. ⏳ Нет `extendTheme`, `createColorScheme`, CssVars-провайдера, и они
  не подключены к `Theme`/`ThemeOptions` (`Theme.vars` отсутствует).
- **`Theme.applyStyles`** — `Any? /* ApplyStyles<'light' | 'dark'> */` в `mui.system.Theme` и
  `Any? /* ApplyStyles<SupportedColorScheme> */` в `CssVarsTheme`.
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
- **Компоненты, наследующие slots/slotProps от родителя** — ⏳ Dialog/Drawer/Menu/Popover/SwipeableDrawer (extend
  Modal/Popover), OutlinedInput/FilledInput (extend InputBase), Checkbox/Radio/Switch (extend SwitchBase) — не
  наследуют собственный `XxxSlotsAndSlotProps`, чтобы избежать diamond `ParentSlots? vs ChildSlots?`. Сам интерфейс
  эмитится, но `slotProps` у компонента типизирован родительским (`DialogProps.slotProps` — `ModalProps`-овский,
  `DialogSlotProps.paper`/`.transition` недоступны без `unsafeCast`).
- **TextField** — ⏳ `TextFieldSlotsAndSlotProps<InputPropsType>` не генерируется (generic type alias), а
  `TextFieldProps = BaseTextFieldProps` вообще не имеет `slots`/`slotProps`. С v9 это единственный способ передать
  props во внутренние input/label/helper text — см. «Средняя сложность».
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
- Компоненты, наследующие `slots/slotProps` от родителя, теряют свою собственную `XxxSlotsAndSlotProps` parent —
  избегаем diamond.
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

- **TextFieldSlotsAndSlotProps<InputPropsType> — generic над type alias.** ⏳ **Первый приоритет** (см. сводку):
  в v9 у TextField не осталось `InputProps`/`inputProps`/`InputLabelProps`/`FormHelperTextProps`/`SelectProps`, а
  `slots`/`slotProps` в Kotlin нет совсем (`TextFieldSlots` генерируется, `TextFieldSlotProps` — нет).
  История попытки на v6: парсер `Converter.kt::convertSlotsAndSlotPropsAliases` удалось научить читать
  `<InputPropsType>` после имени alias'а и эмитить `export interface TextFieldSlotProps<InputPropsType> { … }`, но
  `Converter.kt::findAdditionalProps` делает `body.substringBefore("<")` для имени интерфейса — generic params
  срезаются и не попадают в Kotlin LHS, а ссылки `TextFieldSlotProps<InputPropsType>` остаются → unresolved.
    - Где чинить: `findAdditionalProps` — извлекать generic params рядом с `interfaceName` и прокидывать в
      декларацию (`external interface $interfaceName$typeParams`); сейчас это делается только для empty-body
      интерфейсов.
    - Дополнительно: снять strip `,\s+TextFieldSlotsAndSlotProps<[^<>]*>` из `adaptRawContent` и решить, кто несёт
      `slots`/`slotProps`: `TextFieldProps` сейчас — alias на `BaseTextFieldProps`, а TS-родитель
      `TextFieldSlotsAndSlotProps<…>` висит на `Standard/Filled/OutlinedTextFieldProps`. Простейший вариант — эмитить
      `slotProps` в `BaseTextFieldProps` с `InputPropsType` = `InputBaseProps`.
    - Верифицировать все существующие generic-интерфейсы (`UseAutocompleteProps<Value>` и т.п.) и Pattern 6 в
      `mapSlotPropsToKotlin` (`SlotProps<React.ElementType<Partial<X>>, …>`), который ждёт именно этого шага.

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

- **Типизированный material `Theme`/`ThemeOptions`.** ⏳ Второй приоритет (см. сводку). Варианты: (а) научить
  `adaptRawContent` схлопывать conditional types в `createThemeNoVars.d.ts` и генерировать `BaseTheme`/`Theme`/
  `ThemeOptions` из файла; (б) расширить ручной стаб `override`-членами (`palette: Palette`,
  `typography: TypographyVariants`, `mixins: Mixins`, `motion: Motion`, `transitions: Transitions`, …) — дешевле,
  но вручную и с риском устаревания. Kotlin-конфликт: `mui.system.Theme` уже объявляет те же имена с типами
  `Any?`/пустыми интерфейсами — `var` нельзя сузить, понадобится менять и system-сторону.

- **CssVars вселенная** (`extendTheme`, `createColorScheme`, CssVars-`ThemeProvider`, `Theme.vars`). ⏳ Типы уже
  есть (`createThemeFoundation`); требует:
    - Поддержки TS conditional types в `Converter.kt::adaptRawContent` (или ручных стабов).
    - Поддержки TS mapped types `{ [K in X]: Y }` → Kotlin `Record<X, Y>` или подобное.

- **TS template literal types** (`text${Capitalize<keyof TypeText>}` в `Typography.color`). ⏳ Распознавать
  `` `…${X}…` `` и схлопывать к `String` (либо к `mui.system.Union` с комментарием набора кейсов).

- **TS overloaded call signatures на interface.** ⏳
    - `interface Spacing { (): string; (v): string; (a, b): string; … }` — для него конвертер уже есть
      (`Spacing.kt::convertSpacing`), блокер только в `createSpacing.transform` (см. System).
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
