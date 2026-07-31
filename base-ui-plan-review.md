# Техническое ревью плана миграции `@mui/base` → Base UI в `karakum-team/mui-kotlin`

**Итог: план фактически очень точен по внешним первоисточникам (Base UI, npm, gradle.properties), но содержит одну серьёзную концептуальную ошибку в имени npm-пакета, несколько устаревших/неточных данных, а главное — его раздел про внутренности генератора `buildSrc` не был и не смог быть верифицирован ни автором, ни ревью, поэтому все имена файлов/функций/констант (`Generator.kt`, `Package.baseUi`, `generateBaseUiDeclarations`, `EXCLUDED_TYPES`, `KNOWN_TYPES`, `STANDARD_TYPE_MAP`, `INTERNAL_REJECTED_PARENTS`) должны считаться НЕПОДТВЕРЖДЁННЫМИ гипотезами, а не фактами.**

## TL;DR
- **Критическая ошибка имени пакета:** актуальный npm-пакет называется **`@base-ui/react`** (это плоское имя из плана в основном верно), НО в плане параллельно утверждается, что старый пакет `@base-ui-components/react` был переименован в `@base-ui/react` в v1.0.0/PR #3462 — это **подтверждено дословно**. При этом в плане есть внутреннее противоречие: субимпорты в Phase 2 записаны как `@base-ui/react/<component>` (верно), но в задаче ревью и в некоторых источниках всё ещё встречается старое имя. Итог: целиться на `@base-ui/react@1.6.0` — **правильно**.
- **Раздел `buildSrc` недостоверен:** план сам это признаёт, и ревью подтверждает, что проверить внутренние `.kt`-файлы не удалось (GitHub блокирует автоматический доступ к blob/tree). Всё, что касается имён `Generator.kt`, `Package.kt`, `generateBaseUiDeclarations`, `Package.baseUi`, `Package.kt`-констант — гипотезы. Karakum действительно эмулирует TS-namespace через Kotlin-объекты (подтверждено докой karakum), что напрямую влияет на выбор варианта A vs B в Phase 3.
- **Внешние факты почти все верны:** версии в `gradle.properties`, депрекация `@mui/base@5.0.0-beta.70`, issue #3598, PR #3462, part-based анатомия Menu (ровно 20 частей), плоские ре-экспорты (`MenuRootProps`), `render`-проп, `ChangeEventDetails`/`Actions`, `useRender`/`mergeProps`/`CSPProvider`/`DirectionProvider`, `llms.txt`, React peer `^17||^18||^19`, breaking changes v1.x — **всё подтверждено первоисточниками**. Существующий артефакт `kotlin-mui-base` в kotlin-wrappers реален; обёртки Base UI пока нет.

---

## (а) Вердикт по каждому фактическому утверждению

### Блок 1 — Base UI (пакет, версия, компоненты, типы)

| Утверждение плана | Статус | Реальное положение дел + источник |
|---|---|---|
| Финальное имя пакета — `@base-ui/react` | **Подтверждено** | npm: `@base-ui/react`, latest 1.6.0. Старое имя `@base-ui-components/react` помечено «Package was renamed to @base-ui/react». (npmjs.com/package/@base-ui/react, npmjs.com/package/@base-ui-components/react) |
| Переименование из `@base-ui-components/react` в v1.0.0, PR #3462 by @mnajdova, дословная формулировка | **Подтверждено дословно** | Release v1.0.0 (11 дек 2025): «Breaking change: Rename packages to use the @base-ui org. The package name has changed from @base-ui-components/react to @base-ui/react. (#3462) by @mnajdova». (github.com/mui/base-ui/releases/tag/v1.0.0) |
| Последняя стабильная версия — 1.6.0, `latest` на 31.07.2026 | **Подтверждено** | npm: «Latest version: 1.6.0». Дата публикации на npmx — **18 июня 2026** (не «июнь 2026, июнь 17» — расхождение в 1 день между docs и npm подтверждено самим планом). (base-ui.com/react/overview/releases, npmx.dev) |
| Последняя версия старого имени — `@base-ui-components/react@1.0.0-rc.0` | **Подтверждено** | npm: latest 1.0.0-rc.0, «Package was renamed to @base-ui/react». (npmjs.com) |
| Issue mui/base-ui#3598 про устаревшее deprecation-сообщение | **Подтверждено** | Issue #3598 «Update deprecated message for "@mui/base" package»: сообщение указывает на `@base-ui-components/react`, но должно — на `@base-ui/react`. (github.com/mui/base-ui/issues/3598) |
| v1.0.0 = 35 компонентов (InfoQ, февраль 2026) | **Подтверждено** | InfoQ: «The release ships with 35 accessible components». (infoq.com/news/2026/02/baseui-v1-accessible) |
| К 1.6.0 добавились Drawer и OTPField | **Подтверждено, но неточно по датам/статусу** | Drawer: preview в v1.2.0 (фев 2026), **стабилен с v1.3.0** (12 мар 2026). OTPField: preview в v1.4.0 (апр 2026), **стабилен с v1.6.0** (июнь 2026). Итог: оба существуют, но добавлены раньше, чем «к 1.6.0». (base-ui.com/.../releases) |
| Namespace-паттерн `Tooltip.Root.Props`/`.State`, `Combobox.Root.ChangeEventDetails`, `Menu.Root.Actions` | **Подтверждено** | TypeScript-гайд Base UI: «ChangeEventDetails (such as Combobox.Root.ChangeEventDetails)… Menu.Root.Actions gives access to the shape of the actionsRef object prop». (base-ui.com/react/handbook/typescript) |
| Плоские ре-экспорты: `Menu.Root.Props` = `MenuRootProps`, `Menu.Positioner.State` = `MenuPositionerState` | **Подтверждено** | Docs дословно: «Re-Export of Root props as NavigationMenuRootProps», «Re-Export of Positioner props as NavigationMenuPositionerProps», аналогично для ContextMenu. Паттерн общий для всех компонентов. (base-ui.com/react/components/navigation-menu, .../context-menu) |
| `render?: ReactElement \| ((props: HTMLProps, state: State) => ReactElement)` | **Подтверждено** | Docs: `ReactElement \| ((props: HTMLProps, state: NavigationMenu.Root.State) => ReactElement) \| undefined`. (base-ui.com) |
| `componentsProps`/`slotProps`/`slots` отсутствуют полностью | **Подтверждено** | Base UI использует `render`-проп и `mergeProps`; slots-механики нет. (base-ui.com/react/utils/use-render) |
| Menu ~20 частей (перечисление) | **Подтверждено полностью** | Anatomy: Root, Trigger, Portal, Backdrop, Positioner, Popup, Arrow, Item, LinkItem, SubmenuRoot, SubmenuTrigger, Group, GroupLabel, RadioGroup, RadioItem, RadioItemIndicator, CheckboxItem, CheckboxItemIndicator, Separator, Viewport — **ровно 20, все реальны**. (base-ui.com/react/components/menu) |
| Существуют Drawer, OTPField, Combobox, Autocomplete, ContextMenu, Menubar, NavigationMenu, Toast, Field, Fieldset, Form, Meter, PreviewCard, ScrollArea | **Все подтверждены** | Навигация docs перечисляет все 37 компонентов в 1.6.0. (base-ui.com/react/overview/releases — sidebar) |
| Число компонентов «35 (v1.0) + Drawer/OTPField» | **Неточно** | На 1.6.0 в docs-навигации перечислено **37 компонентов** (35 + Drawer + OTPField). Формула верна арифметически, но лучше зафиксировать «37 в 1.6.0». |
| `useRender`, `mergeProps` существуют | **Подтверждено** | `@base-ui/react/use-render`, `@base-ui/react/merge-props`; docs описывают `useRender.ComponentProps`/`ElementProps`. (base-ui.com/react/utils/use-render, .../merge-props) |
| `DirectionProvider`, `CSPProvider` существуют | **Подтверждено** | Utils-раздел docs: CSP Provider (`@base-ui/react/csp-provider`, проп `nonce`) и Direction Provider. **CSPProvider, НЕ NonceProvider** — план прав. (base-ui.com/react/utils/csp-provider) |
| `useFilteredItems` для Combobox/Autocomplete | **Подтверждено** | Добавлен в v1.2.0: «New useFilteredItems hook for Autocomplete and Combobox». (base-ui.com/.../releases) |
| Субимпорты `@base-ui/react/<component>` (tree-shakeable) | **Подтверждено** | `import { Menu } from '@base-ui/react/menu'`, `import { useRender } from '@base-ui/react/use-render'`. (base-ui.com) |
| Breaking changes: Checkbox/Switch native unchecked, Tabs `keepMounted`, удаление `keepHighlight` из Combobox | **Подтверждено дословно** | v1.0.0-rc.0: «Breaking change: Match native unchecked state in Checkbox and Switch. Breaking change: Fixed Panel keepMounted behavior in Tabs. Breaking change: Removed the keepHighlight prop from Combobox». (base-ui.com/.../releases) |
| Существует `llms.txt` | **Подтверждено** | Docs-навигация содержит ссылку `llms.txt` (base-ui.com/llms.txt) и «New llms.txt and markdown links for AI» в v1.0.0-beta.2. (base-ui.com) |
| React peer deps: 17/18/19 | **Подтверждено** | `react ^17 \|\| ^18 \|\| ^19`, `react-dom ^17 \|\| ^18 \|\| ^19`. Зависит от `@floating-ui/react-dom ^2.1.8`, `use-sync-external-store`. (npmx.dev/package/@base-ui/react) |
| Toast заменяет Snackbar, есть провайдер | **Подтверждено + уточнение** | Императивный API: `Toast.createToastManager()`, `Toast.useToastManager()`, `Toast.Provider toastManager={...}`. Это НЕ покрыто планом детально (пробел). (base-ui.com/react/components/toast) |

### Блок 2 — состояние `@mui/base`

| Утверждение плана | Статус | Реальное положение дел + источник |
|---|---|---|
| Последняя версия `@mui/base@5.0.0-beta.70`, «v9 не будет» | **Подтверждено** | npm: «5.0.0-beta.70 • Published a year ago», 152 версии, 348 dependents. (npmjs.com/package/@mui/base) |
| Пакет npm-deprecated, текст «replaced by @base-ui-components/react» | **Подтверждено с нюансом** | npm-страница `@mui/base` сейчас показывает author message **«This package has been replaced by @base-ui/react»** (уже обновлено!). Но при установке npm всё ещё выдаёт старое `npm warn deprecated @mui/base@…: This package has been replaced by @base-ui-components/react` (см. issue mui/material-ui#45297). Итог: план прав, что сообщение устарело, но на самой npm-странице оно уже поправлено. (npmjs.com, github.com/mui/material-ui/issues/45297) |
| Таблица соответствия `@mui/base` → Base UI | **В основном верна, есть пробелы** | Соответствия Menu/Select/Slider/Switch/Tabs/Tooltip/Modal→Dialog/Snackbar→Toast/Popper→Popover-Positioner — корректны. **Не отражено:** `Unstable_NumberInput`/`useNumberInput` из `@mui/base` → в Base UI это `NumberField` (хук `useNumberInput` исчез, см. issue mui/base-ui#1814 — прямого аналога `getIncrementButtonProps` нет). Это стоит явно указать потребителям. |

### Блок 3 — репозиторий `karakum-team/mui-kotlin` (прочитаны реальные файлы)

| Утверждение плана | Статус | Реальное положение дел + источник |
|---|---|---|
| `kotlin.version=2.4.0` | **Подтверждено** | gradle.properties (main), прочитан дословно. |
| `kotlin.wrappers.version=2026.6.10` | **Подтверждено** | gradle.properties. |
| `kfc.version=19.10.0` | **Подтверждено** | gradle.properties. |
| `seskar.version=4.60.0` | **Подтверждено** | gradle.properties. |
| `mui-base.version=5.0.0-beta.70` | **Подтверждено** | gradle.properties. |
| `mui-material.version=9.1.2` | **Подтверждено** | gradle.properties (также `mui-system.version=9.1.2`, `mui-icons-material.version=9.1.1`, `mui-lab.version=9.0.0-beta.5`). |
| `mui-x-date-pickers.version=9.7.0` | **Подтверждено** | gradle.properties (также `mui-x-tree-view.version=9.7.0`). |
| `kotlin.js.yarn=false` (npm) | **Подтверждено** | gradle.properties: `kotlin.js.yarn=false`. |
| Модули: `mui-kotlin` и `playground`; `playground-ts` существует, но НЕ подключён через `include(...)` | **Подтверждено полностью** | `settings.gradle.kts`: только `include("mui-kotlin")` и `include("playground")`. В дереве репо каталог `playground-ts` физически присутствует, но в settings его нет. (settings.gradle.kts, дерево репо) |
| CI-workflow называется «declarations» | **Подтверждено** | Бейдж README: `workflows/declarations/badge.svg`. |
| Генерация построена на конвертере `karakum-team/karakum`, вызываемом из `buildSrc/src/main/kotlin/karakum/mui/` | **НЕ ПРОВЕРЕНО (ключевой пробел)** | Ни автор плана, ни ревью, ни субагент не смогли открыть содержимое `buildSrc` (GitHub блокирует автодоступ к blob/tree). `buildSrc` существует как каталог (виден в дереве репо), но **имена файлов, функций и зависимость на karakum не верифицированы**. Косвенно: проект использует плагины `io.github.turansky.*`, а Karakum — конвертер от того же автора; wrapper-README в kotlin-wrappers гласят «Declarations in src/jsMain/generated are generated by mui-kotlin». Karakum-дока подтверждает, что namespace эмулируются Kotlin-объектами. Но **прямого подтверждения нет**. |
| Имена `Generator.kt`, `Package.kt`, `Converter.kt`, `KotlinType.kt`, `MemberConverter.kt`, `Overrides.kt`, `Adapter.kt` | **НЕ ПРОВЕРЕНО → трактовать как гипотезы** | Не подтверждены ни одним источником. |
| Константы `EXCLUDED_TYPES`, `KNOWN_TYPES`, `STANDARD_TYPE_MAP`, `INTERNAL_REJECTED_PARENTS` | **НЕ ПРОВЕРЕНО → гипотезы** | Существование не подтверждено; реальные имена неизвестны. |
| В репо `MUI_V6_TODO.md`, `MUI_V7_TODO.md`, `MUI_V9_TODO.md`, `FUTURE_IMPROVEMENTS.md` | **Подтверждено (существуют)** | Видны в дереве репо. Содержимое (упоминания base-ui) не прочитано — **открытый вопрос**. |
| Есть ли открытые issues/PR/ветки про base-ui | **НЕ ПРОВЕРЕНО** | Доступ к issues/PR/branches заблокирован. Нужно проверить владельцу вручную. |

### Блок 4 — kotlin-wrappers

| Утверждение плана | Статус | Реальное положение дел + источник |
|---|---|---|
| Существует артефакт `kotlin-mui-base`, генерируется из mui-kotlin, кладётся в `src/jsMain/generated` | **Подтверждено** | API-reference kotlin-wrappers: «Kotlin wrapper for MUI Base UI». README wrapper'ов: «Declarations in src/jsMain/generated are generated by mui-kotlin». Артефакт `kotlin-mui-base` есть в списке модулей. (jetbrains.github.io/kotlin-wrappers, github.com/JetBrains/kotlin-wrappers) |
| Обёрток Base UI (`@base-ui/react`) в kotlin-wrappers пока НЕТ | **Подтверждено (на момент проверки)** | В списке модулей kotlin-wrappers присутствует только `kotlin-mui-base` (старый `@mui/base`), отдельного `@base-ui/react`-модуля не найдено. **Не найдено** и готовой обёртки в turansky/karakum-team репозиториях. |
| Имя нового артефакта `kotlin-base-ui` | **Предположение (не факт)** | План честно помечает это как предложение. Реальное имя определит JetBrains. |
| `kotlin-mui-base` не удалён/не депрекейтнут | **Подтверждено** | Артефакт активен в актуальном каталоге kotlin-wrappers. |

---

## (б) Оценка архитектурных решений

**1. Вариант A (плоские `MenuRoot`/`MenuRootProps`) vs Вариант B (`external object Menu { val Root }`).**
План рекомендует вариант A — и это **правильный выбор**, причём аргумент даже сильнее, чем в плане:
- Base UI сам экспортирует плоские ре-экспорты (`MenuRootProps`, `MenuPositionerState`) — доказано дословно docs.
- Дока Karakum прямо гласит: «In Kotlin, there is no equivalent for TypeScript namespaces… Karakum tries to emulate TypeScript namespaces using Kotlin objects», причём результат бывает «obviously incorrect / громоздким». То есть автогенерация варианта B через Karakum создаёт именно те трения с `external`, о которых предупреждает план. **Вывод: вариант A архитектурно обоснован и подтверждён поведением самого инструмента.**
- Прецедент: обёртки Radix-подобных part-based библиотек в Kotlin/JS обычно используют либо `external object` с `@JsName`, либо плоские `FC`. `external object X { val Root: FC<Props> }` — легитимный приём Kotlin/JS, но именно с namespace-эмуляцией Karakum он даёт нестабильный вывод. Для DX (визуальная группировка `Menu.Root`) можно рассмотреть **гибрид**: генерировать плоские `MenuRoot` + вручную/через seskar добавить фасадный `external object Menu` с `@JsName`-алиасами поверх — но это доп. работа, не блокирующая MVP.

**2. `render`-проп для kotlin-wrappers react.**
Решение (union-заглушка `ReactElement | function` + функциональный тип `(props, state) -> ReactElement`) **корректно**. Seskar как раз обеспечивает union-машинерию. Нюанс, не отражённый в плане: `useRender.ComponentProps<Tag, State>` и `useRender.ElementProps<Tag>` — дженерики по HTML-тегу; их придётся моделировать через generic-параметр или `Any?`-заглушку. Также известен реальный баг Base UI (issue #3878) — `render` как ReactElement может протекать в DOM; для биндинга это не важно, но полезно знать.

**3. Не выделено в плане, но нужно:**
- **Enum'ы строковых литералов** (`side`, `align`, `orientation: 'horizontal'|'vertical'`, `Direction: 'ltr'|'rtl'`, `TransitionStatus: 'starting'|'ending'`) — план упоминает `ChangeEventReason as enum строк`, но не выделяет системную стратегию enum-генерации для всех строковых union'ов. Это отдельная фаза (seskar `@JsValue`/sealed).
- **Generic-компоненты**: `Select<Value>`, `Combobox` (`multiple`), `Toast.createToastManager()` — план даёт заглушку `Any?`, но многозначный Select/Combobox и императивный Toast (`createToastManager`/`useToastManager`) требуют отдельного плана. **Toast API вообще выпал из фаз** — это архитектурный пробел.
- **Формы**: связка `Form` + `Field` + `Fieldset`, валидация, `onFormSubmit` — план лишь упоминает в таблице соответствия, но не описывает интеграцию.
- **SSR/`'use client'`**: план отметил как риск в Phase 2, но не дал решения. Директивы `'use client'` в `.d.ts` не влияют на Kotlin-биндинг (это runtime-директивы в `.js`), но при генерации из исходников важно снимать типы, а не рантайм.
- **Совместимость React**: Base UI требует `^17||^18||^19`; kotlin-wrappers `2026.6.10` идёт на актуальном React 19 — совместимо, но нужно зафиксировать явно (в плане только «проверить peer-зависимости»).

**4. Терминологическое противоречие плана.** Документ называется «миграцией», но по сути это **новый биндинг с сосуществованием** — план сам это признаёт в TL;DR («новый generator target, а не bump версии»). Рекомендуется переименовать документ в «Новый target Base UI + вывод `@mui/base` из эксплуатации», чтобы не путать реализатора.

---

## (в) Пробелы, которые нужно закрыть до передачи в реализацию

1. **Верификация `buildSrc` (блокирующий).** До старта разработчик ОБЯЗАН открыть реальные файлы `buildSrc/src/main/kotlin/...`, зафиксировать настоящие имена файлов, entry-point, функций `generate*` и конфигурационных констант, а также проверить `buildSrc/build.gradle.kts` на зависимость от Karakum и её версию. Все имена в плане — гипотезы.
2. **Стратегия тестирования/верификации** сгенерированных деклараций против TS: нет ни слова про сверку формы API, про `playground-ts` как эталон (он есть, но не подключён — надо решить, подключать ли).
3. **Автоматический diff при обновлении версий Base UI** (Renovate/Dependabot + пересборка + сравнение сгенерированного кода) — отсутствует.
4. **Enum-стратегия** для строковых литералов (side/align/orientation/direction/transition-status).
5. **Toast императивный API** (`createToastManager`/`useToastManager`) и **формы** (Form/Field/валидация) — отдельные под-планы.
6. **Публикация артефакта**: схема версионирования (kotlin-wrappers использует `<wrappers-date>-<npm-version>-<build>`), обновление CI-workflow «declarations», критерии code review, оценка трудозатрат по фазам — не проработаны.
7. **Потребители `kotlin-mui-base`**: план не выяснил, есть ли они вообще. Нужно проверить downstream (kotlin-mui-showcase и др.) — без потребителей фаза «вывода из эксплуатации» тривиальна.
8. **Что рекомендовать взамен непереносимым компонентам**: `Badge`, `ClickAwayListener`, `FocusTrap`, `NoSsr`, `TablePagination`, `TextareaAutosize`, `Unstable_NumberInput`/`useNumberInput` не имеют прямых аналогов. План перечисляет их как «нет аналога», но не даёт migration-notes (напр. `NumberField` вместо `Unstable_NumberInput`; FocusTrap/ClickAwayListener встроены в popups; TablePagination/TextareaAutosize/Badge — искать в mui-material или оставлять на `@mui/base`).

---

## (г) Исправленная, готовая к реализации версия плана

# План работ: новый target Base UI (`@base-ui/react`) + вывод `@mui/base` из эксплуатации в karakum-team/mui-kotlin

## TL;DR
- Это **новый generator target**, а не bump версии. Цель — новый выходной npm-таргет **`@base-ui/react` (стабильная 1.6.0, опубликована 18 июня 2026, `latest` на 31.07.2026)**, при сохранении старого `@mui/base@5.0.0-beta.70` до достижения паритета.
- Главная новизна — **part-based анатомия** (`Menu.Root`/`Menu.Trigger`/`Menu.Popup`) с namespace-типами `Component.Part.Props`/`.State`/`.ChangeEventDetails`/`.Actions`, проп `render` вместо `slots`/`slotProps`, data-атрибуты и CSS-переменные. В `.d.ts` есть плоские ре-экспорты (`MenuRootProps`, `MenuPositionerState`).
- Стратегия — **сосуществование**: генерировать Base UI параллельно, `mui/base/*` заморозить, в kotlin-wrappers — новый артефакт (рабочее имя `kotlin-base-ui`, финал за JetBrains).
- ⚠️ **Перед стартом обязательно верифицировать внутренности `buildSrc`** — все имена файлов/функций/констант ниже помечены как гипотезы `[?]`.

## Key Findings (исправленные)
1. **Имя и версия.** Пакет — `@base-ui/react` (переименован из `@base-ui-components/react` в v1.0.0, 11.12.2025, PR #3462 by @mnajdova). Стабильная 1.6.0 (18.06.2026). Субимпорты: `@base-ui/react/menu`, `.../use-render`, `.../csp-provider`.
2. **Заморозка `@mui/base`.** `mui-base.version=5.0.0-beta.70` (последний релиз, deprecated). npm-страница уже показывает «replaced by @base-ui/react», но `npm warn deprecated` при установке всё ещё ссылается на старое имя (issue #3598). Остальные MUI-деп. на v9 (material 9.1.2, x-* 9.7.0).
3. **Инструментарий репо (подтверждено `gradle.properties`/`settings.gradle.kts`).** `kotlin.version=2.4.0`, `kotlin.wrappers.version=2026.6.10`, `kfc.version=19.10.0`, `seskar.version=4.60.0`, `kotlin.js.yarn=false`. Плагины `io.github.turansky.kfc.*`, `io.github.turansky.seskar`. Модули: `mui-kotlin`, `playground`. `playground-ts` присутствует в дереве, но НЕ в `settings.gradle.kts`. CI-workflow — «declarations». `[?]` Генератор предположительно в `buildSrc` и опирается на Karakum — **проверить**.
4. **kotlin-wrappers.** `kotlin-mui-base` существует, активен, генерируется из mui-kotlin. Обёртки `@base-ui/react` НЕТ — новый артефакт.
5. **TS-форма Base UI (подтверждено docs 1.6.0).** Namespace `Component.Part.Props/.State`, `ChangeEventDetails/ChangeEventReason`, `Actions`. Плоские ре-экспорты `MenuRootProps` и т.п. `render: ReactElement | ((props, state) => ReactElement)`. 37 компонентов в 1.6.0 (35 в v1.0 + Drawer + OTPField). React peer `^17||^18||^19`.

## Фазы (с исправлениями)

**Phase 0 — Верификация репозитория и разведка (НОВАЯ, блокирующая).**
Прочитать реальные `buildSrc`-файлы, зафиксировать: entry-point, имена `generate*Declarations`, реальные конфиг-константы (аналоги `EXCLUDED_TYPES` и т.д.), выходную структуру пакетов, наличие/версию зависимости на Karakum, содержимое `FUTURE_IMPROVEMENTS.md`. Проверить открытые issues/PR/ветки про base-ui. Проверить downstream-потребителей `kotlin-mui-base`. **DoD:** заполнена «карта генератора» с реальными именами. **Сложность:** низкая (но обязательна).

**Phase 1 — Инвентаризация Base UI.** Снять `.d.ts`-снимок `@base-ui/react@1.6.0` (или `llms.txt`/markdown). Таблица «компонент → части → Props/State/ChangeEventDetails/Actions → data-атрибуты → CSS-переменные» для 37 компонентов + utils (useRender, mergeProps, CSPProvider, DirectionProvider). Baseline-прогон на Menu. **DoD:** таблица + baseline. **Сложность:** низкая-средняя.

**Phase 2 — Зависимости.** `base-ui.version=1.6.0` в `gradle.properties` (сохранить `mui-base.version`). Обновить `package-lock.json` и `.kotlin-locks/js`. Проверить React peer (`^17||^18||^19` совместимо с wrappers 2026.6.10). **DoD:** `@base-ui/react` разрешается, lockfile консистентен. **Риск:** конфликт React. **Сложность:** низкая.

**Phase 3 — Новый модуль генератора.** `[?]` Добавить выходной пакет `baseui/…` и функцию генерации по аналогии с существующими `generate*Declarations` (реальные имена — из Phase 0). Настроить `@JsModule`/moduleNameMapper на субимпорты `@base-ui/react/<component>`. **DoD:** end-to-end генерация ≥1 компонента. **Сложность:** средняя.

**Phase 4 — Part-based анатомия (вариант A).** Плоские `MenuRoot`/`MenuRootProps` (совпадает с ре-экспортами Base UI и обходит проблемную namespace-эмуляцию Karakum). Опционально — фасадный `external object Menu` с `@JsName`-алиасами для DX. `Root` — provider-компонент. **DoD:** все 20 частей Menu с `*Props`. **Сложность:** высокая.

**Phase 5 — `render`, `state`, className/style-функции.** Union-заглушка `ReactElement | function` + функциональный тип. `className: string | ((state)=>string)`, `style: CSSProperties | ((state)=>CSSProperties)`. `*State`-интерфейсы. **Сложность:** высокая.

**Phase 6 — Enum'ы и строковые union'ы (НОВАЯ).** side/align/`orientation`/`Direction`/`TransitionStatus`/`ChangeEventReason` → seskar-enum/sealed. **Сложность:** средняя.

**Phase 7 — data-атрибуты, CSS-переменные, ARIA.** Как в исходном плане (константы через dashed `@JsName`, переиспользование ARIA-машинерии). **Сложность:** низкая-средняя.

**Phase 8 — Хуки/утилиты/провайдеры.** `useRender` (+ `ComponentProps<Tag,State>`, `ElementProps<Tag>`, `Parameters`, `RenderProp`), `mergeProps`, `DirectionProvider`, `CSPProvider` (проп `nonce`), `useFilteredItems`. Union `defaultTagName` (~теги) → `String`/enum. **Сложность:** средняя.

**Phase 9 — Toast и формы (НОВАЯ).** Императивный Toast (`createToastManager`, `useToastManager`, `Toast.Provider toastManager`). Form/Field/Fieldset + валидация + `onFormSubmit`. Generic Select/Combobox (`multiple`). **Сложность:** высокая.

**Phase 10 — Исключения и type-only.** Аналоги `EXCLUDED_TYPES`/`KNOWN_TYPES` для Floating UI типов, `BaseUIEvent<T>`, `VirtualElement`. Type-only для `ChangeEventDetails`. **Сложность:** средняя.

**Phase 11 — Компиляция и покрытие.** Зелёный `:mui-kotlin:compileKotlinJs` со всеми компонентами. Приоритет: Menu, Select, Slider, Switch, Tabs, Tooltip, Input/NumberField, Popover. **Сложность:** высокая.

**Phase 12 — Верификация против TS (НОВАЯ).** Подключить/использовать `playground-ts` как эталон формы API; наладить diff при обновлении версий Base UI (Renovate + пересборка). **Сложность:** средняя.

**Phase 13 — Playground / примеры.** Kotlin-примеры (Menu/Dialog/Select/Slider) с `render` и частями. **Сложность:** средняя.

**Phase 14 — Сосуществование, публикация, вывод из эксплуатации.** Генерировать `baseui/*` параллельно с `mui/base/*`. Новый артефакт в kotlin-wrappers (имя — за JetBrains), обновить CI «declarations», схему версий, README/migration-notes (включая аналоги для Badge/FocusTrap/ClickAwayListener/NoSsr/TablePagination/TextareaAutosize/NumberInput→NumberField). После переезда потребителей — deprecate + удалить `mui/base`. **Сложность:** низкая (организационная).

### Таблица соответствия (исправления)
- `Input / useInput` → Input; Field.*; **NumberField** (для числового ввода).
- `useAutocomplete` → Autocomplete.* / Combobox.* / **useFilteredItems**.
- **Добавить строку:** `Unstable_NumberInput / useNumberInput` (`@mui/base`) → **NumberField** (хук `useNumberInput`/`getIncrementButtonProps` удалён, прямого аналога нет — issue #1814).
- Непереносимые (`Badge`, `ClickAwayListener`, `FocusTrap`, `NoSsr`, `TablePagination`, `TextareaAutosize`): в migration-notes указать — FocusTrap/ClickAwayListener встроены в popups; остальные искать в mui-material или оставить на замороженном `@mui/base`.

## Recommendations
1. **Сначала Phase 0** (верификация `buildSrc`) — без неё все технические фазы стоят на непроверенных именах.
2. **Вертикальный срез на Menu** (Phases 1–7 на одном компоненте) — 20 частей покрывают все ключевые проблемы трансляции.
3. **Вариант A (плоские имена)** — обоснован поведением Karakum (namespace-эмуляция даёт громоздкий код).
4. **Сосуществование, не миграция.**
5. **Порядок покрытия:** сперва компоненты с потребителями `@mui/base` (Menu, Select, Slider, Switch, Tabs, Tooltip, NumberField), затем новые.

**Пороги, меняющие план:** конфликты плоских имён → вариант B; провал Karakum на discriminated unions → ручные type-only заглушки; уход Base UI в breaking changes → фиксировать `base-ui.version`.

## Caveats
- **Внутренности `buildSrc` НЕ прочитаны** (GitHub блокирует автодоступ к blob/tree). Все имена файлов/функций/констант — гипотезы, подлежащие проверке в Phase 0.
- Число компонентов: 37 в 1.6.0 (35 в v1.0 + Drawer + OTPField); Drawer стабилен с v1.3.0, OTPField — с v1.6.0.
- Дата 1.6.0: docs — 17.06.2026, npm — 18.06.2026 (расхождение 1 день).
- npm-страница `@mui/base` уже показывает корректное «replaced by @base-ui/react», но `npm warn deprecated` при установке — устарел.
- Имя артефакта kotlin-wrappers — предложение, не факт.

---

## (д) Открытые вопросы для владельца репозитория до старта

1. **Как реально устроен `buildSrc`?** Настоящие имена файлов, entry-point, `generate*`-функций, конфиг-констант; используется ли внешний Karakum (и версия) или самописный конвертер. Без этого нельзя писать код.
2. **Есть ли уже что-то по base-ui?** Проверить `FUTURE_IMPROVEMENTS.md`, `MUI_V9_TODO.md`, открытые issues/PR/ветки и позицию мейнтейнера (turansky) — возможно, работа начата или есть архитектурное решение.
3. **Кто потребители `kotlin-mui-base`?** Есть ли они вообще (kotlin-mui-showcase и др.)? От этого зависит объём фазы вывода из эксплуатации.
4. **Подключать ли `playground-ts`** к сборке как эталон формы API?
5. **Финальное имя артефакта** в kotlin-wrappers и схема версионирования (согласовать с JetBrains).
6. **Целевая версия Base UI**: фиксировать 1.6.0 или отслеживать `latest`? Учитывая частые breaking changes v1.x — рекомендуется фиксация + контролируемое обновление с автоматическим diff.
7. **Политика для непереносимых компонентов** (`Badge`/`FocusTrap`/`ClickAwayListener`/`NoSsr`/`TablePagination`/`TextareaAutosize`/`NumberInput`): что рекомендовать потребителям и оставлять ли для них замороженный `mui/base`.
8. **Toast и формы**: подтвердить приоритет и объём (императивный API Toast и валидация Form/Field выходят за рамки простого part-биндинга).