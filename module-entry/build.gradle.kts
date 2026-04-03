plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.douplus.entry"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    compileOnly(fileTree(mapOf("dir" to "../libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(project(":hook-api"))
    implementation(project(":data-config"))
    implementation(project(":feature-adapter"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-video"))
    implementation(project(":feature-comment"))
    implementation(project(":feature-ward"))
    implementation(project(":feature-backup"))
    implementation(project(":feature-chat"))
}
