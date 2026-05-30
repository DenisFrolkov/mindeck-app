import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.mindeck.presentation"
        compileSdk = rootProject.extra["compileSdk"] as Int

        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String)
        }

        androidResources {
            enable = true
        }

        withHostTestBuilder { }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    val xcfName = "presentationKit"

    iosX64 { binaries.framework { baseName = xcfName } }
    iosArm64 { binaries.framework { baseName = xcfName } }
    iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.decompose.decompose)
                implementation(libs.decompose.extensions.compose)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.koin.compose.viewmodel)
                api(dependencies.platform(libs.compose.bom))
                api(libs.bundles.compose)
                implementation(libs.compose.material.icons)
                implementation(libs.richeditor.compose)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core)
                implementation(libs.compose.ui.tooling)
            }
        }

        iosMain {
            dependencies { }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
                implementation(libs.turbine)
                implementation(libs.coroutines.test)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(dependencies.platform(libs.compose.bom))
                implementation(libs.compose.ui.test.junit)
            }
        }
    }
}
