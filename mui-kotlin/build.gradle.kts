plugins {
    alias(libs.plugins.kfc.library)
    alias(libs.plugins.seskar)
    `mui-declarations`
}

val seskarVersion = property("seskar.version") as String

dependencies {
    // Build-time AST analysis of npm exports; never a runtime dependency of the bindings.
    jsMainImplementation(devNpm("@babel/parser", "7.29.8"))

    fun npmv(packageName: String) =
        npm(packageName, property(packageName.removePrefix("@").replace("/", "-") + ".version") as String)

    jsMainImplementation(npm("@date-io/core", "3.2.0"))

    jsMainImplementation(npmv("@mui/material"))
    jsMainImplementation(npmv("@mui/base"))
    jsMainImplementation(npmv("@mui/system"))
    jsMainImplementation(npmv("@mui/icons-material"))
    jsMainImplementation(npmv("@mui/lab"))
    jsMainImplementation(npmv("@mui/x-tree-view"))
    jsMainImplementation(npmv("@mui/x-date-pickers"))

    jsMainImplementation(npmv("@base-ui/react"))

    jsMainImplementation("io.github.turansky.seskar:seskar-core:$seskarVersion")

    jsMainApi(kotlinWrappers.reactDom)
    jsMainApi(kotlinWrappers.popperjs.core)
}
