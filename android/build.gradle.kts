import org.jetbrains.kotlin.gradle.plugin.extraProperties

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    alias(androidLibs.plugins.ksp)
}

dependencies {
    implementation(project(":common"))
    implementation(androidLibs.activity.compose)
    implementation(androidLibs.bundles.jetpack)
    debugImplementation(androidLibs.bundles.debug)

//    implementation(androidLibs.compose.destinations.core)
//    ksp(androidLibs.compose.destinations.ksp)

    implementation("com.airbnb.android:showkase:1.0.0-beta18")
    ksp("com.airbnb.android:showkase-processor:1.0.0-beta18")
}

android {
    namespace = "com.github.foodiestudio.application"
    compileSdk = extra["compileSdk"].toString().toInt()
    defaultConfig {
        applicationId = "com.github.foodiestudio.application"
        minSdk = extra["minSdk"].toString().toInt()
        targetSdk = extra["targetSdk"].toString().toInt()
        versionCode = 1
        versionName = "0.1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    // compose-destinations
//    applicationVariants.all {
//        kotlin.sourceSets {
//            getByName(name) {
//                kotlin.srcDir("build/generated/ksp/$name/kotlin")
//            }
//        }
//    }
}