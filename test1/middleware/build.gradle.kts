import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.mobiledrivetech.external.middleware"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.gson)
    implementation(libs.kotlin.reflect)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.koin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.assert.j)
    testImplementation(libs.robolectric)
    testImplementation(libs.arch.core.testing)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.android.test.rules)
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
                Visibility.PUBLIC,
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
                Visibility.PUBLIC,
            )
        )
    }
}