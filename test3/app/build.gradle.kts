@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    id("plugins.app-build-config")
    id("plugins.library-build-config")
    id("kotlin-kapt")
    id("org.jetbrains.dokka")
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
tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        noAndroidSdkLink.set(true)
        noStdlibLink.set(true)
        noJdkLink.set(true)
        skipEmptyPackages.set(true)
        reportUndocumented.set(false)
        skipDeprecated.set(true)
        documentedVisibilities.set(
            setOf(
                Visibility.INTERNAL,
                Visibility.PRIVATE,
                Visibility.PROTECTED,
                Visibility.PUBLIC
            )
        )
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        noAndroidSdkLink.set(true)
        noStdlibLink.set(true)
        noJdkLink.set(true)
        skipEmptyPackages.set(true)
        reportUndocumented.set(false)
        skipDeprecated.set(true)
        documentedVisibilities.set(
            setOf(
                Visibility.INTERNAL,
                Visibility.PRIVATE,
                Visibility.PROTECTED,
                Visibility.PUBLIC
            )
        )
    }
}
