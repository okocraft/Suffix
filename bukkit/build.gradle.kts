plugins {
    id("suffix.platform-conventions")
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") {
        exclude("net.md-5", "bungeecord-chat")
    }
    implementation("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.1")
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml")) {
            expand("projectVersion" to version)
        }
    }
    shadowJar {
        relocate("net.kyori", "net.okocraft.suffix.libs")
    }
}
