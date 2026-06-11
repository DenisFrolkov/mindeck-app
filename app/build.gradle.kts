import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
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
                implementation(projects.core.ui)
                implementation(projects.domain)
                implementation(projects.data)
                implementation(projects.feature.home)
                implementation(projects.feature.card)

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
                implementation(compose.components.resources)

                // Serialization
                implementation(libs.kotlinx.serialization.core)
            }
        }
    }
}
