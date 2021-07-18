plugins {
    id("com.github.turansky.kfc.library")
    `material-ui-declarations`
}

dependencies {
    val materialVersion = property("material-ui.version") as String
    implementation(npm("@material-ui/core", materialVersion))

    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.218-kotlin-1.5.21")
}
