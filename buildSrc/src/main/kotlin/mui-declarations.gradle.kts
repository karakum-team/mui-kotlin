import karakum.mui.generateKotlinDeclarations

tasks {
    named<Delete>("clean") {
        delete("src")
    }

    val generateDeclarations by registering {
        dependsOn(":kotlinNpmInstall")

        doLast {
            val typesDir = rootProject.buildDir
                .resolve("js/node_modules/@mui")
            val sourceDir = projectDir.resolve("src/jsMain/kotlin")

            delete(sourceDir)

            generateKotlinDeclarations(
                typesDir = typesDir,
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
