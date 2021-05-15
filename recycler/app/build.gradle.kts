plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.dokka") version "1.4.30"
}

android {
    compileSdk = 29

    defaultConfig {
        applicationId = "com.example.widget.recycler"
        minSdk = 21
        targetSdk = 29
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile(""))
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.navigation:navigation-fragment:2.0.0")
    implementation("androidx.navigation:navigation-ui:2.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.0.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.0.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.github.foodie-team:sugar:develop-SNAPSHOT")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.4")
}

tasks.dokkaHtml.configure {
    moduleName.set("Recycler")

    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(true)
        }
    }
}