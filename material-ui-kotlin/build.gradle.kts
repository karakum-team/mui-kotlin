plugins {
    id("com.github.turansky.kfc.library")
    `material-declarations`
}

val materialVersion = property("material-ui.version") as String
val kotlinWrappersVersion = "0.0.1-pre.237-kotlin-1.5.30"

dependencies {
    implementation(npm("@material-ui/core", materialVersion))

    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
}
