@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mobiledrivetech.external.common.base"
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

    api(libs.androidx.core.ktx)
    api(libs.appcompat)
    api(libs.material)
    api(libs.koin)
    api(libs.koin.scope)
    api(libs.koin.ext)
    api(libs.coroutines)

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