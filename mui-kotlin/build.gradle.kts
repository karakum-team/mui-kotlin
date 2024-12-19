plugins {
    alias(libs.plugins.kfc.library)
    alias(libs.plugins.seskar)
    `mui-declarations`
}

val seskarVersion = property("seskar.version") as String

dependencies {
    jsMainImplementation(npmv("@mui/material"))
    jsMainImplementation(npmv("@mui/base"))
    jsMainImplementation(npmv("@mui/system"))
    jsMainImplementation(npmv("@mui/icons-material"))
    jsMainImplementation(npmv("@mui/lab"))
    jsMainImplementation(npmv("@mui/x-tree-view"))
    jsMainImplementation(npmv("@mui/x-date-pickers"))

    jsMainImplementation("io.github.turansky.seskar:seskar-core:$seskarVersion")

    jsMainApi(kotlinWrappers.reactDom)
    jsMainApi(kotlinWrappers.reactPopper)
}
