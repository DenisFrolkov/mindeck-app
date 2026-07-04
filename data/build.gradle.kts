import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    android {
        namespace = "com.mindeck.data"
        compileSdk = rootProject.extra["compileSdk"] as Int
        minSdk = rootProject.extra["minSdk"] as Int
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(rootProject.extra["jvmTarget"] as String)
        }
        withHostTestBuilder { }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain)
                implementation(libs.coroutines)
                implementation(libs.koin.core)
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.ktor.client.core)
                implementation(libs.filekit.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.androidx.core)
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
                implementation(libs.turbine)
                implementation(libs.coroutines.test)
                implementation(libs.androidx.room.testing)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    kspAndroid(libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}
