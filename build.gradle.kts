plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.js-plain-objects")
}

tasks.wrapper {
    gradleVersion = "8.7"
}
