plugins {
    alias(libs.plugins.gradle.versions.checker)
    alias(libs.plugins.kotlinx.binaryCompatibilityValidator)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.dokka)
}

allprojects {
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("org.jetbrains.compose.compiler:compiler")).apply {
                using(module("androidx.compose.compiler:compiler:1.2.1-dev-k1.7.10-27cf0868d10"))
            }
        }
    }
}

apiValidation {
    // Ignore all sample projects, since they're not part of our API.
    // Only leaf project name is valid configuration, and every project must be individually ignored.
    // See https://github.com/Kotlin/binary-compatibility-validator/issues/3
    ignoredProjects += project("sample").subprojects.map { it.name }
}
