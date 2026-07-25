import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

val localProperties =
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) load(file.inputStream())
    }

android {
    namespace = "com.mindeck.androidApp"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        applicationId = "com.mindeck.app"
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (localProperties["signing.storeFile"] != null) {
            create("release") {
                storeFile = file(localProperties["signing.storeFile"] as String)
                storePassword = localProperties["signing.storePassword"] as String
                keyAlias = localProperties["signing.keyAlias"] as String
                keyPassword = localProperties["signing.keyPassword"] as String
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
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
    sourceSets {
        named("main") {
            assets.srcDirs(
                project(":core:ui")
                    .layout.buildDirectory
                    .dir("generated/composeResAndroid")
                    .get()
                    .asFile,
                project(":feature:home")
                    .layout.buildDirectory
                    .dir("generated/composeResAndroid")
                    .get()
                    .asFile,
                project(":feature:card")
                    .layout.buildDirectory
                    .dir("generated/composeResAndroid")
                    .get()
                    .asFile,
            )
        }
    }
}

evaluationDependsOn(":core:ui")
evaluationDependsOn(":feature:home")
evaluationDependsOn(":feature:card")

afterEvaluate {
    val composeResProjects = listOf(":core:ui", ":feature:home", ":feature:card")
    listOf("mergeDebugAssets", "mergeReleaseAssets").forEach { taskName ->
        val mergeTask = tasks.findByName(taskName) ?: return@forEach
        composeResProjects.forEach { projectPath ->
            mergeTask.dependsOn(project(projectPath).tasks.named("copyComposeResourcesToAndroid"))
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String))
    }
}

dependencies {
    // Modules
    implementation(projects.app)

    // Decompose
    implementation(libs.decompose.decompose)

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Splash screen
    implementation(libs.androidx.splashscreen)

    // Koin
    implementation(libs.koin.android)

    // Debug tools
    debugImplementation(libs.leakcanary)
}
