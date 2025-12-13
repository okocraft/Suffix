plugins {
    alias(libs.plugins.bundler)
}

jcommon {
    setupPaperRepository()
}

dependencies {
    compileOnly(libs.paper)
    implementation(projects.suffixCore)
}

bundler {
    copyToRootBuildDirectory("Suffix-Paper-${project.version}")
    replacePluginVersionForBukkit(project.version)
}
