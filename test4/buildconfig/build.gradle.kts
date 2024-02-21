plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()

    maven("https://plugins.gradle.org/m2/")
}

dependencies {

    implementation(libs.detekt)
    implementation(libs.spotless)
    implementation(libs.coverage)

    //implementation -> com.android.build.api.dsl.CommonExtension
    implementation(libs.gradle.tools.build)
    //implementation -> org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
    implementation(libs.kotlin.gradle.plugin)
    //implementation -> org.gradle.accessors.dm.LibrariesForLibs
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        //Register plugin id for plugins.app-build-config
        create("AppBuildConfig") {
            id = "plugins.app-build-config"
            implementationClass = "plugins.AppBuildPlugin"
        }

        //Register plugin id for plugins.library-build-config
        create("LibraryBuildConfig") {
            id = "plugins.library-build-config"
            implementationClass = "plugins.LibraryBuildPlugin"
        }
    }
}

