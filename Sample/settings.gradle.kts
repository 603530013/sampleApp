pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        includeBuild("buildconfig")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Sample"
include(
    ":app",
    ":middleware",
    ":common:base",
    ":common:fundamental",
    ":common:component",
    ":core"
)
