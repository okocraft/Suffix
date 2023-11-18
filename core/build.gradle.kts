plugins {
    id("suffix.common-conventions")
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.14.0")
    compileOnly("net.kyori:adventure-text-serializer-plain:4.14.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.14.0")
    implementation("com.github.siroshun09.translationloader:translationloader:2.0.2")
}
