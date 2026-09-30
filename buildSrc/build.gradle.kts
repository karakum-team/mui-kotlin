plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(localGroovy())
    testImplementation(kotlin("test"))
}
