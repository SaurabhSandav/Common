pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "compose-common"

listOf(
    "TYPESAFE_PROJECT_ACCESSORS",
    "VERSION_CATALOGS",
).forEach { enableFeaturePreview(it) }
