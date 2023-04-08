plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
//    alias(androidLibs.plugins.sqldelight) apply false
    alias(androidLibs.plugins.ksp) apply false
}