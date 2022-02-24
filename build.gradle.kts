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
}
