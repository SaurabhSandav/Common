// FIXME IDE warns "`libs` can't be called in this context by implicit receiver"
// FIXME https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    id(libs.plugins.kotlin.plugin.serialization.get().pluginId)
    id("maven-publish")
}

group = "com.saurabhsandav.common"
version = "0.0.4"

kotlin {

    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    android {
        publishLibraryVariants("release")
    }

    sourceSets {

        // FIXME IDE/Gradle show warnings related to this sourceset https://youtrack.jetbrains.com/issue/KT-48436
        removeAll { it.name == "androidAndroidTestRelease" }

        all {

            languageSettings {

                progressiveMode = true
            }
        }

        named("commonMain") {

            dependencies {

                // KotlinX Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // KotlinX Serialization
                implementation(libs.kotlinx.serialization.core)
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

    compileSdk = 31

    defaultConfig {
        minSdk = 28
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

publishing {

    repositories {
        maven {
            name = "Common"
            url = uri("https://maven.pkg.github.com/saurabhsandav/Common")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
