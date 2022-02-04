plugins {
    id("io.github.turansky.kfc.library")
    `mui-icons-declarations`
}

dependencies {
    implementation(project(":mui-kotlin"))

    implementation(npmv("@mui/icons-material"))
}
