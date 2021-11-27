plugins {
    id("com.github.turansky.kfc.dev-server")
}

devServer {
    root = "App"
}

dependencies {
    implementation(project(":mui-kotlin"))
    implementation(project(":mui-icons-kotlin"))

    implementation(npm("@emotion/react", "11.6.0"))
    implementation(npm("@emotion/styled", "11.6.0"))
}
