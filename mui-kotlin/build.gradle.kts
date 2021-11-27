plugins {
    id("com.github.turansky.kfc.library")
    `mui-declarations`
}

val kotlinWrappersVersion = property("kotlin-wrappers.version") as String

dependencies {
    implementation(npmv("@mui/material"))
    implementation(npmv("@mui/icons-material"))
    implementation(npmv("@mui/lab"))

    api(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    api("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
