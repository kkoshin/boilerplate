pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
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
            from("io.github.foodiestudio:libs-versions:2023.04.01")
            library("showkase", "com.airbnb.android:showkase:1.0.2")
            library("showkase-processor", "com.airbnb.android:showkase-processor:1.0.2")
            library("material", "com.google.android.material:material:1.7.0")
        }
        create("desktopLibs") {
            from(files("./gradle/desktop.versions.toml"))
        }
    }
}

include(":android", ":desktop", ":common")
