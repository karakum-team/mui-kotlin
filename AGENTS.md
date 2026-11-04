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

## Task-Specific Rules

Before starting work, read and apply every rule whose condition matches the task.

| When to read | File |
|---|---|
| Adding or changing generated declarations | [Kotlin type selection](.agents/rules/kotlin-types.md) |
| Preparing a commit message or a ready-to-use commit summary | [Commit messages](.agents/rules/commit-messages.md) |
| Changing generator code, generated declarations or playground code, or running browser verification | [Generator verification](.agents/rules/generator-verification.md) |
| Transferring generated declarations to a fork or preparing upstream PRs | [kotlin-wrappers transfer](.agents/rules/kotlin-wrappers-transfer.md) |

---

## Commit & Git Restrictions (Human Verification Required)

To ensure quality, prevent regressions, and enforce human-in-the-loop verification, all AI agents must adhere to the
following git workflow:

* **Rule #3:** **NEVER commit changes on your own initiative.** Even if instructed, if the task is complex or should be
  divided into multiple logical steps/commits, **stop and ask a human to review and commit.**
* **Rule #4:** When a task is complete and ready for commit, prepare a detailed summary of your changes, compile the
  project successfully, and request the human to verify and perform the commit (s). This allows human supervision over
  all generated and modified files before they are finalized.
* For `kotlin-wrappers` transfers: **never push**; the human pushes and opens the PRs.

---

## Definition of Done (DoD)

Before finalizing any task, ensure:

1. Both the library and playground compile with **0 errors**.
2. Generated declarations match a clean generation byte-for-byte.
3. Generator changes affecting types, exposing components or changing inheritance pass real-browser verification
   at the affected sample route and `#/all`. New components and type un-widening must have registered samples.

---

## Registry of Specialized Subagents

Invoke a separate `mui-code-review` subagent using the
[code review manual](.agents/subagents/mui-code-review.md):

* When the user requests a code review, a version bump check, or an audit before commit.
* Before handing off changes to generator code, generated declarations, playground code or dependency versions
  for human verification and commit.

Provide the manual as the subagent's instructions and specify the diff, commit range or file list to review.
The reviewer works read-only and must not edit files or run Gradle tasks. The parent agent handles findings and
required verification within the scope of the user's task.
