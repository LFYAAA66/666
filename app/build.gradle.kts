plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.douplus.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.douplus"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":module-entry"))
    implementation(project(":core"))
    implementation(project(":hook-api"))
    implementation(project(":feature-adapter"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-video"))
    implementation(project(":feature-comment"))
    implementation(project(":feature-ward"))
    implementation(project(":feature-backup"))
    implementation(project(":feature-chat"))
    implementation(project(":data-config"))
    implementation(project(":data-ward"))
    implementation(project(":data-backup"))
    implementation(project(":ui-shared"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    compileOnly(fileTree(mapOf("dir" to "../libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(fileTree(mapOf("dir" to "../libs", "include" to listOf("*service*.jar", "*service*.aar"))))
}
