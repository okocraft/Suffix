plugins {
    id("suffix.platform-conventions")
}

repositories {
    maven(uri("https://repo.papermc.io/repository/maven-public/"))
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    implementation(project(":suffix-core"))
}

tasks {
    processResources {
        filesMatching(listOf("velocity-plugin.json")) {
            expand("projectVersion" to version)
        }
    }
}
