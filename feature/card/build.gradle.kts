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

// Workaround: com.android.kotlin.multiplatform.library doesn't expose the Android assets
// directory to the CMP resources plugin, so copyAndroidMainComposeResourcesToAndroidAssets
// fails with "outputDirectory not set". We copy compose resources with the correct namespace
// prefix into a generated dir and add it as androidMain Java resources so CMP can load
// strings and other assets via classloader on Android.
val composeResAndroidDir = layout.buildDirectory.dir("generated/composeResAndroid")

val copyComposeResourcesToAndroid by tasks.registering(Sync::class) {
    group = "compose resources"
    dependsOn("prepareComposeResourcesTaskForCommonMain")
    from(
        layout.buildDirectory.dir(
            "generated/compose/resourceGenerator/preparedResources/commonMain/composeResources",
        ),
    )
    into(
        composeResAndroidDir.map {
            it.dir("composeResources/mindeck_app.feature.card.generated.resources")
        },
    )
}

kotlin {
    android {
        namespace = "com.mindeck.feature.card"
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
                implementation(projects.domain)

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)

                // DI
                implementation(libs.koin.core)

                // KotlinX
                implementation(libs.kotlinx.datetime)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.compose.ui.preview)
                implementation(libs.koin.compose.viewmodel)
            }
        }
    }
}

afterEvaluate {
    tasks.matching { it.name == "processAndroidMainJavaRes" }.configureEach {
        dependsOn(copyComposeResourcesToAndroid)
    }
}
