plugins {
    id("com.github.turansky.kfc.library")
    `mui-declarations`
}

val muiVersion = property("mui.version") as String
val muiLabVersion = property("mui-lab.version") as String
val kotlinWrappersVersion = property("kotlin-wrappers.version") as String

dependencies {
    implementation(npm("@mui/material", muiVersion))
    implementation(npm("@mui/lab", muiLabVersion))

    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
