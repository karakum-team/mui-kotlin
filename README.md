[![CI Status](https://github.com/karakum-team/mui-kotlin/workflows/declarations/badge.svg)](https://github.com/karakum-team/mui-kotlin/actions)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

# [MUI](https://github.com/mui-org/material-ui) Kotlin

| Library            | NPM version                                              | Version                                                                                                                                                                                                |
|--------------------|----------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| MUI Material       | ![npm](https://img.shields.io/npm/v/@mui/material)       | [![Maven Central](https://img.shields.io/maven-central/v/org.jetbrains.kotlin-wrappers/kotlin-mui-material)](https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-mui)                       |
| MUI Icons          | ![npm](https://img.shields.io/npm/v/@mui/icons-material) | [![Maven Central](https://img.shields.io/maven-central/v/org.jetbrains.kotlin-wrappers/kotlin-mui-icons-material)](https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-mui-icons)           |
| MUI Lab            | ![npm](https://img.shields.io/npm/v/@mui/lab)            | [![Maven Central](https://img.shields.io/maven-central/v/org.jetbrains.kotlin-wrappers/kotlin-mui-lab)](https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-mui-lab)                       |
| MUI X Tree View    | ![npm](https://img.shields.io/npm/v/@mui/x-tree-view)    | [![Maven Central](https://img.shields.io/maven-central/v/org.jetbrains.kotlin-wrappers/kotlin-muix-tree-view)](https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-muix-tree-view) |
| MUI X Date Pickers | ![npm](https://img.shields.io/npm/v/@mui/x-date-pickers) | [![Maven Central](https://img.shields.io/maven-central/v/org.jetbrains.kotlin-wrappers/kotlin-muix-date-pickers)](https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-muix-date-pickers)                       |

## Base UI migration

Migration to `@base-ui/react` is **in progress**, alongside the frozen `@mui/base` bindings.
The target is pinned to **1.8.0**. Nine modules are generated in the `baseui` package and have
playground samples (`playground/src/jsMain/kotlin/playground/samples/baseui/`, routes `#/baseui/<sample>`): **Menu, Slider, Field, Fieldset, Switch, Checkbox, Accordion, NumberField, and Toast**. Their type coverage
is still incomplete; the remaining modules and generator limitations are tracked in
[Base UI plan](docs/plans/base-ui.md), the current migration status and handoff document. How to run and extend
the samples is described in [playground/README.md](playground/README.md).

## Plans and history

| Work area | Current plan | Completed work and verification |
|---|---|---|
| Base UI migration | [Base UI](docs/plans/base-ui.md) | [Acceptance log](docs/history/base-ui.md) |
| Generator limitations and deferred MUI work | [Generator](docs/plans/generator.md) | [MUI 6](docs/history/mui-v6.md), [MUI 7](docs/history/mui-v7.md), [MUI 9](docs/history/mui-v9.md) |
| Future work | [Future improvements](docs/plans/future-improvements.md) | Evidence linked from each plan |

Live plans begin with **Resume here**: the current state, next task, prerequisites, and verification references.
History preserves the results and decisions from the recorded revision.

## Integration with Agentic Tools

[AGENTS.md](AGENTS.md) contains the shared project instructions for AI coding agents. The repository provides
tool-specific entry points that reference it:

- **Claude CLI (Claude Code):** [CLAUDE.md](CLAUDE.md).
- **Gemini CLI:** [GEMINI.md](GEMINI.md).
- **Codex:** [CODEX.md](CODEX.md).

For Cursor or Windsurf, configure `.cursorrules` or `.windsurfrules` to reference [AGENTS.md](AGENTS.md).
