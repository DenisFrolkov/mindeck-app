import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String)
        }
    }

    iosX64()             // simulator on Intel Mac
    iosArm64()           // real iPhone/iPad
    iosSimulatorArm64()  // simulator on Apple Silicon Mac

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.coroutines)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.coroutines.test)
                implementation(libs.turbine)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
            }
        }
    }
}
