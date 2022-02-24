import org.jetbrains.compose.compose

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
}

group = "com.saurabhsandav.lib.common.sample"
version = "1.0"

kotlin {

    android()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {

        all {

            languageSettings {

                progressiveMode = true
            }
        }

        named("commonMain") {
            dependencies {

                implementation(projects.core)
                implementation(projects.compose)

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

                // Kotlin Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // Kotlin Serialization
                implementation(libs.kotlinx.serialization.core)

                // Kermit
                implementation(libs.kermit)
            }
        }

        named("androidMain") {
            dependencies {

                // Kotlin Coroutines
                implementation(libs.kotlinx.coroutines.android)

                // Jetpack
                implementation(libs.activity.compose)
            }
        }

        named("jvmMain")
    }
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 28
        targetSdk = 31
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
