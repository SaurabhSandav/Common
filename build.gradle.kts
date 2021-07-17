plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.android.library") version "4.1.1"
    kotlin("multiplatform") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    id("org.jetbrains.compose") version "0.5.0-build228"
    id("maven-publish")
}

group = "com.saurabhsandav.compose"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {

    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    android {
        publishLibraryVariants("release", "debug")
    }

    sourceSets {

        all {

            listOf(
                "androidx.compose.ui.ExperimentalComposeUiApi",
            ).forEach(languageSettings::useExperimentalAnnotation)
        }

        named("commonMain") {

            dependencies {

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

                // Kotlin Serialization
                implementation(libs.kotlinx.serialization.core)

                // Napier
                implementation(libs.napier)
            }
        }

        named("androidMain") {

            dependencies {

                // Jetpack
                implementation(libs.activity.compose)

                // Accompanist
                api(libs.accompanist.insets)

                // Bundlizer
                implementation(libs.bundlizer)
            }
        }
    }
}

android {

    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(28)
        targetSdkVersion(30)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
