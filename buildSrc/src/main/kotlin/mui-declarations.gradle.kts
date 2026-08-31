import karakum.gradle.IdeaFormatTask
import karakum.gradle.findIdeaFormatScript
import karakum.mui.generateKotlinDeclarations

// `idea.home` from any Gradle property source (`-P`, this project's `gradle.properties`, or
// `~/.gradle/gradle.properties`), then `$IDEA_HOME`. Absent means the usual locations are scanned. Read as
// a Provider rather than eagerly, so that a build which never touches the generated tree also never asks
// where IntelliJ IDEA is.
val ideaHomeSetting = providers.gradleProperty("idea.home")
    .orElse(providers.environmentVariable("IDEA_HOME"))

// `-Pdeclarations.skip=true` compiles the committed tree instead of regenerating it, for the one caller
// that cannot do otherwise: CI. The formatter is IntelliJ IDEA's own, and there is no IntelliJ IDEA a
// Linux runner can install that matches the one used here — JetBrains' newest Community release is 2025.3
// (build 253) against the 262 that produced this tree, and a formatter two majors apart would rewrite the
// tree and fail the very check it was meant to serve.
//
// So CI verifies that the committed declarations compile, and no longer that they are up to date; that
// guarantee is now a local one, documented in BASE_UI_TODO.md. Deliberately an explicit opt-in rather than
// "skip when no IDEA is found": a silent skip would let a developer with no IDEA build against a stale
// tree and never learn of it.
val skipDeclarations = providers.gradleProperty("declarations.skip")
    .map(String::toBoolean)
    .orElse(false)

tasks {
    named<Delete>("clean") {
        delete("src")
    }

    val generateDeclarations by registering {
        onlyIf { !skipDeclarations.get() }

        dependsOn(":kotlinNpmInstall")

        // The task declares no outputs (it must re-run on every build), so Gradle cannot infer that it
        // conflicts with `clean` deleting the very directory it writes. Without this, `./gradlew clean
        // build` may legally generate first and clean afterwards, leaving an empty source tree and a
        // failure far from its cause.
        mustRunAfter("clean")

        doFirst {
            // Resolved before `src` is deleted below. Failing once the tree has been rewritten would leave
            // 653 unformatted files in the working tree — a diff large enough to hide whatever change was
            // being made, and one that has to be reverted before anything else can be read.
            findIdeaFormatScript(ideaHomeSetting.orNull)
        }

        doLast {
            // Root of the npm tree rather than `@mui` itself: the generator already reaches outside
            // that scope for `@date-io/core`, and `@base-ui/react` is added as a target next.
            val nodeModulesDir = rootProject.layout.buildDirectory
                .dir("js/node_modules").get().asFile
            val sourceDir = projectDir.resolve("src/jsMain/kotlin")

            delete(sourceDir)

            generateKotlinDeclarations(
                nodeModulesDir = nodeModulesDir,
                sourceDir = sourceDir,
            )
        }
    }

    // `src/jsMain/kotlin` is generated output: `generateDeclarations` above deletes and rewrites the whole
    // tree on every build. The generator emits Kotlin without indentation, with `X:` instead of `X :`, and
    // with each supertype on its own line — readable output used to depend on running an IDE reformat by
    // hand, which meant the committed tree could not be reproduced from the generator and a regeneration
    // showed ~520 files of pure formatting churn. Formatting here, as part of the pipeline, removes both.
    //
    // The formatter is IntelliJ IDEA's own, run headless. It replaced ktfmt (via Spotless), which laid the
    // tree out differently enough that switching changed 582 of the 653 files — so a hand reformat dirtied
    // the tree, and the committed tree could not survive one. Chaining the two
    // cannot reconcile that: IDEA's formatter re-indents and wraps overlong lines, but never re-joins
    // lines another tool has already broken, so whichever runs last does not win. Using the IDE's own
    // formatter makes ⌥⌘L a no-op again, which is the whole point.
    //
    // One thing ktfmt did is consequently no longer done: KDoc is not reflowed to a column limit, nor are
    // its block tags reordered, so the tree keeps upstream's own wrapping. Unused imports, which ktfmt also
    // removed, are pruned by the generator instead — see `retainReferencedImports` in Generator.kt.
    val formatDeclarations by registering(IdeaFormatTask::class) {
        group = "build"
        description = "Reformats the generated Kotlin declarations with IntelliJ IDEA's own formatter."

        onlyIf { !skipDeclarations.get() }

        dependsOn(generateDeclarations)

        // Implied today by `generateDeclarations`' own `mustRunAfter("clean")` plus the dependency above,
        // and stated anyway: this task declares no outputs either, so if the two are ever decoupled the
        // ordering must not quietly disappear along with the link.
        mustRunAfter("clean")

        sourceDir.set(layout.projectDirectory.dir("src/jsMain/kotlin"))
        codeStyleFile.set(rootProject.layout.projectDirectory.file("gradle/idea-code-style.xml"))
        ideaWorkDir.set(layout.buildDirectory.dir("idea-format"))
        ideaHome.set(ideaHomeSetting)
    }

    // generateDeclarations → formatDeclarations → compileKotlinJs.
    val compileTasks = sequenceOf(
        "compileKotlinJs",
        "compileKotlinJsLegacy",
        "compileKotlinJsIr",
    ).mapNotNull(::findByName).toList()

    // `findByName` per name, because which of them exists depends on the Kotlin/JS compiler mode. But if
    // *none* did, compilation would no longer hang off the pipeline at all and the build would silently
    // compile whatever was last left in `src`. That is the failure the deliberate `named(...)` calls in
    // mui-kotlin/build.gradle.kts guarded against before this moved here; the check keeps the guarantee
    // without giving up the per-name leniency the fan-out needs.
    check(compileTasks.isNotEmpty()) {
        "None of `compileKotlinJs`, `compileKotlinJsLegacy`, `compileKotlinJsIr` exists in " +
            "${project.path} — the generate/format pipeline is not wired into compilation."
    }

    compileTasks.forEach { it.dependsOn(formatDeclarations) }
}
