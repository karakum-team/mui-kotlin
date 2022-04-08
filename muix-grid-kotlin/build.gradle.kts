plugins {
    id("io.github.turansky.kfc.library")
    `muix-grid-declarations`
}

val kotlinWrappersVersion = property("kotlin-wrappers.version") as String

dependencies {
    implementation(npmv("@mui/x-data-grid"))

    api(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    api("org.jetbrains.kotlin-wrappers:kotlin-mui")
}
