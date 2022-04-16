import karakum.mui.generateKotlinDataGridDeclarations

tasks {
    named<Delete>("clean") {
        delete("src")
    }

    val generateDeclarations by registering {
        dependsOn(":kotlinNpmInstall")

        doLast {
            val typesDir = rootProject.buildDir
                .resolve("js/node_modules/@mui")
            val sourceDir = projectDir.resolve("src/main/kotlin")

            delete(sourceDir)

            generateKotlinDataGridDeclarations(
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
