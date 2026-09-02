# GEMINI.md — Project Instructions

This project uses a unified, tool-agnostic AI agent manual. To maintain project invariants, run commands, execute tasks, and understand conventions, please defer to the root `AGENTS.md` file:

👉 [**AGENTS.md**](./AGENTS.md)

---

### Core Guidelines Recap

*   **Invariant:** `mui-kotlin/src/jsMain/kotlin/**` is 100% generated. NEVER edit it directly. All changes must be done in the generator source code under `buildSrc/src/main/kotlin/karakum/mui/**`.
*   **Verification:** Verify generator changes by running `./gradlew build` and testing new API coverage on the playground with `./gradlew :playground:jsViteDev` (Vite port `5173`).
*   **Specialized Prompts:** Use the subagent instructions located under the `agents/` directory (e.g. `agents/mui-code-review.md`).
