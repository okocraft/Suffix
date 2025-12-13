enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Suffix"

sequenceOf(
        "core",
        "bukkit",
        "velocity"
).forEach {
    include("suffix-$it")
    project(":suffix-$it").projectDir = file(it)
}
