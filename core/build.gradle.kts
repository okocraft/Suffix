plugins {
    java
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

group = "net.okocraft"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.1.0")

    implementation("net.kyori:adventure-api:4.12.0")
    implementation("net.kyori:adventure-text-serializer-plain:4.12.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.2.0")

    implementation("com.github.siroshun09.configapi:configapi-yaml:4.6.0")
    implementation("com.github.siroshun09.translationloader:translationloader:2.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    compileJava {
        options.release.set(8)
    }
    test {
        useJUnitPlatform()
    }
}
