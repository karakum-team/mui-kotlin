plugins {
    id("io.github.turansky.kfc.dev-server")
}

devServer {
    root = "App"
}

val kotlinWrappersVersion = property("kotlin-wrappers.version") as String

dependencies {
    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")

    implementation(project(":mui-kotlin"))
    implementation(project(":mui-icons-kotlin"))

    implementation(npm("@emotion/react", "11.9.0"))
    implementation(npm("@emotion/styled", "11.8.1"))
}
