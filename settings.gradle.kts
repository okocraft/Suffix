pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "Suffix"

sequenceOf(
        "core",
        "bukkit",
        "bungeecord",
        "velocity"
).forEach {
    include("suffix-$it")
    project(":suffix-$it").projectDir = file(it)
}
