plugins {
    alias(libs.plugins.kfc.application)
}

dependencies {
    jsMainImplementation(kotlinWrappers.emotion)
    jsMainImplementation(kotlinWrappers.react)
    jsMainImplementation(kotlinWrappers.reactDom)

    jsMainImplementation(project(":mui-kotlin"))

    jsMainImplementation(npm("@emotion/react", "11.9.0"))
    jsMainImplementation(npm("@emotion/styled", "11.8.1"))
}
