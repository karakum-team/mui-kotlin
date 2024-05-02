plugins {
    id("io.github.turansky.kfc.application")
}

val kotlinWrappersVersion = property("kotlin-wrappers.version") as String

dependencies {
    jsMainImplementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    jsMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react")
    jsMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")

    jsMainImplementation(project(":mui-kotlin"))
    jsMainImplementation(project(":mui-icons-kotlin"))

    jsMainImplementation(npm("@emotion/react", "11.9.0"))
    jsMainImplementation(npm("@emotion/styled", "11.8.1"))
}

tasks.patchWebpackConfig {
    patch(file("mui-patch.js"))
}
