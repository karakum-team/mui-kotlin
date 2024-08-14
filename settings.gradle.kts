rootProject.name = "mui-kotlin"

pluginManagement {
    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.js-plain-objects") version kotlinVersion

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

    versionCatalogs {
        create("libs") {
            val wrappersVersion = extra["kotlin.wrappers.version"]
            from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
        }
    }
}

include("mui-kotlin")
include("playground")
