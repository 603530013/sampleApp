@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    id("plugins.app-build-config")
    id("plugins.library-build-config")
    id("kotlin-kapt")
}

val runningTestCoverage = if (project.hasProperty("coverage")) {
    project.property("coverage") == "true"
} else {
    false
}

dependencies {
    implementation(project(":common:base"))
    implementation(project(":middleware"))
    implementation(libs.koin)
    implementation(libs.koin.scope)

    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.arch.core.testing)
}

android {
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
