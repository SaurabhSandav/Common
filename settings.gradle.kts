pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

listOf(
    "TYPESAFE_PROJECT_ACCESSORS",
    "VERSION_CATALOGS",
).forEach { enableFeaturePreview(it) }

dependencyResolutionManagement {

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://maven.pkg.github.com/saurabhsandav/CommonVersions")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    versionCatalogs {

        create("libs") {
            from("com.saurabhsandav:common-versions:0.0.3")
        }
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
