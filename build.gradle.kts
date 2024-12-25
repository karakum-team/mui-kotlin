import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jsPlainObjects) apply false
}

plugins.withType<NodeJsRootPlugin> {
    the<NpmExtension>().apply {
        override("react", "^19.0.0")
        override("react-dom", "^19.0.0")
    }
}

tasks.wrapper {
    gradleVersion = "8.11.1"
}
