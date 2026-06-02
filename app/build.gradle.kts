import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.mindeck.app"
        compileSdk = rootProject.extra["compileSdk"] as Int

        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String)
        }
    }

    val xcfName = "appKit"

    iosArm64 { binaries.framework { baseName = xcfName } }
    iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

    sourceSets {
        androidMain { dependencies { implementation(libs.koin.android) } }

        commonMain {
            dependencies {
                // Module
                implementation(projects.domain)
                implementation(projects.data)

                // DI
                implementation(libs.koin.core)

                // Decompose
                implementation(libs.decompose.decompose)
                implementation(libs.decompose.extensions.compose)

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                // Serialization
                implementation(libs.kotlinx.serialization.core)

                implementation(compose.components.resources)
            }
        }
    }
}