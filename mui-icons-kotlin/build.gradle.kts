plugins {
    id("com.github.turansky.kfc.library")
}

dependencies {
    implementation(project(":mui-kotlin"))

    implementation(npmv("@mui/icons-material"))
}
