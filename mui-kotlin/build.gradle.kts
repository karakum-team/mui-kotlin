plugins {
    id("com.github.turansky.kfc.library")
    `mui-declarations`
}

val materialVersion = property("mui.version") as String
val kotlinWrappersVersion = "0.0.1-pre.244-kotlin-1.5.30"

dependencies {
    implementation(npm("@mui/material", materialVersion))

    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
