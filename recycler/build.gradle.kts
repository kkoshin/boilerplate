buildscript {
    val kotlin_version by extra("1.3.71")
    val dokka_version by extra("1.4.30")

    repositories {
        google()
        jcenter {
            setUrl("https://maven.aliyun.com/repository/jcenter")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter {
            setUrl("https://maven.aliyun.com/repository/jcenter")
        }
        maven { setUrl("https://www.jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//
//buildscript {
//    ext {
//        kotlin_version = '1.3.71'
//        dokka_version = '1.4.30'
//    }
//    repositories {
//        google()
//        jcenter { url "https://maven.aliyun.com/repository/jcenter" }
//    }
//    dependencies {
//        classpath 'com.android.tools.build:gradle:3.6.3'
//        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
//        classpath "org.jetbrains.dokka:dokka-gradle-plugin:${dokka_version}"
//
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        jcenter { url "https://maven.aliyun.com/repository/jcenter" }
//        maven { url 'https://www.jitpack.io' }
//    }
//}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
