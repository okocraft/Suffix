plugins {
    alias(libs.plugins.bundler)
}

jcommon {
    setupPaperRepository()
}

dependencies {
    compileOnly(libs.velocity)
    implementation(projects.suffixCore)
}

bundler {
    copyToRootBuildDirectory("Suffix-Velocity-${project.version}")
    replacePluginVersionForVelocity(project.version)
}
