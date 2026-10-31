# AGENTS.md — AI Agent Manual for mui-kotlin

This document provides machine-readable instructions, conventions, and workflows for all AI agents (such as Gemini CLI, Claude Code, Cursor, Windsurf, Aider, and GitHub Copilot) operating in this repository. 

For the human-facing documentation, please see `README.md`.

---

## Core Project Invariant

The path `mui-kotlin/src/jsMain/kotlin/**` is **100% generated**. On every execution of `generateDeclarations`, the build deletes this directory and rewrites the entire tree.

*   **Rule #1:** **NEVER** edit files inside `mui-kotlin/src/jsMain/kotlin/**` directly. Any manual changes will be lost on the next build, and they mask the real issues in the generator.
*   **Rule #2:** Every fix, type adjustment, or addition belongs in the generator source code under `buildSrc/src/main/kotlin/karakum/mui/**`.
*   **Ground Truth:** The npm-installed upstream typings at `build/js/node_modules/@mui/**` represent the ground truth for any TypeScript shape being converted.

---

## Commit & Git Restrictions (Human Verification Required)

To ensure quality, prevent regressions, and enforce human-in-the-loop verification, all AI agents must adhere to the
following git workflow:

* **Rule #3:** **NEVER commit changes on your own initiative.** Even if instructed, if the task is complex or should be
  divided into multiple logical steps/commits, **stop and ask a human to review and commit.**
* **Rule #4:** When a task is complete and ready for commit, prepare a detailed summary of your changes, compile the
  project successfully, and request the human to verify and perform the commit (s). This allows human supervision over
  all generated and modified files before they are finalized.

---

## Technical & Build Workflows

### 1. Generating & Formatting Declarations
*   **Generate only:**
    ```bash
    ./gradlew :mui-kotlin:generateDeclarations
    ```
    *(Note: Running generate in isolation leaves the tree unformatted; always complete the work with `build` or format).*
*   **Format and Verify:**
    ```bash
    ./gradlew :mui-kotlin:build
    ```

### 2. Live Stand / Playground
The `playground/` directory is the verification stand. Any generator changes that alter types, expose new components, or change inheritance should be verified by running the playground in a real browser.
*   **Running Vite Dev Server (Non-continuous):**
    ```bash
    ./gradlew :playground:jsViteDev
    ```
    *(Then navigate to `http://localhost:5173` using a browser or Chrome DevTools MCP tools).*
*   **Compile / Bundle Rebuild Sync:**
    ```bash
    ./gradlew :playground:jsDevelopmentExecutableCompileSync
    ```
    *(Note: `jsViteDev` doesn't rebuild Kotlin on its own. Recompile and restart the Vite task to pick up changes).*

### 3. Definition of Done (DoD)
Before finalizing any task, ensure:
1.  `./gradlew :mui-kotlin:compileKotlinJs :playground:compileKotlinJs` compiles with **0 errors**.
2.  All changes under `mui-kotlin/src/jsMain/kotlin` match the generator output byte-for-byte in an empty directory. Verify with:
    ```bash
    ./gradlew :mui-kotlin:clean build
    git diff --exit-code -- mui-kotlin/src/jsMain/kotlin
    ```
3.  New components or type un-widening must be integrated into a playground sample inside `playground/src/jsMain/kotlin/` and rendered in the browser.

---

## Publishing Generated Declarations to `kotlin-wrappers`

The generated tree is published by copying it into a fork of
[`JetBrains/kotlin-wrappers`](https://github.com/JetBrains/kotlin-wrappers) and opening PRs upstream. A local clone of
the fork is referred to as `FORK` below; its `master` tracks upstream `master` and must stay untouched — all work
happens on topic branches.

### Package → module map

Every `mui-kotlin/src/jsMain/kotlin/<pkg>` maps 1:1 onto `FORK/<module>/src/jsMain/generated/<pkg>` (same file set):

| Generated package             | Fork module                                                     |
|-------------------------------|-----------------------------------------------------------------|
| `mui/base`                    | `kotlin-mui-base`                                               |
| `mui/material`                | `kotlin-mui-material`                                           |
| `mui/system`, `mui/types`     | `kotlin-mui-system`                                             |
| `mui/icons/material`          | `kotlin-mui-icons-material`                                     |
| `mui/lab`                     | `kotlin-mui-lab`                                                |
| `muix/pickers`, `dateio/core` | `kotlin-muix-date-pickers`                                      |
| `muix/tree/view`              | `kotlin-muix-tree-view`                                         |
| `baseui`                      | — no module yet; **never transfer** (WIP Base UI migration)     |

Hand-written fork sources are out of scope and must not be touched:
`kotlin-mui-material/src/jsMain/kotlin/**`, `kotlin-muix-date-pickers/src/jsMain/kotlin/**`.

### Rules

*   **Rule #5: The generator is the single source of truth.** Never hand-edit declarations in the fork. If the fork's
    version of a file is "more right" (e.g. `js.date.Date` instead of `kotlin.js.Date`, a short name plus `import`
    instead of an inline FQN), fix the generator here first (`KotlinType.kt`, `DateIO.kt`, `IMPORTED_FQNS` in
    `Generator.kt`), rebuild, and only then copy.
*   **One PR = one topic = one branch from `master` = one commit.** Version bumps, API fixes and formatting/import-only
    changes go into separate branches (past examples: ``MUI X `9.17.0` ``, ``MUI. Fix `SpeedDial.ariaLabel` and
    `useSlider.shiftStep` ``, ``MUI. Explicit imports instead of `react.*` ``). If one file belongs to two topics,
    copy it into one branch and hand-edit the other branch's hunk so both PRs merge without conflicts.
*   **Commit message = title only.** No body, no `Co-Authored-By` or any other attribution trailer.
*   **Never push.** When explicitly asked, agents create branches and local commits in the fork; the human pushes and
    opens the PRs. Rules #3–#4 still apply to this repository itself.
*   **Version bumps touch only `gradle/jspkg.versions.toml`.** `.kotlin-locks/**` is regenerated by the fork's
    "Package Lock" workflow after merge and must not be part of a PR. If a local build dirties it, run
    `git checkout -- .kotlin-locks` before committing.
*   The fork compiles with `allWarningsAsErrors = true` and MUI modules have only the `js` target (no `wasmJs`).
    `@file:Suppress(...)` blocks emitted by the generator are therefore required, not cosmetic.

### Workflow

1.  Rebuild here (`./gradlew :mui-kotlin:clean build`) and make sure `git diff -- mui-kotlin/src/jsMain/kotlin`
    contains exactly what you expect.
2.  List the differences per pair (only files belonging to the topics being transferred should show up):
    ```bash
    SRC=$PWD/mui-kotlin/src/jsMain/kotlin; FORK=/path/to/kotlin-wrappers
    for p in mui/base:kotlin-mui-base mui/material:kotlin-mui-material mui/system:kotlin-mui-system \
             mui/types:kotlin-mui-system mui/icons/material:kotlin-mui-icons-material mui/lab:kotlin-mui-lab \
             muix/pickers:kotlin-muix-date-pickers dateio/core:kotlin-muix-date-pickers muix/tree/view:kotlin-muix-tree-view; do
      diff -rq "$SRC/${p%%:*}" "$FORK/${p##*:}/src/jsMain/generated/${p%%:*}"
    done
    ```
3.  Group the files by topic. For each topic: `git checkout -b <branch> master`, `cp` the files (plus the toml bump for
    a version PR), then compile in the fork:
    ```bash
    ./gradlew :kotlin-mui-base:compileKotlinJs :kotlin-mui-material:compileKotlinJs \
              :kotlin-muix-date-pickers:compileKotlinJs :kotlin-muix-tree-view:compileKotlinJs
    ```
    `git status` must list only the intended files; commit with a title-only message.
4.  Verify the union: `git checkout -b tmp-union master`, `git merge` every topic branch (conflicts are a bug in the
    split), rerun the `diff -rq` loop (must print nothing — the fork is now byte-for-byte equal to the generator), then
    ```bash
    ./gradlew :kotlin-mui-base:build :kotlin-mui-material:build :kotlin-muix-date-pickers:build :kotlin-muix-tree-view:build
    (cd examples && ./gradlew :mui-materials:build)
    ```
    Delete `tmp-union` and return to `master`.
5.  Report branch names, commit hashes and file lists to the human; they push and open the PRs.

---

## Registry of Specialized Subagents

This repository contains specialized instruction files for specific agent roles, stored under the unified `agents/` directory:

1.  **`agents/mui-code-review.md` (Code Review):**
    A comprehensive manual for performing semantic and structural code reviews on generator changes, generated output, and playground samples. Invoke this prompt when asked to review changes, check version bumps, or audit modifications before commit.

---

## Integration with Agentic Tools

To ensure various tools load these guidelines automatically:
*   **Claude CLI (Claude Code):** Defer to this `AGENTS.md` and standard commands defined herein via `CLAUDE.md`.
*   **Gemini CLI:** Follows the global preferences and reads `GEMINI.md` which references `AGENTS.md`.
* **Codex:** Defer to this `AGENTS.md` and standard commands defined herein via `CODEX.md`.
*   **Cursor / Windsurf:** Refer to this manual in `.cursorrules` or `.windsurfrules`.
