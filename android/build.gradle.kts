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
    implementation(androidLibs.coil.bom)
    implementation(androidLibs.koin.bom)
    debugImplementation(androidLibs.bundles.debug)
    implementation(androidLibs.coil)
    implementation(androidLibs.koin)
    implementation(androidLibs.sql)
    implementation(androidLibs.ctc)
    implementation(androidLibs.theme)
    implementation(androidLibs.showkase)
    implementation(androidLibs.material)
    implementation(androidLibs.accompanist.permissions)
    ksp(androidLibs.showkase.processor)
    implementation(libs.moshi)
    ksp(libs.moshi.ksp)
    debugImplementation(androidLibs.junit)
//    implementation(androidLibs.compose.destinations.core)
//    ksp(androidLibs.compose.destinations.ksp)
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        release {
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

ksp {
    arg("skipPrivatePreviews", "true")
}