@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("plugins.app-build-config")
    id("plugins.library-build-config")
}

val runningTestCoverage = if (project.hasProperty("coverage")) {
    project.property("coverage") == "true"
} else {
    false
}

dependencies {
    implementation(project(":middleware"))
    implementation(libs.koin)
    implementation(libs.koin.scope)
}
