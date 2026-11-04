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
