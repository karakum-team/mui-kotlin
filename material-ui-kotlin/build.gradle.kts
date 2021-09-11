plugins {
    id("com.github.turansky.kfc.library")
    `material-declarations`
}

val materialVersion = property("material.version") as String
val kotlinWrappersVersion = "0.0.1-pre.243-kotlin-1.5.30"

dependencies {
    implementation(npm("@mui/material", materialVersion))

    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
