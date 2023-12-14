plugins {
    id("io.github.turansky.kfc.library")
    `mui-declarations`
    id("io.github.turansky.seskar")
}

val kotlinWrappersVersion = property("kotlin-wrappers.version") as String
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

    jsMainApi(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    jsMainApi("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
    jsMainApi("org.jetbrains.kotlin-wrappers:kotlin-popper")
}
