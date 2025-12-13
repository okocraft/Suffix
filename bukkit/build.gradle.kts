plugins {
    alias(libs.plugins.bundler)
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") {
        exclude("net.md-5", "bungeecord-chat")
    }
    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.2")
    implementation(projects.suffixCore)
}

tasks {
    shadowJar {
        relocate("net.kyori", "net.okocraft.suffix.libs")
    }
}

bundler {
    copyToRootBuildDirectory("Suffix-Bukkit-${project.version}")
    replacePluginVersionForBukkit(project.version)
}
