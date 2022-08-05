// FIXME IDE warns "`libs` can't be called in this context by implicit receiver"
// FIXME https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.dokka)
    id("maven-publish")
}

group = "com.saurabhsandav.common"
version = "0.2.1"

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
        val sourceSetsToDisable = listOf(
            "androidAndroidTestRelease",
            "androidTestFixtures",
            "androidTestFixturesDebug",
            "androidTestFixturesRelease",
        )
        removeAll { it.name in sourceSetsToDisable }

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

                // UUID
                implementation(libs.uuid)

                // Kermit
                implementation(libs.kermit)
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
                implementation(libs.jetpack.activity.compose)

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

    namespace = "com.saurabhsandav.common.core"
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val dokkaHtmlJar by tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-doc")
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

    publications {
        withType<MavenPublication> {
            artifact(dokkaJavadocJar)
            artifact(dokkaHtmlJar)
        }
    }
}
