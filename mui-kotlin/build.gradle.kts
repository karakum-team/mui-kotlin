plugins {
    id("io.github.turansky.kfc.library")
    `mui-declarations`
}

val kotlinWrappersVersion = property("kotlin-wrappers.version") as String

dependencies {
    jsMainImplementation(npmv("@mui/material"))
    jsMainImplementation(npmv("@mui/icons-material"))
    jsMainImplementation(npmv("@mui/lab"))
    jsMainImplementation(npmv("@mui/x-date-pickers"))

    jsMainApi(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    jsMainApi("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
    jsMainApi("org.jetbrains.kotlin-wrappers:kotlin-popper")
}
