// FIXME IDE warns "`libs` can't be called in this context by implicit receiver"
// FIXME https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.dokka)
    id("maven-publish")
}

group = "com.saurabhsandav.common"
version = "0.2.2"

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
                implementation(libs.jetpack.activity.compose)
                implementation(libs.jetpack.lifecycle.viewmodel.compose)

                // Accompanist
                api(libs.accompanist.insets)

                // Bundlizer
                implementation(libs.bundlizer)
            }
        }
    }
}

android {

    compileSdk = 32

    defaultConfig {
        minSdk = 28
        targetSdk = 32
    }

    namespace = "com.saurabhsandav.common.compose"
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
