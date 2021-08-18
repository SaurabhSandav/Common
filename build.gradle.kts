plugins {
    alias(libs.plugins.gradle.versions.checker)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.jetbrains.compose)
    id("maven-publish")
}

group = "com.saurabhsandav.compose"
version = "0.0.4"

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

            languageSettings {

                progressiveMode = true

                listOf(
                    "androidx.compose.ui.ExperimentalComposeUiApi",
                    "kotlinx.coroutines.ExperimentalCoroutinesApi",
                ).forEach { useExperimentalAnnotation(it) }
            }
        }

        named("commonMain") {

            dependencies {

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

                // KotlinX Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // KotlinX Serialization
                implementation(libs.kotlinx.serialization.core)

                // Napier
                implementation(libs.napier)
            }
        }

        named("commonTest") {

            dependencies {

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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

        named("androidTest") {

            dependencies {

                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        named("jvmTest") {

            dependencies {

                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

android {

    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(28)
        targetSdkVersion(31)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
