plugins {
    alias(libs.plugins.kfc.application)
}

dependencies {
    jsMainImplementation(libs.wrappers.emotion)
    jsMainImplementation(libs.wrappers.react)
    jsMainImplementation(libs.wrappers.reactDom)

    jsMainImplementation(project(":mui-kotlin"))

    jsMainImplementation(npm("@emotion/react", "11.9.0"))
    jsMainImplementation(npm("@emotion/styled", "11.8.1"))
}

tasks.patchWebpackConfig {
    patch(file("mui-patch.js"))
}
