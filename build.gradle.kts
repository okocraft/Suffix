plugins {
    alias(libs.plugins.jcommon)
}

jcommon {
    javaVersion = JavaVersion.VERSION_17

    repositories {
        mavenCentral()
    }

    commonDependencies {
        compileOnlyApi(libs.adventure)
        compileOnlyApi(libs.adventure.text.minimessage)
        compileOnlyApi(libs.adventure.text.serializer.legacy)
        compileOnlyApi(libs.adventure.text.serializer.plain)
        compileOnlyApi(libs.luckperms)
    }
}
