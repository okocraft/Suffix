plugins {
    id("suffix.platform-conventions")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/public/")
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.8-SNAPSHOT")
    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.2")
}

tasks {
    processResources {
        filesMatching(listOf("bungee.yml")) {
            expand("projectVersion" to version)
        }
    }
    shadowJar {
        relocate("net.kyori", "net.okocraft.suffix.libs")
    }
}
