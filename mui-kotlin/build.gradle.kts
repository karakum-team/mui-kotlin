plugins {
    alias(libs.plugins.kfc.library)
    alias(libs.plugins.seskar)
    `mui-declarations`
}

val seskarVersion = property("seskar.version") as String

dependencies {
    fun npmv(packageName: String) =
        npm(packageName, property(packageName.removePrefix("@").replace("/", "-") + ".version") as String)

    jsMainImplementation(npm("@date-io/core", "2.17.0"))

    jsMainImplementation(npmv("@mui/material"))
    jsMainImplementation(npmv("@mui/base"))
    jsMainImplementation(npmv("@mui/system"))
    jsMainImplementation(npmv("@mui/icons-material"))
    jsMainImplementation(npmv("@mui/lab"))
    jsMainImplementation(npmv("@mui/x-tree-view"))
    jsMainImplementation(npmv("@mui/x-date-pickers"))

    jsMainImplementation("io.github.turansky.seskar:seskar-core:$seskarVersion")

    jsMainApi(kotlinWrappers.reactDom)
    jsMainApi(kotlinWrappers.popperjs.core)
}
