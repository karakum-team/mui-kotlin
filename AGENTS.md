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

## Registry of Specialized Subagents

This repository contains specialized instruction files for specific agent roles, stored under the unified `agents/` directory:

1.  **`agents/mui-code-review.md` (Code Review):**
    A comprehensive manual for performing semantic and structural code reviews on generator changes, generated output, and playground samples. Invoke this prompt when asked to review changes, check version bumps, or audit modifications before commit.

---

## Integration with Agentic Tools

To ensure various tools load these guidelines automatically:
*   **Claude CLI (Claude Code):** Defer to this `AGENTS.md` and standard commands defined herein via `CLAUDE.md`.
*   **Gemini CLI:** Follows the global preferences and reads `GEMINI.md` which references `AGENTS.md`.
*   **Cursor / Windsurf:** Refer to this manual in `.cursorrules` or `.windsurfrules`.
