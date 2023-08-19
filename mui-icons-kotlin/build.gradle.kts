plugins {
    id("io.github.turansky.kfc.library")
    `mui-icons-declarations`
}

dependencies {
    jsMainImplementation(project(":mui-kotlin"))

    jsMainImplementation(npmv("@mui/material"))
    jsMainImplementation(npmv("@mui/icons-material"))
}
