# Commit Message Convention

Agents do not commit here ([Rules #3–#4](../../AGENTS.md)), but the summary they hand over must be a ready-to-use commit message in the
repository's established style:

*   **Title: one line, imperative mood, no trailing period.** Identifiers (files, symbols, annotations) go in
    backticks: ``Improve `createThemeFoundation` types``, ``Mark all class interfaces with `@JsPlainObject` ``.
*   **Scoped changes get an area prefix** followed by a colon: `Base UI: …`, `Playground: …`, `Docs: …`,
    `Infrastructure: …`. Generator changes that affect the whole MUI output carry no prefix.
*   **Version bumps use the literal property assignments as the title**, space-separated, exactly as they appear in
    `gradle.properties`: `mui-x-date-pickers.version=9.14.0 mui-x-tree-view.version=9.14.0`. Tooling and npm bumps
    name the artifact and the version in backticks: ``Gradle Wrapper `9.8.2` ``, ``"@date-io/core" `3.2.0` ``.
*   **Body only when the commit has more than one logical change.** Then a blank line and a `- ` bullet per change,
    each naming the generator file/symbol touched and the visible effect on the generated output (e.g.
    ``- Emit `@JsName("ariaLabel") var ariaLabel: String` in `SpeedDialProps` instead of commenting it out, …``).
    A single-change commit has no body. Prose bodies are reserved for explaining a non-obvious *why*.
*   **Reference an upstream issue by full URL** when the commit fixes it, in the title or a bullet:
    `… - fix https://github.com/JetBrains/kotlin-wrappers/issues/2894`.
*   **No trailers.** Never add `Co-Authored-By`, `Signed-off-by`, "Generated with …" or any other attribution line.
    PR numbers like `(#17)` are appended by GitHub on squash merge, not written by hand.
