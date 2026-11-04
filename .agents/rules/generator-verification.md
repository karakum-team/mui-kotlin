# Generator Verification

## 1. Generating & Formatting Declarations

*   **Generate only:**
    ```bash
    ./gradlew :mui-kotlin:generateDeclarations
    ```
    *(Note: Running generate in isolation leaves the tree unformatted; always complete the work with `build` or format).*
*   **Format and Verify:**
    ```bash
    ./gradlew :mui-kotlin:build
    ```

## 2. Live Stand / Playground

Generator changes that alter types, expose new components, or change inheritance must be verified by running the
playground in a real browser, at the affected sample route and `#/all`.

Before changing playground code or running browser verification, read and apply the
[Playground instructions](../../playground/README.md). That document defines the shell/sample boundaries,
conventions, routes, sample registration, dev-server and bundle rebuild commands, and required console/DOM assertions.
Follow its checks for mounted samples, PASS results, the settled aggregate matching `#all-summary`, and zero failing checks.

## 3. Definition of Done (DoD)

Before finalizing any task, ensure:

1.  `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs` compiles with **0 errors**.
2.  All changes under `mui-kotlin/src/jsMain/kotlin` match the generator output byte-for-byte in an empty directory. Verify with:
    ```bash
    ./gradlew :mui-kotlin:clean build
    git diff --exit-code -- mui-kotlin/src/jsMain/kotlin
    ```
3.  New components or type un-widening must be integrated into a `Sample` under `playground/src/jsMain/kotlin/playground/samples/<library>/`, registered in `shell/Registry.kt`, rendered at its `#/<library>/<sample>` route and in `#/all`.
