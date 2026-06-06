import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.mindeck.feature.home"
        compileSdk = rootProject.extra["compileSdk"] as Int

        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                // Module
                api(projects.core.mvi)
                implementation(projects.core.ui)

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.compose.ui.preview)
            }
        }
    }
}
