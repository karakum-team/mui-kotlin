rootProject.name = "mui-kotlin"

pluginManagement {
    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        kotlin("multiplatform") version kotlinVersion

        val kfcVersion = extra["kfc.version"] as String
        id("io.github.turansky.kfc.library") version kfcVersion
        id("io.github.turansky.kfc.application") version kfcVersion

        val seskarVersion = extra["seskar.version"] as String
        id("io.github.turansky.seskar") version seskarVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("mui-kotlin")
include("mui-icons-kotlin")
include("muix-data-grid-kotlin")
include("playground")
