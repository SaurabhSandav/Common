import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
}

dependencies {

    implementation(projects.sample.base)

    // Compose Desktop
    implementation(compose.desktop.currentOs)

    // Kermit
    implementation(libs.kermit)
}

compose.desktop {
    application {
        mainClass = "com.saurabhsandav.sample.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}
