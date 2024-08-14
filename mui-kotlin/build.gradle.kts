plugins {
    id("io.github.turansky.kfc.library")
    id("io.github.turansky.seskar")
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

    jsMainApi(libs.wrappers.react.dom)
    jsMainApi(libs.wrappers.react.dom)
}
