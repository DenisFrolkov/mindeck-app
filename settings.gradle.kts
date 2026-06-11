pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "mindeck-app"
include(":androidApp")
include(":app")
include(":core:mvi")
include(":core:ui")
include(":data")
include(":domain")
include(":feature:home")
include(":feature:card")
