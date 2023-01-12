plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

group = "net.okocraft"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.okocraft:suffix-bukkit:1.0.0")
    implementation("net.okocraft:suffix-bungeecord:1.0.0")
}

tasks {
    compileJava {
        options.release.set(8)
    }
    build {
        dependsOn(shadowJar)
    }
}
