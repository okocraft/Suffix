plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

group = "net.okocraft"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/public/")
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.8-SNAPSHOT")

    implementation("net.okocraft:suffix-core:1.0.0")

    implementation("org.jetbrains:annotations:23.1.0")

    implementation("net.kyori:adventure-api:4.12.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.2.0")

    implementation("com.github.siroshun09.configapi:configapi-yaml:4.6.0")
    implementation("com.github.siroshun09.translationloader:translationloader:2.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.compileJava {
    options.release.set(8)
}

tasks.processResources {
    filesMatching(listOf("bungee.yml")) {
        expand("projectVersion" to version)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    minimize()
    relocate("com.github.siroshun09", "${group}.${name.toLowerCase()}.lib")
}