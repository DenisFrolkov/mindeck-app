import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mindeck.app"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        applicationId = "com.mindeck.app"
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = rootProject.extra["javaVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["javaVersion"] as JavaVersion
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String))
    }
}

dependencies {
    // Modules
    implementation(projects.presentation)
    implementation(projects.domain)
    implementation(projects.data)

    // Splash screen
    implementation(libs.androidx.splashscreen)

    // Core
    implementation(libs.androidx.core)

    // Coroutines
    implementation(libs.coroutines)

    // Hilt
    implementation(libs.bundles.hilt)
    ksp(libs.bundles.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit)
    kspAndroidTest(libs.hilt.compiler)
}
