pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        ivy {
            url = uri("https://mbientlab.com/releases/ivyrep")
            layout("gradle")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        ivy {
            url = uri("https://mbientlab.com/releases/ivyrep")
            layout("gradle")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "WorkoutCounter"
include(":app")
