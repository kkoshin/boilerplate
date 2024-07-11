pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
    versionCatalogs {
        create("androidLibs") {
            from("io.github.foodiestudio:libs-versions:2023.08.01")
            library("showkase", "com.airbnb.android:showkase:1.0.3")
            library("showkase-processor", "com.airbnb.android:showkase-processor:1.0.3")
            library("material", "com.google.android.material:material:1.7.0")
        }
        create("desktopLibs") {
            from(files("./gradle/desktop.versions.toml"))
        }
    }
}

include(":android", ":desktop", ":common")
