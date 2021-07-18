plugins {
    kotlin("js") apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.wrapper {
    gradleVersion = "7.1.1"
}
