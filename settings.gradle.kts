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
).forEach { enableFeaturePreview(it) }

@Suppress("PropertyName")
val GITHUB_ACTOR: String? by settings

@Suppress("PropertyName")
val GITHUB_TOKEN: String? by settings

dependencyResolutionManagement {

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/saurabhsandav/CommonVersions")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: GITHUB_ACTOR
                password = System.getenv("GITHUB_TOKEN") ?: GITHUB_TOKEN
            }
        }
    }

    versionCatalogs {

        create("libs") {
            from("com.saurabhsandav:common-versions:0.53.0")
        }
    }
}

rootProject.name = "common"

listOf(
    "TYPESAFE_PROJECT_ACCESSORS",
).forEach { enableFeaturePreview(it) }

include(
    ":core",
    ":compose",
    ":sample:base",
    ":sample:platform:android",
    ":sample:platform:desktop",
)

setBuildFileName(rootProject)

fun setBuildFileName(project: ProjectDescriptor) {
    project.children.onEach { childProject ->
        childProject.buildFileName = "${childProject.name}.gradle.kts"
        setBuildFileName(childProject)
    }
}
