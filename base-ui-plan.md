# План работ: миграция `@mui/base` → Base UI (`@base-ui/react`) в karakum-team/mui-kotlin

## TL;DR
- Это **новый generator target**, а не bump версии: нужен отдельный модуль `generateBaseUiDeclarations(...)`, новый `Package.baseUi` и новый выходной npm-таргет `@base-ui/react` (актуальная стабильная версия — 1.6.0, опубликована в июне 2026 и остаётся `latest` на 31 июля 2026), при этом старый `@mui/base@5.0.0-beta.70` сохраняется до достижения паритета и последующего вывода из эксплуатации.
- Главная новизна для генератора — **part-based анатомия** (`Menu.Root`/`Menu.Trigger`/`Menu.Popup`) с namespace-типами `Component.Part.Props`/`.State`/`.ChangeEventDetails`, проп `render` вместо `slots`/`slotProps`/`componentsProps`, а также data-атрибуты и CSS-переменные — это требует новой стратегии маппинга имён (в `.d.ts` есть плоские ре-экспорты `MenuRootProps`, либо Kotlin-объекты-неймспейсы).
- Рекомендуемая стратегия — **сосуществование**: генерировать Base UI параллельно в пакет `baseui/*`, оставить `mui/base/*` замороженным, и вывести последний из эксплуатации после переезда потребителей (в kotlin-wrappers — новый артефакт, предлагаемое имя `kotlin-base-ui`).

## Key Findings

1. **Имя пакета и версия.** Финальное имя npm-пакета — `@base-ui/react`. Он переименован из `@base-ui-components/react` в релизе v1.0.0 (11 декабря 2025), конкретно в PR #3462 by @mnajdova: «Breaking change: Rename packages to use the @base-ui org. The package name has changed from `@base-ui-components/react` to `@base-ui/react`». Последняя версия старого имени — `@base-ui-components/react@1.0.0-rc.0`, далее миграция на `@base-ui/react`. Версионность у Base UI своя; последняя стабильная — 1.6.0 (июнь 2026), на 31 июля 2026 всё ещё `latest`. Целиться нужно на `@base-ui/react`.

2. **Заморозка `@mui/base`.** В репозитории (`gradle.properties`) зафиксировано `mui-base.version=5.0.0-beta.70` — это последний релиз пакета («Latest version: 5.0.0-beta.70, last published: a year ago»), v9 не будет. Пакет npm-deprecated; фактическое предупреждение npm: «npm warn deprecated @mui/base@5.0.0-beta.70: This package has been replaced by @base-ui-components/react» (сообщение устарело и, по mui/base-ui issue #3598, должно указывать на `@base-ui/react`). Остальные MUI-зависимости уже на v9 (`mui-material.version=9.1.2`, `mui-x-date-pickers.version=9.7.0`).

3. **Инструментарий репо (подтверждено `gradle.properties`/`settings.gradle.kts`).** `kotlin.version=2.4.0`, `kotlin.wrappers.version=2026.6.10`, `kfc.version=19.10.0` (turansky KFC), `seskar.version=4.60.0` (Seskar — обеспечивает `@JsName`/union/enum-машинерию), `kotlin.js.yarn=false` (npm, lockfile в `.kotlin-locks/js` + корневой `package-lock.json`). Генерация построена на конвертере `karakum-team/karakum`, вызываемом из `buildSrc/src/main/kotlin/karakum/mui/`. Gradle-модули: `mui-kotlin` (выход, компилируется `:mui-kotlin:compileKotlinJs`) и `playground`; каталог `playground-ts` существует, но не подключён через `include(...)` — это TS-сторона для сверки. CI-workflow называется «declarations».

4. **kotlin-wrappers.** Уже существует артефакт `org.jetbrains.kotlin-wrappers:kotlin-mui-base` (обёртка старого `@mui/base`, генерируется из mui-kotlin; declarations кладутся в `src/jsMain/generated`). Обёртки Base UI (`@base-ui/react`) в kotlin-wrappers пока НЕТ — это новый артефакт.

5. **TypeScript-форма Base UI (подтверждено docs 1.6.0).** Namespace-паттерн: каждый компонент экспортирует части, у каждой части есть `Props` и `State` (напр. `Tooltip.Root.Props`, `Tooltip.Root.State`), а также кастомные события `ChangeEventDetails`/`ChangeEventReason` (напр. `Combobox.Root.ChangeEventDetails`), `Actions` (`Menu.Root.Actions`), `Orientation` и т.п. В `.d.ts` эти типы ре-экспортируются с «плоскими» именами: `Menu.Root.Props` = re-export `MenuRootProps`, `Menu.Positioner.State` = `MenuPositionerState`. Проп `render: ReactElement | ((props: HTMLProps, state: State) => ReactElement)`. `componentsProps`/`slotProps`/`slots` отсутствуют полностью. На релизе v1.0.0 зафиксировано 35 компонентов (InfoQ, февраль 2026: «The release ships with 35 accessible components»); к 1.6.0 добавились Drawer и OTPField.

## Details

### Архитектурный контекст генератора

Генерация в mui-kotlin — набор функций `generate*Declarations(...)` в `Generator.kt` (по аналогии с `generatePickersDeclarations`/`generateTreeViewDeclarations`), каждая берёт `.d.ts` соответствующего npm-пакета из `node_modules/@mui/<pkg>` и прогоняет через karakum с набором плагинов/оверрайдов, складывая Kotlin-код в отдельный выходной пакет (`Package.kt`). Существующая машинерия включает: обработку slots/slotProps, ARIA-пропов, `@JsName` с dashed-именами, удаление deprecated, typealias-заглушки, `KNOWN_TYPES`/`STANDARD_TYPE_MAP`, type-only генерацию и `INTERNAL_REJECTED_PARENTS`. Поскольку Base UI имеет иную форму `.d.ts`, старый путь `mui/base` не подходит — нужен отдельный target.

> Примечание: внутренние `.kt`-файлы генератора не удалось прочитать вербатим (см. Caveats); имена констант приведены по известной архитектуре karakum и подтверждённой структуре репозитория, перед реализацией их нужно сверить с фактическим кодом `buildSrc`.

### Фазы работ

#### Phase 0 — Подготовка и разведка
- Зафиксировать целевую версию `@base-ui/react` (1.6.0) и снять полный `.d.ts`-снимок из `node_modules` для инвентаризации (можно опереться и на машиночитаемый `llms.txt`/markdown, которые Base UI публикует).
- Составить инвентарь: 35 компонентов (v1.0) + Drawer/OTPField, плюс утилиты; для каждого — список частей (parts), их `Props`/`State`/`ChangeEventDetails`, data-атрибутов и CSS-переменных.
- Прогнать karakum «как есть» на одном компоненте (Menu) для оценки объёма ручных оверрайдов.

**DoD:** таблица «компонент → части → типы»; baseline-прогон karakum по одному модулю. **Риск:** объём частей велик (у Menu ~20 частей). **Сложность:** низкая.

#### Phase 1 — Зависимости
- Добавить в `gradle.properties` свойство `base-ui-react.version=1.6.0`. Имя не выбирается свободно: хелпер `npmv()` в `mui-kotlin/build.gradle.kts` выводит его из имени npm-пакета как `packageName.removePrefix("@").replace("/", "-") + ".version"`, т.е. `@base-ui/react` → `base-ui-react.version`. Сохранить `mui-base.version=5.0.0-beta.70`.
- Обновить `package-lock.json` и `.kotlin-locks/js`, добавив `@base-ui/react` (не удаляя `@mui/base`).
- Проверить peer-зависимости (react/react-dom) на совместимость с версиями репо.

**DoD:** `@base-ui/react` разрешается в node_modules, lockfile консистентен, старый пакет на месте. **Риск:** конфликт версий React. **Сложность:** низкая.

#### Phase 2 — Новый модуль генератора и Package
- Добавить `Package.baseUi` в `Package.kt` (выходной каталог `baseui/…`).
- Реализовать `generateBaseUiDeclarations(...)` в `Generator.kt`: вход — `.d.ts` из `@base-ui/react` по модулям (`menu`, `dialog`, `select`, …), выход — `Package.baseUi`.
- Настроить `moduleNameMapper`/`@JsModule` на субимпорты `@base-ui/react/<component>` (Base UI tree-shakeable; импорт по модулям вида `@base-ui/react/menu`).

**DoD:** `generateBaseUiDeclarations` вызывается из entry-point и генерирует хотя бы один компонент end-to-end. **Риск:** субпуть-импорты и `'use client'`-директивы. **Сложность:** средняя.

#### Phase 3 — Маппинг part-based анатомии
Ключевое решение — как отразить `Menu.Root` в Kotlin:
- (A) **Плоские имена** через JsName: компонент `MenuRoot` + `external interface MenuRootProps` (Base UI сам ре-экспортит их как `MenuRootProps`), визуальная группировка в файле `Menu.kt`. Проще для Kotlin/JS, совпадает с ре-экспортами в `.d.ts`.
- (B) **Kotlin-объекты-неймспейсы**: `object Menu { val Root: FC<…> }` — ближе к JS-виду `Menu.Root`, но karakum эмулирует TS-namespace через Kotlin-объекты, что даёт громоздкий код и трения с `external`.

Рекомендация: **вариант A** (плоские `MenuRoot`/`MenuRootProps`) как основной — совпадает с формой ре-экспортов Base UI и ложится на существующую машинерию FC/Props. Часть `Root`, не рендерящая DOM-элемент, оформляется как provider-компонент.

**DoD:** для Menu сгенерированы все части (`MenuRoot`, `MenuTrigger`, `MenuPortal`, `MenuPositioner`, `MenuPopup`, `MenuItem`, …) с `*Props`. **Риск:** дублирование имён частей между компонентами (`Separator`, `Portal`, `Backdrop`) — но Base UI уже префиксирует именем компонента (`MenuSeparator`), поэтому на уровне плоских имён конфликтов нет. **Сложность:** высокая.

#### Phase 4 — `render`-проп и `state`
- Замапить `render?: ReactElement | ((props: HTMLProps, state: State) => ReactElement)` (union-заглушка + функциональный тип `(props, state) -> ReactElement`).
- Учесть `className`/`style` как `string | (state) => string` и `CSSProperties | (state) => CSSProperties`.
- Сгенерировать `*State`-интерфейсы (напр. `MenuPositionerState` с `open`, `side`, `align`, `anchorHidden`, `nested`, `instant`).

**DoD:** `render`, `className`-функция и `State` компилируются для Menu. **Риск:** union `ReactElement | function` требует typealias-заглушки. **Сложность:** высокая.

#### Phase 5 — data-атрибуты, CSS-переменные, ARIA
- Data-атрибуты (`data-popup-open`, `data-highlighted`, `data-starting-style`, …) — runtime-атрибуты DOM, не типы; при необходимости оформить константами через `@JsName`-dashed машинерию (уже есть в репо).
- CSS-переменные (`--anchor-height`, `--available-width`, `--transform-origin`, …) — оформить строковыми константами/утилитой.
- ARIA — переиспользовать существующую ARIA-машинерию.

**DoD:** атрибуты/переменные доступны как константы там, где полезно; отсутствие типов не ломает компиляцию. **Риск:** избыточная генерация. **Сложность:** низкая-средняя.

#### Phase 6 — Хуки и утилиты
- Сгенерировать `useRender` (+ `useRender.ComponentProps`, `useRender.ElementProps`, `useRender.Parameters`, `RenderProp`) и `mergeProps`.
- Провайдеры: `DirectionProvider`, `CSPProvider`.
- Прочие хуки (напр. `useFilteredItems` для Combobox/Autocomplete).

**DoD:** `useRender`/`mergeProps`/провайдеры компилируются. **Риск:** гигантский union `defaultTagName` (~150 строковых литералов тегов) — заменить на `String`/enum-заглушку через STANDARD_TYPE_MAP. **Сложность:** средняя.

#### Phase 7 — Исключения и type-only генерация
- Определить `EXCLUDED_TYPES` для Base UI (внутренние типы Floating UI, `BaseUIEvent`, `VirtualElement` и т.п. — заглушки или переиспользование существующих KNOWN_TYPES).
- Type-only генерация для сложных union `ChangeEventReason`/`ChangeEventDetails`.
- `INTERNAL_REJECTED_PARENTS` — отсечь внутренние parent-типы.

**DoD:** нет «висящих» ссылок на несгенерированные типы. **Риск:** пропущенные транзитивные типы. **Сложность:** средняя.

#### Phase 8 — Компиляция и полное покрытие
- Итеративно расширять генерацию на все компоненты, добиваясь зелёного `:mui-kotlin:compileKotlinJs`.
- Приоритет — компоненты с аналогами в `@mui/base` (Menu, Select, Slider, Switch, Tabs, Tooltip, Input, Popover).

**DoD:** `:mui-kotlin:compileKotlinJs` зелёный со всеми компонентами Base UI. **Риск:** длинный хвост экзотических типов. **Сложность:** высокая.

#### Phase 9 — Playground / примеры
- Добавить в `playground` Kotlin-примеры (Menu, Dialog, Select, Slider) с `render`-пропом и частями.
- В `playground-ts` — эталонные TS-примеры для сверки формы API.

**DoD:** примеры компилируются и запускаются. **Сложность:** средняя.

#### Phase 10 — Сосуществование и вывод из эксплуатации
- Генерировать Base UI (`baseui/*`) параллельно с `mui/base/*`.
- В kotlin-wrappers завести новый артефакт (предлагаемое имя `kotlin-base-ui`), не трогая `kotlin-mui-base`.
- После переезда потребителей — пометить `kotlin-mui-base` deprecated и заморозить, затем удалить генерацию `mui/base`.

**DoD:** оба пакета публикуются; есть migration notes. **Риск:** долгий переходный период. **Сложность:** низкая (организационная).

### Таблица соответствия компонентов `@mui/base` → Base UI

| `@mui/base` | Base UI аналог | Статус |
|---|---|---|
| Button / useButton | Button (v1.0.0-beta.5+) | Есть, переработан (part-less, `render`, `nativeButton`) |
| Badge | — | Нет прямого аналога |
| ClickAwayListener | — | Нет (логика встроена в popups) |
| FocusTrap | — | Нет (встроено в Dialog/Popover/Menu) |
| Input / useInput | Input; Field.*; NumberField | Переименован/разбит на части |
| Menu / MenuItem / Dropdown / MenuButton / useMenu | Menu.* (Root/Trigger/Portal/Positioner/Popup/Item/…) | Есть, полностью part-based |
| Modal / useModal | Dialog.* / AlertDialog.* | Заменён на Dialog |
| NoSsr | — | Нет аналога |
| Option / OptionGroup | Select.Item / Select.Group (и Combobox/Autocomplete) | Переработан в части |
| Popper / usePopper | Popover.* / (Positioner-часть) | Заменён Positioner-механикой |
| Popup (unstable) | Popover.Popup / Menu.Popup | Часть каждого popup-компонента |
| Portal | Menu.Portal / Dialog.Portal (part) | Только как часть, нет standalone |
| Select / useSelect | Select.* (part-based, generic по Value) | Есть, part-based |
| Slider / useSlider | Slider.* (Root/Control/Track/Thumb/Indicator/Value) | Есть, part-based |
| Snackbar / useSnackbar | Toast.* (+ ToastProvider) | Заменён на Toast |
| Switch / useSwitch | Switch.* (Root/Thumb) | Есть, part-based |
| Tabs / Tab / TabsList / TabPanel | Tabs.* (Root/List/Tab/Panel/Indicator) | Есть, переименованы части |
| TablePagination | — | Нет аналога |
| TextareaAutosize | — | Нет аналога |
| useAutocomplete | Autocomplete.* / Combobox.* / useFilteredItems | Заменён на компоненты + хук |
| FormControl / useFormControl | Field.* / Fieldset.* / Form | Заменён на Field/Form |

Новые компоненты Base UI без предшественника в `@mui/base`: Accordion, AlertDialog, Avatar, Checkbox, CheckboxGroup, Collapsible, Combobox, ContextMenu, Drawer, Field, Fieldset, Form, Menubar, Meter, NavigationMenu, NumberField, OTPField, PreviewCard, Progress, Radio, ScrollArea, Separator, Toggle, ToggleGroup, Toolbar, Tooltip.

### Ожидаемые проблемы трансляции TS→Kotlin и решения

| Проблема | Пример | Решение |
|---|---|---|
| Namespace-типы `Component.Part.Props` | `Menu.Root.Props`, `Menu.Positioner.State` | Использовать плоские ре-экспорты (`MenuRootProps`, `MenuPositionerState`), которые Base UI уже экспортирует; генерировать плоские Kotlin-интерфейсы |
| Union `ReactElement \| function` в `render` | `render?: ReactElement \| ((props, state) => ReactElement)` | typealias-заглушка union + функциональный тип `(props, state) -> ReactElement` |
| `className`/`style` как значение-или-функция | `string \| ((state) => string)` | union-заглушка или два перегруженных сеттера |
| Discriminated union `ChangeEventDetails` | 12+ вариантов `{ reason; event }` | type-only генерация union-заглушки; `ChangeEventReason` как enum строк |
| Generic-компоненты по Value | `Select<Value>`, `createHandle<Payload>()` | сохранить generic-параметр в FC/Props или заглушка `Any?`, где generic невыразим |
| Огромный union `defaultTagName` (~150 тегов) | параметр `useRender` | заменить на `String` (или enum-заглушку) через STANDARD_TYPE_MAP |
| Функции-children | `children: ReactNode \| ((arg) => ReactNode)` | union-заглушка + функциональный тип |
| `BaseUIEvent<T>`, `VirtualElement`, Floating UI типы | `data.anchor`, `collisionBoundary` | KNOWN_TYPES-заглушки/переиспользование web-типов |
| Data-атрибуты и CSS-переменные | `data-highlighted`, `--anchor-width` | не типы; при необходимости — dashed `@JsName`-константы (машинерия уже есть) |
| Дублирование имён частей | `Separator`, `Portal`, `Backdrop` в разных компонентах | Base UI уже префиксирует (`MenuSeparator`), конфликтов на уровне плоских имён нет |
| Отсутствие `slots`/`slotProps`/`componentsProps` | — | НЕ переиспользовать slots-машинерию для рендера; использовать `render` |

## Recommendations

1. **Начать с вертикального среза на Menu** (Phases 0–5 на одном компоненте): это самый «богатый» компонент (Root/Trigger/Portal/Backdrop/Positioner/Popup/Arrow/Item/LinkItem/SubmenuRoot/SubmenuTrigger/Group/GroupLabel/RadioGroup/RadioItem/RadioItemIndicator/CheckboxItem/CheckboxItemIndicator/Separator/Viewport), покрывающий все ключевые проблемы трансляции. Компилируется Menu — паттерн масштабируется.
2. **Принять решение по неймспейсам рано** (Phase 3): рекомендую плоские имена (`MenuRoot`) — совпадают с ре-экспортами Base UI и минимизируют риск с `external`-объектами.
3. **Сосуществование, не миграция** (Phase 10): держать `mui/base/*` и `baseui/*` параллельно; в kotlin-wrappers — новый артефакт `kotlin-base-ui`, `kotlin-mui-base` заморозить и позже депрекейтнуть.
4. **Порядок покрытия компонентов**: сперва те, у кого есть потребители со стороны `@mui/base` (Menu, Select, Slider, Switch, Tabs, Tooltip, Input), затем новые.

**Пороги, меняющие план:**
- Если вариант с плоскими именами даёт неразрешимые конфликты → переключиться на Kotlin-объекты-неймспейсы (вариант B), приняв рост сложности.
- Если karakum не справляется с discriminated unions `ChangeEventDetails` → перейти на полностью ручные type-only заглушки для событий.
- Если целевая версия Base UI уходит вперёд с breaking changes → зафиксировать версию в `base-ui.version` и обновлять контролируемо.

## Caveats
- Внутренние `.kt`-файлы генератора (`Generator.kt`, `Package.kt`, `Converter.kt`, `KotlinType.kt`, `MemberConverter.kt`, `Overrides.kt`, `Adapter.kt`) не удалось получить вербатим — их точные сигнатуры и списки констант (`EXCLUDED_TYPES`, `KNOWN_TYPES`, `STANDARD_TYPE_MAP`, `INTERNAL_REJECTED_PARENTS`) описаны по аналогии с известной архитектурой karakum и подтверждённой структурой репо, а не по прямому чтению кода. Перед реализацией сверьтесь с фактическим кодом `buildSrc`.
- Имя артефакта kotlin-wrappers (`kotlin-base-ui`) — предложение, не факт: финальное имя определяет команда JetBrains.
- Точный набор частей/пропов приведён по документации Base UI 1.6.0 (Menu разобран детально); для остальных компонентов части перечислены на основе навигации docs и могут отличаться в мелочах между минорными версиями.
- Дата релиза 1.6.0 в docs Base UI указана как 17 июня 2026, а на странице npm — 18 июня 2026 (расхождение в один день между источниками); на 31 июля 2026 версия 1.6.0 остаётся `latest`.
- Base UI активно развивается: в v1.x были breaking changes (переименование пакета в PR #3462, поведение Checkbox/Switch, `keepMounted` в Tabs, удаление `keepHighlight` в Combobox), поэтому целевую версию нужно фиксировать и обновлять контролируемо.