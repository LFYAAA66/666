pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "DouPlusModern"
include(
    ":app",
    ":module-entry",
    ":core",
    ":hook-api",
    ":feature-adapter",
    ":feature-settings",
    ":feature-video",
    ":feature-comment",
    ":feature-ward",
    ":feature-backup",
    ":feature-chat",
    ":data-config",
    ":data-ward",
    ":data-backup",
    ":ui-shared"
)
