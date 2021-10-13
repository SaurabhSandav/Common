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

dependencyResolutionManagement {

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "common"

listOf(
    "TYPESAFE_PROJECT_ACCESSORS",
    "VERSION_CATALOGS",
).forEach { enableFeaturePreview(it) }

include(
    ":core",
    ":compose",
)

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.onEach { childProject ->
        childProject.buildFileName = "${childProject.name}.gradle.kts"
        setBuildFileName(childProject)
    }
}
