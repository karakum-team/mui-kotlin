rootProject.name = "mui-kotlin"

pluginManagement {
    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        kotlin("js") version kotlinVersion

        val kfcVersion = extra["kfc.version"] as String
        id("com.github.turansky.kfc.library") version kfcVersion
        id("com.github.turansky.kfc.dev-server") version kfcVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("mui-kotlin")
include("local-server")
