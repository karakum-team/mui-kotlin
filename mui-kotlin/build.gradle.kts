plugins {
    alias(libs.plugins.kfc.library)
    alias(libs.plugins.seskar)
    alias(libs.plugins.spotless)
    `mui-declarations`
}

val seskarVersion = property("seskar.version") as String
val ktfmtVersion = property("ktfmt.version") as String

// `src/jsMain/kotlin` is generated output: the `mui-declarations` plugin deletes and rewrites the whole
// tree on every build. The generator emits Kotlin without indentation, with `X:` instead of `X :`, and
// with every import spelled out — readable output used to depend on running an IDE reformat by hand,
// which meant the committed tree could not be reproduced from the generator and a regeneration showed
// ~520 files of pure formatting churn. Formatting here as part of the pipeline removes both problems.
spotless {
    kotlin {
        target("src/jsMain/kotlin/**/*.kt")

        // ktfmt rather than ktlint. ktlint enforces a style *policy* and fails on generated code in ways
        // that cannot be fixed at the source: it rejects the inline `/* … */` markers that record the
        // original TypeScript type (`var side: Any? /* Side */`), and the lowercase `@JsValue` union
        // members whose names must mirror the JavaScript API — 47 unfixable violations across the tree.
        // Suppressing all of those rules still left 3551 KDoc blocks with the opening `/**` at column 0,
        // because ktlint indents a comment's continuation lines but not its first line. ktfmt has no
        // rules to satisfy: it reformats unconditionally, comments included.
        ktfmt(ktfmtVersion).kotlinlangStyle()
    }

    // Not wired into `check`, because it could never fail: Spotless marks the check task as no-work when
    // `spotlessApply` has already run in the same build, and here it always has (the pipeline below puts
    // it before compilation). The tree is kept formatted by construction instead, and the guarantee that
    // the *committed* tree matches is enforced in CI by diffing after a build — see
    // .github/workflows/declarations.yml.
    isEnforceCheck = false
}

tasks {
    // generateDeclarations → spotlessKotlinApply → compileKotlinJs.
    // `named`, not `findByName`: if either task ever disappears the build should fail loudly rather than
    // silently compile an unformatted tree.
    named("spotlessKotlin") {
        dependsOn("generateDeclarations")
    }

    named("compileKotlinJs") {
        dependsOn("spotlessKotlinApply")
    }
}

dependencies {
    fun npmv(packageName: String) =
        npm(packageName, property(packageName.removePrefix("@").replace("/", "-") + ".version") as String)

    jsMainImplementation(npm("@date-io/core", "2.17.0"))

    jsMainImplementation(npmv("@mui/material"))
    jsMainImplementation(npmv("@mui/base"))
    jsMainImplementation(npmv("@mui/system"))
    jsMainImplementation(npmv("@mui/icons-material"))
    jsMainImplementation(npmv("@mui/lab"))
    jsMainImplementation(npmv("@mui/x-tree-view"))
    jsMainImplementation(npmv("@mui/x-date-pickers"))

    jsMainImplementation(npmv("@base-ui/react"))

    jsMainImplementation("io.github.turansky.seskar:seskar-core:$seskarVersion")

    jsMainApi(kotlinWrappers.reactDom)
    jsMainApi(kotlinWrappers.popperjs.core)
}
