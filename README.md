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
The target is pinned to **1.6.0**. Six modules are generated in the `baseui` package and have
playground samples: **Menu, Slider, Field, Accordion, NumberField, and Toast**. Their type coverage
is still incomplete; the remaining modules and generator limitations are tracked in
[BASE_UI_TODO.md](BASE_UI_TODO.md), the current migration status and handoff document.
