import karakum.mui.generateKotlinDeclarations

tasks {
    named<Delete>("clean") {
        delete("src")
    }

    val generateDeclarations by registering {
        dependsOn(":kotlinNpmInstall")

        // The task declares no outputs (it must re-run on every build), so Gradle cannot infer that it
        // conflicts with `clean` deleting the very directory it writes. Without this, `./gradlew clean
        // build` may legally generate first and clean afterwards, leaving an empty source tree and a
        // failure far from its cause.
        mustRunAfter("clean")

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

    sequenceOf(
        "compileKotlinJs",
        "compileKotlinJsLegacy",
        "compileKotlinJsIr",
    ).mapNotNull(::findByName)
        .forEach { it.dependsOn(generateDeclarations) }
}
