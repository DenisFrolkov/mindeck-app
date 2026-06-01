plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    jvm()

    val xcfName = "composeKit"
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
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
            }
        }

        iosMain {
            dependencies {}
        }
    }
}
