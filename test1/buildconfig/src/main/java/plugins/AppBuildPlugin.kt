package plugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.kotlinOptions
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

class AppBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension> {
                namespace = Version.namespace
                compileSdk = Version.compileSdk

                defaultConfig {
                    applicationId = Version.applicationId
                    minSdk = Version.minSdk
                    targetSdk = Version.targetSdk
                    versionCode = Version.versionCode
                    versionName = Version.versionName

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = Version.jdk
                    targetCompatibility = Version.jdk
                }


                kotlinOptions {
                    jvmTarget = Version.jdk.toString()
                }

                val libs = the<LibrariesForLibs>()

                dependencies {
                    "implementation"(libs.androidx.core.ktx)
                    "implementation"(libs.appcompat)
                    "implementation"(libs.material)
                    "implementation"(libs.constraintlayout)
                    "testImplementation"(libs.junit)
                    "androidTestImplementation"(libs.ext.junit)
                    "androidTestImplementation"(libs.espresso.core)
                }
            }
        }
    }
}
