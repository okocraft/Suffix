plugins {
    `java-library`
    id("suffix.common-conventions")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":suffix-core"))
}

val artifactFilepath: File = rootProject.layout.buildDirectory.dir("libs").get().file("${project.name}-${project.version}.jar").asFile

tasks {
    build {
        dependsOn("copyArtifacts")
    }
    shadowJar {
        minimize()
    }
    clean {
        doLast {
            artifactFilepath.delete()
        }
    }
    task("copyArtifacts") {
        dependsOn(shadowJar)
        doLast {
            val shadowJarTask = shadowJar.get()
            shadowJarTask.archiveFile.get().asFile.copyTo(target = artifactFilepath, overwrite = true)
        }
    }
}
