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
rootProject.name = "CourseApp"
include(":app")
include(":core")
include(":data")
include(":feature:login")
include(":feature:home")
include(":feature:favorites")
include(":feature:account")
