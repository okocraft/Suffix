repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    `kotlin-dsl` apply true
}

dependencies {
    implementation("com.github.johnrengelman:shadow:8.1.1")
}
