enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Suffix"

sequenceOf(
        "core",
        "paper",
        "velocity"
).forEach {
    include("suffix-$it")
    project(":suffix-$it").projectDir = file(it)
}
