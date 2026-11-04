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

## Generated Type Conventions

When adding or changing generated declarations, read and apply the
[Kotlin type selection rule](agents/rules/kotlin-types.md).

---

## Commit & Git Restrictions (Human Verification Required)

To ensure quality, prevent regressions, and enforce human-in-the-loop verification, all AI agents must adhere to the
following git workflow:

* **Rule #3:** **NEVER commit changes on your own initiative.** Even if instructed, if the task is complex or should be
  divided into multiple logical steps/commits, **stop and ask a human to review and commit.**
* **Rule #4:** When a task is complete and ready for commit, prepare a detailed summary of your changes, compile the
  project successfully, and request the human to verify and perform the commit (s). This allows human supervision over
  all generated and modified files before they are finalized.

### Commit Message Convention

Before preparing a commit message or a ready-to-use commit summary, read and apply the
[commit message rule](agents/rules/commit-messages.md).

---

## Technical & Build Workflows

Before changing generator code, generated declarations or playground code, or running browser verification,
read and apply the
[generator verification rule](agents/rules/generator-verification.md).

### Definition of Done (DoD)

Before finalizing any task, ensure:

1. Both the library and playground compile with **0 errors**.
2. Generated declarations match a clean generation byte-for-byte.
3. Generator changes affecting types, exposing components or changing inheritance pass real-browser verification
   at the affected sample route and `#/all`. New components and type un-widening must have registered samples.

---

## Publishing Generated Declarations to `kotlin-wrappers`

Before transferring generated declarations to a fork or preparing upstream PRs, read and apply the
[kotlin-wrappers transfer rule](agents/rules/kotlin-wrappers-transfer.md).
Never push; the human pushes and opens the PRs.

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
