plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

//group = "com.example"
//version = "1.0-SNAPSHOT"

kotlin {
    androidTarget()
    jvm("desktop") {
        jvmToolchain(17)
    }
    dependencies {
        implementation(platform(androidLibs.coil.bom))
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.preview)
                api(compose.materialIconsExtended)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(androidLibs.junit)
                implementation("org.jetbrains.kotlinx:atomicfu:0.17.2")
            }
        }
        val androidMain by getting {
            dependencies {
                api(androidLibs.logcat)
                implementation(androidLibs.coil)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(androidLibs.junit)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(desktopLibs.image.loader)
            }
        }
        val desktopTest by getting
    }
}

android {
    namespace = "com.github.foodiestudio.application.common"
    compileSdk = extra["compileSdk"].toString().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = extra["minSdk"].toString().toInt()
        targetSdk = extra["targetSdk"].toString().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}