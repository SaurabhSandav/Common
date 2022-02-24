// FIXME IDE warns "`libs` can't be called in this context by implicit receiver"
// FIXME https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.jetbrains.compose)
    id("maven-publish")
}

group = "com.saurabhsandav.common"
version = "0.1.0"

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

                api(projects.core)

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

                // KotlinX Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // KotlinX Serialization
                implementation(libs.kotlinx.serialization.core)

                // Kermit
                implementation(libs.kermit)
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

    compileSdk = 31

    defaultConfig {
        minSdk = 28
        targetSdk = 31
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
