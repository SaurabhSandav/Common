plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.compose)
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.saurabhsandav.lib.common.sample"

        minSdk = 28
        targetSdk = 32

        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {

    implementation(projects.sample.base)

    // Activity
    implementation(libs.jetpack.activity.ktx)
    implementation(libs.jetpack.activity.compose)

    // Compose
    implementation(compose.runtime)

    // Kermit
    implementation(libs.kermit)
}
