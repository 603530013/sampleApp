@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.rootcoverage)
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.dokka)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.tools.build)
    }
}

tasks.dokkaGfmMultiModule {
    outputDirectory.set(buildDir.resolve("dokka/gfm"))
}

tasks.dokkaHtmlMultiModule {
    moduleName.set("Dokka for ReUse Library Project")
    outputDirectory.set(buildDir.resolve("dokka/html"))
}