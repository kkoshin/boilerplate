plugins {
    kotlin("multiplatform") version androidLibs.versions.kotlin apply false
    kotlin("android") version androidLibs.versions.kotlin apply false
    id("org.jetbrains.compose") apply false
//    alias(androidLibs.plugins.sqldelight) apply false
    alias(androidLibs.plugins.ksp) apply false
    alias(androidLibs.plugins.android.application) apply false
    alias(androidLibs.plugins.android.library) apply false
}