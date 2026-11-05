# Kotlin Type Selection

*   **Prefer concrete standard Kotlin types whenever the API semantics allow it.** For TypeScript `number`, normally
    choose `Int` for integral values (counts, indices) and `Double` for values that may be fractional (pixel sizes,
    opacity, ratios). Preserve optionality with `Int?` / `Double?`. For example, typography's `fontSize` and
    `htmlFontSize` use `Double` / `Double?` because fractional pixel sizes are valid.
*   Keep `Number` only when accepting different Kotlin numeric types is intentional and a concrete type would
    incorrectly restrict the API. Upstream's use of TypeScript `number` alone is not a reason to emit `Number`.
*   Apply this check whenever adding or changing declarations. Implement the choice in the generator (reuse
    `NUMBER_AS_INT_PROPERTIES` / `NUMBER_AS_DOUBLE_PROPERTIES` in `KotlinType.kt` where appropriate), and keep related
    model and options types consistent. Do not blanket-convert every TypeScript `number` without checking its meaning.
*   **Annotate plain-object interfaces with `@JsPlainObject` and declare their properties as `val`.**
    *   Plain data, options, owner-state (`*OwnerState`), slots (`*Slots`), classes (`*Classes`), status, parameter, and
        hook return-value interfaces that do not inherit from `react.Props` (or DOM/event types) and do not declare
        methods should carry `@kotlinx.js.JsPlainObject`. Keep every `@JsPlainObject` hierarchy closed: all supertypes
        and subtypes in the family must also be annotated with `@JsPlainObject`.
    *   **Only `val` properties are allowed inside `@JsPlainObject` interfaces — never `var`.** Pass
        `readOnly = isPlainObject` in `MemberConverter.kt` / `Converter.kt` / `Overrides.kt`, and use `val` in all
        hand-written `@JsPlainObject` templates in `Generator.kt`. Construct `@JsPlainObject` instances via their
        generated factory (`Type { ... }`) rather than `unsafeJso`.
    *   **Do not put `@JsPlainObject` on interfaces whose instances are mutated in place at runtime.** When upstream
        passes an existing instance (such as a `SyntheticEvent` augmented with `TreeViewCancellableEvent`) into a
        callback and checks a mutated property on that same reference (`event.defaultMuiPrevented = true`), keep `var`
        and omit `@JsPlainObject`, because JSO `copy` allocates a new object instead of mutating the original instance.
*   **Mark union interfaces with `sealed /* union */` before `external interface`.** Every TypeScript union converted to
    a sealed interface — whether via `UnionConverter.kt` (`convertUnion`, `convertSealed`), `Spacing.kt`, or
    hand-written templates in `Generator.kt` — must emit `sealed /* union */` on the line before `external interface` to
    match `kotlin-wrappers` conventions. Callable function interfaces (`sealed external interface` with
    `operator fun invoke`) are not unions and must not carry `/* union */`.
