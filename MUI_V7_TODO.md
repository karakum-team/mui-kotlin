# MUI v6.5 → v7 bump — DONE (компиляция зелёная)

Бамп MUI core с 6.5.0 на 7.x. Этот файл — передача контекста.
**Статус: `:mui-kotlin:compileKotlinJs` И `:playground:compileKotlinJs` — ЗЕЛЁНЫЕ (0 ошибок).**
Оставалось ~20 ошибок mui-kotlin + 1 в playground (`Autocomplete.options`) — всё починено 2026-06-27.

## Текущее состояние

- **Версии** (`gradle.properties`): `mui-material/system/icons = 7.3.11`, `mui-lab = 7.0.1-beta.25`.
  `mui-base = 5.0.0-beta.70` (frozen, осознанно — см. `MUI_V6_TODO.md`), `mui-x-* = 7.28.0` (НЕ трогаем,
  отдельный шаг позже).
- **`generateDeclarations` — зелёный**, 596 `.kt`.
- **`compileKotlinJs`: 0 ошибок** (было 1050 → ~20 → 0).
- **`:playground:compileKotlinJs`: 0 ошибок.**
- **`:buildSrc:test` — NO-SOURCE** (тестов генератора нет).
- **Upstream `JetBrains/kotlin-wrappers` ещё на MUI 6.5.0** → эталона v7 для диффа НЕТ.

## ОТЛОЖЕНО — сделать в другой итерации (триггеры)

*Нет активных TODO. Оба пункта закрыты 2026-06-28.*

## Восстановление качества типов (done 2026-06-27)

После зелёной компиляции прошёл ревью диффа vs v6 — нашлись ~18 деградаций качества типов (побочки
агрессивных системных v7-фиксов: `dropMemberValueObjects`, срез ` | undefined`, обработка функц-типов).
Все починены ГЕНЕРАЛИЗОВАННО в генераторе (не точечными заменами вывода). Компиляция осталась зелёной
(mui-kotlin + playground). Группы:

- **A. `slots`/`slotProps`/`components`/`componentsProps`/`classes` → `Any?`** (массово). `dropMemberValueObjects`
  съедал инлайн-объекты до KotlinType. Фикс: исключить эти имена из коллапса (`STRUCTURED_MEMBER_NAMES` в
  `Adapter.kt`); KotlinView line-395 `{`-fallback пропускает их (+ `*Props`-имена → `react.Props`);
  `componentInterface`/`getClassesContent` теперь вырезают JSDoc (`stripInlineDocs`).
- **B. Лишние скобки `(((…)->…))?`** — `unwrapRedundantParens` после среза ` | undefined` (`KotlinType.kt`).
- **C. Именованные юнионы** — в `findUnionSource` срез ` | undefined` перенесён ПЕРЕД `removeSurrounding`
  (чинит `Stack.direction`→`StackDirection`); `variant`-чек принимает v7 `TypographyVariant | 'inherit'`;
  убран двойной рейнейм `Variant`→`TypographyVariant` в `Generator.kt` (давал `TypographyTypographyVariant`).
- **D. `Avatar.imgProps`** — `unwrapRedundantParens` снимает обрамляющие скобки перед STANDARD_TYPE_MAP.
- **E. `Record<…>`/mapped-types** — `dropMemberValueObjects` пропускает `{ [key in X]: Y }` (мапленные типы);
  `createBreakpoints.values`→`Record<Breakpoint, Number>`.
- **F. `string | string[]`** → `ReadonlyArray<String>` (`FunctionType.kt`; `createTransitions.create`).
- **G. Родители/форма компонента:** `ListItemText` (срез generic-баундов, `adaptListItemText`);
  `PropsWithComponent` восстановлен для ВСЕХ ButtonBase-компонентов (Button/IconButton/MenuItem/… — добавлен
  `: ExtendButtonBase<` в `hasComponent`); `Chip` теперь extends `ChipSlotsAndSlotProps` (вплетение
  SlotsAndSlotProps из TypeMap-интерсекции в `findMapProps`); `FC` вместо `ComponentType` для
  JSXElementConstructor-компонентов (SwipeableDrawer/SwitchBase/TablePaginationActions); `Modal.onClose`
  bivarianceHack→колбэк (`adaptBivarianceHack`); `Dialog.TransitionComponent`/`RichTreeView.item` →
  `ComponentType<…>` (обобщён хендлер `JSXElementConstructor`/`ComponentType` в `KotlinType.kt`).
- **NB:** `mui/material/Container.kt` НЕ менялся (идентичен v6) — деградировал `mui/system/ContainerProps.kt`
  (TypeMap с инлайн-пропсами; защита `props: AdditionalProps & {` в `dropInlineIntersections`, чинит и
  Stack/Grid system).

## Рабочий цикл (как итерировать)

```bash
# регенерация после правок генератора:
./gradlew :mui-kotlin:clean :mui-kotlin:generateDeclarations --console=plain
# компиляция со сбором ошибок (ВАЖНО --rerun-tasks, иначе UP-TO-DATE покажет 0):
./gradlew :mui-kotlin:compileKotlinJs --rerun-tasks --console=plain 2>&1 | grep -E "^e: " > /tmp/err.txt
wc -l < /tmp/err.txt
sed -E 's|^e: file://[^ ]*/kotlin/||' /tmp/err.txt          # читаемый список
sed -E 's|.*/kotlin/||; s/:[0-9]+:[0-9]+ .*//' /tmp/err.txt | sort | uniq -c | sort -rn  # по файлам
```

Генератор: `buildSrc/src/main/kotlin/karakum/mui/`. node_modules: `build/js/node_modules/@mui/<pkg>` (v7 .d.ts тут).
**Сброс при проблемах с версиями**:
`rm -rf build/js .kotlin-locks/js/package-lock.json && ./gradlew kotlinUpgradePackageLock`
(НЕ только `node_modules` — вложенные `build/js/packages/*/node_modules` хранят старьё).

## Уже сделанные системные фиксы (НЕ откатывать)

Изменены 8 файлов (`git diff --stat gradle.properties buildSrc/`):

1. **`KotlinType.kt`** — в начале `kotlinType()` срез хвостового ` | undefined` (рекурсивно). v7 добавил
   его на ВСЕ опциональные члены; nullability и так из `?:` (`MemberConverter.kt:92`). Убрало ~580 ошибок.
   Также: правило «тип начинается с `{`» → `Any? /* … */` (инлайн-объект как тип члена); фикс `palette`
   `Record<…> | undefined` (срез undefined перед `STANDARD_TYPE_MAP`).
2. **`Adapter.kt`** — `dropInlineIntersections()` (drop ` & { … }` балансно) + `dropMemberValueObjects()`
   (comment-aware drop `name?: { … }` → `any`). Вызовы в `adaptRawContent`.
3. **`FunctionType.kt`** — guard «не функция без `=>` → null» (чтобы `(A & B)`/`(A|B)` шли в Any?-fallback);
   `collapseInlineObjects()` (балансно `{…}` → `Any` ВНУТРИ функц-типов, в КОНЦЕ цепочки — после спец-replace
   `<{}>`/`{matches}`); cleanup `Partial<Any>`→Any, `?: Any`→`: Any?`, `String | ReadonlyArray<String>`→Any.
4. **`Converter.kt`** — `React.JSXElementConstructor<` → `React.ComponentType<` (v7 internal/SwitchBase);
   guard в `findComponent`: если извлечённый «коммент» не оканчивается на `*/` — обнулить (иначе
   `substringAfterLast` сливает всё тело файла как коммент → сырой дамп).
5. **`UnionFinder.kt`** — срез ` | undefined` в `findUnionSource`; в color-хендлере строить enum только
   для чистых литералов (без бэктика — Typography template-literal уходит в OverridableStringUnion-fallback);
   `UsePaginationProps` extends через `depthAwareSplit` (v7 однострочный). NB: `!!` заменены на `error(...)`
   с диагностикой — можно оставить (лучше падать с сообщением).
6. **`ParentType.kt`** — `UsePaginationProps` extends через `depthAwareSplit`.
7. **`Override.kt`** — в `VAR_TYPE_MISMATCH_ON_OVERRIDE_FIX_REQUIRED` добавлены Accordion/Dialog/Drawer/Popover.

## Что починили в этом прогоне (2026-06-27)

Все ~20 точечных + playground. Формат: проблема — фикс.

1. **Slider generic `Value`** (Slider.kt 92/159/167/244) — новый `adapters/Slider.kt`:
   guard на `SliderOwnProps<Value extends number | readonly number[]>`, мапит `Value` →
   `number | number[]` ТОЛЬКО в Slider.d.ts (`: Value | undefined`, `value: Value`). Зарегистрирован в
   `Adapter.kt`.
2. **Autocomplete** (disabled/readOnly/onKeyDown/renderValue + playground `options`):
    - `adapters/Autocomplete.kt`: (a) срезает v7 generic-bound'ы на `AutocompleteProps<… extends boolean|undefined …>`
      → `<Value, Multiple, DisableClearable, FreeSolo>`, чтобы `findParentType` снова увидел родителей
      (`UseAutocompleteProps`/`StandardProps`) — без этого терялись `options/value/onChange/…` (это и была
      ошибка playground); (b) `AutocompleteRenderValue<…>` / `AutocompleteRenderValueGetItemProps<Multiple>` → `any`.
    - `FunctionType.kt`: добавлен маппинг `React.KeyboardEvent<HTMLDivElement>` (рядом с HTMLButtonElement).
    - `ParentType.kt`: хендлер `UseAutocompleteProps<` переписан на `depthAwareSplit` (v7 однострочный extends),
      маппит в `mui.base.UseAutocompleteProps<Value>` + `parseStandardProps`, дропает AutocompleteSlotsAndSlotProps.
    - `Overrides.kt`: `Autocomplete` → `.override("disabled"/"readOnly"/"onKeyDown")` (унаследованы из
      UseAutocompleteProps и StandardProps→HTMLAttributes→DOMAttributes).
3. **CssVarsTheme** — застаблен пустым (`adapters/CreateThemeFoundation.kt`, см. «Что НЕ доделано» выше).
4. **Badge/Popper `componentsProps`** (Badge.kt:114, Popper.kt:39) — `adapters/ComponentsAndSlots.kt`
   `cleanupDeprecatedComponentsProps()`: indexed-access `BadgeOwnProps['slotProps']` /
   `BasePopperProps['slotProps']` → `any` (генератор иначе делал несуществующий `.SlotProps`).
5. **base `InputOwnProps`** (Input.types.kt:13) — `adaptInput()` перенесён В НАЧАЛО `adaptRawContent`
   (до `dropInlineIntersections`), иначе ` & { … }`, на который опирается переписывание alias→interface,
   срезался раньше времени.
6. **TouchRipple** (TouchRipple.kt:25) — новый `adapters/TouchRipple.kt`: `type TouchRippleProps =
   StandardProps<…> & { … }` → `interface TouchRippleProps extends StandardProps<…> { … }` (тоже до
   `dropInlineIntersections`). Мёртвый хендлер в `UnionConverter.kt` удалён.
7. **Accordion `classes` clash** (Accordion.kt:28) — `adapters/Accordion.kt`: `AccordionOwnProps` →
   `extends PaperProps` (форма как у AppBar), AccordionClasses теперь *скрывает* PaperClasses в одной цепочке,
   что покрывает `VIRTUAL_MEMBER_HIDDEN` (Accordion уже в `OVERRIDE_FIX_REQUIRED`).

## После зелёной компиляции (осталось)

1. ✅ `:playground:compileKotlinJs` — зелёный (Grid→GridLegacy и пр. ренеймы не понадобились).
2. ✅ `:buildSrc:test` — NO-SOURCE.
3. Регрессия: не-v7 код сверить с upstream `JetBrains/kotlin-wrappers/kotlin-mui-*` (на 6.5) — НЕ делалось.
4. ✅ Новые v7-исключения/стабы задокументированы здесь.
5. Smoke (не запускалось): `./gradlew :playground:jsViteDev` → http://localhost:5173/.
6. ✅ `CssVarsTheme` — члены восстановлены (21 член) через точечные адаптеры в `CreateThemeFoundation.kt`
   (indexed-access→dynamic, rest-param срезан, ThemeCssVar/SupportedColorScheme/SxProps → String/Any).
   Depth-aware `findParentType` применён (2026-06-28); точечные адаптеры Autocomplete/ListItemText упрощены;
   6 внутренних mui-x родителей добавлены в `INTERNAL_REJECTED_PARENTS` (BaseDateTimePickerProps,
   BaseTimePickerProps, MobileOnlyPickerProps, PickersArrowSwitcherSlotProps, RichTreeViewPluginParameters,
   DesktopDatePickerProps, MobileDatePickerProps). Компиляция зелёная.
